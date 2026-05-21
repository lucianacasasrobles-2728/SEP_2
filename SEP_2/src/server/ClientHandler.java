package server;

import model.Application;
import model.Company;
import model.Internship;
import model.Student;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket socket;
  private final InternshipRepository repository;
  private final ApplicationRepository applicationRepository;
  private final UserRepository userRepository;

  public ClientHandler(
      Socket socket,
      InternshipRepository repository,
      ApplicationRepository applicationRepository,
      UserRepository userRepository
  ) {
    this.socket = socket;
    this.repository = repository;
    this.applicationRepository = applicationRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void run() {

    try (
        ObjectOutputStream out =
            new ObjectOutputStream(socket.getOutputStream());

        ObjectInputStream in =
            new ObjectInputStream(socket.getInputStream())
    ) {

      Object commandObj = in.readObject();

      if (!(commandObj instanceof String)) {
        out.writeObject("ERROR");
        out.flush();
        return;
      }

      String command = (String) commandObj;

      switch (command) {

        case "LOGIN_STUDENT":
          Object studentUsernameObj = in.readObject();
          Object studentPasswordObj = in.readObject();

          if (studentUsernameObj instanceof String username
              && studentPasswordObj instanceof String password) {
            Student student = userRepository.loginStudent(username, password);
            out.writeObject(student);
          } else {
            out.writeObject(null);
          }

          out.flush();
          break;

        case "LOGIN_COMPANY":
          Object companyUsernameObj = in.readObject();
          Object companyPasswordObj = in.readObject();

          if (companyUsernameObj instanceof String username
              && companyPasswordObj instanceof String password) {
            Company company = userRepository.loginCompany(username, password);
            out.writeObject(company);
          } else {
            out.writeObject(null);
          }

          out.flush();
          break;

        case "GET_ALL":
          out.writeObject(repository.getAll());
          out.flush();
          break;

        case "ADD":
          Object addObj = in.readObject();

          if (addObj instanceof Internship internship) {
            repository.add(internship);
            out.writeObject("OK");
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "DELETE":
          Object deleteObj = in.readObject();

          if (deleteObj instanceof Integer id) {
            boolean removed = repository.delete(id);
            out.writeObject(removed ? "OK" : "NOT_FOUND");
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "APPLY":
          Object applyObj = in.readObject();

          if (applyObj instanceof Application application) {
            boolean added = applicationRepository.addApplication(application);
            out.writeObject(added ? "OK" : "INTERNSHIP_FILLED");
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "GET_APPLICATIONS_BY_STUDENT":
          Object studentObj = in.readObject();

          if (studentObj instanceof Integer studentId) {
            out.writeObject(
                applicationRepository.getApplicationsByStudent(studentId)
            );
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "GET_ALL_APPLICATIONS":
          out.writeObject(applicationRepository.getAllApplications());
          out.flush();
          break;

        case "GET_APPLICATIONS_BY_COMPANY":
          Object companyObj = in.readObject();

          if (companyObj instanceof Integer companyId) {
            out.writeObject(
                applicationRepository.getApplicationsByCompany(companyId)
            );
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "UPDATE_APPLICATION_STATUS_FOR_COMPANY":
          Object companyAppIdObj = in.readObject();
          Object companyStatusObj = in.readObject();
          Object statusCompanyObj = in.readObject();

          if (companyAppIdObj instanceof Integer applicationId
              && companyStatusObj instanceof String newStatus
              && statusCompanyObj instanceof Integer companyId) {

            boolean updated =
                applicationRepository.updateApplicationStatusForCompany(
                    applicationId,
                    newStatus,
                    companyId
                );

            out.writeObject(updated ? "OK" : "NOT_FOUND");

          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        case "DELETE_APPLICATION":
          Object deleteApplicationObj = in.readObject();

          if (deleteApplicationObj instanceof Integer applicationId) {
            boolean removed =
                applicationRepository.deleteApplication(applicationId);
            out.writeObject(removed ? "OK" : "NOT_FOUND");
          } else {
            out.writeObject("ERROR");
          }

          out.flush();
          break;

        default:
          out.writeObject("ERROR");
          out.flush();
      }

    } catch (IOException | ClassNotFoundException e) {

      System.err.println("ClientHandler error: " + e.getMessage());

    } finally {

      try {
        socket.close();
      } catch (IOException e) {
        System.err.println("Could not close socket: " + e.getMessage());
      }
    }
  }
}
