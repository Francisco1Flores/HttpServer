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

/*
            if (request.getMethod().equals("GET")) {
                if (request.getPath().equals("/")) {
                    response = new HttpResponse(HttpStatusCode.OK);
                    streamOut.write(response.getBytes());
                    client.close();
                    System.out.println("[SERVER]: connection closed");

                } else if (request.getPath().equals("/user-agent")) {
                    String body = request.getHeaderValue("user-agent");
                    List<Field> header = new ArrayList<>();
                    header.add(new Field("Content-Type", "text/plain"));
                    header.add(new Field("Content-Length", String.valueOf(body.length())));

                    response = new HttpResponse(HttpStatusCode.OK, header, body);

                    streamOut.write(response.getBytes());
                    client.close();
                    System.out.println("[SERVER]: connection closed");

                } else if (request.getPath().startsWith("/echo")) {
                    String[] splittedPath = request.getPath().split("/");

                    String body = splittedPath[splittedPath.length - 1];
                    List<Field> header = new ArrayList<>();

                    header.add(new Field("Content-Type", "text/plain"));

                    if (request.headerKeyExist("accept-encoding")) {
                        if (request.getHeaderValue("accept-encoding").contains("gzip")) {

                            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                            GZIPOutputStream gzip = new GZIPOutputStream(arrayOutputStream);
                            gzip.write(body.getBytes());
                            gzip.close();

                            header.add(new Field("Content-Length", String.valueOf(arrayOutputStream.toByteArray().length)));
                            header.add(new Field("Content-Encoding", "gzip"));

                            response = new HttpResponse(HttpStatusCode.OK, header, arrayOutputStream.toByteArray());

                            streamOut.write(response.getBytes());

                            client.close();
                            System.out.println("[SERVER]: connection closed");
                        } else {
                            header.add(new Field("Content-Length", String.valueOf(body.length())));

                            response = new HttpResponse(HttpStatusCode.OK, header, body);

                            streamOut.write(response.getBytes());
                            client.close();
                            System.out.println("[SERVER]: connection closed");
                        }

                    } else {
                        header.add(new Field("Content-Length", String.valueOf(body.length())));

                        response = new HttpResponse(HttpStatusCode.OK, header, body);

                        streamOut.write(response.getBytes());
                        client.close();
                        System.out.println("[SERVER]: connection closed");
                    }
                } else if (request.getPath().startsWith("/files")) {

                    String filePath = fileDirectory + request.getPath().split("/")[2];
                    File file = new File(filePath);
                    String fileContent;

                    if (file.exists()) {
                        try {
                            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
                            List<Field> header = new ArrayList<>();
                            header.add(new Field("Content-Type", "application/octet-stream"));
                            header.add(new Field("Content-Length", String.valueOf(fileContent.length())));

                            response = new HttpResponse(HttpStatusCode.OK, header, fileContent);
                            streamOut.write(response.getBytes());
                            client.close();
                            System.out.println("[SERVER]: connection closed");

                        } catch (IOException e) {
                            System.out.println("[ERROR]: an error occurred trying to accessing to a file");
                        }
                    } else {
                        response = new HttpResponse(HttpStatusCode.NOT_FOUND);
                        streamOut.write(response.getBytes());
                        client.close();
                        System.out.println("[SERVER]: connection closed");
                    }
                } else {
                    response = new HttpResponse(HttpStatusCode.NOT_FOUND);
                    streamOut.write(response.getBytes());
                    client.close();
                    System.out.println("[SERVER]: connection closed");
                }
            } else {
                String filePath = fileDirectory + request.getPath().split("/")[2];
                FileOutputStream fileOut = new FileOutputStream(filePath);
                fileOut.write(request.getBody().getBytes());

                response = new HttpResponse(HttpStatusCode.CREATED);
                streamOut.write(response.getBytes());
                client.close();
                System.out.println("[SERVER]: connection closed");
            }
        } catch (IOException e) {
            System.out.println("[SERVER]: connection closed");
        } catch (BadRequestException e) {
            System.out.println("[ERROR]: bad request");
        } finally {
            if (client != null) {
                try {
                    client.close();
                    System.out.println("[SERVER]: connection closed");
                } catch (IOException e) {
                    System.out.println("[ERROR]: " + e.getMessage());
                }
            }
            */


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
