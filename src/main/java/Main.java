import httpServer.HttpRequestHandler;
import httpServer.logger.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            Logger.server("server started");
            while (true) {
                Logger.server("waiting for connection ...");
                clientSocket = serverSocket.accept();
                Logger.server("accepted new connection");
                HttpRequestHandler requestHandler = new HttpRequestHandler(clientSocket);
                Thread.startVirtualThread(requestHandler);
            }
        } catch(IOException e) {
            Logger.info(e.getMessage());
        }
    }
}
