package model;

public class Student {

  private int studentId;
  private String name;
  private String email;
  private String password;
  private String cv;

  public Student(int studentId,
      String name,
      String email,
      String password,
      String cv) {

    this.studentId = studentId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.cv = cv;
  }

  public boolean login(String email, String password) {

    return this.email.equals(email)
        && this.password.equals(password);
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

  public void browseInternships() {

  }

  public void clickApply() {

  }

  public void confirmApplication() {

  }

  public void receiveNotification() {

  }
}
