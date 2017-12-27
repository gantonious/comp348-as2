package part1.http.server;

/**
 * Created by George on 2017-12-26.
 */
public class HttpServerBuilder {
    private int port = HttpServer.DEFAULT_PORT;

    public HttpServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public HttpServer build() {
        return new HttpServer(port);
    }

    public void run() {
        build().run();
    }
}
