# COMP 348 Assignment 2

I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.

Submitted by: George Antonious (3364768)

## Assumptions

- The code and Makefile was written and tested on Mac OS X

## Building Instructions

Assuming JDK 1.8 is installed on the host machine, both parts can be built by doing:

```bash
make runWebServer
```

## Part 1: HttpServer

### Usage

To run the webserver do:

```bash
make runWebServer
```

This will host the contents of `wwwroot` on `localhost:8080`.

### Design

The design of the http server was seperated into 2 major components. Serialization/Deserialization of the requests and response and actually handling the given request and converting it to the appropriate response.

At a high level when a request comes down the pipe it is deserialized to a `HttpRequest` object. That object is then passed to a `RequestPipeline` which "handles" the request and returns a `HttpResponse` object. That object is then serialized back into the Http protocol and sent over the wire back to the client.

The `RequestPipeline` consists of a list of middleware objects. A piece of middleware must implement the `IRequestMiddleware` interface which has the following method:

```java
HttpResponse handleRequest(HttpRequest httpRequest, RequestPipeline requestPipeline);
```

A piece of middleware will recieve an `HttpRequest` it has the opportunity to inspect/modify the original request before handling it. A piece of middlware can decide if it wants to handle the request or pass it off to the next piece. If it wants to directly handle the request it can immediately return an appropriate `HTTPRepsonse`. This is called shortcircuting the request pipeline. If it cannot handle the current request then it can ask the `RequestPipeline` to pass off the request to the next piece of middleware using:

```java
HttpResponse continueWith(HttpRequest httpRequest);
```

Note the above method returns an `HttpResponse`. This means that after the remaining pieces of middleware handle the request you can inspect the resulting response and even modify it. Before returning it.

I drew insipriation from ASP.NET's middleware model (https://docs.microsoft.com/en-us/aspnet/core/fundamentals/middleware/?tabs=aspnetcore2x) when working on this implementation.

To handle serving static content a middleware implementation was created named `StaticFileRequestMiddleware`. This piece of middleware attempts to handle all `GET` requests received. If it can find the file it will return and `HTTP OK` with the contents of the file. If it cannot find the file then it returns and `HTTP NOT FOUND` and short circuts the request pipeline.


### Test Plan

This assignment is a bit difficult to black box test, essentially the wwwroot/ directory is the input and the rendered webpage is the output. To test the webserver I took the source of my website (http://antonious.ca) and dropped it into the wwwroot/ folder. I then attempted to access the website through the webserver. I compared the network log avaialble on Google Chrome to the log generated by my live website hosted on Github. As seen below, this webserver implementation properly serves all the elements requested:

![Part 1 Test](/docs/part1_test.png)

The webserver was also tested using different webbrowsers and http clients. The website was properly served on all clients. The following clients were tested:

- Google Chrome
- Safari
- Mozilla Firebox
- curl

## Part 2: HttpServer with Logging

### Usage

To run the webserver do:

```bash
make runWebServerWithLogging
```

This will host the contents of `wwwroot` on `localhost:8081`. The output log file can be found in `./serverlog.txt`.

### Design

This part just extends the code already implemented for part 1. To add logging functionality a new piece of middleware was added to inspect the request and response objects for a given request and log details about them to an external log file.

When the `RequestLoggingMiddleware` recevies a request it immediately passes off the request to the rest of the pipeline using the `contineWith` method. It then takes the response object returned from the rest of the middleware and in conjuction with the reqeust object it builds a log entry and saves it to disk.

This piece of middleware is then plugged in to the existing `HttpServerBuilder` to add the new functionality to the server without changing any existing code.

### Test Plan

The only new functionality introduced in this part was server logging. To test that the log file was legally formatted it was passed into the web log analyzer implemented in assignment 1. The generated weblog was succesfully parsed and analyzed.

The output of the log file was also compared to the output of the Google Chrome network log. They showed equivalent information for the content served locally (not including content from CDNs for example). See the comparision below (note the weblog screenshot was cropped to focus on the request path and result):

![Part 2 test](/docs/part2_test.png)