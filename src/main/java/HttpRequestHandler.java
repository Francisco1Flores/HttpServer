import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestHandler implements Runnable {
    private Socket client;
    private OutputStream streamOut;
    private InputStream streamIn;
    private HttpRequestParser request;
    private HttpResponse response;
    private String fileDirectory;

    public HttpRequestHandler(Socket client, String fileDirectory) throws IOException {
        this.client = client;
        streamIn = client.getInputStream();
        streamOut = client.getOutputStream();
        if (fileDirectory != null)
            this.fileDirectory = fileDirectory;
    }
    @Override
    public void run() {
        try {
            request = new HttpRequestParser(streamIn);

            if (request.getPath().equals("/")) {
                response = new HttpResponse(HttpStatusCode.OK);
                streamOut.write(response.getBytes());
                client.close();

            } else if (request.getPath().equals("/user-agent")) {
                String body = request.getHeaderValue("User-Agent");
                List<Field> header = new ArrayList<>();
                header.add(new Field("Content-Type:", "text/plain"));
                header.add(new Field("Content-Length:", String.valueOf(body.length())));

                response = new HttpResponse(HttpStatusCode.OK, header, body);

                streamOut.write(response.getBytes());
                client.close();

            } else if (request.getPath().startsWith("/echo")) {
                String[] splittedPath = request.getPath().split("/");

                //if (splittedPath[1].equals("echo")) {
                    String body = splittedPath[splittedPath.length - 1];
                    List<Field> header = new ArrayList<>();
                    header.add(new Field("Content-Type:", "text/plain"));
                    header.add(new Field("Content-Length:", String.valueOf(body.length())));

                    response = new HttpResponse(HttpStatusCode.OK, header, body);

                    streamOut.write(response.getBytes());
                    client.close();

                //}
            } else if (request.getPath().startsWith("/files")) {

                String filePath = fileDirectory + request.getPath().split("/")[2];
                File file = new File(filePath);
                String fileContent;

                if (file.exists()) {
                    try {
                        fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
                        List<Field> header = new ArrayList<>();
                        header.add(new Field("Content-Type:", "application/octet-stream"));
                        header.add(new Field("Content-Length:", String.valueOf(fileContent.length())));

                        response = new HttpResponse(HttpStatusCode.OK, header, fileContent);
                        streamOut.write(response.getBytes());
                        client.close();

                    } catch (IOException e) {
                        System.out.println("[ERROR]: an error occurred trying to accessing to a file");
                    }
                } else {
                    response = new HttpResponse(HttpStatusCode.NOT_FOUND);
                    streamOut.write(response.getBytes());
                    client.close();
                }
            } else {
                    response = new HttpResponse(HttpStatusCode.NOT_FOUND);
                    streamOut.write(response.getBytes());
                    client.close();
            }

        } catch (IOException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (BadRequestException e) {
            System.out.println("[ERROR]: bad request");
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    System.out.println("[ERROR]: " + e.getMessage());
                }
            }
        }
    }
}
