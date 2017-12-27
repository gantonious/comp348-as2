package part1.webserver.middleware;

import part1.http.middleware.IRequestMiddleware;
import part1.http.middleware.RequestPipeline;
import part1.http.models.HttpRequest;
import part1.http.models.HttpResponse;
import part1.webserver.MimeTypeUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by George on 2017-12-26.
 */
public class StaticFileRequestMiddleware implements IRequestMiddleware {
    public final static String DEFAULT_WEBROOT = "./src/part1/webserver/wwwroot";

    private String webRoot;

    public StaticFileRequestMiddleware() {
        this(DEFAULT_WEBROOT);
    }

    public StaticFileRequestMiddleware(String webRoot) {
        this.webRoot = webRoot;
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline) {
        if (canHandle(httpRequest)) {
            return handleRequest(httpRequest);
        }
        return requestPipeline.executeNext(httpRequest);
    }

    private boolean canHandle(HttpRequest httpRequest) {
        return httpRequest.hasMethod("GET");
    }

    private HttpResponse handleRequest(HttpRequest httpRequest) {
        if (httpRequest.getPath().endsWith("/")) {
            return handleFileRequest(resolvePath(httpRequest.getPath() + "index.html"));
        }
        return handleFileRequest(resolvePath(httpRequest.getPath()));
    }

    private boolean isPathWithinWebRoot(String path) {
        Path requestPath = Paths.get(path);
        Path webRootPath = Paths.get(webRoot);

        try {
            return requestPath.toRealPath().startsWith(webRootPath.toRealPath());
        } catch (Exception e) {
            return false;
        }
    }

    private String resolvePath(String relativePath) {
        return Paths.get(webRoot, relativePath).toString();
    }

    private HttpResponse handleFileRequest(String filePath) {
        if (!isPathWithinWebRoot(filePath)) {
            return HttpResponse.notFound();
        }

        try {
            Path pathToFile = Paths.get(filePath);

            return HttpResponse.ok()
                    .withHeader("Content-Type", MimeTypeUtils.getContentTypeFrom(pathToFile.getFileName().toString()))
                    .withHeader("Content-Length", String.valueOf(Files.size(pathToFile)))
                    .withBody(Files.readAllBytes(pathToFile));
        } catch (Exception e) {
            return HttpResponse.notFound();
        }
    }
}
