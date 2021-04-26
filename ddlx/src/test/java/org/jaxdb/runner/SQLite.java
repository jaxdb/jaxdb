/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb.runner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.jar.JarFile;

import org.jaxdb.vendor.DBVendor;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.libj.sql.AuditConnection;
import org.libj.util.zip.ZipFiles;

public class SQLite extends Vendor {
  private static final String sqliteDb = "sqlite.db";
  private static final File db = new File("target/generated-test-resources/jaxdb/" + sqliteDb);

  public SQLite() throws IOException {
    final File classes = new File("target/classes/" + sqliteDb);
    if (classes.exists() && !FileUtil.deleteAll(classes.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    final File testClasses = new File("target/test-classes/" + sqliteDb);
    if (testClasses.exists() && !FileUtil.deleteAll(testClasses.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    if (!db.exists()) {
      final URL url = ClassLoader.getSystemClassLoader().getResource(sqliteDb);
      if (url != null) {
        db.getParentFile().mkdirs();
        if (URLs.isJar(url)) {
          final JarFile jarFile = new JarFile(URLs.getJarURL(url).getPath());
          final String path = URLs.getJarPath(url);
          ZipFiles.extract(jarFile, db.getParentFile(), f -> f.getName().startsWith(path));
        }
        else {
          Files.copy(new File(url.getPath()).toPath(), db.toPath());
        }
      }
    }
    else if (db.length() == 0 && !db.delete()) {
      throw new IOException("Unable to delete " + db.getPath());
    }
  }

  @Override
  public Connection getConnection() throws IOException, SQLException {
    try {
      return new AuditConnection(DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath()));
    }
    catch (final SQLException e) {
      if (!e.getMessage().startsWith("path to "))
        throw e;

      try {
        Thread.sleep(100);
      }
      catch (final InterruptedException ignore) {
      }
      return getConnection();
    }
  }

  @Override
  public void destroy() throws SQLException {
  }

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.SQLITE;
  }
}