package part1.http.server;

import java.net.ServerSocket;

/**
 * Created by George on 2017-12-26.
 */
public class HttpServer {
    public ServerSocket serverSocket;

    public HttpServer() {
        serverSocket = new ServerSocket(2);
    }
}
