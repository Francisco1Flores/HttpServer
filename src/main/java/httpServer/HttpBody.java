package httpServer;

import java.nio.charset.StandardCharsets;

public class HttpBody {
    private final String body;
    private final byte[] bytes;

    public HttpBody(String body) {
        this.body = body;
        bytes = body.getBytes(StandardCharsets.UTF_8);
    }

    public int size() {
        return bytes.length;
    }

    public static HttpBody emptyBody() {
        return new HttpBody("");
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return body;
    }
}
