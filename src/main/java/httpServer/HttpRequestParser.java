package httpServer;

import httpServer.exeption.BadRequestException;
import httpServer.logger.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpRequestParser {
    private final InputStream inputStream;

    public HttpRequestParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HttpRequest parse() throws IOException, BadRequestException {
        if (inputStream == null) {
            throw new IOException("input stream null");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String firstLine = reader.readLine();
        var a = new BufferedInputStream(inputStream);

        if (firstLine == null) {
            throw new IOException("First line null");
        }
        if (firstLine.isBlank()) {
            throw new BadRequestException();
        }
        final HttpMethod method = extractMethod(firstLine);
        final String path = extractPath(firstLine);
        final String httpVersion = extractHttpVersion(firstLine);

        HttpHeader header = parseHeader(reader);

        if (!header.containsKey("Content-Length")
            && inputStream.available() == 0) {  // Stream end reached. There isn´t body
            return new HttpRequest(
                    method,
                    path,
                    httpVersion,
                    header,
                    HttpBody.emptyBody()
            );
        }

        Logger.info("Procesando body");
        HttpBody body = parseBody(reader, header);
        return new HttpRequest(
                method,
                path,
                httpVersion,
                header,
                body
        );
    }

    public HttpBody parseBody(BufferedReader reader, HttpHeader header) throws IOException {
        if (header.containsKey("Transfer-Encoding")) {
            // TODO: decodificar distintos tipos de codificado
            if (header.get("Transfer-Encoding").contains("chunked")) {
                System.out.println("Encontro transfer chunked");
                return parseChunkedBody(reader);
            }
            return null;
        } else {
            int bodySize = header.containsKey("Content-Length") ? header.getIntValue("Content-Length") : 0;
            char[] charBuff = new char[bodySize];
            int bytesRead = reader.read(charBuff);
            if (bytesRead != bodySize) {
                //TODO: devolver un error al leer el body
            }
            return new HttpBody(String.valueOf(charBuff));
        }
    }

    public HttpBody parseChunkedBody(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int chunkSize = Integer.parseInt(reader.readLine(),16);
        while (chunkSize != 0) {
            char[] buff = new char[chunkSize];
            int bytesRead = reader.read(buff,0,chunkSize);

            if (bytesRead == -1) {
                throw new IOException("Unexpected end of file");
            }

            sb.append(buff);

            reader.readLine();
            chunkSize = Integer.parseInt(reader.readLine(), 16);
        }
        return new HttpBody(sb.toString());
        /*
        Codigo que lee los chunk en bytes

        StringBuilder sb = new StringBuilder();
        int buffSize = 4096;
        byte[] buff1 = new byte[buffSize];
        int bytes = inputStream.read(buff1);
        boolean inputStreamEnd = bytes == -1;
        System.out.println("byes read: " + bytes);
        int chunkSize = -1;
        while (!inputStreamEnd) {
            int start = 0;
            int current = 0;

            while (current < buffSize && chunkSize != 0) {
                while (buff1[current] != 13 && buff1[current + 1] != 10) {
                    current++;
                }
                chunkSize = Integer.parseInt(
                        new String(buff1, start, current -1, StandardCharsets.UTF_8)
                        ,16);
                current += 2;

                sb.append(new String(buff1, start, chunkSize, StandardCharsets.UTF_8));
                current += 3;
                start = current;
            }

            bytes = inputStream.read(buff1);
            inputStreamEnd = bytes == -1;
        }
        return new HttpBody(sb.toString());
         */
    }

    public HttpHeader parseHeader(BufferedReader reader) throws IOException {
        HttpHeader header = new HttpHeader();
        String line;
        while(!(line = reader.readLine()).isBlank()) {
            header.parseAndAdd(line);
        }
        return header;
    }

    private String extractHttpVersion(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3) {
            throw new BadRequestException();
        }
        return splittedFirstLIne[2].trim().substring(5); // 5 is the index of the string "HTTP/1.1" where
    }

    private HttpMethod extractMethod(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3) {
            throw new BadRequestException();
        }
        return switch(splittedFirstLIne[0]) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "UPDATE" -> HttpMethod.UPDATE;
            case "DELETE" -> HttpMethod.DELETE;
            case "PATCH" -> HttpMethod.PATCH;
            default -> HttpMethod.NOT_SUPORTED;
        };
    }

    private String extractPath(String firstLine) throws BadRequestException {
        String[] splittedFirstLIne = firstLine.split(" ");
        if (splittedFirstLIne.length != 3) {
            throw new BadRequestException();
        }

        String path = splittedFirstLIne[1];
        if (path.isBlank()) {
            throw new BadRequestException();
        }
        return path;
    }
}
