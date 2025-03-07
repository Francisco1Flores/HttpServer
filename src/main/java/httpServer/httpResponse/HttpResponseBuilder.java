package httpServer.httpResponse;

import httpServer.HttpBody;
import httpServer.HttpHeader;
import httpServer.HttpStatusCode;

public class HttpResponseBuilder {

    private final HttpResponse response;

    public HttpResponseBuilder() {
        response = new HttpResponse();
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public HttpResponse build() {
        return response;
    }

    public HttpResponseBuilder statusCode(HttpStatusCode c) {
        response.setStatusCode(c);
        return this;
    }

    public HttpResponseBuilder header(HttpHeader header) {
        response.setHeader(header);
        return this;
    }

    public HttpResponseBuilder addHeaderEntry(String key, String value) {
        response.addHeader(key, value);
        return this;
    }

    public HttpResponseBuilder body(HttpBody body) {
        response.setBody(body);
        return this;
    }

    public HttpResponseBuilder body(String body) {
        response.setBody(body);
        return this;
    }

    public HttpResponseBuilder compressedBody(byte[] body) {
        response.setCompressedBody(body);
        return this;
    }

    public HttpResponseBuilder ok() {
        response.setStatusCode(HttpStatusCode.OK);
        return this;
    }

    public HttpResponseBuilder created() {
        response.setStatusCode(HttpStatusCode.CREATED);
        return this;
    }

    public HttpResponseBuilder notFound() {
        response.setStatusCode(HttpStatusCode.NOT_FOUND);
        return this;
    }
}
