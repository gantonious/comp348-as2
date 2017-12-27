package http.serialization;

import http.models.HttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by George on 2017-12-26.
 */
public class HttpResponseSerializer {

    public void writeTo(HttpResponse httpResponse, OutputStream outputStream) {
        try {
            tryToWriteTo(httpResponse, outputStream);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    private void tryToWriteTo(HttpResponse httpResponse, OutputStream outputStream) throws Exception {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        byte[] responseLine = String.format("HTTP/1.1 %d %s\r\n",
                httpResponse.getResponseCode(),
                httpResponse.getResponseMessage()).getBytes();

        dataOutputStream.write(responseLine);

        for (Map.Entry<String, String> headerItem : httpResponse.getHeaders().entrySet()) {
            byte[] headerLine = String.format("%s: %s\r\n", headerItem.getKey(), headerItem.getValue()).getBytes();
            dataOutputStream.write(headerLine);
        }

        if (httpResponse.hasBody()) {
            dataOutputStream.write("\r\n".getBytes());
            dataOutputStream.write(httpResponse.getBody());
            dataOutputStream.write("\r\n".getBytes());
        } else {
            dataOutputStream.write("\r\n".getBytes());
        }
    }
}