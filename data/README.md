<img src="https://www.cohesionfirst.org/logo.png" align="right" />
## xdb-data<br>![java-enterprise][java-enterprise] <a href="https://www.cohesionfirst.org/"><img src="https://img.shields.io/badge/CohesionFirst%E2%84%A2--blue.svg"></a>
> eXtensible Data Binding Data

### Introduction

**xdb-data** is a vendor-agnostic, XML-based SQL data definition standard that offers the power of XML validation for your static SQL data. Based on the CohesionFirst™ approach, the **xdb-data** framework utilizes a strongly-typed [XDS (eXtensible Data Schema)][xds.xsd] file to generate a XML Schema document that translates XDS constructs into the XSD language. With a [single XSLT transform][xdd.xsl], the **xdb-data** tool leverages the full power of XML Schema Validation and provides a cohesive structured model for the creation of SQL data (that conforms to your SQL schema, defined in a XSD file).

### Why **xdb-data**?

**xdb-data** is a natural extension of [**xdb-schema**][xdb-schema], offering the next needed highly-cohesive expression standard for SQL databases. Together with [**xdb-entities**][xdb-entities], ***xdb-data*** is the last missing gap in XDB's quest to achieve an advanced, cohesive and lightweight schema, static data, and ORM solution.

#### CohesionFirst™

Developed with the CohesionFirst™ approach, **xdb-data** is the cohesive alternative to the creation of RDBMS static data that offers validation and fail-fast execution. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **xdb-data** is a complete solution for the creation and management of SQL static data. The **xdb-data** solution differentiates itself from alternative approaches with the strength of its cohesion to the XML Schema language and the DDL model.

#### Vendor-Agnostic

How can one create a SQL static data files that are not vendor specific? Often, a DDL written for MySQL will not execute in PostreSQL, as each vendor has many proprietary semantics, keywords, and more. Despite the fact that all RDBMS database servers are supposed to conform to the SQL-92 and SQL-99 standards, many do not, and many offer proprietary extensions to the DDL specification of more advanced (and popular) definition constructs (for instance, index type semantics, enum semantics, function name differences, etc). Vendors implement proprietary extensions to their DDL and DML semantics, because SQL-92 and SQL-99 do not offer descriptors for many of the modern-day RDBMS features we use today. Using **xdb-data** as the primary descriptor of one's SQL static data definition files, one can maintain a single SQL static data standard uncoupled to a RDBMS vendor.

#### Validating and Fail-Fast

**xdb-data** is a standard that abstracts the static data loading DML with a vendor-agnostic model. Utilizing the full power of XML Schema Validation, **xdb-data** provides a cohesive, error-checking and fail-fast, structured model for the creation of SQL static data.

### Getting Started

#### Prerequisites

* [Java 8][jdk8-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

#### Example

1. As the **xdb-data** tool extends the functionality of **xdb-schema**, please begin this example by first completing [the **xdb-schema** example][xdb-schema-example].

4. After having created the basis.xds file, include an extra execution tag into the configuration of the xdb-maven-plugin.

  ```xml
  <plugin>
    <groupId>org.safris.maven.plugin</groupId>
    <artifactId>xdb-maven-plugin</artifactId>
    <version>1.0.1</version>
    <executions>
      <execution>
        <id>default-data</id>
        <phase>generate-resources</phase>
        <goals>
          <goal>data</goal>
        </goals>
        <configuration>
          <manifest xmlns="http://maven.safris.org/common/manifest.xsd">
            <destdir>${project.build.directory}/generated-resources/xdb</destdir>
            <schemas>
              <schema>${basedir}/src/main/resources/basis.xds</schema>
            </schemas>
          </manifest>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ```

5. Run `mvn generate-resources`, and upon successful execution of the `xdb-maven-plugin`, an `basis.xsd` will be created in `generated-resources/xdb`.

6. Create a `data.xdd` file in the `src/main/resources` directory.

  ```xml
  <data
    xmlns="xdd.basis"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="xdd.basis ../../../target/generated-resources/basis.xsd">
    <Account
      email="scott@tiger.com"
      password="f15c16b99f82d8201767d3a841ff40849c8a1b812ffbfd2e393d2b6aa6682a6e"
      firstName="Scott"
      lastName="Tiger"
      createdOn="2016-12-26T09:00:00"
      modifiedOn="2016-12-26T09:00:00"
      id="a9de46a9-c096-4b4e-98fc-274ec2f22e67"/>
  </data>
  ```

7. The `data.xdd` file is strictly compliant to the `basis.xds` file that specifies the data model. You can now create static data that complies to the data model, having the power of XML to foster confidence in the validity of the data far before you load it in the DB.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[xdd.xsl]: https://github.com/SevaSafris/xdb/blob/master/data/src/main/resources/xdd.xsl
[java-enterprise]: https://img.shields.io/badge/java-enterprise-blue.svg
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart
[maven]: https://maven.apache.org/
[xdb-entities]: https://github.com/SevaSafris/xdb/blob/master/entities
[xdb-maven-plugin]: https://github.com/SevaSafris/xdb-maven-plugin
[xdb-schema-example]: https://github.com/SevaSafris/xdb/tree/master/schema#example
[xdb-schema]: https://github.com/SevaSafris/xdb/blob/master/schema
[xds.xsd]: https://github.com/SevaSafris/xdb/blob/master/schema/src/main/resources/xds.xsd