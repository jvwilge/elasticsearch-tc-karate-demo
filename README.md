# Demo project with elasticsearch 7, Testcontainers and Karate

## Checking for vulnerabilities

To run the Maven build with the _Owasp dependency check_ enabled run :
```asciidoc
mvn -Pcve clean verify
```

This will result in a report in a report in `target/dependency-check-report.html`