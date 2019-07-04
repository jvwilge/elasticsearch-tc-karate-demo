Feature: More advanced elasticsearch tests

  Background:
    * url baseUrl
    * callonce read('es-load-testdata.feature') { 'fileName' : 'test-owners.jsonl' }

  Scenario: Verify find all
    Given path '/all'
    When method GET
    Then status 200
    And match $ == '#[180]'

  Scenario: Verify Hans Gruber drives a Peugeot (might return other Grubers)
    Given path '/gruber'
    When method GET
    Then status 200
    And match $ contains any { 'firstName' : 'Hans', 'lastName' : 'Gruber', 'car' : 'Peugeot', 'id' : '#notnull' }