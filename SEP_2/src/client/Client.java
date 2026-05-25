package client;

import model.Application;
import model.Company;
import model.Internship;
import model.Student;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {

  private static final String HOST = "localhost";
  private static final int PORT = 9090;

  public Student loginStudent(String username, String password)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("LOGIN_STUDENT");
      out.writeObject(username);
      out.writeObject(password);
      out.flush();

      return (Student) in.readObject();
    }
  }

  public Company loginCompany(String username, String password)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("LOGIN_COMPANY");
      out.writeObject(username);
      out.writeObject(password);
      out.flush();

      return (Company) in.readObject();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Internship> getAll() throws IOException, ClassNotFoundException {
    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("GET_ALL");
      out.flush();

      return (List<Internship>) in.readObject();
    }
  }

  public boolean add(Internship internship) throws IOException, ClassNotFoundException {
    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("ADD");
      out.writeObject(internship);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }

  public boolean delete(int id) throws IOException, ClassNotFoundException {
    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("DELETE");
      out.writeObject(id);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }

  public boolean apply(Application application) throws IOException, ClassNotFoundException {
    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("APPLY");
      out.writeObject(application);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }

  @SuppressWarnings("unchecked")
  public List<Application> getApplicationsByStudent(int studentId)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("GET_APPLICATIONS_BY_STUDENT");
      out.writeObject(studentId);
      out.flush();

      return (List<Application>) in.readObject();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Application> getAllApplications()
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("GET_ALL_APPLICATIONS");
      out.flush();

      return (List<Application>) in.readObject();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Application> getApplicationsByCompany(int companyId)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("GET_APPLICATIONS_BY_COMPANY");
      out.writeObject(companyId);
      out.flush();

      return (List<Application>) in.readObject();
    }
  }

  public boolean updateApplicationStatusForCompany(
      int applicationId,
      String newStatus,
      int companyId
  ) throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("UPDATE_APPLICATION_STATUS_FOR_COMPANY");
      out.writeObject(applicationId);
      out.writeObject(newStatus);
      out.writeObject(companyId);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }

  public boolean deleteApplication(int applicationId)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("DELETE_APPLICATION");
      out.writeObject(applicationId);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }
}
