package part2.webserver;

import http.middleware.ErrorHandlingMiddleware;
import http.server.HttpServer;
import part1.webserver.middleware.StaticFileRequestMiddleware;
import part2.webserver.middleware.RequestLoggingMiddleware;

/**
 * Created by George on 2017-12-26.
 */
public class ServerMain {
    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create()
                .usePort(8081)
                .useMiddleware(new RequestLoggingMiddleware("src/part2/webserver/serverlog.txt"))
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
