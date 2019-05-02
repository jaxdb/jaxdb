# JAX-DB DDLx

> Relational Data Binding DDLx

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.png)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/ddlx.svg)](https://www.javadoc.io/doc/org.jaxdb/ddlx)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/ddlx.svg)](https://mvnrepository.com/artifact/org.jaxdb/ddlx)

### Introduction

**DDLx** is a XML definition of the DDL specification. It is a vendor-agnostic, SQL compliant XSD used to create SQL Schemas in XML. The [DDLx Schema][ddlx.xsd] utilizes the power of XML Schema Validation to provides a cohesive structured model for the creation of SQL Schemas.

### Why **DDLx**?

**DDLx** can also be used with [**jSQL**][jsql], an advanced, cohesive and lightweight ORM approach.

#### CohesionFirst

Developed with the CohesionFirst approach, **DDLx** is the cohesive alternative to the creation of RDBMS data models that offers validation and fail-fast execution. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **DDLx** is a complete solution for the creation and management of a SQL Schema. The **DDLx** solution differentiates itself from alternative approaches with the strength of its cohesion to the XML Schema language and the DDL model.

#### Vendor-Agnostic

How can one create a SQL Schema that is not vendor specific? Often, a DDL written for MySQL will not execute in PostreSQL, as each vendor differs in SQL dialect. Though the SQL standards (SQL-89, SQL-92, SQL-99, SQL-2003) set a significant amount of requirements to the RDBMS vendors, there is still significant room for variation. These variations are what define the differences of the vendor-specific dialects. As example of such variation, proprietary extensions to the DDL specification of more advanced definition semantics (for instance, index type semantics, enum semantics, function name differences, etc). Because the SQL standards do not specify the full scope of SQL, vendors implement proprietary extensions to DDL and DML semantics of the modern-day RDBMS features we use today. Using **DDLx** as the primary descriptor of one's SQL Data Model, one can maintain a single SQL Schema uncoupled to a RDBMS vendor.

#### Validating and Fail-Fast

**DDLx** is a standard that abstracts the DDL with a vendor-agnostic model, and provides cohesive semantics for the definition of most (close to all) of the SQL-92 and SQL-99 DDL structures. Utilizing the full power of XML Schema Validation, **DDLx** provides a cohesive, error-checking and fail-fast, structured model for the creation of SQL Schemas.

### Getting Started

#### Prerequisites

* [Java 8][jdk8-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

#### Example

1. In your preferred development directory, create a [`maven-archetype-quickstart`][maven-archetype-quickstart] project.

  ```bash
  mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
  ```

2. Create a `basis.ddlx` **DDLx** schema and put it in `src/main/resources/`.

  ```xml
  <schema name="basis"
    xmlns="http://www.jaxdb.org/ddlx-0.3.9.xsd"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.jaxdb.org/ddlx-0.3.9.xsd http://www.jaxdb.org/ddlx.xsd">

    <table name="id" abstract="true">
      <column name="id" xsi:type="char" length="36" null="false"/>
      <constraints>
        <primaryKey>
          <column name="id"/>
        </primaryKey>
      </constraints>
    </table>

    <table name="account" extends="id">
      <column name="email" xsi:type="char" varying="true" length="255" null="false">
        <index unique="true" type="HASH"/>
        <check operator="ne" condition=""/>
      </column>
      <column name="password" xsi:type="char" length="64" null="false">
        <check operator="ne" condition=""/>
      </column>
      <column name="first_name" xsi:type="char" varying="true" length="64" null="false"/>
      <column name="last_name" xsi:type="char" varying="true" length="64" null="false"/>
      <column name="forgot_token" xsi:type="char" length="6" null="true"/>
      <column name="forgot_token_on" xsi:type="dateTime" null="true"/>
      <constraints>
        <unique>
          <column name="email"/>
        </unique>
      </constraints>
    </table>

  </schema>
  ```

3. Add the [`org.jaxdb:jaxdb-maven-plugin`][jaxdb-maven-plugin] to the POM.

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
            <schema>src/main/resources/basis.ddlx</schema>
          </schemas>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ```

4. Run `mvn generate-resources`, and upon successful execution of the [`jaxdb-maven-plugin`][jaxdb-maven-plugin], an `example.sql` will be created in `generated-resources/jaxdb` that complies to the `PostgreSQL` vendor as is specified in the POM.

5. Import the DDL into your database. The

  ```bash
  psql -d example < generated-resources/jaxdb/basis.sql
  ```

  Subsequent imports of `basis.sql` into the database will `DROP` and re-`CREATE` the data model.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[ddlx.xsd]: /ddlx/src/main/resources/ddlx.xsd
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[jsql]: /jsql
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart/
[maven]: https://maven.apache.org/
[jaxdb-maven-plugin]: /../../../../jaxdb/jaxdb-maven-plugin