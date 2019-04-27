# OpenJAX RDB Maven Archetype

> Quick-start Maven Archetype for RDB framework

[![Build Status](https://travis-ci.org/openjax/rdb.png)](https://travis-ci.org/openjax/rdb)
[![Coverage Status](https://coveralls.io/repos/github/openjax/rdb/badge.svg)](https://coveralls.io/github/openjax/rdb)
[![Javadocs](https://www.javadoc.io/badge/org.openjax.rdb/rdb-maven-archetype.svg)](https://www.javadoc.io/doc/org.openjax.rdb/rdb-maven-archetype)
[![Released Version](https://img.shields.io/maven-central/v/org.openjax.rdb/rdb-maven-archetype.svg)](https://mvnrepository.com/artifact/org.openjax.rdb/rdb-maven-archetype)

### Introduction

The `rdb-maven-archetype` archetype is a quick-start example of how to use the [`rdb`][rdb] framework.

### Usage Overview

To use the archetype, execute the following:

  ```bash
  mvn archetype:generate \
  -DgroupId=com.mycompany.app -DartifactId=my-app \
  -DarchetypeGroupId=org.openjax.maven.archetype -DarchetypeArtifactId=rdb-maven-archetype \
  -DarchetypeCatalog=http://mvn.repo.openjax.org -DinteractiveMode=false
  ```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-archetype]: https://img.shields.io/badge/mvn-archetype-yellow.svg
[rdb]: /