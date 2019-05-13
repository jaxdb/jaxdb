# JAX-DB Maven Archetype

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.png)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/jaxdb-maven-archetype.svg)](https://www.javadoc.io/doc/org.jaxdb/jaxdb-maven-archetype)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/jaxdb-maven-archetype.svg)](https://mvnrepository.com/artifact/org.jaxdb/jaxdb-maven-archetype)

## Introduction

The `jaxdb-maven-archetype` archetype is a quick-start example of how to use the [JAX-DB][jaxdb] framework.

## Usage Overview

To use the archetype, execute the following:

```bash
mvn archetype:generate \
-DgroupId=com.mycompany.app -DartifactId=my-app \
-DarchetypeGroupId=org.jaxdb -DarchetypeArtifactId=jaxdb-maven-archetype \
-DinteractiveMode=false
```

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-archetype]: https://img.shields.io/badge/mvn-archetype-yellow.svg
[jaxdb]: /