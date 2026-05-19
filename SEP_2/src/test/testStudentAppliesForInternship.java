package test;

import model.Application;
import model.Internship;
import model.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class testStudentAppliesForInternship {
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
  public void testUpdateApplicationStatus() {

    Application application = new Application(

        1,
        1,
        1,
        "Pending",
        LocalDate.of(2026, 5, 2)
    );

    application.setStatus("Accepted");

    assertEquals("Accepted", application.getStatus());
  }
}