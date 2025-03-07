package httpServer;

public enum HttpStatusCode {
    OK("HTTP/1.1 200 OK"),
    CREATED("HTTP/1.1 201 Created"),
    NOT_FOUND("HTTP/1.1 404 Not Found");
    public final String message;
    private HttpStatusCode(String message){
        this.message = message;
    }
}
