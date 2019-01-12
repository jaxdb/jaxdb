# OpenJAX RDB Maven Archetype

> Quick-start Maven Archetype for RDB framework

[![Build Status](https://travis-ci.org/openjax/rdb.png)](https://travis-ci.org/openjax/rdb)

### Introduction

The `rdb-maven-archetype` archetype is a quick-start example of how to use the [`rdb`][rdb] framework.

### Usage Overview

To use the archetype, execute the following:

  ```tcsh
  mvn archetype:generate \
  -DgroupId=com.mycompany.app -DartifactId=my-app \
  -DarchetypeGroupId=org.openjax.maven.archetype -DarchetypeArtifactId=rdb-maven-archetype \
  -DarchetypeCatalog=http://mvn.repo.openjax.org -DinteractiveMode=false
  ```

### JavaDocs

JavaDocs are available [here](https://rdb.openjax.org/apidocs/).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-archetype]: https://img.shields.io/badge/mvn-archetype-yellow.svg
[rdb]: /