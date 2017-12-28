package http.middleware;

import http.models.HttpRequest;
import http.models.HttpResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by George on 2017-12-27.
 */
public class ErrorHandlingMiddleware implements IRequestMiddleware {

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline) {
        try {
            return requestPipeline.continueWith(httpRequest);
        } catch (Exception e) {
            return HttpResponse.internalServerError().withBody(getStackTraceAsString(e));
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
