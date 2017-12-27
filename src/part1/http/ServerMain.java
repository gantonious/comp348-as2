package part1.http;

import part1.http.server.HttpServer;

/**
 * Created by George on 2017-12-26.
 */
public class ServerMain {

    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create().port(1824).build();

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
