import java.util.List;
import java.util.Map;

public class HttpResponse {
    private HttpStatusCode statusCode;
    private List<Field> header;
    private String stringHeader;
    private String body;
    private String completeResponse;

    public HttpResponse(HttpStatusCode statusCode, List<Field> header, String body) {
        this.statusCode = statusCode;
        this.header = header;
        this.body = body;

        stringHeader = headerToString();

        completeResponse = this.statusCode.message + "\r\n" + stringHeader + "\r\n" + this.body;
    }

    public HttpResponse(HttpStatusCode statusCode, List<Field> header) {
        this.statusCode = statusCode;
        this.header = header;

        stringHeader = headerToString();

        completeResponse = this.statusCode.message + "\r\n" + stringHeader + "\r\n";
    }

    public HttpResponse(HttpStatusCode statusCode) {
        this.statusCode = statusCode;

        completeResponse = this.statusCode.message + "\r\n\r\n";
    }

    public byte[] getBytes() {
        return completeResponse.getBytes();
    }

    public String getString() {
        return completeResponse;
    }

    private String headerToString() {
        StringBuilder sh = new StringBuilder();
        for (Field field : header) {
            sh.append(field.getKey() + ": " + field.getValue() + "\r\n");
        }
        return sh.toString();
    }
}
