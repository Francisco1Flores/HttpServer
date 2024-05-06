import java.util.List;
import java.util.Map;

public class HttpResponse {
    HttpStatusCode statusCode;
    List<Field> header;

    String stringHeader;
    String body;

    String completeResponse;

    public HttpResponse(HttpStatusCode statusCode, List<Field> header, String body) {
        this.statusCode = statusCode;
        this.header = header;
        this.body = body;

        stringHeader = headerToString();

        completeResponse = this.statusCode.message + "\r\n" + stringHeader + "\r\n" + this.body;
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
            sh.append(field.getKey() + " " + field.getValue() + "\r\n");
        }
        return sh.toString();
    }
}
