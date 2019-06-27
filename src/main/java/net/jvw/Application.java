package net.jvw;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.io.IOException;

/**
 * DO NOT USE THIS AS PRODUCTION CODE. IT IS A DIRTY SOLUTION WITH BLOCKING CODE AND SHOULD ONLY BE USED FOR DEMO PURPOSES
 */
public class Application {

  private static OwnerRepository repository;

  public static void main(String[] args) throws IOException {
    repository = new OwnerRepository();
    repository.init("localhost:9200");
    initVertx();
  }

  private static void initVertx() {
    Vertx vertx = Vertx.vertx();
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    Route route = router.get("/all").produces("application/json").handler(event ->
        event
            .response()
            .putHeader("Content-Type", "application/json")
            .end(repository.findAll())
    );

    router.route().handler(routingContext -> {

      // This handler will be called for every request
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/plain");

      // Write to the response and end it
      response.end("Hello World from Vert.x-Web!");
    });

    server.requestHandler(router).listen(8080);
  }

}
