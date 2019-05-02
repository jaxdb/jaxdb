# JAX-DB Maven Plugin

> Maven Plugin for [JAX-DB][jaxdb] framework

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.png)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/jaxdb-maven-plugin.svg)](https://www.javadoc.io/doc/org.jaxdb/jaxdb-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/jaxdb-maven-plugin.svg)](https://mvnrepository.com/artifact/org.jaxdb/jaxdb-maven-plugin)

### Introduction

The `jaxdb-maven-plugin` plugin is used to execute database-related generators, which are currently the [JAX-DB][jaxdb] framework.

### Goals Overview

* [`jaxdb:ddlx`](#jaxdbddlx) generates .sql schema from .ddlx.
* [`jaxdb:sqlx`](#jaxdbsqlx) generates .xsd schema from .ddlx.
* [`jaxdb:jsql`](#jaxdbjsql) generates jSQL Entities from .ddlx.

### Usage

#### `jaxdb:ddlx`

The `jaxdb:ddlx` goal is bound to the `generate-resources` phase, and is used to generate DDL schema files from XML files conforming to the [DDLx Schema][ddlx-schema].

##### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.3.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>ddlx</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/resource.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Configuration Parameters

| Name              | Type    | Use      | Description                                                                   |
|:------------------|:--------|:---------|:------------------------------------------------------------------------------|
| `/vendor`         | String  | Required | Target vendor of generated DDL.                                               |
| `/destDir`        | String  | Required | Destination path of generated bindings.                                       |
| `/schemas`        | List    | Required | List of `schema` elements.                                                    |
| `/schemas/schema` | String  | Required | File path of XML Schema.                                                      |

#### `jaxdb:sqlx`

The `jaxdb:sqlx` goal is bound to the `generate-resources` phase, and is used to generate an XML Schema to allow one to create a validating SQLx file for static data.

##### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.3.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>sqlx</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/schema.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Configuration Parameters

| Name              | Type    | Use      | Description                                                                   |
|:------------------|:--------|:---------|:------------------------------------------------------------------------------|
| `/destDir`        | String  | Required | Destination path of generated bindings.                                       |
| `/schemas`        | List    | Required | List of `schema` elements.                                                    |
| `/schemas/schema` | String  | Required | File path of XML Schema.                                                      |

#### `jaxdb:jsql`

The `jaxdb:jsql` goal is bound to the `generate-sources` phase, and is used to generate jSQL Entities from XML files conforming to the [DDLx Schema][ddlx-schema].

##### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.3.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>jsql</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-sources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/schema.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Configuration Parameters

| Name              | Type    | Use      | Description                                                                   |
|:------------------|:--------|:---------|:------------------------------------------------------------------------------|
| `/destDir`        | String  | Required | Destination path of generated bindings.                                       |
| `/schemas`        | List    | Required | List of `resource` elements.                                                  |
| `/schemas/schema` | String  | Required | File path of XML Schema.                                                      |

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[ddlx-schema]: /ddlx/src/main/resources/ddlx.xsd
[mvn-plugin]: https://img.shields.io/badge/mvn-plugin-lightgrey.svg
[jaxdb]: /