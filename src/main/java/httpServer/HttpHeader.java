package httpServer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class HttpHeader {
    private final LinkedHashMap<String, String> entries;

    public HttpHeader() {
        entries = new LinkedHashMap<>();
    }

    public void add(String key, String value) {
        entries.put(key, value);
    }

    public boolean containsKey(String key) {
        return entries.containsKey(key);
    }

    public boolean valuesIsNumber(String key) {
        try {
            Integer.parseInt(entries.get(key));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getIntValue(String key) {
       return Integer.parseInt(entries.get(key));
    }

    public void parseAndAdd(String line) {
        String[] s = line.split(":");
        String key = s[0].trim();
        String value = s[1].trim();
        add(key, value);
    }

    public String get(String key) {
        return entries.get(key);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public Set<Map.Entry<String, String>> getEntriesSet() {
        return entries.entrySet();
    }

    public void forEach(Consumer<Map.Entry<String,String>> consumer) {
        entries.sequencedEntrySet().forEach(consumer);
    }

    @Override
    public String toString() {
        if (entries.isEmpty()) {
            return "";
        }
        return entries.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue() + "\r\n")
                .reduce(String::concat)
                .orElse("");
    }

}
