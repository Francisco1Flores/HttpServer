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

          String firstLine = bufferIn.readLine();
          System.out.println(firstLine);

          String[] splittedFirstLine = firstLine.split(" ");
          String path = splittedFirstLine[1];
          String[] splittedPath = path.split("/");

          HttpResponse response;

          if (path.equals("/")) {
              response = new HttpResponse(HttpStatusCode.OK);
              streamOut.write(response.getBytes());
              clientSocket.close();
          }else if (splittedPath[0].equals("echo")) {

              String body = splittedPath[splittedPath.length -1];
              List<Field> header = new ArrayList<>();
              header.add(new Field("Content-Type:", "text/plain"));
              header.add(new Field("Content-Length:", String.valueOf(body.length())));

              response = new HttpResponse(HttpStatusCode.OK, header, body);

              streamOut.write(response.getBytes());
              clientSocket.close();
          } else {
              response = new HttpResponse(HttpStatusCode.NOT_FOUND);
              streamOut.write(response.getBytes());
              clientSocket.close();
          }

          System.out.println("accepted new connection");
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
  }

  public static String parsePath(String path) {
      String[] splittedPath = path.split("/");
      return splittedPath[splittedPath.length - 1];
  }
}
