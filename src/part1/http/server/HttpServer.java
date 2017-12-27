package part1.http.server;

import part1.http.models.HttpRequest;
import part1.http.models.HttpResponse;
import part1.http.serialization.HttpRequestDeserializer;
import part1.http.serialization.HttpResponseSerializer;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by George on 2017-12-26.
 */
public class HttpServer {
    public static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket;

    public HttpServer(int port) {
        serverSocket = tryToBuildServerSocker(port);
    }

    public static HttpServerBuilder create() {
        return new HttpServerBuilder();
    }

    private ServerSocket tryToBuildServerSocker(int port) {
        try {
            return new ServerSocket(port);
        } catch (Exception e) {
            throw new RuntimeException(); // TODO: put our own exception
        }
    }

    public void run() {
        HttpRequestDeserializer httpRequestDeserializer = new HttpRequestDeserializer();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                HttpRequest request = httpRequestDeserializer.deserializeFrom(clientSocket.getInputStream());

                HttpResponse response = HttpResponse.ok()
                        .withHeader("Location", request.getPath())
                        .withHeader("Method", request.getMethod())
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Powered-By", "GERG")
                        .withBody("{ \"wapoz\": true }");

                httpResponseSerializer.writeTo(response, clientSocket.getOutputStream());
                clientSocket.close();

            } catch (Exception e) {
                continue;
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            // TODO: look
        }
    }
}
