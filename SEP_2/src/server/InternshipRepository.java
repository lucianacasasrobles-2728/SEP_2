package server;

import model.Internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipRepository {

  public List<Internship> getAll() {

    List<Internship> internships = new ArrayList<>();

    String sql = """
            SELECT internship_id,
                   title,
                   
                   description,
                   company_id,
                   position,
                   start_date,
                   end_date,
                   status
            FROM internship
            """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()) {

      while (rs.next()) {

        Date startDateSql = rs.getDate("start_date");
        Date endDateSql = rs.getDate("end_date");

        Internship internship = new Internship(
            rs.getInt("internship_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("company_id"),
            "",
            rs.getString("position"),
            startDateSql == null ? null : startDateSql.toLocalDate(),
            endDateSql == null ? null : endDateSql.toLocalDate(),
            rs.getString("status")
        );

        internships.add(internship);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Internships found in database: " + internships.size());

    return internships;
  }

  public void add(Internship internship) {

    String sql = """
            INSERT INTO internship
            (title, description, company_id, position, start_date, end_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, internship.getTitle());
      statement.setString(2, internship.getDescription());
      statement.setInt(3, internship.getCompanyId());
      statement.setString(4, internship.getPosition());

      if (internship.getStartDate() == null) {
        statement.setNull(5, Types.DATE);
      } else {
        statement.setDate(5, Date.valueOf(internship.getStartDate()));
      }

      if (internship.getEndDate() == null) {
        statement.setNull(6, Types.DATE);
      } else {
        statement.setDate(6, Date.valueOf(internship.getEndDate()));
      }

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