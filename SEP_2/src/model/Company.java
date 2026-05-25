package model;

import java.io.Serializable;

public class Company implements Serializable {

  private static final long serialVersionUID = 1L;

  private int companyId;
  private String companyName;
  private String email;
  private String password;
  private String description;

  public Company(int companyId, String companyName, String email,
      String password, String description) {
    this.companyId = companyId;
    this.companyName = companyName;
    this.email = email;
    this.password = password;
    this.description = description;
  }

  public int getCompanyId() {
    return companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public String getEmail() {
    return email;
  }

  public String getDescription() {
    return description;
  }

  public boolean login(String email, String password) {
    return this.email.equals(email)
        && this.password.equals(password);
  }

  public void logout() {
    System.out.println(companyName + " logged out.");
  }

  public void addInternship() {
    System.out.println(companyName + " added an internship.");
  }

  public void deleteInternship() {
    System.out.println(companyName + " deleted an internship.");
  }

  public void viewApplications() {
    System.out.println(companyName + " is viewing applications.");
  }
}
