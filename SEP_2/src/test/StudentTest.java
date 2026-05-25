package test;

import model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

  @Test
  public void testStudentCreation() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertEquals(1, student.getStudentId());
    assertEquals("Luciana", student.getName());
    assertEquals("luciana@test.com", student.getEmail());
    assertEquals("My CV", student.getCv());
  }

  @Test
  public void testStudentFullConstructor() {

    Student student = new Student(
        2,
        "Emma",
        "emma@test.com",
        "1234",
        "Frontend CV",
        23,
        "Aarhus University",
        "HTML, CSS, JavaScript experience",
        "Creative, structured and communicative"
    );

    assertEquals(2, student.getStudentId());
    assertEquals("Emma", student.getName());
    assertEquals("emma@test.com", student.getEmail());
    assertEquals("Frontend CV", student.getCv());
    assertEquals(23, student.getAge());
    assertEquals("Aarhus University", student.getUniversity());
    assertEquals("HTML, CSS, JavaScript experience", student.getWorkingExperience());
    assertEquals("Creative, structured and communicative", student.getPersonalityTraits());
  }

  @Test
  public void testLoginWithCorrectCredentials() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertTrue(student.login("luciana@test.com", "1234"));
  }

  @Test
  public void testLoginWithWrongPassword() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertFalse(student.login("luciana@test.com", "wrong"));
  }

  @Test
  public void testLoginWithWrongEmail() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertFalse(student.login("other@test.com", "1234"));
  }

  // Z — zero / default values from 5-arg constructor
  @Test
  public void testStudentWithZeroAgeAndEmptyFields() {

    Student student = new Student(
        0,
        "Luciana",
        "luciana@test.com",
        "1234",
        ""
    );

    assertEquals(0, student.getStudentId());
    assertEquals(0, student.getAge());
    assertEquals("", student.getCv());
    assertEquals("", student.getUniversity());
    assertEquals("", student.getWorkingExperience());
    assertEquals("", student.getPersonalityTraits());
  }

  // Z — login with empty credentials
  @Test
  public void testLoginWithEmptyCredentials() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertFalse(student.login("", ""));
  }

  // B / E — negative age rejected in constructor
  @Test
  public void testNegativeAgeInConstructorThrows() {

    assertThrows(IllegalArgumentException.class, () -> new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV",
        -1,
        "VIA",
        "",
        ""
    ));
  }

  // B / E — negative age rejected in setter
  @Test
  public void testSetNegativeAgeThrows() {

    Student student = new Student(
        1,
        "Luciana",
        "luciana@test.com",
        "1234",
        "My CV"
    );

    assertThrows(IllegalArgumentException.class, () -> student.setAge(-5));
  }
}
