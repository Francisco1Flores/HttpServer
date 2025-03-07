package httpServer;

public record HttpRequest (
        HttpMethod method,
        String path,
        String httpVersion,
        HttpHeader header,
        HttpBody body
    ) {
    public void print() {
        System.out.print(this.method + " " + path + " HTTP/"+ httpVersion + "\r\n");
        header.forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
        System.out.println("\r\n" + body.toString());
    }
}
