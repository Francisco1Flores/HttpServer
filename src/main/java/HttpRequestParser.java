import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequestParser {
    private final String path;
    private final String method;
    private final float httpVersion;

    private final Map<String, String> header = new LinkedHashMap<>();
    private String body;
    public HttpRequestParser(InputStream inputStream) throws IOException, BadRequestException {
        if (inputStream == null)
            throw new IOException();

        BufferedReader bufferIn = new BufferedReader(new InputStreamReader(inputStream));
         String firstLine = bufferIn.readLine();

        if (firstLine == null)
            throw new IOException();
        if (firstLine.isBlank())
            throw new BadRequestException();

        method = extractMethod(firstLine);
        path = extractPath(firstLine);
        httpVersion = extractHttpVersion(firstLine);
        System.out.println("\nheader:\n");
        for (String headerLine = bufferIn.readLine(); !headerLine.isEmpty(); headerLine = bufferIn.readLine()) {
            String key = headerLine.substring(0,headerLine.indexOf(':'));
            String value = headerLine.substring(headerLine.indexOf(':') + 2);

            header.put(key,value);
            System.out.println( key + ": " + value);
        }
    }

    private float extractHttpVersion(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3)
            throw new BadRequestException();

        String version = splittedFirstLIne[2].substring(5); // 5 is the index of the string "HTTP/1.1" where
                                                                      // version number starts
        return Float.parseFloat(version);
    }

    private String extractMethod(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3)
            throw new BadRequestException();
        // TODO verify if method is valid
        return splittedFirstLIne[0];
    }

    private String extractPath(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3)
            throw new BadRequestException();

        String path = splittedFirstLIne[1];
        if (path.isBlank())
            throw new BadRequestException();
        return path;
    }

    public String getPath(){return path;}

    public String getMethod(){return method;}

    public Float getHttpVersion(){return httpVersion;}

    public boolean headerKeyExist(String key) {
        return header.containsKey(key);
    }

    public String getHeaderValue(String key) {
        return header.get(key);
    }


}
