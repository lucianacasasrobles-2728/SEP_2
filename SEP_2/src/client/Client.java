package client;

import model.Internship;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {

  private static final String HOST = "localhost";
  private static final int PORT = 9090;

  /**
   * GET ALL internshipss
   */
  @SuppressWarnings("unchecked")
  public List<Internship> getAll() throws IOException, ClassNotFoundException {
    try (
        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      // Send command
      out.writeObject("GET_ALL");
      out.flush();

      // Receive response
      return (List<Internship>) in.readObject();
    }
  }

  /**
   * ADD internship
   */
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

  /**
   * DELETE internship by ID
   */
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
}

