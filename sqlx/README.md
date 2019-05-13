# JAX-DB SQLx

> JAX-DB SQL w/ XML

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.png)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/sqlx.svg)](https://www.javadoc.io/doc/org.jaxdb/sqlx)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/sqlx.svg)](https://mvnrepository.com/artifact/org.jaxdb/sqlx)

## Introduction

<ins>SQLx</ins> is a vendor-agnostic, XML-based SQL data definition standard that offers the power of XML validation for your static SQL data. The <ins>SQLx</ins> framework utilizes a strongly-typed [DDLx][ddlx.xsd] file to generate a XML Schema document that translates DDLx constructs into the XSD language. With the [SQLx XSLT][sqlx.xsl], the <ins>SQLx</ins> tool leverages the power of XML Schema Validation and provides a cohesive structured model to define SQL data (that conforms to your SQL schema, defined in a <ins>DDLx</ins> file).

<ins>SQLx</ins> is a natural extension of [<ins>DDLx</ins>][ddlx], offering the next needed highly-cohesive expression standard for SQL databases. Together with [<ins>jSQL</ins>][jsql], <ins>SQLx</ins> is the last missing gap in JAX-DB's quest to achieve an advanced, cohesive and lightweight schema, static data, and ORM solution.

## Getting Started

### Prerequisites

* [Java 8][jdk8-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

### Example

1. As the <ins>SQLx</ins> tool extends the functionality of <ins>DDLx</ins>, please begin this example by first completing [the <ins>DDLx</ins> example][ddlx-example].

1. After having created the `example.ddlx` file, include an extra execution tag into the configuration of the [`jaxdb-maven-plugin`][jaxdb-maven-plugin].

   ```xml
   <plugin>
     <groupId>org.jaxdb</groupId>
     <artifactId>jaxdb-maven-plugin</artifactId>
     <version>0.3.9</version>
     <executions>
       <execution>
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

1. Run `mvn generate-resources`, and upon successful execution of the [`jaxdb-maven-plugin`][jaxdb-maven-plugin], an `example.xsd` will be created in `generated-resources/jaxdb`.

1. Create `src/main/resources/data.sqlx`.

   ```xml
   <example
     xmlns="sqlx.example"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="sqlx.example ../../../target/generated-resources/jaxdb/example.xsd">
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
   </example>
   ```

1. The `data.sqlx` file is strictly compliant to the `example.ddlx` file that specifies the data model. You can now create static data that complies to the data model, having the power of XML to foster confidence in the validity of the data far before you load it in the DB.

## FAQ

### What does it mean to be "Vendor-Agnostic"?

How can one create a SQL Schema that is not vendor specific? Often, a DDL written for MySQL will not execute in PostreSQL, as each vendor differs in SQL dialect. Though the SQL standards (SQL-89, SQL-92, SQL-99, SQL-2003) set a significant amount of requirements to the RDBMS vendors, there is still significant room for variation. These variations are what define the differences of the vendor-specific dialects. As example of such variation, proprietary extensions to the DDL specification of more advanced definition semantics (for instance, index type semantics, enum semantics, function name differences, etc). Because the SQL standards do not specify the full scope of SQL, vendors implement proprietary extensions to DDL and SQL semantics of the modern-day RDBMS features we use today.

### What does it mean to be "Validating and Fail-Fast"?

<ins>SQLx</ins> is a standard that abstracts the static data loading SQL with a vendor-agnostic model. Utilizing the full power of XML Schema Validation, <ins>SQLx</ins> provides a cohesive, error-checking and fail-fast, structured model for the creation of SQL static data.

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[ddlx-example]: /ddlx#example
[ddlx.xsd]: /ddlx/src/main/resources/ddlx.xsd
[ddlx]: /ddlx
[sqlx.xsl]: /sqlx/src/main/resources/sqlx.xsl
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[jsql]: /jsql
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart
[maven]: https://maven.apache.org/
[jaxdb-maven-plugin]: /maven-plugin