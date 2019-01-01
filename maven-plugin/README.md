# OpenJAX RDB Maven Plugin

**Maven Plugin for [RDB][rdb] framework**

### Introduction

The `rdb-maven-plugin` plugin is used to execute database-related generators, which are currently the [RDB][rdb] framework.

### Goals Overview

* [`rdb:ddlx`](#rdbddlx) generates .sql schema from .ddlx.
* [`rdb:sqlx`](#rdbsqlx) generates .xsd schema from .ddlx.
* [`rdb:jsql`](#rdbjsql) generates jSQL Entities from .ddlx.

### Usage

#### `rdb:ddlx`

The `rdb:ddlx` goal is bound to the `generate-resources` phase, and is used to generate DDL schema files from XML files conforming to the [DDLx Schema][ddlx-schema].

##### Example

```xml
<plugin>
  <groupId>org.openjax.rdb</groupId>
  <artifactId>rdb-maven-plugin</artifactId>
  <version>0.9.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>ddlx</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-resources/rdb</destDir>
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

#### `rdb:sqlx`

The `rdb:sqlx` goal is bound to the `generate-resources` phase, and is used to generate an XML Schema to allow one to create a validating SQLx file for static data.

##### Example

```xml
<plugin>
  <groupId>org.openjax.rdb</groupId>
  <artifactId>rdb-maven-plugin</artifactId>
  <version>0.9.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>sqlx</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-resources/rdb</destDir>
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

#### `rdb:jsql`

The `rdb:jsql` goal is bound to the `generate-sources` phase, and is used to generate jSQL Entities from XML files conforming to the [DDLx Schema][ddlx-schema].

##### Example

```xml
<plugin>
  <groupId>org.openjax.rdb</groupId>
  <artifactId>rdb-maven-plugin</artifactId>
  <version>0.9.9-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>jsql</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor>
        <destDir>${project.build.directory}/generated-sources/rdb</destDir>
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

### JavaDocs

JavaDocs are available [here](https://rdb.openjax.org/apidocs/).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

<a href="http://cooltext.com" target="_top"><img src="https://cooltext.com/images/ct_pixel.gif" width="80" height="15" alt="Cool Text: Logo and Graphics Generator" border="0" /></a>

[ddlx-schema]: /ddlx/src/main/resources/ddlx.xsd
[mvn-plugin]: https://img.shields.io/badge/mvn-plugin-lightgrey.svg
[rdb]: /