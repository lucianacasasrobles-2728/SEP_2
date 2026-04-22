package server;

import model.Internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipRepository {

  public List<Internship> getAll() {
    List<Internship> internships = new ArrayList<>();


    String sql =
            "SELECT i.internship_id, i.title, i.description, i.company_id, " +
                    "c.location, i.position, i.start_date, i.end_date, i.status " +
                    "FROM internship i " +
                    "JOIN company c ON i.company_id = c.company_id";

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()) {

      while (rs.next()) {
        Internship internship = new Internship(
                rs.getInt("internship_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("company_id"),
                rs.getString("position"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getString("status")
        );

        internships.add(internship);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return internships;
  }

  public void add(Internship internship) {

    String sql =
            "INSERT INTO internship " +
                    "(title, description, company_id, position, start_date, end_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {


      statement.setString(1, internship.getTitle());
      statement.setString(2, internship.getDescription());
      statement.setInt(3, internship.getCompanyId());
      statement.setString(4, internship.getPosition());
      statement.setDate(5,
              internship.getStartDate() != null ? Date.valueOf(internship.getStartDate()) : null);
      statement.setDate(6,
              internship.getEndDate() != null ? Date.valueOf(internship.getEndDate()) : null);
      statement.setString(7, internship.getStatus());

      statement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean delete(int id) {
    String sql = "DELETE FROM internship WHERE internship_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, id);
      int rows = statement.executeUpdate();
      return rows > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}