Feature: More advanced elasticsearch tests

  Background:
    * url baseUrl
    * def OwnerRepository = Java.type("net.jvw.OwnerRepository")
    * def repo = new OwnerRepository()
    * eval repo.init(karate.properties['elasticsearch.address'])

  Scenario: Save test data to elasticsearch (initialize)
    Given url 'http://' + karate.properties['elasticsearch.address'] + '/_bulk?refresh'
    And request karate.readAsString('classpath:karate/data/test-owners.jsonl')
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

  Scenario: Verify find all
    Given path '/all'
    When method GET
    Then status 200
    And match $ == '#[180]'
