Feature: Basic elasticsearch tests

  Background:

  Scenario: Verify elasticsearch is running
    Given url 'http://' + karate.properties['elasticsearch.address'] + '/'
    When method GET
    Then status 200
    And match $.tagline == 'You Know, for Search'
