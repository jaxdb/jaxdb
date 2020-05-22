# Changes by Version

## [v0.4.1](https://github.com/jaxdb/jaxdb/compare/3c76b0b32592bef2d92015639f5364940c6d02b3..HEAD) (2020-05-23)
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

## v0.3.9 (2019-05-13)
* Initial public release.