# JAX-DB Maven Archetype

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.svg?1)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg?1)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/jaxdb-maven-archetype.svg?1)](https://www.javadoc.io/doc/org.jaxdb/jaxdb-maven-archetype)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/jaxdb-maven-archetype.svg?1)](https://mvnrepository.com/artifact/org.jaxdb/jaxdb-maven-archetype)
![Snapshot Version](https://img.shields.io/nexus/s/org.jaxdb/jaxdb-maven-archetype?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

The `jaxdb-maven-archetype` archetype is a quick-start example of how to use the [JAX-DB][jaxdb] framework.

## Usage Overview

To use the archetype, execute the following:

```bash
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate \
-DgroupId=com.mycompany.app -DartifactId=my-app \
-DarchetypeGroupId=org.jaxdb -DarchetypeArtifactId=jaxdb-maven-archetype -DarchetypeVersion=0.4.1-SNAPSHOT \
-DarchetypeCatalog=https://oss.sonatype.org/content/repositories/snapshots/ \
-DinteractiveMode=false
```

Once the project is created, you can build it with `mvn install`.

Before running `MyApp`, you must create your MySQL database:

```
CREATE DATABASE MyDB;
CREATE USER MyDB IDENTIFIED BY 'MyDB';
GRANT ALL ON MyDB.* TO 'MyDB'@'%';
```

Then, load the generated schema via:

```bash
mysql -D MyDB -u MyDB --password=MyDB < ./model/target/generated-resources/jaxdb/db.sql
```

**Note**: You can change the name from `MyDB` to something else, but make sure to update the DBCP spec in `config.xml`.

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-archetype]: https://img.shields.io/badge/mvn-archetype-yellow.svg
[jaxdb]: /