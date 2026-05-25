package test;

import model.Internship;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InternshipTest {

  @Test
  public void testInternshipCreation() {

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

    assertEquals(1, internship.getId());
    assertEquals("Software Internship", internship.getTitle());
    assertEquals("Java project", internship.getDescription());
    assertEquals("Aarhus", internship.getLocation());
    assertEquals("Available", internship.getStatus());
  }

  @Test
  public void testInternshipAdditionalGetters() {

    Internship internship = new Internship(
        2,
        "Backend Internship",
        "Spring Boot project",
        42,
        "Copenhagen",
        "Backend Developer",
        LocalDate.of(2026,7,1),
        LocalDate.of(2027,1,1),
        "Available"
    );

    assertEquals(42, internship.getCompanyId());
    assertEquals("Backend Developer", internship.getPosition());
    assertEquals(LocalDate.of(2026,7,1), internship.getStartDate());
    assertEquals(LocalDate.of(2027,1,1), internship.getEndDate());
  }

  // B — equal start and end date is allowed (zero-day internship)
  @Test
  public void testInternshipWithEqualStartAndEndDate() {

    LocalDate date = LocalDate.of(2026, 6, 1);

    Internship internship = new Internship(
        3,
        "One Day Workshop",
        "Single day event",
        1,
        "Aarhus",
        "Trainee",
        date,
        date,
        "Available"
    );

    assertEquals(date, internship.getStartDate());
    assertEquals(date, internship.getEndDate());
  }

  // B / E — endDate before startDate is rejected
  @Test
  public void testEndDateBeforeStartDateThrows() {

    assertThrows(IllegalArgumentException.class, () -> new Internship(
        4,
        "Broken Internship",
        "Invalid dates",
        1,
        "Aarhus",
        "Developer",
        LocalDate.of(2026, 12, 1),
        LocalDate.of(2026, 6, 1),
        "Available"
    ));
  }
}
