# JAX-DB

[![Build Status](https://travis-ci.org/jaxdb/jaxdb.svg?1)](https://travis-ci.org/jaxdb/jaxdb)
[![Coverage Status](https://coveralls.io/repos/github/jaxdb/jaxdb/badge.svg?1)](https://coveralls.io/github/jaxdb/jaxdb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxdb/jaxdb.svg?1)](https://www.javadoc.io/doc/org.jaxdb/jaxdb)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxdb/jaxdb.svg?1)](https://mvnrepository.com/artifact/org.jaxdb/jaxdb)

## Introduction

<ins>JAX-DB (Java Architecture Extension for [Relational] Database Binding)</ins> is a framework that cohesively binds the [Java Application Layer](#what-is-the-java-application-layer) to a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer).

### Modules

* [**<ins>JAX-DB Maven Archetype</ins>**](/jaxdb-maven-archetype) - Quick-start Maven Archetype for the JAX-DB framework.

* [**<ins>JAX-DB Maven Plugin</ins>**](/jaxdb-maven-plugin) - Maven Plugin for the JAX-DB framework.

* [**<ins>DDLx</ins>**](/ddlx) - An XML-based and vendor-agnostic model for SQL <ins>schema</ins> definitions.

* [**<ins>SQLx</ins>**](/sqlx) - An XML-based and vendor-agnostic model for SQL <ins>data</ins> definitions.

* [**<ins>jSQL</ins>**](/jsql) - Strong-typed SQL semantics and ORM implementation.

## FAQ

### What is Cohesive Binding?

Binding is a high-level software pattern that represents a link between two different software components that do not necessarily share a common platform, language, or interface. Such a pattern can be used to bridge gaps of platform and/or language in application stacks, so as to provide a link that cohesively binds the components. The idea is simple: Tie the two components of different platforms or languages in a way where interface changes to one component would intrinsically direct the developer to respectively change the other component. Cohesive Binding distinguishes itself from non-Cohesive Binding by the strength of representative cohesion and type-safety. For example, consider the gap between the [Java Application Layer](#what-is-the-java-application-layer) and a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer). A non-Cohesive Binding solution would simply wrap the low-level JDBC API, providing the developer with a higher-layer interface that is more conveniet and less wordy, for instance. Such a binding approach is limited, because it only encompasses the JDBC API, which carries a very low risk of problematic changes in the future. A Cohesive Binding solution would provide an encompassing wrapper around the JDBC API and the business/project/solution-specific data model in its entirety. Such a solution would allow the developer to write code with classes and methods that directly bind to the business/project/solution-specific data model itself. As this type of binding soltion encompasses the business/project/solution-specific layer, it can be used to direct the developer to propogate changes made to the data model that would affect both the [Java Application Layer](#what-is-the-java-application-layer) and the [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer).

### What is the Java Application Layer?

This is the layer in the application stack that is responsible for the business logic of the application. This layer sits between the presentation and data layers, and represents the cohesive center of the application.

### What is a Vendor Agnostic RDBMS Layer?

This is the layer where data is retained in a Relational Database Management System. Being Vendor Agnostic simply means that the [Java Application Layer](#what-is-the-java-application-layer) should not have to know the specific vendor of the database system.

### What is so special about <ins>JAX-DB</ins>?

<ins>JAX-DB</ins> is a highly cohesive binding framework that binds a business/project/solution-specific data model between the [Java Application Layer](#what-is-the-java-application-layer) and a [Vendor Agnostic RDBMS Layer](#what-is-a-vendor-agnostic-rdbms-layer). <ins>JAX-DB</ins> is also a highly cohesive binding framework that binds the SQL language itself, which allows developers to efficiently implement SQL logic with the help of compile- and edit-time error checking. Such ability for error checking is powerful, because it allows errors to be caught earlier in the development lifecycle. With other frameworks, errors between the application and data layers are most always encountered in runtime. With <ins>JAX-DB</ins>, an incredibly high percentage of possible errors has been pushed into the compile- and edit-time. This quality significantly reduces the software risk of an application, because it allows developers to spend less time and effort finding and fixing bugs -- instead, bugs are elucidated purely due to the construct of the <ins>JAX-DB</ins> framework itself.

### What is so special about the construct of the <ins>JAX-DB</ins> framework?

The <ins>JAX-DB</ins> framework is designed with one prime objective: To allow for the realization of as many bugs that exist, or can exist, due to mistakes in the data model, or mistakes in the expression of SQL itself. More so, the <ins>JAX-DB</ins> framework must not disallow for the legal expressions of the data model or SQL itself. This is a particularly challenging order, as the extent of the data model language and the SQL language is very wide.

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.