package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static final String URL =
      System.getenv().getOrDefault(
          "SEP2_DB_URL", "jdbc:postgresql://localhost:5432/sep2");
  private static final String USER =
      System.getenv().getOrDefault("SEP2_DB_USER", "postgres");
  private static final String PASSWORD = getRequiredEnv("SEP2_DB_PASSWORD");

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  private static String getRequiredEnv(String key) {
    String value = System.getenv(key);
    if (value == null || value.isBlank()) {
      throw new IllegalStateException(
          "Missing required environment variable: " + key);
    }
    return value;
  }
}
