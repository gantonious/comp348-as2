package http.server;

import http.middleware.IRequestMiddleware;
import http.middleware.RequestPipeline;
import http.models.HttpRequest;
import http.models.HttpResponse;
import http.serialization.HttpRequestDeserializer;
import http.serialization.HttpResponseSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-12-27.
 */
public class HttpClientHandler implements Closeable {

    private Socket clientSocket;
    private HttpRequestDeserializer httpRequestDeserializer;
    private HttpResponseSerializer httpResponseSerializer;
    private List<IRequestMiddleware> middlewareLayers;


    public HttpClientHandler(Socket clientSocket, List<IRequestMiddleware> requestMiddleware) throws IOException {
        this.clientSocket = clientSocket;
        httpRequestDeserializer = new HttpRequestDeserializer(clientSocket.getInputStream());
        httpResponseSerializer = new HttpResponseSerializer(clientSocket.getOutputStream());
        middlewareLayers = new ArrayList<>(requestMiddleware);
    }

    public void serveClient() throws IOException {
        handleNextRequest();
        close();
    }

    private void handleNextRequest() {
        HttpRequest httpRequest = httpRequestDeserializer.deserializeNextRequest();
        RequestPipeline requestPipeline = new RequestPipeline(middlewareLayers);
        HttpResponse httpResponse = requestPipeline.continueWith(httpRequest);
        httpResponseSerializer.writeResponse(httpResponse);
    }

    @Override
    public void close() throws IOException {
        clientSocket.close();
    }
}
