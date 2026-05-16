package model;

public class UniversityMember {

  private int universityMemberId;
  private String name;
  private String email;
  private String role;

  public UniversityMember(int universityMemberId, String name, String email, String role) {
    this.universityMemberId = universityMemberId;
    this.name = name;
    this.email = email;
    this.role = role;
  }

  public int getUniversityMemberId() {
    return universityMemberId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

  public void monitorStudentProgress() {
    // Future functionality: view student application progress
  }

  public void viewActiveCompanies() {
    // Future functionality: view companies offering internships
  }
}