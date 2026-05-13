//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main
{

  private static final String URL = "jdbc:postgresql://localhost:5432/sep2";
  private static final String USER = "postgres";
  private static final String PASSWORD = "Salta1981";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  public static void main(String[] args) throws SQLException {

    try {
      Connection conn = getConnection();

      if (conn != null) {
        System.out.println("Connected to PostgreSQL");
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
    }
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    System.out.print("Hello and welcome!");

    for (int i = 1; i <= 5; i++)
    {
      //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
      // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
      System.out.println("i = " + i);
    }
  }
}