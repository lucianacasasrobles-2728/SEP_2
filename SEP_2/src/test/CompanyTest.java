package test;

import model.Company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyTest {

  @Test
  public void testCompanyCreation() {

    Company company = new Company(
        1,
        "Systematic",
        "systematic@test.com",
        "1234",
        "Software company in Aarhus"
    );

    assertEquals(1, company.getCompanyId());
    assertEquals("Systematic", company.getCompanyName());
    assertEquals("systematic@test.com", company.getEmail());
    assertEquals("Software company in Aarhus", company.getDescription());
  }

  @Test
  public void testLoginWithCorrectCredentials() {

    Company company = new Company(
        1,
        "Systematic",
        "systematic@test.com",
        "1234",
        "Software company in Aarhus"
    );

    assertTrue(company.login("systematic@test.com", "1234"));
  }

  @Test
  public void testLoginWithWrongPassword() {

    Company company = new Company(
        1,
        "Systematic",
        "systematic@test.com",
        "1234",
        "Software company in Aarhus"
    );

    assertFalse(company.login("systematic@test.com", "wrong"));
  }

  @Test
  public void testLoginWithWrongEmail() {

    Company company = new Company(
        1,
        "Systematic",
        "systematic@test.com",
        "1234",
        "Software company in Aarhus"
    );

    assertFalse(company.login("other@test.com", "1234"));
  }

  // Z — login with empty credentials fails against real credentials
  @Test
  public void testLoginWithEmptyCredentials() {

    Company company = new Company(
        1,
        "Systematic",
        "systematic@test.com",
        "1234",
        "Software company in Aarhus"
    );

    assertFalse(company.login("", ""));
  }
}
