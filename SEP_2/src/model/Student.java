package model;

import java.io.Serializable;

public class Student implements Serializable {

  private static final long serialVersionUID = 1L;

  private int studentId;
  private String name;
  private String email;
  private String password;
  private String cv;
  private int age;
  private String university;
  private String workingExperience;
  private String personalityTraits;

  public Student(
      int studentId,
      String name,
      String email,
      String password,
      String cv
  ) {
    this(studentId, name, email, password, cv, 0, "", "", "");
  }

  public Student(
      int studentId,
      String name,
      String email,
      String password,
      String cv,
      int age,
      String university,
      String workingExperience,
      String personalityTraits
  ) {

    this.studentId = studentId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.cv = cv;
    this.age = age;
    this.university = university;
    this.workingExperience = workingExperience;
    this.personalityTraits = personalityTraits;
  }

  public boolean login(String email, String password) {

    return this.email.equals(email)
        && this.password.equals(password);
  }

  public void logout() {
    System.out.println(name + " logged out.");
  }

  public int getStudentId() {
    return studentId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getCv() {
    return cv;
  }

  public int getAge() {
    return age;
  }

  public String getUniversity() {
    return university;
  }

  public String getWorkingExperience() {
    return workingExperience;
  }

  public String getPersonalityTraits() {
    return personalityTraits;
  }

  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setCv(String cv) {
    this.cv = cv;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setUniversity(String university) {
    this.university = university;
  }

  public void setWorkingExperience(String workingExperience) {
    this.workingExperience = workingExperience;
  }

  public void setPersonalityTraits(String personalityTraits) {
    this.personalityTraits = personalityTraits;
  }

  public void browseInternships() {

  }

  public void clickApply() {

  }

  public void confirmApplication() {

  }

  public void applyForInternship() {
    System.out.println(name + " applied for internship.");
  }

  public void receiveNotification() {

  }
}
