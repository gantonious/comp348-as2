package part2.webserver.middleware;

import http.middleware.IRequestMiddleware;
import http.middleware.RequestPipeline;
import http.models.HttpRequest;
import http.models.HttpResponse;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by George on 2017-12-26.
 */
public class RequestLoggingMiddleware implements IRequestMiddleware {
    public DataOutputStream logfileDataOutputStream;

    public RequestLoggingMiddleware(String logFilePath) {
        try {
            this.logfileDataOutputStream = new DataOutputStream(new FileOutputStream(logFilePath));
        } catch (Exception e) {
            throw new RuntimeException("Could not open log file");
        }
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline) {
        String host = httpRequest.getHeader("Host");
        String requestMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();
        String timeStamp = getCurrentTimestamp();

        HttpResponse httpResponse = requestPipeline.executeNext(httpRequest);
        int responseCode = httpResponse.getResponseCode();
        int bytesTransmitted = httpResponse.getBody().length;

        String logEntry = String.format("%s - - [%s] \"%s %s HTTP/1.1\" %d %d\n",
                host, timeStamp, requestMethod, path, responseCode, bytesTransmitted);

        try {
            logfileDataOutputStream.write(logEntry.getBytes());
        } catch (Exception ex) {
            // TODO: handle
        }

        return httpResponse;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        return simpleDateFormat.format(new Date());
    }
}
