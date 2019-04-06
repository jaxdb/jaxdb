/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.ddlx.runner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.jar.JarFile;

import org.openjax.rdb.vendor.DBVendor;
import org.openjax.standard.io.FastFiles;
import org.openjax.standard.net.URLs;
import org.openjax.standard.sql.AuditConnection;
import org.openjax.standard.util.zip.ZipFiles;

public class SQLite implements Vendor {
  private static final File db = new File("target/generated-test-resources/rdb/sqlite.db");

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.SQLITE;
  }

  @Override
  public synchronized void init() throws IOException, SQLException {
    final File classes = new File("target/classes/sqlite.db");
    if (classes.exists() && !FastFiles.deleteAll(classes.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    final File testClasses = new File("target/test-classes/sqlite.db");
    if (testClasses.exists() && !FastFiles.deleteAll(testClasses.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    if (db.exists()) {
      if (db.length() != 0)
        return;

      if (!db.delete())
        throw new IOException("Unable to delete " + db.getPath());
    }

    final URL url = Thread.currentThread().getContextClassLoader().getResource("sqlite.db");
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

  @Override
  public Connection getConnection() throws IOException, SQLException {
    return new AuditConnection(DriverManager.getConnection("jdbc:sqlite:" + db.getPath()));
  }

  @Override
  public void destroy() throws SQLException {
  }
}