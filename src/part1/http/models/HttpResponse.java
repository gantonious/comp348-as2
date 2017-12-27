package part1.http.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 2017-12-26.
 */
public class HttpResponse {
    private int responseCode;
    private String responseMessage;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body = new byte[0];

    public HttpResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public static HttpResponse ok() {
        return new HttpResponse(200, "OK");
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, "Not Found");
    }

    public static HttpResponse methodNotAllowed() {
        return new HttpResponse(405, "Method Not Allowed");
    }

    public static HttpResponse internalServerError() {
        return new HttpResponse(500, "Internal Server Error");
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public HttpResponse withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public boolean hasBody() {
        return body != null && body.length > 0;
    }

    public HttpResponse withBody(String body) {
        this.body = body.getBytes();
        return this;
    }

    public HttpResponse withBody(byte[] body) {
        this.body = body;
        return this;
    }
}
