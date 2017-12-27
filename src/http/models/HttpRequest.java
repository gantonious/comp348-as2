package http.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 2017-12-26.
 */
public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body = new byte[0];

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public boolean hasMethod(String method) {
        return getMethod().equalsIgnoreCase(method);
    }

    public HttpRequest withMethod(String method) {
        this.method = method;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HttpRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean hasHeader(String key) {
        return headers.containsKey(key);
    }

    public HttpRequest withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}
