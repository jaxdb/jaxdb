<img src="https://www.cohesionfirst.org/logo.png" align="right" />
## xdb-schema<br>![java-enterprise][java-enterprise] [![CohesionFirst™][cohesionfirst_badge]][cohesionfirst]
> eXtensible Data Binding Schema

### Introduction

**xdb-schema** is a vendor-agnostic, SQL-92 and SQL-99 compliant, XSD used to create SQL Schemas in XML. Based on the CohesionFirst™ approach, the [XDS Schema][xds.xsd] utilizes the full power of XML Schema Validation and provides a cohesive structured model for the creation of SQL Schemas.

### Why **xdb-schema**?

**xdb-schema** can also be used with [**xdb-entities**][xdb-entities], an advanced, cohesive and lightweight ORM layer that sits on top of JDBC.

#### CohesionFirst™

Developed with the CohesionFirst™ approach, **xdb-schema** is the cohesive alternative to the creation of RDBMS data models that offers validation and fail-fast execution. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **xdb-schema** is a complete solution for the creation and management of a SQL Schema. The **xdb-schema** solution differentiates itself from alternative approaches with the strength of its cohesion to the XML Schema language and the DDL model.

#### Vendor-Agnostic

How can one create a SQL Schema that is not vendor specific? Often, a DDL written for MySQL will not execute in PostreSQL, as each vendor has many proprietary semantics, keywords, and more. Despite the fact that all RDBMS database servers are supposed to conform to the SQL-92 and SQL-99 standards, many do not, and many offer proprietary extensions to the DDL specification of more advanced (and popular) definition constructs (for instance, index type semantics, enum semantics, function name differences, etc). Vendors implement proprietary extensions to their DDL and DML semantics, because SQL-92 and SQL-99 do not offer descriptors for many of the modern-day RDBMS features we use today. Using **xdb-schema** as the primary descriptor of one's SQL Data Model, one can maintain a single SQL Schema uncoupled to a RDBMS vendor.

#### Validating and Fail-Fast

**xdb-schema** is a standard that abstracts the DDL with a vendor-agnostic model, and provides cohesive semantics for the definition of most (close to all) of the SQL-92 and SQL-99 DDL structures. Utilizing the full power of XML Schema Validation, **xdb-schema** provides a cohesive, error-checking and fail-fast, structured model for the creation of SQL Schemas.

### Getting Started

#### Prerequisites

* [Java 7][jdk7-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

#### Example

1. In your preferred development directory, create a [`maven-archetype-quickstart`][maven-archetype-quickstart] project.

  ```tcsh
  mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
  ```

2. Add the `mvn.repo.safris.org` Maven repositories to the POM.

  ```xml
  <repositories>
    <repository>
      <id>mvn.repo.safris.org</id>
      <url>http://mvn.repo.safris.org/m2</url>
    </repository>
  </repositories>
  <pluginRepositories>
    <pluginRepository>
      <id>mvn.repo.safris.org</id>
      <url>http://mvn.repo.safris.org/m2</url>
    </pluginRepository>
  </pluginRepositories>
  ```

3. Create a `basis.xds` **xdb-schema** Schema and put it in `src/main/resources/`.

  ```xml
  <database name="basis"
    xmlns="http://xdb.safris.org/xds.xsd"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xdb.safris.org/xds.xsd http://xdb.safris.org/xds.xsd">

    <table name="id" abstract="true">
      <column name="id" xsi:type="char" length="36" null="false" generateOnInsert="UUID"/>
      <constraints>
        <primaryKey>
          <column name="id"/>
        </primaryKey>
      </constraints>
    </table>

    <table name="timestamp_id" abstract="true" extends="id">
      <column name="created_on" xsi:type="dateTime" null="false" generateOnInsert="TIMESTAMP"/>
      <column name="modified_on" xsi:type="dateTime" null="false" generateOnInsert="TIMESTAMP" generateOnUpdate="TIMESTAMP"/>
      <column name="version" xsi:type="integer" precision="9" unsigned="true" default="0" null="false" checkOnUpdate="EQUALS" generateOnUpdate="INCREMENT"/>
    </table>

    <table name="account" extends="timestamp_id">
      <column name="email" xsi:type="char" variant="true" length="255" null="false">
        <index unique="true" type="HASH"/>
        <check operator="ne" condition=""/>
      </column>
      <column name="password" xsi:type="char" length="64" null="false">
        <check operator="ne" condition=""/>
      </column>
      <column name="first_name" xsi:type="char" variant="true" length="64" null="false"/>
      <column name="last_name" xsi:type="char" variant="true" length="64" null="false"/>
      <column name="forgot_token" xsi:type="char" length="6" null="true"/>
      <column name="forgot_token_on" xsi:type="dateTime" null="true"/>
      <constraints>
        <unique>
          <column name="email"/>
        </unique>
      </constraints>
    </table>

  </database>
  ```

4. Add the [`org.safris.maven.plugin:xdb-maven-plugin`][xdb-maven-plugin] to the POM.

  ```xml
  <plugin>
    <groupId>org.safris.maven.plugin</groupId>
    <artifactId>xdb-maven-plugin</artifactId>
    <version>1.3.2</version>
    <executions>
      <execution>
        <id>default-schema</id>
        <phase>generate-resources</phase>
        <goals>
          <goal>schema</goal>
        </goals>
        <configuration>
          <vendor>PostgreSQL</vendor>
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

5. Run `mvn generate-resources`, and upon successful execution of the `xdb-maven-plugin`, an `example.sql` will be created in `generated-resources/xds` that complies to the `PostgreSQL` vendor as is specified in the POM.

6. Import the DDL into your database. The 

  ```tcsh
  psql -d example < generated-resources/xdb/basis.sql
  ```
  
  Subsequent imports of `schema.sql` into the database will `DROP` and re-`CREATE` the data model.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[cohesionfirst]: https://www.cohesionfirst.com/
[cohesionfirst_badge]: https://img.shields.io/badge/CohesionFirst%E2%84%A2--blue.svg
[java-enterprise]: https://img.shields.io/badge/java-enterprise-blue.svg
[jdk7-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart/
[maven]: https://maven.apache.org/
[xdb-entities]: https://github.com/SevaSafris/xdb/blob/master/schema/src/main/resources/xds.xsd
[xdb-maven-plugin]: https://github.com/SevaSafris/xdb-maven-plugin
[xds.xsd]: https://github.com/SevaSafris/xdb/blob/master/schema/src/main/resources/xds.xsd