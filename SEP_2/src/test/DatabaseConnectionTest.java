package test;

import org.junit.jupiter.api.Test;
import server.DatabaseConnection;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

  @Test
  public void testDatabaseConnection() {

    try {

      Connection connection = DatabaseConnection.getConnection();

      assertNotNull(connection);
      assertFalse(connection.isClosed());

      connection.close();

    } catch (Exception e) {

      fail("Database connection failed");
    }
  }
}
