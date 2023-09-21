# JAX-DB Maven Plugin

[![Build Status](https://github.com/jaxdb/jaxdb/actions/workflows/build.yml/badge.svg)](https://github.com/jaxdb/jaxdb/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/jaxdb-maven-plugin.svg)](https://www.javadoc.io/doc/org.jaxdb/jaxdb-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/jaxdb-maven-plugin.svg)](https://mvnrepository.com/artifact/org.jaxdb/jaxdb-maven-plugin)
![Snapshot Version](https://img.shields.io/nexus/s/org.jaxdb/jaxdb-maven-plugin?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

The `jaxdb-maven-plugin` plugin is used to execute database-related generators, which are currently the [JAX-DB][jaxdb] framework.

## Goals Overview

* [**`jaxdb:ddlx2sql`**](#jaxdbddlx2sql)<br>&nbsp;&nbsp;&nbsp;&nbsp;Generate a `.sql` DDL schema from a `.ddlx` file for a specific RDBMS vendor.

* [**`jaxdb:ddlx2sqlx`**](#jaxdbddlx2sqlx)<br>&nbsp;&nbsp;&nbsp;&nbsp;Generate a [<ins>SQLx</ins>][sqlx] schema from a `.ddlx` file, which allows for the definition of `.sqlx` files.

* [**`jaxdb:ddlx2jsql`**](#jaxdbddlx2jsql)<br>&nbsp;&nbsp;&nbsp;&nbsp;Generate [<ins>jSQL</ins>][jsql] entities from a `.ddlx` file, which allows for the use of strong-typed SQL semantics.

* [**`jaxdb:ddlx2xsd`**](#jaxdbddlx2xsd)<br>&nbsp;&nbsp;&nbsp;&nbsp;Generate a `.xsd` schema from a `.ddlx` file, which allows for XML Schema Binding with [JAX-SB](https://github.com/jaxsb/jaxsb/).

* [**`jaxdb:sqlx2sql`**](#jaxdbsqlx2sql)<br>&nbsp;&nbsp;&nbsp;&nbsp;Generate `.sql` DML data file from a `.sqlx` file.

## Usage

### `jaxdb:ddlx2sql`

The `jaxdb:ddlx2sql` goal is bound to the `generate-resources` phase, and is used to generate DDL schema files from XML files conforming to the [DDLx Schema][ddlx-schema].

#### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.5.1</version>
  <executions>
    <execution>
      <phase>generate-resources</phase>
      <goals>
        <goal>ddlx2sql</goal>
      </goals>
      <configuration>
        <vendor>PostgreSQL</vendor> <!-- Derby | MariaDB | MySQL | Oracle | PostgreSQL | SQLite -->
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/example.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                                      | Type                       | Use                          | Description                                                                                                                                                      |
|:------------------------------------------|:---------------------------|:-----------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp;        | Boolean<br>&nbsp;          | Optional<br>&nbsp;           | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`.                                                                                               |
| <samp>/destDir¹</samp>                    | String                     | Required                     | Destination path of generated bindings.                                                                                                                          |
| <samp>/vendor¹</samp><br>&nbsp;           | String<br>&nbsp;           | Required<br>&nbsp;           | RDBMS vendor:<br>&nbsp;&nbsp;&nbsp;&nbsp;`<Derby\|MariaDB\|MySQL\|Oracle\|PostgreSQL\|SQLite>`.                                                                  |
| <samp>/rename¹</samp><br>&nbsp;<br>&nbsp; | String<br>&nbsp;<br>&nbsp; | Optional<br>&nbsp;<br>&nbsp; | Regex pattern specifying the name of the output `.sql` file<br>based on the input `.ddlx` file:<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `/([^.]+).ddlx/$1.sql/`. |
| <samp>/schemas¹</samp>                    | List                       | Required                     | List of `schema` elements.                                                                                                                                       |
| <samp>/schemas/schemaⁿ</samp>             | String                     | Required                     | File path of `.ddlx` schema.                                                                                                                                     |

### `jaxdb:ddlx2sqlx`

The `jaxdb:ddlx2sqlx` goal is bound to the `generate-resources` phase, and is used to generate an XML Schema to allow one to create a validating [<ins>SQLx</ins>][sqlx] file for static data.

#### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.5.1</version>
  <executions>
    <execution>
      <phase>generate-resources</phase>
      <goals>
        <goal>ddlx2sqlx</goal>
      </goals>
      <configuration>
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/example.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                               | Type              | Use                | Description                                                                                   |
|:-----------------------------------|:------------------|:-------------------|:----------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp; | Boolean<br>&nbsp; | Optional<br>&nbsp; | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`. |
| <samp>/destDir¹</samp>             | String            | Required           | Destination path of generated bindings.                                                       |
| <samp>/schemas¹</samp>             | List              | Required           | List of `schema` elements.                                                                    |
| <samp>/schemas/schemaⁿ</samp>      | String            | Required           | File path of `.ddlx` schema.                                                                  |

### `jaxdb:ddlx2jsql`

The `jaxdb:ddlx2jsql` goal is bound to the `generate-sources` phase, and is used to generate [<ins>jSQL</ins>][jsql] entities from XML files conforming to the [DDLx Schema][ddlx-schema].

#### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.5.1</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>jsql</goal>
      </goals>
      <configuration>
        <destDir>${project.build.directory}/generated-sources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/example.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                               | Type              | Use                | Description                                                                                   |
|:-----------------------------------|:------------------|:-------------------|:----------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp; | Boolean<br>&nbsp; | Optional<br>&nbsp; | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`. |
| <samp>/destDir¹</samp>             | String            | Required           | Destination path of generated bindings.                                                       |
| <samp>/schemas¹</samp>             | List              | Required           | List of `schema` elements.                                                                    |
| <samp>/schemas/schemaⁿ</samp>      | String            | Required           | File path of `.ddlx` schema.                                                                  |

### `jaxdb:ddlx2xsd`

The `jaxdb:ddlx2xsd` goal is bound to the `generate-resources` phase, and is used to generate an XML Schema, which allows for XML Schema Binding with [JAX-SB](https://github.com/jaxsb/jaxsb/).

#### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.5.1</version>
  <executions>
    <execution>
      <phase>generate-resources</phase>
      <goals>
        <goal>ddlx2xsd</goal>
      </goals>
      <configuration>
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/example.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                               | Type              | Use                | Description                                                                                   |
|:-----------------------------------|:------------------|:-------------------|:----------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp; | Boolean<br>&nbsp; | Optional<br>&nbsp; | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`. |
| <samp>/destDir¹</samp>             | String            | Required           | Destination path of generated bindings.                                                       |
| <samp>/schemas¹</samp>             | List              | Required           | List of `schema` elements.                                                                    |
| <samp>/schemas/schemaⁿ</samp>      | String            | Required           | File path of `.ddlx` schema.                                                                  |

### `jaxdb:sqlx2sql`

The `jaxdb:sqlx2sql` goal is bound to the `generate-resources` phase, and is used to generate DML data files from XML files conforming to the [DDLx Schema][ddlx-schema].

#### Example

```xml
<plugin>
  <groupId>org.jaxdb</groupId>
  <artifactId>jaxdb-maven-plugin</artifactId>
  <version>0.5.1</version>
  <executions>
    <execution>
      <phase>generate-resources</phase>
      <goals>
        <goal>sqlx2sql</goal>
      </goals>
      <configuration>
        <destDir>${project.build.directory}/generated-resources/jaxdb</destDir>
        <schemas>
          <schema>src/main/resources/example.ddlx</schema>
        </schemas>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                               | Type              | Use                | Description                                                                                   |
|:-----------------------------------|:------------------|:-------------------|:----------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp; | Boolean<br>&nbsp; | Optional<br>&nbsp; | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`. |
| <samp>/destDir¹</samp>             | String            | Required           | Destination path of generated bindings.                                                       |
| <samp>/schemas¹</samp>             | List              | Required           | List of `schema` elements.                                                                    |
| <samp>/schemas/schemaⁿ</samp>      | String            | Required           | File path of `.ddlx` schema.                                                                  |

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[ddlx-schema]: /ddlx/src/main/resources/ddlx.xsd
[jaxdb]: /
[jsql]: /../../../../jaxdb/jsql
[sqlx]: /../../../../jaxdb/sqlx