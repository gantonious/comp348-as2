package http.middleware;

import http.models.HttpRequest;
import http.models.HttpResponse;

/**
 * Created by George on 2017-12-26.
 */
public class PoweredByMiddleware implements IRequestMiddleware {
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline) {
        return requestPipeline
                .executeNext(httpRequest)
                .withHeader("Powered-By", "George Antonious");
    }
}
