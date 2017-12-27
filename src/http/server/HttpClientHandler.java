package http.server;

import http.middleware.IRequestMiddleware;
import http.middleware.RequestPipeline;
import http.models.HttpRequest;
import http.models.HttpResponse;
import http.serialization.HttpRequestDeserializer;
import http.serialization.HttpResponseSerializer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-12-27.
 */
public class HttpClientHandler {
    private List<IRequestMiddleware> middlewareLayers;
    private HttpRequestDeserializer httpRequestDeserializer;
    private HttpResponseSerializer httpResponseSerializer;

    public HttpClientHandler(Socket clientSocket, List<IRequestMiddleware> requestMiddleware) throws Exception {
        middlewareLayers = new ArrayList<>(requestMiddleware);
        httpRequestDeserializer = new HttpRequestDeserializer(clientSocket.getInputStream());
        httpResponseSerializer = new HttpResponseSerializer(clientSocket.getOutputStream());
    }

    public void serveClient() {
        try {
            HttpRequest httpRequest = httpRequestDeserializer.deserializeNextRequest();
            RequestPipeline requestPipeline = new RequestPipeline(middlewareLayers);
            HttpResponse httpResponse = requestPipeline.continueWith(httpRequest);
            httpResponseSerializer.writeResponse(httpResponse);
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));

            HttpResponse internalServerErrorResponse = HttpResponse
                    .internalServerError()
                    .withBody(stringWriter.toString());

            httpResponseSerializer.writeResponse(internalServerErrorResponse);
        }
    }
}
