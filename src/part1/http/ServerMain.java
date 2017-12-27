package part1.http;

import part1.http.server.HttpServer;

/**
 * Created by George on 2017-12-26.
 */
public class ServerMain {
    public static void main(String[] args) {
        HttpServer.create()
                .port(8088)
                .run();
    }
}
