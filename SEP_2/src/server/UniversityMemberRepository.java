package server;

import model.UniversityMember;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UniversityMemberRepository {

  public List<UniversityMember> getAll() {

    List<UniversityMember> members =
        new ArrayList<>();

    String sql =
        "SELECT * FROM university_member";

    try (
        Connection connection =
            DatabaseConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql);

        ResultSet rs =
            statement.executeQuery()
    ) {

      while (rs.next()) {

        UniversityMember member =
            new UniversityMember(
                rs.getInt("university_member_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("role")
            );

        members.add(member);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return members;
  }
}