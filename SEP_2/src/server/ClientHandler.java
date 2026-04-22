package server;

import model.Internship;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket socket;
  private final InternshipRepository repository;

  public ClientHandler(Socket socket, InternshipRepository repository) {
    this.socket = socket;
    this.repository = repository;
  }

  @Override
  public void run() {
    try (
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
      Object commandObj = in.readObject();

      if (!(commandObj instanceof String)) {
        out.writeObject("ERROR");
        out.flush();
        return;
      }

      String command = (String) commandObj;

      switch (command) {
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