package httpServer.contentTypes;

public enum ContentType {

    HTML("text/html"),
    PLAIN("text/html");

    private final String s;

    private ContentType(String s) {
        this.s = s;
    }

    public String text() {
        return s;
    }
}
