package server;

import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

  public void addApplication(Application application) {

    String sql = """
        INSERT INTO application
        (student_id, internship_id, status, application_date)
        VALUES (?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, application.getStudentId());
      statement.setInt(2, application.getInternshipId());
      statement.setString(3, application.getStatus());
      statement.setDate(4, Date.valueOf(application.getApplicationDate()));

      statement.executeUpdate();

      System.out.println("Application saved in database.");

    } catch (SQLException e) {
      e.printStackTrace();
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
        UPDATE applications
        SET status = ?
        WHERE application_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, newStatus);
      statement.setInt(2, applicationId);

      int rows = statement.executeUpdate();

      return rows > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void deleteApplication(int applicationId) {

    String sql = """
        DELETE FROM applications
        WHERE application_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, applicationId);
      statement.executeUpdate();

      System.out.println("Application deleted.");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}