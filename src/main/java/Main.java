import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static String folderPath;
    public static void main(String[] args) {

        if (args.length >= 1)
            folderPath = args[1].endsWith("/") ? args[1] : args[1] + "/";

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            System.out.println("[SERVER]: server started");
            while (true) {
              System.out.println("[SERVER]: waiting for connection ...");
              clientSocket = serverSocket.accept();
              System.out.println("[SERVER]: accepted new connection");
              HttpRequestHandler requestHandler = new HttpRequestHandler(clientSocket, folderPath);
              Thread clientThread = new Thread(requestHandler);
              clientThread.start();
            }
        } catch(IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
