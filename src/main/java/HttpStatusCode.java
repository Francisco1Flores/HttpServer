public enum HttpStatusCode {
    OK("HTTP/1.1 200 OK"),
    NOT_FOUND("HTTP/1.1 404 Not Found");
    public final String message;
    private HttpStatusCode(String message){
        this.message = message;
    }
}
