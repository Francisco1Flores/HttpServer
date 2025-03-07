package httpServer.post;

import httpServer.HttpRequest;
import httpServer.RequestHandler;
import httpServer.config.Config;
import httpServer.httpResponse.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpPostRequestHandler implements RequestHandler {

    private final HttpRequest request;
    private HttpResponse response;

    public HttpPostRequestHandler(HttpRequest request) {
        this.request = request;
    }

    @Override
    public HttpResponse processRequest() throws IOException {
        Path absolutefilePath = Paths.get(Config.FOLDER_PATH, request.path());
        String path = absolutefilePath.toString();
        Path directoryPath = Paths.get(path.substring(0, path.lastIndexOf('\\')));

        if (Files.notExists(directoryPath)) {
            Files.createDirectory(directoryPath);
        }

        Files.write(absolutefilePath, request.body().getBytes());

        return HttpResponse.builder().created().build();
    }

    @Override
    public HttpResponse getResponse() {
        return null;
    }

    @Override
    public void respond(OutputStream streamOut) {

    }
}
