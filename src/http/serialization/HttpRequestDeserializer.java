package http.serialization;

import http.models.HttpRequest;

import java.io.*;

/**
 * Created by George on 2017-12-26.
 */
public class HttpRequestDeserializer {

    public HttpRequest deserializeFrom(InputStream inputStream) {
        try {
            return tryToDeserializeFrom(inputStream);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    private HttpRequest tryToDeserializeFrom(InputStream inputStream) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String requestLine = bufferedReader.readLine();
        String[] requestTokens = requestLine.split(" ");
        HttpRequest httpRequest = new HttpRequest(requestTokens[0], requestTokens[1]);

        while (true) {
            String nextLine = bufferedReader.readLine();

            if (nextLine.trim().isEmpty()) {
                break;
            }

            String[] headerTokens = nextLine.split(":");
            httpRequest.withHeader(headerTokens[0], headerTokens[1]);
        }

        // TODO: parse body

        return httpRequest;
    }
}
