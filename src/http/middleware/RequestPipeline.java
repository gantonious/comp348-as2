package http.middleware;

import http.models.HttpRequest;
import http.models.HttpResponse;

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

    public HttpResponse executeNext(HttpRequest httpRequest) {
        if (currentMiddlewareIndex < requestMiddleware.size()) {
            return requestMiddleware.get(currentMiddlewareIndex++).handleRequest(httpRequest, this);
        }
        throw new IllegalStateException("There is no more middleware to execute.");
    }
}
