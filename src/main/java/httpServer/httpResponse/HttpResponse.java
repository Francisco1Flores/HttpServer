package httpServer.httpResponse;

import httpServer.HttpBody;
import httpServer.HttpHeader;
import httpServer.HttpStatusCode;

public class HttpResponse {

    private HttpStatusCode statusCode;
    private HttpHeader header;
    private HttpBody body;
    private byte[] compressedBody;
    private boolean isCompressed = false;

    public HttpResponse() {}

    public HttpResponse(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String key, String value) {
        if (header == null) {
            header = new HttpHeader();
        }
        header.add(key, value);
    }

    public void setStatusCode(HttpStatusCode code) {
        this.statusCode = code;
    }

    public void setBody(HttpBody body) {
        this.body = body;
    }

    public void setBody(String body) {
        this.body = new HttpBody(body);
    }

    public void setHeader(HttpHeader header) {
        this.header = header;
    }

    public void setCompressedBody(byte[] compressedBody) {
        this.compressedBody = compressedBody;
        isCompressed = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(statusCode.message).append("\r\n");
        if (header != null) {
            sb.append(header.toString());
        }
        sb.append("\r\n");
        if ( body != null) {
            sb.append(body.toString());
        }
        return sb.toString();
    }

    public byte[] getBytes() {
         return isCompressed ? toCompressBytes() : this.toString().getBytes();
    }

    private byte[] toCompressBytes() {
        byte[] a = this.toString().getBytes();
        byte[] compressedResponse = new byte[a.length + compressedBody.length];
        System.arraycopy(
                a,
               0,
                compressedResponse
                ,0,
                a.length);
        System.arraycopy(
                compressedBody
                ,0
                ,compressedResponse
                ,a.length
                ,compressedBody.length);
        return compressedResponse;
    }

    public static HttpResponseBuilder builder () {
        return new HttpResponseBuilder();
    }
}
