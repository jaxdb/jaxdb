/* Copyright (c) 2017 lib4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libx4j.rdb.ddlx.runner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.jar.JarFile;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.fastjax.sql.ConnectionProxy;
import org.lib4j.io.Files;
import org.lib4j.io.JarFiles;
import org.fastjax.net.URLs;
import org.libx4j.rdb.vendor.DBVendor;

public class Derby implements Vendor {
  private static final File db = new File("target/generated-test-resources/rdb/derby.db");

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.DERBY;
  }

  @Override
  @SuppressWarnings("unused")
  public synchronized void init() throws IOException, SQLException {
    new EmbeddedDriver();
    final File classes = new File("target/classes/derby.db");
    if (classes.exists() && !Files.deleteAll(classes.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    final File testClasses = new File("target/test-classes/derby.db");
    if (testClasses.exists() && !Files.deleteAll(testClasses.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    if (db.exists())
      return;

    final URL url = Thread.currentThread().getContextClassLoader().getResource("derby.db");
    if (url != null) {
      db.getParentFile().mkdirs();
      if (URLs.isJar(url)) {
        final JarFile jarFile = new JarFile(URLs.getParentJar(url).getPath());
        final String path = URLs.getPathInJar(url);
        JarFiles.extract(jarFile, path, db.getParentFile());
      }
      else {
        Files.copy(new File(url.getPath()), db);
      }
    }
    else {
      new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath() + ";create=true"));
    }

    new File(db, "tmp").mkdir();
  }

  @Override
  public Connection getConnection() throws IOException, SQLException {
    return new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath())) {
      @Override
      public void setAutoCommit(final boolean autoCommit) throws SQLException {
      }

      @Override
      public void commit() throws SQLException {
      }

      @Override
      public void rollback() throws SQLException {
      }
    };
  }

  @Override
  @SuppressWarnings("unused")
  public void destroy() throws SQLException {
    try {
      new EmbeddedDriver();
      DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }
    catch (final SQLException e) {
      if (!"XJ015".equals(e.getSQLState()) && !"08001".equals(e.getSQLState()))
        throw e;
    }

    new File("derby.log").deleteOnExit();
  }
}