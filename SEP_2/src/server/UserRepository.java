package server;

import model.Company;
import model.Student;

import java.sql.*;

public class UserRepository {

  public Student loginStudent(String username, String password) {
    String sql = """
        SELECT u.id,
               u.username,
               u.password,
               s.first_name,
               s.last_name,
               s.cv,
               s.age,
               s.university,
               s.working_experience,
               s.personality_traits
        FROM users u
        JOIN students s ON s.user_id = u.id
        WHERE u.username = ?
          AND u.password = ?
          AND LOWER(u.role) = 'student'
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureStudentColumns(connection);
      ensureDefaultStudents(connection);

      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, username);
        statement.setString(2, password);

        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            String firstName = valueOrEmpty(rs.getString("first_name"));
            String lastName = valueOrEmpty(rs.getString("last_name"));
            String name = (firstName + " " + lastName).trim();
            int age = rs.getInt("age");

            if (rs.wasNull()) {
              age = 0;
            }

            return new Student(
                rs.getInt("id"),
                name,
                rs.getString("username"),
                rs.getString("password"),
                valueOrEmpty(rs.getString("cv")),
                age,
                valueOrEmpty(rs.getString("university")),
                valueOrEmpty(rs.getString("working_experience")),
                valueOrEmpty(rs.getString("personality_traits"))
            );
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Company loginCompany(String username, String password) {
    String sql = """
        SELECT c.company_id,
               c.name,
               c.location,
               u.username,
               u.password
        FROM company c
        JOIN users u ON c.user_id = u.id
        WHERE u.username = ?
          AND u.password = ?
          AND LOWER(u.role) = 'company'
        """;

    try (Connection connection = DatabaseConnection.getConnection()) {
      ensureCompanyUserColumn(connection);
      ensureDefaultCompanyUsers(connection);

      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, username);
        statement.setString(2, password);

        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            return new Company(
                rs.getInt("company_id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("password"),
                "Location: " + valueOrEmpty(rs.getString("location"))
            );
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void ensureStudentColumns(Connection connection) throws SQLException {
    String sql = """
        ALTER TABLE students
          ADD COLUMN IF NOT EXISTS cv TEXT,
          ADD COLUMN IF NOT EXISTS age INTEGER,
          ADD COLUMN IF NOT EXISTS university VARCHAR(150),
          ADD COLUMN IF NOT EXISTS working_experience TEXT,
          ADD COLUMN IF NOT EXISTS personality_traits TEXT
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.executeUpdate();
    }
  }

  private void ensureCompanyUserColumn(Connection connection) throws SQLException {
    String sql = """
        ALTER TABLE company
          ADD COLUMN IF NOT EXISTS user_id INTEGER
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.executeUpdate();
    }
  }

  private void ensureDefaultStudents(Connection connection) throws SQLException {
    saveStudent(
        connection,
        "Thor",
        "123",
        "Thor",
        "Petersen",
        "Student CV",
        23,
        "VIA University College",
        "Java and database project experience",
        "Responsible, practical and focused"
    );
    saveStudent(
        connection,
        "Lars",
        "123",
        "Lars",
        "Larsen",
        "Student CV",
        24,
        "Aarhus University",
        "Frontend and teamwork experience",
        "Structured, communicative and reliable"
    );
    saveStudent(
        connection,
        "luciana@email.com",
        "1234",
        "Luciana",
        "",
        "My CV",
        22,
        "VIA University College",
        "Java, SQL and group project experience",
        "Curious, reliable and collaborative"
    );
    saveStudent(
        connection,
        "emma@email.com",
        "1234",
        "Emma",
        "",
        "Frontend CV",
        23,
        "Aarhus University",
        "HTML, CSS, JavaScript and UX project experience",
        "Creative, structured and communicative"
    );
    saveStudent(
        connection,
        "oliver@email.com",
        "1234",
        "Oliver",
        "",
        "Backend CV",
        24,
        "University of Southern Denmark",
        "Java, PostgreSQL and REST API experience",
        "Analytical, persistent and responsible"
    );
  }

  private void ensureDefaultCompanyUsers(Connection connection)
      throws SQLException {

    saveCompanyUser(connection, 1, "systematic@email.com", "1234");
    saveCompanyUser(connection, 2, "lego@email.com", "1234");
    saveCompanyUser(connection, 3, "company@email.com", "1234");
    saveCompanyUser(connection, 4, "kamstrup@email.com", "1234");
    saveCompanyUser(connection, 5, "vestas@email.com", "1234");
    saveCompanyUser(connection, 6, "terma@email.com", "1234");
    saveCompanyUser(connection, 7, "bankdata@email.com", "1234");
    saveCompanyUser(connection, 8, "danske@email.com", "1234");
  }

  private void saveStudent(
      Connection connection,
      String username,
      String password,
      String firstName,
      String lastName,
      String cv,
      int age,
      String university,
      String workingExperience,
      String personalityTraits
  ) throws SQLException {

    int userId = saveUser(connection, username, password, "STUDENT");

    String insertSql = """
        INSERT INTO students
        (user_id, first_name, last_name, cv, age, university,
         working_experience, personality_traits)
        SELECT ?, ?, ?, ?, ?, ?, ?, ?
        WHERE NOT EXISTS (
          SELECT 1 FROM students WHERE user_id = ?
        )
        """;

    try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
      statement.setInt(1, userId);
      statement.setString(2, firstName);
      statement.setString(3, lastName);
      statement.setString(4, cv);
      statement.setInt(5, age);
      statement.setString(6, university);
      statement.setString(7, workingExperience);
      statement.setString(8, personalityTraits);
      statement.setInt(9, userId);
      statement.executeUpdate();
    }

    String updateSql = """
        UPDATE students
        SET first_name = ?,
            last_name = ?,
            cv = ?,
            age = ?,
            university = ?,
            working_experience = ?,
            personality_traits = ?
        WHERE user_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setString(3, cv);
      statement.setInt(4, age);
      statement.setString(5, university);
      statement.setString(6, workingExperience);
      statement.setString(7, personalityTraits);
      statement.setInt(8, userId);
      statement.executeUpdate();
    }
  }

  private void saveCompanyUser(
      Connection connection,
      int companyId,
      String username,
      String password
  ) throws SQLException {

    if (!companyExists(connection, companyId)) {
      return;
    }

    int userId = saveUser(connection, username, password, "COMPANY");

    String sql = """
        UPDATE company
        SET user_id = ?
        WHERE company_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, userId);
      statement.setInt(2, companyId);
      statement.executeUpdate();
    }
  }

  private int saveUser(
      Connection connection,
      String username,
      String password,
      String role
  ) throws SQLException {

    Integer existingUserId = findUserId(connection, username);

    if (existingUserId != null) {
      String updateSql = """
          UPDATE users
          SET password = ?,
              role = ?
          WHERE id = ?
          """;

      try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
        statement.setString(1, password);
        statement.setString(2, role);
        statement.setInt(3, existingUserId);
        statement.executeUpdate();
      }

      return existingUserId;
    }

    String insertSql = """
        INSERT INTO users (username, password, role)
        VALUES (?, ?, ?)
        RETURNING id
        """;

    try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, role);

      try (ResultSet rs = statement.executeQuery()) {
        rs.next();
        return rs.getInt("id");
      }
    }
  }

  private Integer findUserId(Connection connection, String username)
      throws SQLException {

    String sql = """
        SELECT id
        FROM users
        WHERE username = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);

      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    }

    return null;
  }

  private boolean companyExists(Connection connection, int companyId)
      throws SQLException {

    String sql = """
        SELECT 1
        FROM company
        WHERE company_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, companyId);

      try (ResultSet rs = statement.executeQuery()) {
        return rs.next();
      }
    }
  }

  private String valueOrEmpty(String value) {
    return value == null ? "" : value;
  }
}
