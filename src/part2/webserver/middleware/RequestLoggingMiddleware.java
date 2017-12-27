package part2.webserver.middleware;

import http.middleware.IRequestMiddleware;
import http.middleware.RequestPipeline;
import http.models.HttpRequest;
import http.models.HttpResponse;

/**
 * Created by George on 2017-12-26.
 */
public class RequestLoggingMiddleware implements IRequestMiddleware {
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline) {
        return null;
    }
}
