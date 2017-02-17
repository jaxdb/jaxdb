<img src="https://www.cohesionfirst.org/logo.png" align="right" />
## xdb-entities<br>![java-enterprise][java-enterprise] <a href="https://www.cohesionfirst.org/"><img src="https://img.shields.io/badge/CohesionFirst%E2%84%A2--blue.svg"></a>
> eXtensible Data Binding Entities

### Introduction

**xdb-entities** is an extension to [**xdb-schema**][xdb-schema], offering a lightweight ORM (Object Relational Mapping) solution that runs on the JDBC v4.1 API. Based on the CohesionFirst™ approach, the **xdb-entities** framework is designed to provide a highly cohesive and strongly-typed binding between the Java language and RDBMS servers. XDE uses a SQL schema defined in a [XDS file][hospital.xds] to create a one-to-one, Object-Model-to-Data-Model API that is vendor agnostic.

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

* [Java 8][jdk8-download] - The minimum required JDK version.
* [Maven][maven] - The dependency management system.

#### Example

1. As **xdb-entities** framework requires a XDS-based SQL Schema, start with a [`xdb-schema` Example][schema-example].

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
            <resources>
              <resource>src/main/resources/schema.xdl</resource>
            </resources>
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

6. To run the code, you must now connect **xdb-entities** to your database. **xdb-entities** relies on the [`commons-dbcp`][commons-dbcp] module to aide in configuration of Database Connection Pools. Create a `dbcp.xml` file in `src/main/resources` that conforms to [this XSD][dbcp.xsd], which defines the Database Connection Pool settings for your connection.

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

