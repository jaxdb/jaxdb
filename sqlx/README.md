<img src="https://www.cohesionfirst.org/logo.png" align="right">

## rdb-sqlx<br>![java-enterprise][java-enterprise] <a href="https://www.cohesionfirst.org/"><img src="https://img.shields.io/badge/CohesionFirst%E2%84%A2--blue.svg"></a>
> Relational Data Binding SQLx

### Introduction

**SQLx** is a vendor-agnostic, XML-based SQL data definition standard that offers the power of XML validation for your static SQL data. The **SQLx** framework utilizes a strongly-typed [DDLx][ddlx.xsd] file to generate a XML Schema document that translates DDLx constructs into the XSD language. With the [SQLx XSLT][sqlx.xsl], the **SQLx** tool leverages the power of XML Schema Validation and provides a cohesive structured model to define SQL data (that conforms to your SQL schema, defined in a **DDLx** file).

### Why **SQLx**?

**SQLx** is a natural extension of [**DDLx**][ddlx], offering the next needed highly-cohesive expression standard for SQL databases. Together with [**jSQL**][jsql], ***SQLx*** is the last missing gap in rdb's quest to achieve an advanced, cohesive and lightweight schema, static data, and ORM solution.

#### CohesionFirst™

Developed with the CohesionFirst™ approach, **SQLx** is the cohesive alternative to the creation of RDBMS static data that offers validation and fail-fast execution. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **SQLx** is a complete solution for the creation and management of SQL static data. The **SQLx** solution differentiates itself from alternative approaches with the strength of its cohesion to the XML Schema language and the DDL model.

#### Vendor-Agnostic

How can one create a SQL Schema that is not vendor specific? Often, a DDL written for MySQL will not execute in PostreSQL, as each vendor differs in SQL dialect. Though the SQL standards (SQL-89, SQL-92, SQL-99, SQL-2003) set a significant amount of requirements to the RDBMS vendors, there is still significant room for variation. These variations are what define the differences of the vendor-specific dialects. As example of such variation, proprietary extensions to the DDL specification of more advanced definition semantics (for instance, index type semantics, enum semantics, function name differences, etc). Because the SQL standards do not specify the full scope of SQL, vendors implement proprietary extensions to DDL and SQL semantics of the modern-day RDBMS features we use today.

#### Validating and Fail-Fast

**SQLx** is a standard that abstracts the static data loading SQL with a vendor-agnostic model. Utilizing the full power of XML Schema Validation, **SQLx** provides a cohesive, error-checking and fail-fast, structured model for the creation of SQL static data.

### Getting Started

#### Prerequisites

* [Java 8][jdk8-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

#### Example

1. As the **SQLx** tool extends the functionality of **DDLx**, please begin this example by first completing [the **DDLx** example][ddlx-example].

2. After having created the basis.ddlx file, include an extra execution tag into the configuration of the [`rdb-maven-plugin`][rdb-maven-plugin].

  ```xml
  <plugin>
    <groupId>org.libx4j.maven.plugin</groupId>
    <artifactId>rdb-maven-plugin</artifactId>
    <version>0.9.8-SNAPSHOT</version>
    <executions>
      <execution>
        <goals>
          <goal>sqlx</goal>
        </goals>
        <configuration>
          <destDir>${project.build.directory}/generated-resources/rdb</destDir>
          <schemas>
            <schema>src/main/resources/basis.ddlx</schema>
          </schemas>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ```

3. Run `mvn generate-resources`, and upon successful execution of the [`rdb-maven-plugin`][rdb-maven-plugin], an `basis.xsd` will be created in `generated-resources/rdb`.

4. Create a `data.sqlx` file in the `src/main/resources` directory.

  ```xml
  <basis
    xmlns="sqlx.basis"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="sqlx.basis ../../../target/generated-resources/rdb/basis.xsd">
    <insert>
      <account
        email="scott@tiger.com"
        password="f15c16b99f82d8201767d3a841ff40849c8a1b812ffbfd2e393d2b6aa6682a6e"
        firstName="Scott"
        lastName="Tiger"
        createdOn="2016-12-26T09:00:00"
        modifiedOn="2016-12-26T09:00:00"
        id="a9de46a9-c096-4b4e-98fc-274ec2f22e67"/>
    </insert>
  </basis>
  ```

5. The `data.sqlx` file is strictly compliant to the `basis.ddlx` file that specifies the data model. You can now create static data that complies to the data model, having the power of XML to foster confidence in the validity of the data far before you load it in the DB.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[ddlx-example]: https://github.com/libx4j/rdb/tree/master/ddlx#example
[ddlx.xsd]: https://github.com/libx4j/rdb/blob/master/ddlx/src/main/resources/ddlx.xsd
[ddlx]: https://github.com/libx4j/rdb/blob/master/ddlx
[sqlx.xsl]: https://github.com/libx4j/rdb/blob/master/sqlx/src/main/resources/sqlx.xsl
[java-enterprise]: https://img.shields.io/badge/java-enterprise-blue.svg
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[jsql]: https://github.com/libx4j/rdb/blob/master/jsql
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart
[maven]: https://maven.apache.org/
[rdb-maven-plugin]: https://github.com/libx4j/rdb-maven-plugin