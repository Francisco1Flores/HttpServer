public class HttpResponse {
    private final String status;
    private final String header;
    private final String body;

    public HttpResponse(String status, String header, String body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }

    public HttpResponse(String status) {
        this.status = status;
        header = "";
        body = "";
    }

    public String getResponse() {
        return "" + status + " \r\n" + header + "\r\n" + body;
    }
}
