package com.mycompany.app;

import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.jaxdb.jsql.Registry;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.db;
import org.jaxsb.runtime.Bindings;
import org.openjax.dbcp.DataSources;
import org.xml.sax.SAXException;

import com.mycompany.app.config.xAA.Config;

/**
 * Before running this app, you must create your MySQL database:
 * <pre>
 * >> CREATE DATABASE MyDB;
 * >> CREATE USER MyDB IDENTIFIED BY 'MyDB';
 * >> GRANT ALL ON MyDB.* TO 'MyDB'@'%';
 * </pre>
 * Then, you must load the generated schema via:
 * <pre>
 * mysql -D MyDB -u MyDB --password=MyDB < ./model/target/generated-resources/jaxdb/db.sql
 * </pre>
 * Note: You can change the name from "MyDB" to something else, but make sure to
 * update the DBCP spec in config.xml.
 */
public class MyApp {
  public static void main(final String[] args) throws IOException, SAXException, SQLException {
    final Config config = (Config)Bindings.parse(ClassLoader.getSystemClassLoader().getResource("config.xml"));
    Registry.global().registerPrepared(db.class, DataSources.createDataSource("uncommitted", config.getDbcpDbcp()));

    db.Account a = new db.Account();
    try (final RowIterator<db.Account> rowIterator =
      SELECT(a).
      FROM(a).
      WHERE(EQ(a.email, "jake@mycompany.com"))
        .execute()) {
      while (rowIterator.nextRow()) {
        a = rowIterator.nextEntity();
        System.out.println(a.email.get());
      }
    }
  }
}