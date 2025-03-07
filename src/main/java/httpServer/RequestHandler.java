package httpServer;

import httpServer.httpResponse.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestHandler {
    HttpResponse processRequest() throws IOException;
    HttpResponse getResponse();
    void respond(OutputStream streamOut);
}
