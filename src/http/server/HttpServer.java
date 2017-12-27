package http.server;

import http.middleware.IRequestMiddleware;
import http.middleware.RequestPipeline;
import http.models.HttpRequest;
import http.models.HttpResponse;
import http.serialization.HttpRequestDeserializer;
import http.serialization.HttpResponseSerializer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-12-26.
 */
public class HttpServer {
    public static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket;
    private List<IRequestMiddleware> middlewareLayers;

    public HttpServer(int port, List<IRequestMiddleware> middlewareLayers) {
        serverSocket = tryToBuildServerSocket(port);
        this.middlewareLayers = new ArrayList<>(middlewareLayers);
    }

    public static HttpServerBuilder create() {
        return new HttpServerBuilder();
    }

    private ServerSocket tryToBuildServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (Exception e) {
            throw new RuntimeException(); // TODO: put our own exception
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                HttpRequestDeserializer httpRequestDeserializer = new HttpRequestDeserializer(clientSocket.getInputStream());
                HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer(clientSocket.getOutputStream());

                HttpRequest request = httpRequestDeserializer.deserializeNextRequest();
                RequestPipeline requestPipeline = new RequestPipeline(middlewareLayers);

                HttpResponse response;

                try {
                    response = requestPipeline.continueWith(request);
                } catch (Exception e) {
                    response = HttpResponse.internalServerError().withBody(e.getLocalizedMessage());
                }

                httpResponseSerializer.writeResponse(response);
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
