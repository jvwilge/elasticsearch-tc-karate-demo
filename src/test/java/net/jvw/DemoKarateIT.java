package net.jvw;

import com.intuit.karate.junit5.Karate;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DemoKarateIT {

  private static final Logger LOG = LoggerFactory.getLogger(DemoKarateIT.class);

  private static ElasticsearchContainer container;

  private static Vertx vertx;

  private static VertxTestContext testContext = new VertxTestContext();

  @BeforeAll
  static void beforeAll() throws IOException {
    System.setProperty("karate.env", "test");

    String url = "docker.elastic.co/elasticsearch/elasticsearch:" + AutoDetectElasticVersion.detectHighestVersion();
    container = new ElasticsearchContainer(url);
    container.start();
    String elasticsearchAddress = container.getHttpHostAddress();
    System.setProperty("elasticsearch.address", elasticsearchAddress);
    LOG.info("Elasticsearch container started: {}", elasticsearchAddress);
    OwnerRepository ownerRepository = new OwnerRepository();
    ownerRepository.init(elasticsearchAddress);

    vertx = Vertx.vertx();
    Integer vertxPort = findRandomPort();
    System.setProperty("vertx.port", "" + vertxPort);
    DemoVerticle demoVerticle = new DemoVerticle(vertx, ownerRepository, vertxPort);
    vertx.deployVerticle(demoVerticle, testContext.succeeding());
  }

  @AfterAll
  static void afterAll() throws Throwable {
    container.stop();
    vertx.close(testContext.completing());

    assertThat(testContext.awaitCompletion(2, TimeUnit.SECONDS)).isTrue();
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }

  @Karate.Test
  Karate testAll() {
    return new Karate()
        .feature("classpath:karate")
        .tags("~@ignore");
  }

  private static Integer findRandomPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }

}
