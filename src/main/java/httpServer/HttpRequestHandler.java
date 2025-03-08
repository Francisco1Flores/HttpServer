package httpServer;

import httpServer.exeption.BadRequestException;
import httpServer.get.HttpGetRequestHandler;
import httpServer.httpResponse.HttpResponse;
import httpServer.post.HttpPostRequestHandler;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandler implements Runnable {
    private final Socket client;
    private final OutputStream streamOut;
    private final InputStream streamIn;

    public HttpRequestHandler(Socket client) throws IOException {
        this.client = client;
        streamIn = client.getInputStream();
        streamOut = client.getOutputStream();
    }

    @Override
    public void run() {
        try {
            HttpRequestParser requestParser = new HttpRequestParser(streamIn);
            HttpRequest httpRequest = requestParser.parse();
            httpRequest.print();

            RequestHandler requestHandler = getPertinentHandler(httpRequest);
            HttpResponse response = requestHandler.processRequest();
            System.out.println(response.toString());
            streamOut.write(response.getBytes());

            try {
                client.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("[SERVER]: connection closed");
        } catch (BadRequestException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public RequestHandler getPertinentHandler(HttpRequest request) {
       return switch (request.method()) {
           case GET -> new HttpGetRequestHandler(request);
           case POST -> new HttpPostRequestHandler(request);
           case UPDATE -> null;
           case DELETE -> null;
           case PATCH -> null;
           case NOT_SUPORTED -> null;
       };
    }

}
