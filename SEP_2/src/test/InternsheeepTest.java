package test;

import model.Application;
import model.Internship;
import model.Student;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.sql.Connection;
import server.DatabaseConnection;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class InternsheeepTest {
@Test
    public void testStudent () {

      Student student = new Student(
          1,
          "Luciana",
          "luciana@test.com",
          "1234",
          "My CV"

      );
      Internship internship = new Internship(
          1,
          "Software Internship",
          "Java project",
          1,
          "Aarhus",
          "Developer",
          LocalDate.of(2026,6,1),
          LocalDate.of(2026, 12, 1),
          "Available"

      );
      Application application = new Application(
          1,
          1,
          1,
          "Pending",
          LocalDate.of(2026, 5, 2)

      );
      assertEquals(1, student.getStudentId());
      assertEquals("Luciana", student.getName());
      assertEquals("luciana@test.com", student.getEmail());
      assertEquals("My CV", student.getCv());

      assertEquals(1, internship.getId());
      assertEquals("Software Internship", internship.getTitle());
      assertEquals("Java project", internship.getDescription());
      assertEquals(1, internship.getCompanyId());
      assertEquals("Aarhus", internship.getLocation());
      assertEquals("Developer", internship.getPosition());
      assertEquals(LocalDate.of(2026, 6, 1), internship.getStartDate());
      assertEquals(LocalDate.of(2026, 12, 1), internship.getEndDate());
      assertEquals("Available", internship.getStatus());

    assertEquals(1, application.getApplicationId());
    assertEquals(1, application.getStudentId());
    assertEquals(1, application.getInternshipId());
    assertEquals("Pending", application.getStatus());
    assertEquals(LocalDate.of(2026, 5, 2), application.getApplicationDate());


    // IMPORTANT !!! We verified that the objects store the correct data and that the Application connects
  // the correct student with the correct internship.//

  assertEquals(student.getStudentId(), application.getStudentId());
  assertEquals(internship.getId(), application.getInternshipId());
}
@Test
  public void testApplicationStatusChange(){

   Application application = new Application(
       1,
       1,
       1,
       "Pending",
       LocalDate.of(2026,5,2)
   );

  // initial status
   assertEquals("Pending", application.getStatus());

// Change to Accepted
    application.updateStatus("Accepted");
    assertEquals("Accepted", application.getStatus());

    // Chance to Rejected

    application.updateStatus("Rejected");
    assertEquals("Rejected", application.getStatus());
  }
  @Test
  public void testStudentCanApplyForInternship() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    Internship internship = new Internship(
        1,
        "Software Internship",
        "Java project",
        1,
        "Aarhus",
        "Developer",
        LocalDate.of(2026,6,1),
        LocalDate.of(2026,12,1),
        "Available"
    );

    Application application = new Application(
        1,
        student.getStudentId(),
        internship.getId(),
        "Pending",
        LocalDate.of(2026,5,2)
    );

    // Verify internship is available
    assertEquals("Available", internship.getStatus());

    // Verify correct student applied
    assertEquals(student.getStudentId(), application.getStudentId());

    // Verify correct internship was selected
    assertEquals(internship.getId(), application.getInternshipId());

    // Verify application starts as pending
    assertEquals("Pending", application.getStatus());
  }
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
  @Test
  public void testSocketConnectionIsClosedCorrectly() {

    try {
      Socket socket = new Socket("localhost", 9090);

      // Verify socket connects correctly
      assertTrue(socket.isConnected());

      // Close socket
      socket.close();

      // Verify socket is closed
      assertTrue(socket.isClosed());

    } catch (Exception e) {
      fail("Socket connection failed");
    }
  }
}