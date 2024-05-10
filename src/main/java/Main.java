import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;



public class Main {

    public static ArrayList<Thread> clients = new ArrayList<>();

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests
      System.out.println("Logs from your program will appear here!");

      ServerSocket serverSocket = null;
      Socket clientSocket = null;

      try {
          serverSocket = new ServerSocket(4221);
          serverSocket.setReuseAddress(true);
          System.out.println("[SERVER]: server started");
          while (true) {
              System.out.println("[SERVER]: waiting for connection");
              clientSocket = serverSocket.accept();
              System.out.println("[SERVER]: accepted new connection");
              Runnable httpHandler = new HttpRequestHandler(clientSocket);
              Thread clientThread = new Thread(httpHandler);
              clients.add(clientThread);
              clientThread.start();
          }
          } catch(IOException e) {
          System.out.println("IOException: " + e.getMessage());
      }
  }
}
