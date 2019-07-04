@ignore
# @ignore because this files does not run standalone, only as part of other feature files
Feature: Load testdata with refresh

Scenario:

Given url 'http://' + karate.properties['elasticsearch.address'] + '/_bulk?refresh'
And request karate.readAsString('classpath:karate/data/' + fileName)
And header Content-Type = 'application/json'
When method POST
Then status 200