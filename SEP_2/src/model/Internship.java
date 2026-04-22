package model;

import java.io.Serializable;
import java.time.LocalDate;


public class Internship implements Serializable {

  private int id;
  private String title;
  private String description;
  private int companyId;
  private String position;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;

  public Internship(int id, String title, String description, int companyId,
                    String position, LocalDate startDate,
                    LocalDate endDate, String status) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.companyId = companyId;
    this.position = position;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public int getCompanyId() { return companyId; }
  public String getPosition() { return position; }
  public LocalDate getStartDate() { return startDate; }
  public LocalDate getEndDate() { return endDate; }
  public String getStatus() { return status; }
}
