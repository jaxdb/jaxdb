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

* MySQL, PostgreSQL, and Derby are the only vendors currently supported by **xdb-entities**. However, as the vendor-specific facets of the DML have been abstracted, support for any other vendor can be added hastily.

### Class Diagram

![PlantUML model](http://www.plantuml.com/plantuml/svg/bPHDTfim4CVtdC8NI4_GZMlabbUC7GilifHXGjKAYKeY2NqzV8U0RP24NxE1cTz_PlGrKY6OCIiAcZF1_x2pW2YwUA1Nd_y-FAnYqEVk1Zrzrz77z8v-KCPM6kP1qNJC8MujE3C1lb5rpMGPZVy1KX1GsA2KI0CwB9sQaba8IZ4Ml4d-B19fGRym4P3s3MXcbGv8j3t_n7dYSWTZNAcP-tjTOgQiQ0D5D7N6SBe9q9WWdfKXRuq6OxYIqSsyx64hxRQLXGsfMLjZyU9EFeNbR6hxYTr9pg5kb6RYFqAkWpWhavRRBXf-0wf0zhiEfHCQCylohi4-lbZZ9chDEeTzuHKUgmSbqrwaK6SNAIM0IOTkVZ3j8ZyNFGYWdDSUXeBNs7KELMr_LUUGoBPsq5OevgulLR9oq0vAtjR-Lb0SMVZuVPg1PZzezSBVZpUwccE3hVFJSPrwF7ywaAZ_Z7SioyXnugTacq_SO_BrszJVdj9qufwoP9zvGs3dFhOupsS-l3etR-mBhE7JEA5vacrohzntw5wLngORZV68rvJC2IKfmVGPIp9AZWux7U9rxkJ6c8lC7NgZPCc7ot4rPwru2t31B2m7NIz4uFFNCCsDaWUDFL7qRUeEDb6kvbWVx2l09DNHoHitrYpAoN9cod_1efYrGYoAUAxuQE31bi6rtPM72h_7pYW_zQtoiOJiwMC9TByZ_m00)
<!-- http://www.plantuml.com/plantuml/uml/bPLDTfim4CVtdC8NI4_GHLmZjxnYx5XuaQKC4rU9aAWaaj3NmtS8s0P9-3KRC_Fxpuo-HlAziKpRfX9H8PWn2mgQCy7_iAE0A3fxe1TV_zxTBMBGn_OAFNtJqeVqXhvJnfuqJ8EYyPXzt59mPW9yfQgAoJ8QlZsa8A2mGIcH1dHOEjGbin2KOYnubVnP9DA2VcKY86rhqCmg7P1eStp6UU9oUsDOgPdwMrrYfZpH6efex0pXT1MWCK4yAKFUQmt6S2MZgtdUmqRQTICB6rApfiBYnOry2lFPr7OJkv6SGrKhpSH_X5m6SLOcBRTjr7zwT0BPxJgKJcZCBClR6lluR0sRg3Lh7VQDd_1OFIYPzYA5Ehf8AG5CEdJrYsaN-RdeGG3blF8m5Bp2hdEeQhghEeL4DfM3ra8nTtsfaYS7RQ3aJULl1SMHXOy_fXbezfrMh_pZUAEBEJJOCZ_ibwd7yySTYRgVuOvb6TcF_9Iiyv6xJxx_6FlhGvgEt4EMR3Dl2EpSXmRd-SJxL-Vg3Ns1DNmQ9pHFibNkbVi9xPlIs7I3CHpnaf8feIGbc3vXIKR9tc67NRpCdTmOSv6vGw-KB7b_EIxcf1Ml0IwOXEMWgva8VFvgfhbHSgUn9ugkJTt1Hih57Eg3VGCOfJewUUV6cbavcSzC-GlCYbaZnAo8vugF1Xvi6LpRJRcly7leZF9BrogVIyWUFvT0jniItOCiyR_v7m00 -->

### Dev Status

*Key: :white_check_mark: Completed :large_blue_circle: In progress :red_circle: Not working :white_circle: Not supported*<br>
*Note: Most icons are clickable links to referred tests*

Specification                                                                                                                                                         |        Derby        |        MySQL        |     PostgreSQL      |
----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------:|:-------------------:|:-------------------:|
**<samp><a name="query_expression">&lt;query expression&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>SELECT</samp>                                                                                                                                       | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                                                                              | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;select list&gt;](#select_list)</samp>                                                                                              | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>{ FROM [&lt;table references&gt;](#table_references)</samp>                                                                             | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ [&lt;joined tables&gt;](#joined_tables) ]</samp>                                                                          | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ WHERE [&lt;where conditions&gt;](#where_conditions) ]</samp>                                                              | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ GROUP BY [&lt;column names&gt;](#column_names) ]</samp>                                                                   | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ HAVING [&lt;having conditions&gt;](#having_conditions) ] }</samp>                                                         | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[ ORDER BY [&lt;order expressions&gt;](#order_expressions) ]</samp>                                                                     | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[ LIMIT [&lt;limit expression&gt;](#limit_expression) ]</samp>                                                                          | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[ UNION [&lt;query expression&gt;](#query_expression) ]</samp>                                                                          | [:white_check_mark:][UnionExpressionTest]  | [:white_check_mark:][UnionExpressionTest]  | [:white_check_mark:][UnionExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="select_list">&lt;select list&gt;</a>** ::=</samp>                                                                                                    | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>{ [&lt;set function&gt;](#set_function) \|</samp>                                                                                                   | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;column expression&gt;](#column_expression) \|</samp>                                                                               | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>{ [ [&lt;table reference&gt;](#table_reference) . ] * } }</samp>                                                                        | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[ , [&lt;select list&gt;](#select_list) ]</samp>                                                                                                    | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="set_function">&lt;set function&gt;</a>** ::=</samp>                                                                                                  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;<samp>{ COUNT</samp>                                                                                                                                      | [:white_check_mark:][CountFunctionTest]  | [:white_check_mark:][CountFunctionTest]  | [:white_check_mark:][CountFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                                                                              | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>{ * \|</samp>                                                                                                                           | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) } } \|</samp>                                                                 | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>{ { AVG \| MAX \| MIN \| SUM }</samp>                                                                                       | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ DISTINCT \| ALL ]</samp>                                                                                      | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) }</samp>                                                          | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  | [:white_check_mark:][SetFunctionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="column_expression">&lt;column expression&gt;</a>** ::=</samp>                                                                                        | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>:column_name [ AS :column_alias ]</samp>                                                                                                            | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="table_references">&lt;table references&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[&lt;table reference&gt;](#table_reference) [ , [&lt;table references&gt;](#table_references) ]</samp>                                              | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="table_reference">&lt;table reference&gt;</a>** ::=</samp>                                                                                            | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>:table_name \|</samp>                                                                                                                               | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>:joined_table_name \|</samp>                                                                                                                        | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;<samp>{ [ LATERAL ]</samp>                                                                                                                                |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression)</samp>                                                                                    |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;&ensp;&ensp;<samp>[ AS :correlation_name</samp>                                                                                                           |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[ ([&lt;column names&gt;](#column_names)) ] ] }</samp>                                                                      |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="joined_tables">&lt;joined tables&gt;</a>** ::=</samp>                                                                                                | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;<samp>[&lt;joined table&gt;](#joined_table) [ [&lt;joined tables&gt;](#joined_tables) ]</samp>                                                            | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="joined_table">&lt;joined table&gt;</a>** ::=</samp>                                                                                                  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;<samp>{ { CROSS \|</samp>                                                                                                                                 | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>NATURAL }</samp>                                                                                                            | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>JOIN :table_name } \|</samp>                                                                                                            | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;<samp>{ { INNER \|</samp>                                                                                                                                 | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>{ { LEFT \| RIGHT \| FULL }</samp>                                                                                                      | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>OUTER } }</samp>                                                                                                            | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>JOIN :table_name</samp>                                                                                                                 | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>{ ON [&lt;where condition&gt;](#where_condition) \|</samp>                                                                              | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  | [:white_check_mark:][JoinedTableTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>USING ( [&lt;column names&gt;](#column_names) ) }</samp>                                                                    |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="column_names">&lt;column names&gt;</a>** ::=</samp>                                                                                                  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>:column_name [ , [&lt;column names&gt;](#column_names) ]</samp>                                                                                     | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="where_conditions">&lt;where conditions&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[&lt;where condition&gt;](#where_condition) [ { OR \| AND } [&lt;where conditions&gt;](#where_conditions) ]</samp>                                  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="where_condition">&lt;where condition&gt;</a>** ::=</samp>                                                                                            | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) [&lt;predicate&gt;](#predicate) [&lt;value expression&gt;](#value_expression)</samp>                  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="having_conditions">&lt;having conditions&gt;</a>** ::=</samp>                                                                                        | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;having condition&gt;](#having_condition) [ { OR \| AND } [&lt;having conditions&gt;](#where_conditions) ]</samp>                               | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="having_condition">&lt;having condition&gt;</a>** ::=</samp>                                                                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>{ [&lt;value expression&gt;](#value_expression) \|</samp>                                                                                           | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;set function&gt;](#set_function) }</samp>                                                                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;predicate&gt;](#predicate)</samp>                                                                                                              | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>{ [&lt;value expression&gt;](#value_expression) \|</samp>                                                                                           | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;set function&gt;](#set_function) }</samp>                                                                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="value_expression">&lt;value expression&gt;</a>** ::=</samp>                                                                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) \|</samp>                                                                         | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) \|</samp>                                                                             | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;string value expression&gt;](#string_value_expression) \|</samp>                                                                               | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  |
&ensp;&ensp;<samp>[&lt;numeric value expression&gt;](#numeric_value_expression) \|</samp>                                                                             | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;datetime value expression&gt;](#datetime_value_expression)</samp>                                                                              | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="reference_value_expression">&lt;reference value expression&gt;</a>** ::=</samp>                                                                      | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[ [&lt;table reference&gt;](#table_reference) . ] :column_name</samp>                                                                               | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="boolean_value_expression">&lt;boolean value expression&gt;</a>** ::=</samp>                                                                          | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                                      |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;<samp>{ [&lt;reference value expression&gt;](#reference_value_expression) \|</samp>                                                                       | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) }</samp>                                                                  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;<samp>[ IS</samp>                                                                                                                                         |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                          |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;&ensp;&ensp;<samp>{ TRUE \| FALSE \| UNKNOWN } ]</samp>                                                                                                   |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;<samp>[ { OR \| AND }</samp>                                                                                                                              | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;boolean value expression&gt;](#boolean_value_expression) ]</samp>                                                                  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  | [:white_check_mark:][QueryExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="numeric_value_expression">&lt;numeric value expression&gt;</a>** ::=</samp>                                                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>{ :numeric_term \|</samp>                                                                                                                           | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;numeric function&gt;](#numeric_function) \|</samp>                                                                                 | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                                              | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[ { + \| - \| * \| / }</samp>                                                                                                                       | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;numeric value expression&gt;](#numeric_value_expression) ]</samp>                                                                  | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="numeric_function">&lt;numeric function&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ABS(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>SIGN(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ROUND(x, d) \|</samp>                                                                                                                               | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>FLOOR(x) \|</samp>                                                                                                                                  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>CEIL(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>SQRT(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>EXP(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>POW(x, y) \|</samp>                                                                                                                                 | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>MOD(x, y) \|</samp>                                                                                                                                 | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>LN(x) \|</samp>                                                                                                                                     | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>LOG(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>LOG2(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>LOG10(x) \|</samp>                                                                                                                                  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>SIN(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ASIN(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>COS(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ACOS(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>TAN(x) \|</samp>                                                                                                                                    | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ATAN(x) \|</samp>                                                                                                                                   | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;&ensp;<samp>ATAN2(x, y)</samp>                                                                                                                                  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  | [:white_check_mark:][NumericFunctionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="string_value_expression">&lt;string value expression&gt;</a>** ::=</samp>                                                                            | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  |
&ensp;&ensp;<samp>{ :string_term \|</samp>                                                                                                                            | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                                              | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  |
&ensp;&ensp;<samp>[ \|\| [&lt;string value expression&gt;](#string_value_expression) ]</samp>                                                                         | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  | [:white_check_mark:][StringValueExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="datetime_value_expression">&lt;datetime value expression&gt;</a>** ::=</samp>                                                                        | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;&ensp;<samp>{ :datetime_term \|</samp>                                                                                                                          | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;&ensp;&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression) }</samp>                                                              | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;&ensp;<samp>{ + \| - }</samp>                                                                                                                                   | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;&ensp;<samp>INTERVAL ':numeric_term { MICROS \| MILLIS \| SECONDS \| MINUTES \| HOURS \| DAYS \| WEEKS \| MONTHS \| QUARTERS \| YEARS \| DECADES \| CENTURIES \| MILLENNIA }'</samp> | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  | [:white_check_mark:][DateTimeValueExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="predicate">&lt;predicate&gt;</a>** ::=</samp>                                                                                                        | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;comparison predicate&gt;](#comparison_predicate) \|</samp>                                                                                     | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;between predicate&gt;](#between_predicate) \|</samp>                                                                                           | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  |
&ensp;&ensp;<samp>[&lt;in predicate&gt;](#in_predicate) \|</samp>                                                                                                     | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;&ensp;<samp>[&lt;like predicate&gt;](#like_predicate) \|</samp>                                                                                                 | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  |
&ensp;&ensp;<samp>[&lt;null predicate&gt;](#null_predicate) \|</samp>                                                                                                 | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;&ensp;<samp>[&lt;quantified comparison predicate&gt;](#quantified_comparison_predicate) \|</samp>                                                               | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>[&lt;exists predicate&gt;](#exists_predicate) \|</samp>                                                                                             | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>&lt;unique predicate&gt; \|</samp>                                                                                                                  |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;<samp>&lt;match predicate&gt; \|</samp>                                                                                                                   |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;&ensp;<samp>&lt;overlaps predicate&gt;</samp>                                                                                                                   |    :white_circle:   |    :white_circle:   |    :white_circle:   |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="comparison_predicate">&lt;comparison predicate&gt;</a>** ::=</samp>                                                                                  | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) { = \| != \| &lt; \| &gt; \| &lt;= \| &gt;= } [&lt;value expression&gt;](#value_expression)</samp>    | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="between_predicate">&lt;between predicate&gt;</a>** ::=</samp>                                                                                        | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                                                | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                                      | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  |
&ensp;&ensp;<samp>BETWEEN [&lt;value expression&gt;](#value_expression) AND [&lt;value expression&gt;](#value_expression)</samp>                                      | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  | [:white_check_mark:][BetweenPredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="in_predicate">&lt;in predicate&gt;</a>** ::=</samp>                                                                                                  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                                                | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                                      | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;&ensp;<samp>IN { ( [&lt;value expressions&gt;](#value_expressions) ) \|</samp>                                                                                  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression) }</samp>                                                                | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  | [:white_check_mark:][InPredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="value_expressions">&lt;value expressions&gt;</a>** ::=</samp>                                                                                        | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression) [ , [&lt;value expressions&gt;](#value_expressions) ]</samp>                                          | :white_check_mark:  | :white_check_mark:  | :white_check_mark:  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="like_predicate">&lt;like predicate&gt;</a>** ::=</samp>                                                                                              | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  |
&ensp;&ensp;<samp>[&lt;string value expression&gt;](#string_value_expression)</samp>                                                                                  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                                      | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  |
&ensp;&ensp;<samp>LIKE :pattern</samp>                                                                                                                                | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  | [:white_check_mark:][LikePredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="null_predicate">&lt;null predicate&gt;</a>** ::=</samp>                                                                                              | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;&ensp;<samp>[&lt;value expression&gt;](#value_expression)</samp>                                                                                                | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;&ensp;<samp>IS</samp>                                                                                                                                           | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;&ensp;<samp>[ NOT ]</samp>                                                                                                                                      | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;&ensp;<samp>NULL</samp>                                                                                                                                         | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  | [:white_check_mark:][NullPredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="quantified_comparison_predicate">&lt;quantified comparison predicate&gt;</a>** ::=</samp>                                                            | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>[&lt;reference value expression&gt;](#reference_value_expression)</samp>                                                                            | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>{ = \| != \| &lt; \| &gt; \| &lt;= \| &gt;= }</samp>                                                                                                | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>{ ALL \| SOME \| ANY }</samp>                                                                                                                       | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>[&lt;query expression&gt;](#query_expression)</samp>                                                                                                | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][QuantifiedComparisonPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="exists_predicate">&lt;exists predicate&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;&ensp;<samp>EXISTS [&lt;query expression&gt;](#query_expression)</samp>                                                                                         | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  | [:white_check_mark:][ExistsPredicateTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="order_expressions">&lt;order expressions&gt;</a>** ::=</samp>                                                                                        | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;&ensp;<samp>[&lt;order expression&gt;](#order_expression) [ , [&lt;order expressions&gt;](#order_expressions) ]</samp>                                          | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="order_expression">&lt;order expression&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;&ensp;<samp>{ :column_name \| :column_position }</samp>                                                                                                         | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;&ensp;<samp>[ ASC \| DESC ]</samp>                                                                                                                              | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  | [:white_check_mark:][OrderExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |
**<samp><a name="limit_expression">&lt;limit expression&gt;</a>** ::=</samp>                                                                                          | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  |
&ensp;&ensp;<samp>:row_count</samp>                                                                                                                                   | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  |
&ensp;&ensp;<samp>[ OFFSET :row_count ]</samp>                                                                                                                        | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  | [:white_check_mark:][LimitExpressionTest]  |
&ensp;                                                                                                                                                                |                     |                     |                     |

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
[CountFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/CountFunctionTest.java
[DateTimeValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/DateTimeValueExpressionTest.java
[ExistsPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/ExistsPredicateTest.java
[InPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/InPredicateTest.java
[JoinedTableTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/JoinedTableTest.java
[LikePredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/LikePredicateTest.java
[UnionExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/UnionExpressionTest.java
[LimitExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/LimitExpressionTest.java
[NullPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/NullPredicateTest.java
[NumericFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/NumericFunctionTest.java
[OrderExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/OrderExpressionTest.java
[QuantifiedComparisonPredicateTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/QuantifiedComparisonPredicateTest.java
[QueryExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/QueryExpressionTest.java
[SetFunctionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/SetFunctionTest.java
[StringValueExpressionTest]: https://github.com/SevaSafris/xdb-maven-plugin/blob/master/src/test/java/org/safris/maven/plugin/xdb/xde/StringValueExpressionTest.java