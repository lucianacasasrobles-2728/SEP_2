package test;

import model.UniversityMember;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniversityMemberTest {

  @Test
  public void testUniversityMemberCreation() {

    UniversityMember member = new UniversityMember(
        1,
        "Anna Hansen",
        "anna@via.dk",
        "Supervisor"
    );

    assertEquals(1, member.getUniversityMemberId());
    assertEquals("Anna Hansen", member.getName());
    assertEquals("anna@via.dk", member.getEmail());
    assertEquals("Supervisor", member.getRole());
  }
}