7. Add [`org.safris.commons:commons-dbcp`][commons-dbcp] dependency to the POM.

  ```xml
  <dependency>
    <groupId>org.safris.commons</groupId>
    <artifactId>commons-dbcp</artifactId>
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

* To model the strong-typed relation amongst the DataType(s), the **xdb-entities** framework has many method definitions that have hundreds of overloads. This pattern causes a compilation performance inefficiency that results in lengthy compilation times. This is a known bug of javac that has been fixed in JDK 9. The bug can be referenced [here](https://bugs.openjdk.java.net/browse/JDK-8051946).

### Dev Status

*Key: :white_check_mark: Completed :large_blue_circle: In progress :red_circle: Not working :white_circle: Not supported*<br>
*Note: Most icons are clickable links to referred tests*

Specification                                                                                                                           |                          Derby                          |                          MySQL                          |                       PostgreSQL                        |
----------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------:|:-------------------------------------------------------:|:-------------------------------------------------------:|
**<samp><a name="query_expression">&lt;query expression&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>SELECT</samp>                                                                                                         | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                                                | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;select list&gt;](#select_list)</samp>                                                                | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>{ FROM [&lt;table references&gt;](#table_references)</samp>                                               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ [&lt;joined tables&gt;](#joined_tables) ]</samp>                                            | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ WHERE [&lt;where conditions&gt;](#where_conditions) ]</samp>                                | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ GROUP BY [&lt;column names&gt;](#column_names) ]</samp>                                     | [:white_check_mark:][GroupClauseTest]                   | [:white_check_mark:][GroupClauseTest]                   | [:white_check_mark:][GroupClauseTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ HAVING [&lt;having conditions&gt;](#having_conditions) ] }</samp>                           | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;&ensp;&ensp;<samp>[ ORDER BY [&lt;order expressions&gt;](#order_expressions) ]</samp>                                       | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[ LIMIT [&lt;limit expression&gt;](#limit_expression) ]</samp>                                            | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[ UNION [&lt;query expression&gt;](#query_expression) ]</samp>                                            | [:white_check_mark:][UnionExpressionTest]               | [:white_check_mark:][UnionExpressionTest]               | [:white_check_mark:][UnionExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="select_list">&lt;select list&gt;</a>** ::=</samp>                                                                      | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>{ [&lt;set function&gt;](#set_function) \|</samp>                                                                     | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;column expression&gt;](#column_expression) \|</samp>                                                 | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>{ [&lt;query expression&gt;](#query_expression)</samp>                                                    | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ AS :correlation_name ] } \|</samp>                                                          | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            |
&ensp;&ensp;&ensp;&ensp;<samp>{ [ [&lt;table reference&gt;](#table_reference) . ] * } }</samp>                                          | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[ , [&lt;select list&gt;](#select_list) ]</samp>                                                                      | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="set_function">&lt;set function&gt;</a>** ::=</samp>                                                                    | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;<samp>{ COUNT</samp>                                                                                                        | [:white_check_mark:][CountFunctionTest]                 | [:white_check_mark:][CountFunctionTest]                 | [:white_check_mark:][CountFunctionTest]                 |
&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                                                | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;&ensp;&ensp;<samp>{ * \|</samp>                                                                                             | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) } } \|</samp>                                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>{ { AVG \| MAX \| MIN \| SUM }</samp>                                                         | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                        | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) }</samp>                            | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   | [:white_check_mark:][SetFunctionTest]                   |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="column_expression">&lt;column expression&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>:column_name [ AS :column_alias ]</samp>                                                                              | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="table_references">&lt;table references&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[&lt;table reference&gt;](#table_reference) [ , [&lt;table references&gt;](#table_references) ]</samp>                | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="table_reference">&lt;table reference&gt;</a>** ::=</samp>                                                              | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>:table_name \|</samp>                                                                                                 | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>:joined_table_name \|</samp>                                                                                          | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;<samp>{ [ LATERAL ]</samp>                                                                                                  | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression)</samp>                                                      | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            |
&ensp;&ensp;&ensp;&ensp;<samp>[ { AS :correlation_name } \|</samp>                                                                      | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            | [:white_check_mark:][CorrelatedSubQueryTest]            |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>([&lt;column names&gt;](#column_names)) ] }</samp>                                            | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="joined_tables">&lt;joined tables&gt;</a>** ::=</samp>                                                                  | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;<samp>[&lt;joined table&gt;](#joined_table) [ [&lt;joined tables&gt;](#joined_tables) ]</samp>                              | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="joined_table">&lt;joined table&gt;</a>** ::=</samp>                                                                    | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;<samp>{ { CROSS \|</samp>                                                                                                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>NATURAL }</samp>                                                                              | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;<samp>JOIN :table_name } \|</samp>                                                                              | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;<samp>{ { INNER \|</samp>                                                                                                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;<samp>{ { LEFT \| RIGHT \| FULL }</samp>                                                                        | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>OUTER } }</samp>                                                                              | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;<samp>JOIN :table_name</samp>                                                                                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;<samp>{ ON [&lt;where condition&gt;](#where_condition) \|</samp>                                                | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   | [:white_check_mark:][JoinedTableTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>USING ( [&lt;column names&gt;](#column_names) ) } }</samp>                                    | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="column_names">&lt;column names&gt;</a>** ::=</samp>                                                                    | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>:column_name [ , [&lt;column names&gt;](#column_names) ]</samp>                                                       | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="where_conditions">&lt;where conditions&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[&lt;where condition&gt;](#where_condition) [ { OR \| AND } [&lt;where conditions&gt;](#where_conditions) ]</samp>    | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="where_condition">&lt;where condition&gt;</a>** ::=</samp>                                                              | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) [&lt;predicate&gt;](#predicate) [&lt;value expression&gt;](#value_expression)</samp> | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="having_conditions">&lt;having conditions&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;<samp>[&lt;having condition&gt;](#having_condition) [ { OR \| AND } [&lt;having conditions&gt;](#where_conditions) ]</samp> | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="having_condition">&lt;having condition&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;<samp>{ [&lt;value expression&gt;](#value_expression) \|</samp>                                                             | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;set function&gt;](#set_function) }</samp>                                                            | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;<samp>[&lt;predicate&gt;](#predicate)</samp>                                                                                | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;<samp>{ [&lt;value expression&gt;](#value_expression) \|</samp>                                                             | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;set function&gt;](#set_function) }</samp>                                                            | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  | [:white_check_mark:][HavingClauseTest]                  |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="value_expression">&lt;value expression&gt;</a>** ::=</samp>                                                            | :white_check_mark:                                      | :white_check_mark:                                      | :white_check_mark:                                      |
&ensp;&ensp;<samp>[&lt;direct value expression&gt;](#direct_value_expression) \|</samp>                                                 | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[CAST](#cast) ( [&lt;value expression&gt;](#value_expression) AS [&lt;data type&gt;](#data_type) )</samp>             | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="direct_value_expression">&lt;direct value expression&gt;</a>** ::=</samp>                                              | :white_check_mark:                                      | :white_check_mark:                                      | :white_check_mark:                                      |
&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) \|</samp>                                           | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) \|</samp>                                               | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;&ensp;<samp>[&lt;string value expression&gt;](#string_value_expression) \|</samp>                                                 | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         |
&ensp;&ensp;<samp>[&lt;numeric value expression&gt;](#numeric_value_expression) \|</samp>                                               | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;<samp>[&lt;datetime value expression&gt;](#datetime_value_expression)</samp>                                                | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="reference_value_expression">&lt;reference value expression&gt;</a>** ::=</samp>                                        | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;&ensp;<samp>[ [&lt;table reference&gt;](#table_reference) . ] :column_name</samp>                                                 | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               | [:white_check_mark:][QueryExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="boolean_value_expression">&lt;boolean value expression&gt;</a>** ::=</samp>                                            | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                        | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;<samp>{ [&lt;reference value expression&gt;](#reference_value_expression) \|</samp>                                         | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) }</samp>                                    | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;&ensp;<samp>[ IS</samp>                                                                                                           | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                            | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;&ensp;&ensp;<samp>{ TRUE \| FALSE \| UNKNOWN } ]</samp>                                                                     | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;<samp>[ { OR \| AND }</samp>                                                                                                | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) ]</samp>                                    | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        | [:white_check_mark:][BooleanValueExpressionTest]        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="numeric_value_expression">&lt;numeric value expression&gt;</a>** ::=</samp>                                            | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;<samp>{ :numeric_term \|</samp>                                                                                             | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;numeric function&gt;](#numeric_function) \|</samp>                                                   | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;<samp>[ [&lt;numeric predicate&gt;](#numeric_predicate)</samp>                                                              | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;numeric value expression&gt;](#numeric_value_expression) ]</samp>                                    | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="numeric_predicate">&lt;numeric predicate&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;&ensp;<samp>{ + \| - \| * \| / }</samp>                                                                                           | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="numeric_function">&lt;numeric function&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ABS(x) \|</samp>                                                                                                      | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>SIGN(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ROUND(x, d) \|</samp>                                                                                                 | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>FLOOR(x) \|</samp>                                                                                                    | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>CEIL(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>SQRT(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>EXP(x) \|</samp>                                                                                                      | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>POW(x, y) \|</samp>                                                                                                   | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>MOD(x, y) \|</samp>                                                                                                   | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>LN(x) \|</samp>                                                                                                       | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>LOG(x, y) \|</samp>                                                                                                   | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>LOG2(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>LOG10(x) \|</samp>                                                                                                    | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>SIN(x) \|</samp>                                                                                                      | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ASIN(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>COS(x) \|</samp>                                                                                                      | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ACOS(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>TAN(x) \|</samp>                                                                                                      | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ATAN(x) \|</samp>                                                                                                     | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;&ensp;<samp>ATAN2(x, y)</samp>                                                                                                    | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               | [:white_check_mark:][NumericFunctionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="string_value_expression">&lt;string value expression&gt;</a>** ::=</samp>                                              | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         |
&ensp;&ensp;<samp>{ :string_term \|</samp>                                                                                              | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         |
&ensp;&ensp;<samp>[ \|\| [&lt;string value expression&gt;](#string_value_expression) ]</samp>                                           | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         | [:white_check_mark:][StringValueExpressionTest]         |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="datetime_value_expression">&lt;datetime value expression&gt;</a>** ::=</samp>                                          | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;&ensp;<samp>{ :datetime_term \|</samp>                                                                                            | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;&ensp;<samp>{ + \| - }</samp>                                                                                                     | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;&ensp;<samp>INTERVAL ':numeric_term { MICROS \| MILLIS \| SECONDS \| MINUTES \| HOURS \| DAYS \| WEEKS \| MONTHS \| QUARTERS \| YEARS \| DECADES \| CENTURIES \| MILLENNIA }'</samp> | [:white_check_mark:][DateTimeValueExpressionTest] | [:white_check_mark:][DateTimeValueExpressionTest] | [:white_check_mark:][DateTimeValueExpressionTest]       |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="predicate">&lt;predicate&gt;</a>** ::=</samp>                                                                          | :white_check_mark:                                      | :white_check_mark:                                      | :white_check_mark:                                      |
&ensp;&ensp;<samp>[&lt;comparison predicate&gt;](#comparison_predicate) \|</samp>                                                       | [:white_check_mark:][ComparisonPredicateTest]           | [:white_check_mark:][ComparisonPredicateTest]           | [:white_check_mark:][ComparisonPredicateTest]           |
&ensp;&ensp;<samp>[&lt;between predicate&gt;](#between_predicate) \|</samp>                                                             | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              |
&ensp;&ensp;<samp>[&lt;in predicate&gt;](#in_predicate) \|</samp>                                                                       | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;&ensp;<samp>[&lt;like predicate&gt;](#like_predicate) \|</samp>                                                                   | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 |
&ensp;&ensp;<samp>[&lt;null predicate&gt;](#null_predicate) \|</samp>                                                                   | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;&ensp;<samp>[&lt;quantified comparison predicate&gt;](#quantified_comparison_predicate) \|</samp>                                 | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>[&lt;exists predicate&gt;](#exists_predicate) \|</samp>                                                               | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>&lt;unique predicate&gt; \|</samp>                                                                                    | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;<samp>&lt;match predicate&gt; \|</samp>                                                                                     | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;&ensp;<samp>&lt;overlaps predicate&gt;</samp>                                                                                     | :white_circle:                                          | :white_circle:                                          | :white_circle:                                          |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="comparison_predicate">&lt;comparison predicate&gt;</a>** ::=</samp>                                                    | [:white_check_mark:][ComparisonPredicateTest]           | [:white_check_mark:][ComparisonPredicateTest]           | [:white_check_mark:][ComparisonPredicateTest]           |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) { = \| != \| &lt; \| &gt; \| &lt;= \| &gt;= } [&lt;value expression&gt;](#value_expression)</samp> | [:white_check_mark:][ComparisonPredicateTest] | [:white_check_mark:][ComparisonPredicateTest] | [:white_check_mark:][ComparisonPredicateTest]    |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="between_predicate">&lt;between predicate&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                  | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                        | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              |
&ensp;&ensp;<samp>BETWEEN [&lt;value expression&gt;](#value_expression) AND [&lt;value expression&gt;](#value_expression)</samp>        | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              | [:white_check_mark:][BetweenPredicateTest]              |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="in_predicate">&lt;in predicate&gt;</a>** ::=</samp>                                                                    | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                  | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                        | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;&ensp;<samp>IN { ( [&lt;value expressions&gt;](#value_expressions) ) \|</samp>                                                    | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression) }</samp>                                  | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   | [:white_check_mark:][InPredicateTest]                   |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="value_expressions">&lt;value expressions&gt;</a>** ::=</samp>                                                          | :white_check_mark:                                      | :white_check_mark:                                      | :white_check_mark:                                      |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) [ , [&lt;value expressions&gt;](#value_expressions) ]</samp>            | :white_check_mark:                                      | :white_check_mark:                                      | :white_check_mark:                                      |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="like_predicate">&lt;like predicate&gt;</a>** ::=</samp>                                                                | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 |
&ensp;&ensp;<samp>[&lt;string value expression&gt;](#string_value_expression)</samp>                                                    | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                        | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 |
&ensp;&ensp;<samp>LIKE :pattern</samp>                                                                                                  | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 | [:white_check_mark:][LikePredicateTest]                 |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="null_predicate">&lt;null predicate&gt;</a>** ::=</samp>                                                                | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                  | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;&ensp;<samp>IS</samp>                                                                                                             | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                        | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;&ensp;<samp>NULL</samp>                                                                                                           | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 | [:white_check_mark:][NullPredicateTest]                 |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="quantified_comparison_predicate">&lt;quantified comparison predicate&gt;</a>** ::=</samp>                              | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression)</samp>                                              | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>{ = \| != \| &lt; \| &gt; \| &lt;= \| &gt;= }</samp>                                                                  | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>{ ALL \| SOME \| ANY }</samp>                                                                                         | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression)</samp>                                                                  | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][QuantifiedComparisonPredicateTest] | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="exists_predicate">&lt;exists predicate&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;&ensp;<samp>EXISTS [&lt;query expression&gt;](#query_expression)</samp>                                                           | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               | [:white_check_mark:][ExistsPredicateTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="order_expressions">&lt;order expressions&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;&ensp;<samp>[&lt;order expression&gt;](#order_expression) [ , [&lt;order expressions&gt;](#order_expressions) ]</samp>            | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="order_expression">&lt;order expression&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;&ensp;<samp>{ :column_name \| :column_position }</samp>                                                                           | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;&ensp;<samp>[ ASC \| DESC ]</samp>                                                                                                | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               | [:white_check_mark:][OrderExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="limit_expression">&lt;limit expression&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               |
&ensp;&ensp;<samp>:row_count</samp>                                                                                                     | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               |
&ensp;&ensp;<samp>[ OFFSET :row_count ]</samp>                                                                                          | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               | [:white_check_mark:][LimitExpressionTest]               |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="insert_expression">&lt;insert expression&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        |
&ensp;&ensp;<samp>INSERT [ INTO ] [&lt;table reference&gt;](#table_reference)</samp>                                                    | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        |
&ensp;&ensp;&ensp;&ensp;<samp>{ [ ( [&lt;column names&gt;](#column_names) ) ]</samp>                                                    | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>VALUES ( [&lt;value expressions&gt;](#value_expressions) ) } \|</samp>                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression)</samp>                                                      | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        | [:white_check_mark:][InsertTest]                        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="update_expression">&lt;update expression&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;&ensp;<samp>UPDATE [&lt;table reference&gt;](#table_reference)</samp>                                                             | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;&ensp;&ensp;&ensp;<samp>[ SET [&lt;update column&gt;](#update_column) ]</samp>                                                    | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;&ensp;&ensp;&ensp;<samp>[ WHERE [&lt;where conditions&gt;](#where_conditions) ]</samp>                                            | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="update_column">&lt;update column&gt;</a>** ::=</samp>                                                                  | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;&ensp;<samp>[&lt;column name&gt;](#column_name) = [&lt;value expressions&gt;](#value_expressions) [ , [&lt;update column&gt;](#update_column) ]</samp> | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        | [:white_check_mark:][UpdateTest]                        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |
**<samp><a name="delete_expression">&lt;delete expression&gt;</a>** ::=</samp>                                                          | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        |
&ensp;&ensp;<samp>DELETE FROM [&lt;table reference&gt;](#table_reference)</samp>                                                        | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        |
&ensp;&ensp;&ensp;&ensp;<samp>[ WHERE [&lt;where conditions&gt;](#where_conditions) ]</samp>                                            | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        | [:white_check_mark:][DeleteTest]                        |
&ensp;                                                                                                                                  |                                                         |                                                         |                                                         |

<br>

Specification                                                                                                                           |                          Derby                          |                          MySQL                          |                       PostgreSQL                        |
----------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------:|:-------------------------------------------------------:|:-------------------------------------------------------:|
**<a name="data_types">&lt;data type&gt;</a>** ::=&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</samp>  | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          |

![PlantUML model](http://www.plantuml.com/plantuml/svg/ZPHBReCm48RtFWKNQ0vGHKWGd1I9XnHWKLLT66fHAbvoZPH8FNnjS1pRF9BTp3_-nl_OO9J_Ke8AQj5hZtSUeYXb0K2Q70qD9bLXg4xVa-woN8CN5YwN0aHA7ES_kA169M6hKMs8AMxwZZ0PQ0wj_T_BY-vEJPIoRdDbaUPtTIVL-jeQCSHQESKNEcpM7uthnd40dGYeYVLgxTskgp5gRJ2VMg6FGUoau1voybXtYDggQApVPL7YdoBeWGNDxB2sinTdRW214dFIwuQynhOccuonlI7EkgXtgVQ21pzq0g0cuxbLfOUBga6rhTTcgEd3K1URhPX961ZuaRY9MpPskD87WuJGyxDGmZBbTFBT4RgDIr0WVajnYSasvFedoKqnHuEkBP-RqpYwcVoaTpchqM-kFptPl9dPvl6KquaORGnHV1sw9_4QAAqERyuPo6Wy5aegQM0Em2WBWOmu0DkX4SkdnqXp6ZddTw34dCZPD_VnAHZbaLDz1R4BPCIXCqPVSf1bo9MGAo2_xGVA9iAeaHDD3vxwkpdfxPrWqNG51QhPMuZNeTGF1SGZ2QLLWxTtkeEtJDjnRF0F)
<!-- http://www.plantuml.com/plantuml/uml/ZPNRZe8m48Rl-nIymFe06qC4gIv9WKJWOhFPYueDouPJQaaqsOVV8hMsvQ1tS-Wtyq-Bu-RCC6LTLG9yF3EACsQnQqjM7cOuuOOH3hkAq28peaxRqkOodOCNRawd4AOvYOw_96D69kQdiIaf8LNRK1u6ca9h_VVsfgjJ7IMjovmvfEcTxQHAdpiZXfXB9-J2kiNyit7TA4BG2O7gME_MscsQak3Q1kEf5Ng78YS13xCdTsM3cQq6ZVEhD8pzVGWzC14pAynrzgBKHH0eTazwJNSiYQs5JSPeRmH7LTItL6l1hHyu2AZExTwAt2DPKU7IrdCZrDHXH2UBpOY9EOZyKBp4tRjLkB8dWmJGyzD0mjBjwVYdeMmPbw10_9gHdD1boFNtaXli7aLDdRysft5re_2BskMiHhrn_EN9niNCCi-d76x4A6C4nTUXQnBRG2cr_N0EG5gtQs6qa0B600pJ04Yh1s3TLUBui8mqfQ5p-0IAtHD7_irzVGj6MNAgBXIvG5ezT23u90UPXbm9kGBowxPZV2A2ApdHiF3KdvkJtFS4juOdo739To6PXn8_451lIIWfAhBSwG7UFQtZK613wXF_k_m7 -->

<br>

Specification                                                                                                                           |                          Derby                          |                          MySQL                          |                       PostgreSQL                        |
----------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------:|:-------------------------------------------------------:|:-------------------------------------------------------:|
**<a name="numeric_types">&lt;numeric type&gt;</a>** ::=&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</samp>  | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          |

 Type                                   | Bytes                 | Min                                                        | Max                                                                                             | Precision             | Java Type                             |
---------------------------------------:|:----------------------|:-----------------------------------------------------------|:------------------------------------------------------------------------------------------------|:----------------------|:--------------------------------------|
**<samp>SMALLINT<br>.UNSIGNED</samp>**  | <samp>1<br>1</samp>   | <samp>-128<br>0</samp>                                     | <samp>127<br>255</samp>                                                                         | <samp>3<br>3</samp>   | <samp>Byte<br>Short</samp>            |
**<samp>MEDIUMINT<br>.UNSIGNED</samp>** | <samp>2<br>2</samp>   | <samp>-32768<br>0</samp>                                   | <samp>32767<br>64535</samp>                                                                     | <samp>5<br>5</samp>   | <samp>Short<br>Integer</samp>         |
**<samp>INT<br>.UNSIGNED</samp>**       | <samp>4<br>4</samp>   | <samp>-2147483648<br>0</samp>                              | <samp>2147483647<br>4294967295</samp>                                                           | <samp>10<br>10</samp> | <samp>Integer<br>Long</samp>          |
**<samp>BIGINT<br>.UNSIGNED</samp>**    | <samp>8<br>8</samp>   | <samp>-9223372036854775808<br>0</samp>                     | <samp>9223372036854775807<br>18446744073709551615</samp>                                        | <samp>19<br>20</samp> | <samp>Long<br>BigInteger</samp>       |
**<samp>FLOAT<br>.UNSIGNED</samp>**     | <samp>4<br>4</samp>   | <samp>-3.4028235E+38<br>0</samp>                           | <samp>3.4028235E+38<br>3.4028235E+38</samp>                                                     | <samp>6<br>6</samp>   | <samp>Float<br>Float</samp>           |
**<samp>DOUBLE<br>.UNSIGNED</samp>**    | <samp>8<br>8</samp>   | <samp>-1.7976931348623E+308<br>0</samp>                    | <samp>1.7976931348623E+308<br>1.7976931348623E+308</samp>                                       | <samp>15<br>15</samp> | <samp>Double<br>Double</samp>         |
**<samp>DECIMAL<br>.UNSIGNED</samp>**   | <samp>16<br>16</samp> | <samp>-170141183460469231731687303715884105728<br>0</samp> | <samp>170141183460469231731687303715884105727<br>340282366920938463463374607431768211455</samp> | <samp>39<br>39</samp> | <samp>BigDecimal<br>BigDecimal</samp> |

<br>

Specification                                                                                                                           |                          Derby                          |                          MySQL                          |                       PostgreSQL                        |
----------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------:|:-------------------------------------------------------:|:-------------------------------------------------------:|
**<samp><a name="numeric_combination_cast">&lt;numeric combination cast&gt;</a>** ::=&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</samp> | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        | [:white_check_mark:][NumericValueExpressionTest]        |

                           | **<samp>FLOAT</samp>**   | **<samp>DOUBLE</samp>**  | **<samp>DECIMAL</samp>** | **<samp>SMALLINT</samp>** | **<samp>MEDIUMINT</samp>** | **<samp>INT</samp>** | **<samp>BIGINT</samp>** |
--------------------------:|--------------------------|--------------------------|--------------------------|---------------------------|----------------------------|----------------------|-------------------------|
    **<samp>FLOAT</samp>** | **<samp>FLOAT</samp>**   | <samp>DOUBLE</samp>      | <samp>DECIMAL</samp>     | <samp>FLOAT</samp>        | <samp>FLOAT</samp>         | <samp>DOUBLE</samp>  | <samp>DOUBLE</samp>     |
   **<samp>DOUBLE</samp>** | <samp>DOUBLE</samp>      | **<samp>DOUBLE</samp>**  | <samp>DECIMAL</samp>     | <samp>DOUBLE</samp>       | <samp>DOUBLE</samp>        | <samp>DOUBLE</samp>  | <samp>DOUBLE</samp>     |
  **<samp>DECIMAL</samp>** | <samp>DECIMAL</samp>     | <samp>DECIMAL</samp>     | **<samp>DECIMAL</samp>** | <samp>DECIMAL</samp>      | <samp>DECIMAL</samp>       | <samp>DECIMAL</samp> | <samp>DECIMAL</samp>    |
 **<samp>SMALLINT</samp>** | <samp>FLOAT</samp>       | <samp>DOUBLE</samp>      | <samp>DECIMAL</samp>     | **<samp>SMALLINT</samp>** | <samp>MEDIUMINT</samp>     | <samp>INT</samp>     | <samp>BIGINT</samp>     |
**<samp>MEDIUMINT</samp>** | <samp>FLOAT</samp>       | <samp>DOUBLE</samp>      | <samp>DECIMAL</samp>     | <samp>MEDIUMINT</samp>    | **<samp>MEDIUMINT</samp>** | <samp>INT</samp>     | <samp>BIGINT</samp>     |
      **<samp>INT</samp>** | <samp>DOUBLE</samp>      | <samp>DOUBLE</samp>      | <samp>DECIMAL</samp>     | <samp>INT</samp>          | <samp>INT</samp>           | **<samp>INT</samp>** | <samp>BIGINT</samp>     |
   **<samp>BIGINT</samp>** | <samp>DOUBLE</samp>      | <samp>DOUBLE</samp>      | <samp>DECIMAL</samp>     | <samp>BIGINT</samp>       | <samp>BIGINT</samp>        | <samp>BIGINT</samp>  | **<samp>BIGINT</samp>** |

*__NOTE:__ UNSIGNED types follow the same type cast behavior. The UNSIGNED quality of a type is preserved for two UNSIGNED types, and is lost for a mixed pair.*

<br>

Specification                                                                                                                           |                          Derby                          |                          MySQL                          |                       PostgreSQL                        |
----------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------:|:-------------------------------------------------------:|:-------------------------------------------------------:|
**<samp><a name="cast">CAST ([&lt;value expression&gt;](#value_expression) AS [&lt;data type&gt;](#data_type))</a></samp>** ::=&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</samp>  | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          | [:white_check_mark:][CastTest]                          |

   &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>t</samp><br><samp>o</samp><br>**<samp>from</samp>**&ensp;&ensp; | &ensp;<br>&ensp;<br><samp>B</samp><br><samp>O</samp><br><samp>O</samp><br><samp>L</samp><br><samp>E</samp><br><samp>A</samp><br><samp>N</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>F</samp><br><samp>L</samp><br><samp>O</samp><br><samp>A</samp><br><samp>T</samp> | &ensp;<br>&ensp;<br>&ensp;<br><samp>D</samp><br><samp>O</samp><br><samp>U</samp><br><samp>B</samp><br><samp>L</samp><br><samp>E</samp> | &ensp;<br>&ensp;<br><samp>D</samp><br><samp>E</samp><br><samp>C</samp><br><samp>I</samp><br><samp>M</samp><br><samp>A</samp><br><samp>L</samp> | &ensp;<br><samp>S</samp><br><samp>M</samp><br><samp>A</samp><br><samp>L</samp><br><samp>L</samp><br><samp>I</samp><br><samp>N</samp><br><samp>T</samp> | <samp>M</samp><br><samp>E</samp><br><samp>D</samp><br><samp>I</samp><br><samp>U</samp><br><samp>M</samp><br><samp>I</samp><br><samp>N</samp><br><samp>T</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>L</samp><br><samp>O</samp><br><samp>N</samp><br><samp>G</samp> | &ensp;<br>&ensp;<br>&ensp;<br><samp>B</samp><br><samp>I</samp><br><samp>G</samp><br><samp>I</samp><br><samp>N</samp><br><samp>T</samp> | <samp>C</samp><br><samp>H</samp><br><samp>A</samp><br><samp>R</samp><br><samp>/</samp><br><samp>E</samp><br><samp>N</samp><br><samp>U</samp><br><samp>M</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>D</samp><br><samp>A</samp><br><samp>T</samp><br><samp>E</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>T</samp><br><samp>I</samp><br><samp>M</samp><br><samp>E</samp> | &ensp;<br><samp>D</samp><br><samp>A</samp><br><samp>T</samp><br><samp>E</samp><br><samp>T</samp><br><samp>I</samp><br><samp>M</samp><br><samp>E</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>C</samp><br><samp>L</samp><br><samp>O</samp><br><samp>B</samp> | &ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br>&ensp;<br><samp>B</samp><br><samp>L</samp><br><samp>O</samp><br><samp>B</samp> | &ensp;<br>&ensp;<br>&ensp;<br><samp>B</samp><br><samp>I</samp><br><samp>N</samp><br><samp>A</samp><br><samp>R</samp><br><samp>Y</samp> |
--------------------------:|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|--------------------------|
  **<samp>BOOLEAN</samp>** |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: |                          |                          |                          | :heavy_multiplication_x: |                          |                          |
    **<samp>FLOAT</samp>** |                          |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |                          |
   **<samp>DOUBLE</samp>** |                          | :heavy_multiplication_x: |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |                          |
  **<samp>DECIMAL</samp>** |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
 **<samp>SMALLINT</samp>** |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
**<samp>MEDIUMINT</samp>** |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
      **<samp>INT</samp>** |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
   **<samp>BIGINT</samp>** |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
**<samp>CHAR/ENUM</samp>** |                          |                          |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |
     **<samp>DATE</samp>** |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: |                          |                          |                          |                          |                          |                          |
     **<samp>TIME</samp>** |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: |                          | :heavy_multiplication_x: |                          |                          |                          |                          |
 **<samp>DATETIME</samp>** |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |                          |                          |                          |
     **<samp>CLOB</samp>** |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: |                          |                          |                          | :heavy_multiplication_x: |                          |                          |
     **<samp>BLOB</samp>** |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: |                          |
   **<samp>BINARY</samp>** |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          |                          | :heavy_multiplication_x: | :heavy_multiplication_x: |

*__NOTE:__ UNSIGNED types follow the same type cast behavior. The UNSIGNED quality of a type is preserved for two UNSIGNED types, and is lost for a mixed pair.*

### Simplified Class Diagram

![PlantUML model](http://www.plantuml.com/plantuml/svg/bLFBRfmm5DtxA-O7Cb_G3HceX4fWKXDLNNhWDd9hRDSsIQZwyJKo3viQbAoud7Fkm_T1RjeeqfWJ1aK9et_8ZG52P0k3Ggjv-lJtxkwKWheD1_p3W1-B0_ugj7bM48gGNuVJ9OmIlLPq4Lr7U1iHpipqdK2Y8KfWOD2OA9Lp3IfEFScMc7Z0r0PSgV_Boxa1zK9OXAeaA6A4yXlVttxQ-YBYcK5N9Lwefe8lBpNd1DiMEi7KFUq5hLHhm_5dBA69bU7J_iyK2byYQ9fPU4sHlKa5UgmghCF3LtEUMy_Dvvut9iQMVMSpDWV-6FQziHchFN2kePD24VOEoP9ayp9kg1cy5fX1mZabXjJM2BKXjReieunCPUMs4o-J-kbhKWJdQXvxw_w3sblQg0CE-N_JU8iYWgOvhlFoKdj9li3mAbIxZJ_3Q3BM-miHTtWXvYuknDofXPYxCOI2FivRn5lgnhEONjFlgwh8adBBBR_V595oR8olTffx8HWGNeFg8gmzU4aKrO9V5RIq6Mls1pjZcyIUU36dT61U0Rne6pvxezheNJhVzopeBzWbovwnNT8kXTroknLjzBc7j8lmUrghzZpksSSSokyVSy3LG_OF)
<!-- http://www.plantuml.com/plantuml/uml/bLNRRfmm47ttL-mFPB-W3zaiL48bi8c9gZvwOHgvDJQnXoHK_VWQSRE9GStJ3kUSkNWksbk3J6DR2yAk1ZKhyK11SoRu7tOLG3x2eCbY7hxylRavHA2ltGv_tz67T8U_A8FF6dmHSUiOT2V1cLagEgkwPh8A22Tc_1r1PY18K11GOf1Af04TffwQYc4uO6G3BjJ7yXA9e5-P251v0vgXqcxZs-ilMrz0F37aejKhDrp9-QLuF57Rma4mTiyuGNDTsN3ocJPGXifmQVTZ34NF0JIABRoaYDyR3QQlog_3mHTpcbihyMihImonMLlRZEMEFuLjRufzjJlE1TIDqapyXsHEidgPH8wTqmBi6ZX635bX3L_hKulBFCh0L1PkFV7iL3mzdbBlNF6XjUu_kRsbbThZYFirZZTD0sYKq2B9peMJv1jqRqfNw_WJJ4PZkJyV6GxFnuQByx7Xr7nik352l3vEMoGhFendDfsct-Lv6f-oDJT_VjUiQVh6kEf1SwUK02OBq7M0jGVVCCsDaXSD5I_xcjt1JjWgiIEUpJ4Tu1k04st5n-_SeDcaastFbF-6JJ9hXTWaxLBOBRTRKGMVkqiF4Lx6jQgE_PvzpY7xyJa7cj_7v1PaPV_X_W40 -->

### License
 
This  project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details. 

[commons-dbcp]: https://github.com/SevaSafris/commons-dbcp
[dbcp.xsd]: https://github.com/SevaSafris/commons-dbcp/blob/master/src/main/resources/dbcp.xsd
[hospital.xds]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/resources/hospital.xds
[java-enterprise]: https://img.shields.io/badge/java-enterprise-blue.svg
[jdk8-download]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[maven]: https://maven.apache.org/
[schema-example]: https://github.com/SevaSafris/xdb/tree/master/schema#example
[xdb-schema]: https://github.com/SevaSafris/xdb/tree/master/schema/

[BetweenPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/BetweenPredicateTest.java
[BooleanValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/BooleanValueExpressionTest.java
[CastTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/CastTest.java
[ComparisonPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/ComparisonPredicateTest.java
[CorrelatedSubQueryTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/CorrelatedSubQueryTest.java
[CountFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/CountFunctionTest.java
[DateTimeValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/DateTimeValueExpressionTest.java
[DeleteTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/DeleteTest.java
[ExistsPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/ExistsPredicateTest.java
[GroupClauseTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/GroupClauseTest.java
[HavingClauseTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/HavingClauseTest.java
[InPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/InPredicateTest.java
[InsertTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/InsertTest.java
[JoinedTableTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/JoinedTableTest.java
[LikePredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/LikePredicateTest.java
[LimitExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/LimitExpressionTest.java
[NullPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/NullPredicateTest.java
[NumericFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/NumericFunctionTest.java
[NumericValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/NumericValueExpressionTest.java
[OrderExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/OrderExpressionTest.java
[QuantifiedComparisonPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/QuantifiedComparisonPredicateTest.java
[QueryExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/QueryExpressionTest.java
[SetFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/SetFunctionTest.java
[StringValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/StringValueExpressionTest.java
[UnionExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/UnionExpressionTest.java
[UpdateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/UpdateTest.java