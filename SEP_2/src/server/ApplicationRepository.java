package server;

import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

  public boolean addApplication(Application application) {

    String sql = """
        INSERT INTO application
        (student_id, internship_id, status, application_date)
        VALUES (?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      connection.setAutoCommit(false);

      try {
        if (!lockInternship(connection, application.getInternshipId())
            || hasAcceptedApplication(connection, application.getInternshipId())) {
          connection.rollback();
          return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setInt(1, application.getStudentId());
          statement.setInt(2, application.getInternshipId());
          statement.setString(3, application.getStatus());
          statement.setDate(4, Date.valueOf(application.getApplicationDate()));

          int rows = statement.executeUpdate();
          connection.commit();

          System.out.println("Application saved in database.");
          return rows > 0;
        }

      } catch (SQLException e) {
        connection.rollback();
        throw e;
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Application> getAllApplications() {

    List<Application> applications = new ArrayList<>();

    String sql = """
        SELECT application_id,
               student_id,
               internship_id,
               status,
               application_date
        FROM application
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()) {

      while (rs.next()) {

        Application application = new Application(
            rs.getInt("application_id"),
            rs.getInt("student_id"),
            rs.getInt("internship_id"),
            rs.getString("status"),
            rs.getDate("application_date").toLocalDate()
        );

        applications.add(application);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return applications;
  }

  public List<Application> getApplicationsByStudent(int studentId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
        SELECT application_id,
               student_id,
               internship_id,
               status,
               application_date
        FROM application
        WHERE student_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, studentId);

      try (ResultSet rs = statement.executeQuery()) {

        while (rs.next()) {

          Application application = new Application(
              rs.getInt("application_id"),
              rs.getInt("student_id"),
              rs.getInt("internship_id"),
              rs.getString("status"),
              rs.getDate("application_date").toLocalDate()
          );

          applications.add(application);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return applications;
  }

  public boolean updateApplicationStatus(int applicationId, String newStatus) {

    String sql = """
        UPDATE application
        SET status = ?
        WHERE application_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      connection.setAutoCommit(false);

      try {
        Integer internshipId = getInternshipId(connection, applicationId);

        if (internshipId == null || !lockInternship(connection, internshipId)) {
          connection.rollback();
          return false;
        }

        if ("Accepted".equalsIgnoreCase(newStatus)
            && hasOtherAcceptedApplication(connection, applicationId, internshipId)) {
          connection.rollback();
          return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setString(1, newStatus);
          statement.setInt(2, applicationId);

          int rows = statement.executeUpdate();

          if (rows > 0 && "Accepted".equalsIgnoreCase(newStatus)) {
            rejectOtherPendingApplications(connection, applicationId, internshipId);
          }

          connection.commit();
          return rows > 0;
        }

      } catch (SQLException e) {
        connection.rollback();
        throw e;
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteApplication(int applicationId) {

    String sql = """
        DELETE FROM application
        WHERE application_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, applicationId);
      int rows = statement.executeUpdate();

      System.out.println("Application deleted.");
      return rows > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Application> getApplicationsByCompany(int companyId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
        SELECT a.application_id,
               a.student_id,
               a.internship_id,
               a.status,
               a.application_date,
               s.first_name,
               s.last_name,
               s.age,
               s.university,
               s.working_experience,
               s.personality_traits
        FROM application a
        JOIN internship i ON a.internship_id = i.internship_id
        LEFT JOIN students s ON s.user_id = a.student_id
        WHERE i.company_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, companyId);

      try (ResultSet rs = statement.executeQuery()) {

        while (rs.next()) {

          String firstName = rs.getString("first_name");
          String lastName = rs.getString("last_name");
          String studentName = (
              (firstName == null ? "" : firstName)
                  + " "
                  + (lastName == null ? "" : lastName)
          ).trim();

          int age = rs.getInt("age");

          if (rs.wasNull()) {
            age = 0;
          }

          String university = rs.getString("university");
          String workingExperience = rs.getString("working_experience");
          String personalityTraits = rs.getString("personality_traits");

          Application application = new Application(
              rs.getInt("application_id"),
              rs.getInt("student_id"),
              rs.getInt("internship_id"),
              rs.getString("status"),
              rs.getDate("application_date").toLocalDate(),
              studentName,
              age,
              university == null ? "" : university,
              workingExperience == null ? "" : workingExperience,
              personalityTraits == null ? "" : personalityTraits
          );

          applications.add(application);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return applications;
  }

  public boolean updateApplicationStatusForCompany(
      int applicationId,
      String newStatus,
      int companyId
  ) {

    String sql = """
        UPDATE application
        SET status = ?
        WHERE application_id = ?
          AND internship_id IN (
            SELECT internship_id
            FROM internship
            WHERE company_id = ?
          )
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      connection.setAutoCommit(false);

      try {
        Integer internshipId = getInternshipId(connection, applicationId);

        if (internshipId == null || !lockInternship(connection, internshipId)) {
          connection.rollback();
          return false;
        }

        if ("Accepted".equalsIgnoreCase(newStatus)
            && hasOtherAcceptedApplication(connection, applicationId, internshipId)) {
          connection.rollback();
          return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setString(1, newStatus);
          statement.setInt(2, applicationId);
          statement.setInt(3, companyId);

          int rows = statement.executeUpdate();

          if (rows > 0 && "Accepted".equalsIgnoreCase(newStatus)) {
            rejectOtherPendingApplications(connection, applicationId, internshipId);
          }

          connection.commit();
          return rows > 0;
        }

      } catch (SQLException e) {
        connection.rollback();
        throw e;
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean lockInternship(Connection connection, int internshipId)
      throws SQLException {

    String sql = """
        SELECT internship_id
        FROM internship
        WHERE internship_id = ?
        FOR UPDATE
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, internshipId);

      try (ResultSet rs = statement.executeQuery()) {
        return rs.next();
      }
    }
  }

  private Integer getInternshipId(Connection connection, int applicationId)
      throws SQLException {

    String sql = """
        SELECT internship_id
        FROM application
        WHERE application_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, applicationId);

      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("internship_id");
        }
      }
    }

    return null;
  }

  private boolean hasAcceptedApplication(Connection connection, int internshipId)
      throws SQLException {

    String sql = """
        SELECT 1
        FROM application
        WHERE internship_id = ?
          AND LOWER(status) = 'accepted'
        LIMIT 1
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, internshipId);

      try (ResultSet rs = statement.executeQuery()) {
        return rs.next();
      }
    }
  }

  private boolean hasOtherAcceptedApplication(
      Connection connection,
      int applicationId,
      int internshipId
  ) throws SQLException {

    String sql = """
        SELECT 1
        FROM application
        WHERE internship_id = ?
          AND application_id <> ?
          AND LOWER(status) = 'accepted'
        LIMIT 1
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, internshipId);
      statement.setInt(2, applicationId);

      try (ResultSet rs = statement.executeQuery()) {
        return rs.next();
      }
    }
  }

  private void rejectOtherPendingApplications(
      Connection connection,
      int applicationId,
      int internshipId
  ) throws SQLException {

    String sql = """
        UPDATE application
        SET status = 'Rejected'
        WHERE internship_id = ?
          AND application_id <> ?
          AND LOWER(status) = 'pending'
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, internshipId);
      statement.setInt(2, applicationId);
      statement.executeUpdate();
    }
  }
}
