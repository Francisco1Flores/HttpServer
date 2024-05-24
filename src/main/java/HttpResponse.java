import java.util.List;
import java.util.Map;

public class HttpResponse {
    private HttpStatusCode statusCode;
    private List<Field> header;
    private String stringHeader;
    private String body;

    private byte[] gzipCompressedBody;

    private boolean bodyIsCompressed = false;
    private String completeResponse;

    private byte[] compressedResponse;

    public HttpResponse(HttpStatusCode statusCode, List<Field> header, String body) {
        this.statusCode = statusCode;
        this.header = header;
        this.body = body;

        stringHeader = headerToString();

        completeResponse = this.statusCode.message + "\r\n" + stringHeader + "\r\n" + this.body;
    }

    public HttpResponse(HttpStatusCode statusCode, List<Field> header, byte[] body) {
        this.statusCode = statusCode;
        this.header = header;
        gzipCompressedBody = body;

        stringHeader = headerToString();

        completeResponse = this.statusCode.message + "\r\n" + stringHeader + "\r\n";

        compressedResponse = new byte[completeResponse.getBytes().length + gzipCompressedBody.length];

        System.arraycopy(completeResponse.getBytes(),0, compressedResponse,0,completeResponse.getBytes().length);
        System.arraycopy(gzipCompressedBody,0,compressedResponse,completeResponse.getBytes().length - 1, gzipCompressedBody.length);

        bodyIsCompressed = true;
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
        return (bodyIsCompressed) ? compressedResponse : completeResponse.getBytes();
    }

    public String getString() {
        return (bodyIsCompressed) ? (completeResponse +  gzipCompressedBody.toString()) : completeResponse;
    }

    private String headerToString() {
        StringBuilder sh = new StringBuilder();
        for (Field field : header) {
            sh.append(field.getKey() + ": " + field.getValue() + "\r\n");
        }
        return sh.toString();
    }
}
