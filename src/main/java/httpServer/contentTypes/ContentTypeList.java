package httpServer.contentTypes;

import java.util.HashMap;

public final class ContentTypeList {
    private ContentTypeList() {}

    private static final HashMap<String, ContentType> types = new HashMap<>();

    static {
        types.put("html", ContentType.HTML);
        types.put("txt", ContentType.PLAIN);
    }

    public static ContentType get(String key) {
        return types.get(key);
    }
}
