package net.jvw;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(DemoVerticle.class);

  private OwnerRepository repository;
  private int port;

  public DemoVerticle(Vertx vertx, OwnerRepository repository, int port) {
    this.vertx = vertx;
    this.repository = repository;
    this.port = port;
  }

  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.get("/all").produces("application/json").handler(event ->
        event
            .response()
            .putHeader("Content-Type", "application/json")
            .end(repository.findAll())
    );

    router.get("/:lastName").produces("application/json").handler(event -> {
          String lastName = event.request().getParam("lastName");

          event
              .response()
              .putHeader("Content-Type", "application/json")
              .end(repository.findByLastName(lastName));
        }
    );

    server.requestHandler(router).listen(port);
    LOG.info("Demo Verticle started on port {} ", port);
  }

}
