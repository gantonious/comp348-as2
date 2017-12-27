package http.middleware;

import http.models.HttpRequest;
import http.models.HttpResponse;

/**
 * Created by George on 2017-12-26.
 */
public interface IRequestMiddleware {
    HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline);
}
