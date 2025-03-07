package httpServer.get;

import httpServer.HttpBody;
import httpServer.HttpRequest;
import httpServer.config.Config;
import httpServer.contentTypes.ContentType;
import httpServer.contentTypes.ContentTypeList;
import httpServer.httpResponse.HttpResponse;
import httpServer.RequestHandler;
import httpServer.httpResponse.HttpResponseBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class HttpGetRequestHandler implements RequestHandler {
    private final HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public HttpGetRequestHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public HttpResponse processRequest() throws IOException {
        Path filePath = Paths.get(Config.FOLDER_PATH, httpRequest.path());
        if (Files.notExists(filePath) || httpRequest.path().equals("/")) {
            return httpResponse = HttpResponse.builder()
                    .notFound()
                    .build();
        }

        ContentType contentType = ContentTypeList.get(extractContentType(httpRequest.path()));
        HttpResponseBuilder builder = HttpResponse.builder().ok();

        HttpBody body = new HttpBody(Files.readString(filePath));

        if (acceptGzip()) {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(arrayOutputStream);
            gzip.write(body.getBytes());
            gzip.close();

            byte[] bodyBytes = arrayOutputStream.toByteArray();

            return builder.addHeaderEntry("Content-Encoding", "gzip")
                    .addHeaderEntry("Content-Type", "text/" + contentType + "; charset=UTF-8")
                    .addHeaderEntry("Content-Length", String.valueOf(bodyBytes.length))
                    .compressedBody(bodyBytes)
                    .build();
        }

        return builder
                .addHeaderEntry("Content-Type", "text/" + contentType + "; charset=UTF-8")
                .addHeaderEntry("Content-Length", String.valueOf(body.size()))
                .body(body)
                .build();
    }

    @Override
    public HttpResponse getResponse() {
        return httpResponse;
    }

    @Override
    public void respond(OutputStream streamOut) {
        try {
            streamOut.write(httpResponse.toString().getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String extractContentType(String path) {
        return path.substring(path.indexOf('.') + 1);
    }

    private boolean acceptGzip() {
        if (!httpRequest.header().containsKey("Accept-Encoding")) {
            return false;
        }

        for (String encode : httpRequest.header().get("Accept-Encoding").split(",")) {
            if (encode.trim().equals("gzip")) {
                return true;
            }
        }
        return false;
    }
}
