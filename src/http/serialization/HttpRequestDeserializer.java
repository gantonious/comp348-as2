package http.serialization;

import http.models.HttpRequest;

import java.io.*;

/**
 * Created by George on 2017-12-26.
 */
public class HttpRequestDeserializer {
    private BufferedReader bufferedReader;

    public HttpRequestDeserializer(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    public HttpRequest deserializeNextRequest() {
        try {
            return tryToDeserializeNextRequest();
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    private HttpRequest tryToDeserializeNextRequest() throws Exception {
        String requestLine = bufferedReader.readLine();
        String[] requestTokens = requestLine.split(" ");
        HttpRequest httpRequest = new HttpRequest(requestTokens[0], requestTokens[1]);

        while (true) {
            String nextLine = bufferedReader.readLine();

            if (nextLine.trim().isEmpty()) {
                break;
            }

            String[] headerTokens = nextLine.split(":");
            httpRequest.withHeader(headerTokens[0].trim(), headerTokens[1].trim());
        }

        return httpRequest;
    }
}
