package part1.webserver;

import http.middleware.ErrorHandlingMiddleware;
import http.middleware.PoweredByMiddleware;
import http.server.HttpServer;
import part1.webserver.middleware.StaticFileRequestMiddleware;

/**
 * Created by George on 2017-12-26.
 */
public class ServerMain {

    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create()
                .usePort(8080)
                .useMiddleware(new ErrorHandlingMiddleware())
                .useMiddleware(new StaticFileRequestMiddleware())
                .build();

        createShutdownHook(httpServer);
        httpServer.run();
    }

    private static void createShutdownHook(final HttpServer httpServer) {
        Runnable shutdownRunnable = new Runnable() {
            @Override
            public void run() {
                httpServer.stop();
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownRunnable));
    }
}
