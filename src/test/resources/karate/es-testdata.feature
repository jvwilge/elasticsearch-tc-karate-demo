Feature: More advanced elasticsearch tests

  Background:
    * url baseUrl
    * def OwnerRepository = Java.type("net.jvw.OwnerRepository")
    * def repo = new OwnerRepository()
    * eval repo.init(karate.properties['elasticsearch.address'])
    * callonce read('es-load-testdata.feature') { 'fileName' : 'test-owners.jsonl' }

  Scenario: Verify find all
    Given path '/all'
    When method GET
    Then status 200
    And match $ == '#[180]'

  Scenario: Verify Hans Gruber drives a Peugeot
    Given path '/gruber'
    When method GET
    Then status 200
    And match $ contains any { 'firstName' : 'Hans', 'lastName' : 'Gruber', 'car' : 'Peugeot', 'id' : '#notnull' }