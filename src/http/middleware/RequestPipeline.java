package http.middleware;

import http.models.HttpRequest;
import http.models.HttpResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-12-26.
 */
public class RequestPipeline {
    private int currentMiddlewareIndex = 0;
    private List<IRequestMiddleware> requestMiddleware;

    public RequestPipeline(List<IRequestMiddleware> requestMiddleware) {
        this.requestMiddleware = new ArrayList<>();
        this.requestMiddleware.add(new PoweredByMiddleware());
        this.requestMiddleware.addAll(requestMiddleware);
        this.requestMiddleware.add(new DefaultRequestMiddleware());
    }

    public HttpResponse continueWith(HttpRequest httpRequest) {
        if (currentMiddlewareIndex < requestMiddleware.size()) {
            try {
                return requestMiddleware.get(currentMiddlewareIndex++).handleRequest(httpRequest, this);
            } catch (Exception e) {
                return HttpResponse.internalServerError().withBody(getStackTraceAsString(e));
            }
        }
        throw new IllegalStateException("There is no more middleware to execute.");
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
