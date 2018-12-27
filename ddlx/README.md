# OpenJAX RDB DDLx

**Relational Data Binding DDLx**

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

  ```tcsh
  mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
  ```

2. Add the `mvn.repo.openjax.org` Maven repositories to the POM.

  ```xml
  <repositories>
    <repository>
      <id>mvn.repo.openjax.org</id>
      <url>http://mvn.repo.openjax.org/m2</url>
    </repository>
  </repositories>
  <pluginRepositories>
    <pluginRepository>
      <id>mvn.repo.openjax.org</id>
      <url>http://mvn.repo.openjax.org/m2</url>
    </pluginRepository>
  </pluginRepositories>
  ```

3. Create a `basis.ddlx` **DDLx** schema and put it in `src/main/resources/`.

  ```xml
  <schema name="basis"
    xmlns="http://rdb.openjax.org/ddlx-0.9.9.xsd"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://rdb.openjax.org/ddlx-0.9.9.xsd http://rdb.openjax.org/ddlx.xsd">

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

4. Add the [`org.openjax.rdb:rdb-maven-plugin`][rdb-maven-plugin] to the POM.

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
            <schema>src/main/resources/basis.ddlx</schema>
          </schemas>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ```

5. Run `mvn generate-resources`, and upon successful execution of the [`rdb-maven-plugin`][rdb-maven-plugin], an `example.sql` will be created in `generated-resources/rdb` that complies to the `PostgreSQL` vendor as is specified in the POM.

6. Import the DDL into your database. The 

  ```tcsh
  psql -d example < generated-resources/rdb/basis.sql
  ```
  
  Subsequent imports of `basis.sql` into the database will `DROP` and re-`CREATE` the data model.

### JavaDocs

JavaDocs are available [here](https://rdb.openjax.org/javadocs/).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

<a href="http://cooltext.com" target="_top"><img src="https://cooltext.com/images/ct_pixel.gif" width="80" height="15" alt="Cool Text: Logo and Graphics Generator" border="0" /></a>

[ddlx.xsd]: /ddlx/src/main/resources/ddlx.xsd
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[jsql]: /jsql
[maven-archetype-quickstart]: http://maven.apache.org/archetypes/maven-archetype-quickstart/
[maven]: https://maven.apache.org/
[rdb-maven-plugin]: /../../../../openjax/rdb-maven-plugin