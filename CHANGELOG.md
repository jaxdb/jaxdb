# Changes by Version

## [v0.6.1-SNAPSHOT](https://github.com/libj/util/compare/991b118c291f2c4990dd496ac5b1c35906e544ba..HEAD)

## [v0.6.0](https://github.com/libj/util/compare/d0f0ae913a7617b14a6be5a0cc42c28ad588eb76..991b118c291f2c4990dd496ac5b1c35906e544ba) (2024-02-27)
* #91 Improve performance of `Notifier.tableNameToNotifier`
* #89 Name `CONSTRAINT`s based on column names instead of ordinals
* #88 Add `cuedByUser()` and `cuedBySystem()`
* #87 SqlMojo use mojo parameters provided by `BaseMojo`
* #85 Add `data.Table.toStringCued()`
* #84 Upgrade schemas to v0.6

## [v0.5.1](https://github.com/jaxdb/jaxdb/compare/7218be9c6d299f66753a9a3943f4cefc433b733e..d0f0ae913a7617b14a6be5a0cc42c28ad588eb76) (2023-09-20)
* #81 `Notifier.flushQueues()` `IllegalStateException`: index (1) > count (0)
* #76 Support enum templates in `sqlx.xsl`
* #75 Make JAX-DB entities Serializable
* #74 Use `MapDB` for `DefaultCache`
* #73 Race condition in `Notifier` cache for `insert()` vs `update()`
* #72 Reduce `String` thrashing
* #71 Support `Batch.clear()`
* #70 Support `CONSTRAINT` inheritance across table hierarchy
* #69 `ON CONFLICT IGNORE` should also ignore `UNIQUE` violations
* #68 CREATE FUNCTION: tuple concurrently updated
* #67 Use `BATCH` for `Notifier` `TRIGGER SELECT` and `CREATE`
* #66 `Modification.Result` and `NotifiableModification.NotifiableResult`
* #65 Add microsecond timestamp to `NOTIFY` invocations
* #63 Improve XSD validation
* #62 `data.Column.equals(...)` does not work for array-typed columns
* #61 Paginate `pg_notify()` payloads below 8000 chars
* #59 `sqlx.xsl` does not consider topological weight of composite foreign keys
* #57 Implement default caching
* #56 Reenable table inheritance
* #55 Support `Transaction.Event.EXECUTE`
* #54 Guard logger invocations
* #52 Support `SELECT().execute(Connection)`
* #51 Assert `ddlx:index/@name`
* #50 Make `RowIterator` `Iterable`
* #49 Rows must be discarded to avoid `OOMException`
* #48 Reimplement `data.*.hashCode()` and `data.*.equals()` as per Java's spec
* #47 Add abstraction for `Notifier` and `Notification`
* #46 Don't convert table or column names to camel case in `sqlx.xsl`
* #45 Add public `data.Table.getName()`
* #44 Remove hardcoded `driverClassName` from `DBVendor`

## [v0.4.1](https://github.com/jaxdb/jaxdb/compare/3c76b0b32592bef2d92015639f5364940c6d02b3..7218be9c6d299f66753a9a3943f4cefc433b733e) (2020-05-23)
* Add `DBVendorSpecific`.
* Support CLI execution of `Generator`.
* Improve performance of `Generator`.
* Fix quoting issue with `ENUM` types in PostgreSQL vs MySQL.
* Reduce visibility of classes for better isolation of API.
* Add `DBVendor.loadDriver()`.
* Remove `key`/`keyref` checking in `ddlx-0.4` for expanded options with inherited tables.
* Improve handling of `InvocationTargetException`.
* Update `jaxdb-maven-archetype` in lieu of [#1](https://github.com/jaxdb/jaxdb/issues/1)
* Rewrite class model and POM spec in `jaxdb-maven-plugin`.
* General API improvements across the codebase.
* Improve tests.
* Improve javadocs and xmldocs.

## [v0.4.0](https://github.com/jaxdb/jaxdb/compare/4d7b1b8e9d1d0b5ec300b0154f1de98a2e13383c..3c76b0b32592bef2d92015639f5364940c6d02b3) (2019-07-21)
* Update schema spec from `v0.3` to `v0.4`.
* Update APIs to propagate `SAXException`.
* Upgrade `http://www.openjax.org/xml/datatypes-0.8.xsd` to `http://www.openjax.org/xml/datatypes-0.9.xsd`.
* Upgrade `com.ibm.db2:jcc:11.1.4.4` to `11.5.0.0`.
* Upgrade `mysql:mysql-connector-java:8.0.15` to `8.0.16`.
* Upgrade `org.libj:io:0.7.5` to `0.7.6`.
* Upgrade `org.postgresql:postgresql:42.2.5.jre7` to `42.2.6.jre7`.
* Upgrade `org.xerial:sqlite-jdbc:3.27.2.1` to `3.28.0`.
* Upgrade `org.jaxsb:runtime:2.1.4` to `2.1.5`.
* Upgrade `org.libj:net:0.5.0` to `0.7.1`.

## [v0.3.9](https://github.com/entinae/pom/compare/89108d2fd7c1782659c594889c5ae25f489bb7a8..4d7b1b8e9d1d0b5ec300b0154f1de98a2e13383c) (2019-05-13)
* Initial public release.