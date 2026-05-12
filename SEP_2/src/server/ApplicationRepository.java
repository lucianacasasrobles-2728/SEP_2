package server;

import model.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

  private List<Application> applications;

  public ApplicationRepository() {
    applications = new ArrayList<>();
  }

  public void addApplication(Application application) {
    applications.add(application);
    System.out.println("Application added.");
  }

  public List<Application> getAllApplications() {
    return applications;
  }

  public List<Application> getApplicationsByStudent(int studentId) {

    List<Application> result = new ArrayList<>();

    for (Application application : applications) {

      if (application.getStudentId() == studentId) {
        result.add(application);
      }
    }

    return result;
  }

  public boolean updateApplicationStatus(int applicationId, String newStatus) {

    for (Application application : applications) {

      if (application.getApplicationId() == applicationId) {
        application.setStatus(newStatus);
        System.out.println("Status updated to " + newStatus);
        return true;
      }
    }

    System.out.println("Application not found.");
    return false;
  }

  public void deleteApplication(int applicationId) {

    applications.removeIf(
        application -> application.getApplicationId() == applicationId
    );

    System.out.println("Application deleted.");
  }
}