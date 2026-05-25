package test;

import model.Application;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

  @Test
  public void testApplicationStatusChange() {

    Application application = new Application(
        1,
        1,
        1,
        "Pending",
        LocalDate.of(2026,5,2)
    );

    assertEquals("Pending", application.getStatus());

    application.updateStatus("Accepted");
    assertEquals("Accepted", application.getStatus());

    application.updateStatus("Rejected");
    assertEquals("Rejected", application.getStatus());
  }

  @Test
  public void testApplicationGetters() {

    Application application = new Application(
        10,
        5,
        7,
        "Pending",
        LocalDate.of(2026,5,2)
    );

    assertEquals(10, application.getApplicationId());
    assertEquals(5, application.getStudentId());
    assertEquals(7, application.getInternshipId());
    assertEquals(LocalDate.of(2026,5,2), application.getApplicationDate());
  }

  @Test
  public void testApplicationFullConstructor() {

    Application application = new Application(
        11,
        6,
        8,
        "Pending",
        LocalDate.of(2026,5,3),
        "Luciana",
        22,
        "VIA University College",
        "Java and SQL experience",
        "Curious and reliable"
    );

    assertEquals(11, application.getApplicationId());
    assertEquals(6, application.getStudentId());
    assertEquals(8, application.getInternshipId());
    assertEquals("Pending", application.getStatus());
    assertEquals(LocalDate.of(2026,5,3), application.getApplicationDate());
    assertEquals("Luciana", application.getStudentName());
    assertEquals(22, application.getStudentAge());
    assertEquals("VIA University College", application.getUniversity());
    assertEquals("Java and SQL experience", application.getWorkingExperience());
    assertEquals("Curious and reliable", application.getPersonalityTraits());
  }

  // Z — zero IDs and empty defaults from 5-arg constructor
  @Test
  public void testApplicationWithZeroIdsAndDefaults() {

    Application application = new Application(
        0,
        0,
        0,
        "Pending",
        LocalDate.of(2026,5,2)
    );

    assertEquals(0, application.getApplicationId());
    assertEquals(0, application.getStudentId());
    assertEquals(0, application.getInternshipId());
    assertEquals("", application.getStudentName());
    assertEquals(0, application.getStudentAge());
    assertEquals("", application.getUniversity());
    assertEquals("", application.getWorkingExperience());
    assertEquals("", application.getPersonalityTraits());
  }

  // E — updateStatus rejects null
  @Test
  public void testUpdateStatusWithNullThrows() {

    Application application = new Application(
        1,
        1,
        1,
        "Pending",
        LocalDate.of(2026,5,2)
    );

    assertThrows(IllegalArgumentException.class, () -> application.updateStatus(null));
  }
}
