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
            this.logfileDataOutputStream = new DataOutputStream(new FileOutputStream(logFilePath, true));
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

        HttpResponse httpResponse = requestPipeline.continueWith(httpRequest);
        int responseCode = httpResponse.getResponseCode();
        int bytesTransmitted = httpResponse.getBody().length;

        String logEntry = String.format("%s - - [%s] \"%s %s HTTP/1.1\" %d %d\n",
                host, timeStamp, requestMethod, path, responseCode, bytesTransmitted);

        logToConsole(logEntry, httpResponse);
        logToFile(logEntry);

        return httpResponse;
    }

    private void logToConsole(String logMessage, HttpResponse httpResponse) {
        if (httpResponse.isServerError()) {
            System.err.print(logMessage);
        } else {
            System.out.print(logMessage);
        }
    }

    private void logToFile(String logMessage) {
        try {
            logfileDataOutputStream.write(logMessage.getBytes());
        } catch (Exception ex) {
            // no-op: it's okay we still pushed the log entry to stdout
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        return simpleDateFormat.format(new Date());
    }
}
