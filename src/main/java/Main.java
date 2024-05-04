import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests
      System.out.println("Logs from your program will appear here!");

      String responseOK = "HTTP/1.1 200 OK \r\n\r\n";
      String responseNOTFOUND = "HTTP/1.1 404 Not Found \r\n\r\n";

      ServerSocket serverSocket = null;
      Socket clientSocket = null;

      try {
          serverSocket = new ServerSocket(4221);
          serverSocket.setReuseAddress(true);
          clientSocket = serverSocket.accept(); // Wait for connection from client.

          BufferedReader bufferIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          OutputStream streamOut = clientSocket.getOutputStream();

          String input = new String(bufferIn.readLine());
          String[] inputLines = input.split(" ");

          streamOut.write((inputLines[1].equals("/")) ?
                  responseOK.getBytes() :
                  responseNOTFOUND.getBytes());

          clientSocket.close();

          System.out.println("accepted new connection");
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
  }
}
