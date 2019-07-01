package net.jvw;

import io.vertx.core.Vertx;

public class Runner {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    OwnerRepository ownerRepository = new OwnerRepository();
    ownerRepository.init("localhost:9200");

    DemoVerticle demoVerticle = new DemoVerticle(vertx, ownerRepository, 8080);
    vertx.deployVerticle(demoVerticle);
  }

}
