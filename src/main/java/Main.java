import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
        serverSocket = new ServerSocket(4221);
        serverSocket.setReuseAddress(true);
        clientSocket = serverSocket.accept(); // Wait for connection from client.
        OutputStream streamOut = clientSocket.getOutputStream();
        InputStream streamIn = clientSocket.getInputStream();
        String input = new String(streamIn.readAllBytes());
        String[] inputLines = input.split(" ");

        String responseOK = "HTTP/1.1 200 OK \r\n\r\n";
        String responseNOTFOUND = "HTTP/1.1 404 NOT FOUND \r\n\r\n";

        streamOut.write(
                (inputLines[1].equals("/") ?
                        responseOK.getBytes() :
                        responseNOTFOUND.getBytes()));

        System.out.println("accepted new connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
