package net.jvw;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

class DemoKarateIT {

  private static final Logger LOG = LoggerFactory.getLogger(DemoKarateIT.class);

  private static ElasticsearchContainer container;

  @BeforeAll
  static void beforeAll() {
    System.setProperty("karate.env", "test");
    String url = "docker.elastic.co/elasticsearch/elasticsearch:" + AutoDetectElasticVersion.detectHighestVersion();
    container = new ElasticsearchContainer(url);
    container.start();
    String httpHostAddress = container.getHttpHostAddress();
    System.setProperty("elasticsearch.address", httpHostAddress);
    LOG.info("Elasticsearch container started: {}", httpHostAddress);
  }

  @AfterAll
  static void afterAll() {
    container.stop();
  }

  @Karate.Test
  Karate testAll() {
    return new Karate()
        .feature("classpath:karate")
        .tags("~@ignore");
  }

}
