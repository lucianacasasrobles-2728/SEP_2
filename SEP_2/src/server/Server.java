package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private static final int PORT = 9090;

  public static void main(String[] args) {
    InternshipRepository repository = new InternshipRepository();

    System.out.println("Server started on port " + PORT + "...");

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected: " + clientSocket.getInetAddress());

        Thread thread = new Thread(new ClientHandler(clientSocket, repository));
        thread.start();
      }
    } catch (IOException e) {
      System.err.println("Server error: " + e.getMessage());
    }
  }
}