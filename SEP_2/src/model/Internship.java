package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Internship implements Serializable {
  private int id;
  private String title;
  private String description;
  private int companyId;
  private String location;
  private String position;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;

  public Internship(int id, String title, String description, int companyId,
      String location, String position,
      LocalDate startDate, LocalDate endDate, String status) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.companyId = companyId;
    this.location = location;
    this.position = position;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public int getCompanyId() { return companyId; }
  public void setCompanyId(int companyId) { this.companyId = companyId; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}