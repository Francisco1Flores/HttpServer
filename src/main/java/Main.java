import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests
      System.out.println("Logs from your program will appear here!");

      ServerSocket serverSocket = null;
      Socket clientSocket = null;

      try {
          serverSocket = new ServerSocket(4221);
          serverSocket.setReuseAddress(true);
          clientSocket = serverSocket.accept(); // Wait for connection from client.


          HttpRequestParser request = new HttpRequestParser(clientSocket.getInputStream());
          OutputStream streamOut = clientSocket.getOutputStream();

          HttpResponse response;

          if (request.getPath().equals("/")) {
              response = new HttpResponse(HttpStatusCode.OK);
              streamOut.write(response.getBytes());
              clientSocket.close();

          }else if (request.getPath().equals("/user-agent")) {

              String body = request.getHeaderValue("User-Agent");
              List<Field> header = new ArrayList<>();
              header.add(new Field("Content-Type:", "text/plain"));
              header.add(new Field("Content-Length:", String.valueOf(body.length())));

              response = new HttpResponse(HttpStatusCode.OK, header, body);

              streamOut.write(response.getBytes());
              clientSocket.close();

          } else {
              String[] splittedPath = request.getPath().split("/");

              if (splittedPath[1].equals("echo")) {
                  String body = splittedPath[splittedPath.length - 1];
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
          }

          System.out.println("accepted new connection");
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      } catch (BadRequestException bre) {
          System.out.println(bre.getMessage());
      }
  }
}
