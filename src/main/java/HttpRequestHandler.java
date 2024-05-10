import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestHandler implements Runnable {
    private Socket client;
    private OutputStream streamOut;
    private InputStream streamIn;
    private HttpRequestParser request;
    private HttpResponse response;

    public HttpRequestHandler(Socket client) throws IOException {
        this.client = client;
        streamIn = client.getInputStream();
        streamOut = client.getOutputStream();

        try {
            request = new HttpRequestParser(streamIn);
        } catch (BadRequestException e) {
            response = new HttpResponse(HttpStatusCode.NOT_FOUND);
        }
    }
    @Override
    public void run() {
        try {
            if (request.getPath().equals("/")) {
                response = new HttpResponse(HttpStatusCode.OK);
                streamOut.write(response.getBytes());
                client.close();

            } else if (request.getPath().equals("/user-agent")) {
                String body = request.getHeaderValue("User-Agent");
                List<Field> header = new ArrayList<>();
                header.add(new Field("Content-Type:", "text/html"));
                header.add(new Field("Content-Length:", String.valueOf(body.length())));

                response = new HttpResponse(HttpStatusCode.OK, header, body);

                streamOut.write(response.getBytes());
                client.close();

            } else {
                String[] splittedPath = request.getPath().split("/");

                if (splittedPath[1].equals("echo")) {
                    String body = splittedPath[splittedPath.length - 1];
                    List<Field> header = new ArrayList<>();
                    header.add(new Field("Content-Type:", "text/plain"));
                    header.add(new Field("Content-Length:", String.valueOf(body.length())));

                    response = new HttpResponse(HttpStatusCode.OK, header, body);

                    streamOut.write(response.getBytes());
                    client.close();
                } else {
                    response = new HttpResponse(HttpStatusCode.NOT_FOUND);
                    streamOut.write(response.getBytes());
                    client.close();
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
    }
}
