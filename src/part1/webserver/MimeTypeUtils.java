package part1.webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 2017-12-26.
 */
public class MimeTypeUtils {
    private static Map<String, String> mimeTypeMap = new HashMap<>();

    static {
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("css", "text/css");
        mimeTypeMap.put("js", "text/javascript");
        mimeTypeMap.put("jpg", "image/jpeg");
        mimeTypeMap.put("png", "image/png");
    }

    public static String getContentTypeFrom(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return mimeTypeMap.getOrDefault(extension, "text");
    }
}
