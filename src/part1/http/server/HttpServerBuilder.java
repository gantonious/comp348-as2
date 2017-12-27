package part1.http.server;

import part1.http.middleware.IRequestMiddleware;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-12-26.
 */
public class HttpServerBuilder {
    private int port = HttpServer.DEFAULT_PORT;
    private List<IRequestMiddleware> middlewareLayers = new ArrayList<>();

    public HttpServerBuilder usePort(int port) {
        this.port = port;
        return this;
    }

    public HttpServerBuilder useMiddleware(IRequestMiddleware requestMiddleware) {
        middlewareLayers.add(requestMiddleware);
        return this;
    }

    public HttpServer build() {
        return new HttpServer(port, middlewareLayers);
    }

    public void run() {
        build().run();
    }
}
