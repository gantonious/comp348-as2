package http.serialization;

import http.models.HttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by George on 2017-12-26.
 */
public class HttpResponseSerializer {
    private DataOutputStream dataOutputStream;

    public HttpResponseSerializer(OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    public void writeResponse(HttpResponse httpResponse) {
        try {
            tryToWriteResponse(httpResponse);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    private void tryToWriteResponse(HttpResponse httpResponse) throws Exception {
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
