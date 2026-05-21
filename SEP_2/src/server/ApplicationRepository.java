package server;

import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

  public boolean addApplication(Application application) {

    String sql = """
        INSERT INTO application
        (student_id, internship_id, status, application_date,
         student_name, student_age, university,
         working_experience, personality_traits)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureStudentProfileColumns(connection);
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
          statement.setString(5, application.getStudentName());
          statement.setInt(6, application.getStudentAge());
          statement.setString(7, application.getUniversity());
          statement.setString(8, application.getWorkingExperience());
          statement.setString(9, application.getPersonalityTraits());

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
               application_date,
               student_name,
               student_age,
               university,
               working_experience,
               personality_traits
        FROM application
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureStudentProfileColumns(connection);

      try (PreparedStatement statement = connection.prepareStatement(sql);
          ResultSet rs = statement.executeQuery()) {

        while (rs.next()) {

          applications.add(createApplication(rs));
        }
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
               application_date,
               student_name,
               student_age,
               university,
               working_experience,
               personality_traits
        FROM application
        WHERE student_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureStudentProfileColumns(connection);

      try (PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, studentId);

        try (ResultSet rs = statement.executeQuery()) {

          while (rs.next()) {

            applications.add(createApplication(rs));
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return applications;
  }

  public List<Application> getApplicationsByCompany(int companyId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
        SELECT a.application_id,
               a.student_id,
               a.internship_id,
               a.status,
               a.application_date,
               a.student_name,
               a.student_age,
               a.university,
               a.working_experience,
               a.personality_traits
        FROM application a
        JOIN internship i ON a.internship_id = i.internship_id
        WHERE i.company_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureStudentProfileColumns(connection);

      try (PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, companyId);

        try (ResultSet rs = statement.executeQuery()) {

          while (rs.next()) {

            applications.add(createApplication(rs));
          }
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
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      connection.setAutoCommit(false);

      try {
        Integer internshipId =
            getInternshipIdForCompany(connection, applicationId, companyId);

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

  private Application createApplication(ResultSet rs) throws SQLException {
    int studentAge = rs.getInt("student_age");
    if (rs.wasNull()) {
      studentAge = 0;
    }

    return new Application(
        rs.getInt("application_id"),
        rs.getInt("student_id"),
        rs.getInt("internship_id"),
        rs.getString("status"),
        rs.getDate("application_date").toLocalDate(),
        valueOrEmpty(rs.getString("student_name")),
        studentAge,
        valueOrEmpty(rs.getString("university")),
        valueOrEmpty(rs.getString("working_experience")),
        valueOrEmpty(rs.getString("personality_traits"))
    );
  }

  private String valueOrEmpty(String value) {
    return value == null ? "" : value;
  }

  private void ensureStudentProfileColumns(Connection connection)
      throws SQLException {

    String sql = """
        ALTER TABLE application
          ADD COLUMN IF NOT EXISTS student_name VARCHAR(100),
          ADD COLUMN IF NOT EXISTS student_age INTEGER,
          ADD COLUMN IF NOT EXISTS university VARCHAR(150),
          ADD COLUMN IF NOT EXISTS working_experience TEXT,
          ADD COLUMN IF NOT EXISTS personality_traits TEXT
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.executeUpdate();
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

      if (rows > 0) {
        System.out.println("Application deleted.");
      }

      return rows > 0;

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

  private Integer getInternshipIdForCompany(
      Connection connection,
      int applicationId,
      int companyId
  ) throws SQLException {

    String sql = """
        SELECT a.internship_id
        FROM application a
        JOIN internship i ON a.internship_id = i.internship_id
        WHERE a.application_id = ?
          AND i.company_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, applicationId);
      statement.setInt(2, companyId);

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
