package server;

import model.Internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipRepository {

  public List<Internship> getAll() {
    List<Internship> internships = new ArrayList<>();

    String sql = "SELECT internship_id, title, description, company_id, location, position, start_date, end_date, status FROM internship";

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()) {

      while (rs.next()) {
        Internship internship = new Internship(
            rs.getInt("internship_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("company_id"),
            rs.getString("location"),
            rs.getString("position"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
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
    String sql = "INSERT INTO internship (title, description, company_id, location, position, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, internship.getTitle());
      statement.setString(2, internship.getDescription());
      statement.setInt(3, internship.getCompanyId());
      statement.setString(4, internship.getLocation());
      statement.setString(5, internship.getPosition());
      statement.setDate(6, Date.valueOf(internship.getStartDate()));
      statement.setDate(7, Date.valueOf(internship.getEndDate()));
      statement.setString(8, internship.getStatus());

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