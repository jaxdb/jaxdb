# OpenJAX RDB

> Relational Data Binding

[![Build Status](https://travis-ci.org/openjax/rdb.png)](https://travis-ci.org/openjax/rdb)

### Introduction

#### What is RDB?

In short, RDB (Relational Database Binding) is a framework that cohesively binds the [Java Application Layer](#what-is-the-java-application-layer) to a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer).

#### What is Cohesive Binding?

Binding is a high-level software pattern that represents a link between two different software components that do not necessarily share a common platform, language, or interface. Such a pattern can be used to bridge gaps of platform and/or language in application stacks, so as to provide a link that cohesively binds the components. The idea is simple: Tie the two components of different platforms or languages in a way where interface changes to one component would intrinsically direct the developer to respectively change the other component. Cohesive Binding distinguishes itself from non-Cohesive Binding by the strength of representative cohesion and type-safety. For example, consider the gap between the [Java Application Layer](#what-is-the-java-application-layer) and a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer). A non-Cohesive Binding solution would simply wrap the low-level JDBC API, providing the developer with a higher-layer interface that is more conveniet and less wordy, for instance. Such a binding approach is limited, because it only encompasses the JDBC API, which carries a very low risk of problematic changes in the future. A Cohesive Binding solution would provide an encompassing wrapper around the JDBC API and the business/project/solution-specific data model in its entirety. Such a solution would allow the developer to write code with classes and methods that directly bind to the business/project/solution-specific data model itself. As this type of binding soltion encompasses the business/project/solution-specific layer, it can be used to direct the developer to propogate changes made to the data model that would affect both the [Java Application Layer](#what-is-the-java-application-layer) and the [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer).

#### What is the Java Application Layer?

This is the layer in the application stack that is responsible for the business logic of the application. This layer sits between the presentation and data layers, and represents the cohesive center of the application.

#### What is a Vendor Agnostic RDBMS Layer?

This is the layer where data is retained in a Relational Database Management System. Being Vendor Agnostic simply means that the [Java Application Layer](#what-is-the-java-application-layer) should not have to know the specific vendor of the database system.

#### What is so special about RDB?

RDB is a highly cohesive binding framework that binds a business/project/solution-specific data model between the [Java Application Layer](#what-is-the-java-application-layer) and a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer). RDB is also a highly cohesive binding framework that binds the SQL language itself, which allows developers to efficiently implement SQL logic with the help of compile- and edit-time error checking. Such ability for error checking is powerful, because it allows errors to be caught earlier in the development lifecycle. With other frameworks, errors between the application and data layers are most always encountered in runtime. With RDB, an incredibly high percentage of possible errors has been pushed into the compile- and edit-time. This quality significantly reduces the software risk of an application, because it allows developers to spend less time and effort finding and fixing bugs -- instead, bugs are elucidated purely due to the construct of the RDB framework itself.

#### What is so special about the construct of the RDB framework?

The RDB framework is designed with one prime objective: To allow for the realization of as many bugs that exist, or can exist, due to mistakes in the data model, or mistakes in the expression of SQL itself. More so, the RDB framework must not disallow for the legal expressions of the data model or SQL itself. This is a particularly challenging order, as the extent of the data model language and the SQL language is very wide.

### Why **RDB**?

#### CohesionFirst

Developed with the CohesionFirst approach, **RDB** is reliably designed, consistently implemented, and straightforward to use. Made possible by the rigorous conformance to design patterns and best practices in every line of its implementation, **RDB** is a complete binding solution between the DB tier and the business tier. The **RDB** solution differentiates itself from the rest with the strength of its cohesion to the Java language and the SQL, DDL, and DML models.

#### Modules

[**rdb-ddlx**](/ddlx) - A XML-based and vendor-agnostic model for SQL schema definitions.

[**rdb-sqlx**](/sqlx) - A XML-based and vendor-agnostic model for SQL data definitions.

[**rdb-jsql**](/jsql) - A light-weight ORM implementation that utilizes strongly-typed DML semantics.

### JavaDocs

JavaDocs are available [here](https://rdb.openjax.org/apidocs/).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.
