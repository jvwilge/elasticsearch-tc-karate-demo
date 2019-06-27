(function () {
    var env = karate.env; // get java system property 'karate.env'

    if (!env) {
        env = 'local'; //no environment found, assuming local (laptop)
        karate.properties['elasticsearch.address'] = 'localhost:9200'
    }

    karate.log('karate.env property:', env);

    var config = {};

    return config;
});