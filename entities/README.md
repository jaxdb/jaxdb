<img src="http://safris.org/logo.png" align="right" />
## xdb-entities<br>[![JavaCommons](https://img.shields.io/badge/java-enterprise-blue.svg)](https://cohesionfirst.com/) [![CohesionFirst](https://img.shields.io/badge/CohesionFirst%E2%84%A2--blue.svg)](https://cohesionfirst.com/)
> eXtensible Data Binding Entities

### Introduction

**xdb-entities** is an extension to [**xdb-schema**](https://github.com/SevaSafris/xdb/tree/master/schema/), offering a lightweight ORM (Object Relational Mapping) solution that runs on the JDBC v4.1 API. Based on the CohesionFirst™ approach, the **xdb-entities** framework is designed to provide a highly cohesive and strongly-typed binding between the Java language and RDBMS servers. XDE uses a SQL schema defined in a [XDS file](https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/resources/hospital.xds) to create a one-to-one, Object-Model-to-Data-Model API that is vendor agnostic.

### Why **xdb-entities**?

#### CohesionFirst™

Developed with the CohesionFirst™ approach, **xdb-entities** is reliably designed, consistently implemented, and straightforward to use. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **xdb-entities** is a complete ORM solution and cohesive DML wrapper around JDBC v4.1. The **xdb-entities** solution differentiates itself from the rest with the strength of its cohesion of the SQL DML to the Java language.

#### Strongly Typed DML Semantics

In addition to generating Java classes that bind to a DDL, the **xdb-entities** framework offers an API for **Strongly-Typed DML Semantics**. These APIs come in the form of method invocations that resemble a non-cohesive, String-based SQL alternative. For example:

```java
public static basis.Account findAccount(final String email) throws SQLException {
  final basis.Account a = new basis.Account();
  try (final RowIterator<basis.Account> rows =
    SELECT(a).
    FROM(a).
    WHERE(EQ(a.email, email)).
    execute()) {
    return rows.nextRow() ? rows.nextEntity() : null;
  }
}
```

**Strongly-Typed DML Semantics** are powerful, because they extend the power of the Java compiler to realize errors in edit-time or compile-time. Alternatively, if non-cohesive, String-based SQL is used, errors are only presented in runtime upon execution by the application to the database. In addition to binding Java classes to the DDL, **xdb-entities** provides a strongly typed approach for the construction of SQL DML.

Together, these two concepts provide the integrity into an otherwise non-cohesive aspect of the application stack: the database tier.

#### Fast and Memory Efficient

**xdb-entities** is fast, memory efficient, lightweight and intuitive ORM solution that does not involve a steep learning curve, and does not involve proprietary semantics that would couple a codebase to the ORM provider (like Hybernate, or JPE).

#### Cohesive and Fail-Fast

**xdb-entities** is cohesive, offering the power of Java's compiler to realize errors in edit-time or compile-time.

### Getting Started

#### Prerequisites

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) - The minimum required JDK version.
* [Maven](https://maven.apache.org/) - The dependency management system.

#### Example

1. As **xdb-entities** framework requires a XDS-based SQL Schema, start with a [XDL Example](https://github.com/SevaSafris/xdb/tree/master/schema#example).

2. Next, add the `org.safris.xdb:xdb-entities` dependency into the POM.

  ```xml
  <dependency>
    <groupId>org.safris.xdb</groupId>
    <artifactId>xdb-entities</artifactId>
    <version>1.3.3</version>
  </dependency>
  ```

3. Include a `xde` goal in your `xdb-maven-plugin` in the POM.

  ```xml
  <plugin>
    <groupId>org.safris.maven.plugin</groupId>
    <artifactId>xdb-maven-plugin</artifactId>
    <version>1.3.2</version>
    <executions>
      <!-- [...] the xdl <execution> is here -->
      <execution>
        <id>xde</id>
        <configuration>
          <manifest xmlns="http://maven.safris.org/common/manifest.xsd">
            <destdir>${project.build.directory}/generated-sources/xde</destdir>
            <schemas>
              <schema>${basedir}/src/main/resources/schema.xdl</schema>
            </schemas>
          </manifest>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ```

4. Run `mvn install`, and upon successful execution of the `xdb-maven-plugin`, classes will be generated in `target/generated-sources/xdb`. Add this path to your Build Paths in your IDE to integrate into your project.

5. In `App.java`, include:

  ```java
  public static void main(final String[] args) throws SQLException {
    basis.Account account = new basis.Account();
    account.email.set("john@doe");
    account.firstName.set("John");
    account.lastName.set("Doe");
    account.password.set("w3lcome");

    INSERT(account).execute();

    account.firstName.set("Bob");

    UPDATE(account).execute();

    final basis.Account found = findAccount(account.email.get());
    System.out.println(found != null ? found.firstName.get() : "null");
  }
  
  public static basis.Account findAccount(final String email) throws SQLException {
    final basis.Account a = new basis.Account();
    try (final RowIterator<basis.Account> rows =
      SELECT(a).
      FROM(a).
      WHERE(EQ(a.email, email)).
      execute()) {
      return rows.nextRow() ? rows.nextEntity() : null;
    }
  }
  ```

6. To run the code, you must now connect **xdb-entities** to your database. **xdb-entities** relies on the [`org.safris.commons:dbcp`](https://github.com/SevaSafris/commons-dbcp/) module to aide in configuration of Database Connection Pools. Create a `dbcp.xml` file in `src/main/resources` that conforms to [this XSD](https://github.com/SevaSafris/commons-dbcp/blob/master/src/main/resources/dbcp.xsd), which defines the Database Connection Pool settings for your connection.

  ```xml
  <dbcp name="basis"
    xmlns="http://commons.safris.org/dbcp.xsd"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://commons.safris.org/dbcp.xsd http://commons.safris.org/dbcp.xsd">
    <jdbc>
      <url>jdbc:postgresql://localhost/basis</url>
      <driverClassName>org.postgresql.Driver</driverClassName>
      <username>basis</username>
      <password>basis</password>
      <loginTimeout>5000</loginTimeout>
    </jdbc>
    <default>
      <autoCommit>true</autoCommit>
      <readOnly>false</readOnly>
      <transactionIsolation>READ_UNCOMMITTED</transactionIsolation>
    </default>
    <size>
      <initialSize>0</initialSize>
      <maxActive>16</maxActive>
      <maxIdle>16</maxIdle>
      <minIdle>0</minIdle>
      <maxWait>1000</maxWait>
    </size>
    <management>
      <timeBetweenEvictionRuns>-1</timeBetweenEvictionRuns>
      <numTestsPerEvictionRun>3</numTestsPerEvictionRun>
      <minEvictableIdleTime>1800000</minEvictableIdleTime>
    </management>
    <preparedStatements>
      <poolPreparedStatements>false</poolPreparedStatements>
      <maxOpenPreparedStatements>-1</maxOpenPreparedStatements>
    </preparedStatements>
    <removal>
      <removeAbandoned>false</removeAbandoned>
      <removeAbandonedTimeout>300</removeAbandonedTimeout>
      <logAbandoned>false</logAbandoned>
    </removal>
    <logging>
      <level>ALL</level>
      <logExpiredConnections>true</logExpiredConnections>
      <logAbandoned>true</logAbandoned>
    </logging>
  </dbcp>
  ```

7. Add [`org.safris.commons:dbcp`](https://github.com/SevaSafris/dbcp/) dependency to the POM.

  ```xml
  <dependency>
    <groupId>org.safris.commons</groupId>
    <artifactId>dbcp</artifactId>
    <version>2.0.2</version>
  </dependency>
  ```
  
8. In the beginning of the `main()` method in `App.java`, initialize the **xdb-entities** `EntityRegistry`.

  ```java
  final dbcp_dbcp dbcp = (dbcp_dbcp)Bindings.parse(new InputSource(Resources.getResourceOrFile("dbcp.xml").getURL().openStream()));
  final DataSource dataSource = DataSources.createDataSource(dbcp);
  EntityRegistry.register(basis.class, PreparedStatement.class, new EntityDataSource() {
    @Override
    public Connection getConnection() throws SQLException {
      return new ConnectionProxy(dataSource.getConnection());
    }
  });
  ```

9. Run `App`.

### Known Issues

* Inner statements are currently not implemented.
* MySQL, PostgreSQL, and Derby are the only vendors currently supported by **xdb-entities**. However, as the vendor-specific facets of the DML have been abstracted, support for any other vendor can be added hastily.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.