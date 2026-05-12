package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Application implements Serializable {

  private int applicationId;
  private int studentId;
  private int internshipId;
  private String status;
  private LocalDate applicationDate;

  public Application(int applicationId, int studentId,
      int internshipId, String status,
      LocalDate applicationDate) {

    this.applicationId = applicationId;
    this.studentId = studentId;
    this.internshipId = internshipId;
    this.status = status;
    this.applicationDate = applicationDate;
  }

  public int getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(int applicationId) {
    this.applicationId = applicationId;
  }

  public int getStudentId() {
    return studentId;
  }

  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

  public int getInternshipId() {
    return internshipId;
  }

  public void setInternshipId(int internshipId) {
    this.internshipId = internshipId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(LocalDate applicationDate) {
    this.applicationDate = applicationDate;
  }

  public void applyForInternship() {
    System.out.println("Application submitted.");
  }

  public void checkStatus() {
    System.out.println("Application status: " + status);
  }

  public void updateStatus(String newStatus) {
    this.status = newStatus;
    System.out.println("Status updated to: " + status);
  }
}