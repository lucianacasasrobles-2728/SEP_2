package client;

import model.Application;
import model.Internship;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {

  private static final String HOST = "localhost";
  private static final int PORT = 9090;

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

  public boolean updateApplicationStatus(int applicationId, String newStatus)
      throws IOException, ClassNotFoundException {

    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      out.writeObject("UPDATE_APPLICATION_STATUS");
      out.writeObject(applicationId);
      out.writeObject(newStatus);
      out.flush();

      String response = (String) in.readObject();
      return "OK".equals(response);
    }
  }
}