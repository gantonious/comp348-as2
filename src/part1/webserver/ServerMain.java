package part1.webserver;

import http.middleware.ErrorHandlingMiddleware;
import http.server.HttpServer;
import part1.webserver.middleware.StaticFileRequestMiddleware;

/**
 * title: ServerMain.java
 * description: The main entry for the original WebServer implementation
 * date: February 26, 2018
 * @author George Antonious
 * @version 1.0
 * @copyright 2018 George Antonious
 *
 * I declare that this assignment is my own work and that all material
 * previously written or published in any source by any other person
 * has been duly acknowledged in the assignment. I have not submitted
 * this work, or a significant part thereof, previously as part of any
 * academic program. In submitting this assignment I give permission to
 * copy it for assessment purposes only.
 *
 * The usage, design, and test plan for this part can be found in the
 * README.md file in the root of this project. It is recommended to view
 * it in a markdown reader.
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
