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
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.jar.JarFile;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.standard.io.FastFiles;
import org.openjax.standard.net.URLs;
import org.openjax.standard.sql.AuditConnection;
import org.openjax.standard.util.zip.ZipFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Derby implements Vendor {
  protected static final Logger logger = LoggerFactory.getLogger(Derby.class);
  private static final File db = new File("target/generated-test-resources/rdb/derby.db");
  private static final File[] dbPaths = new File[] {new File("target/classes/derby.db"), new File("target/classes/derby.db")};

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.DERBY;
  }

  @Override
  @SuppressWarnings("unused")
  public synchronized void init() throws IOException, SQLException {
    new EmbeddedDriver();
    for (final File dbPath : dbPaths)
      if (dbPath.exists() && !FastFiles.deleteAll(dbPath.toPath()))
        throw new IOException("Unable to delete " + dbPath.getPath());

    if (db.exists() && new File(db, "seg0").exists())
      return;

    final URL url = Thread.currentThread().getContextClassLoader().getResource("derby.db");
    if (url != null) {
      logger.info("Copying Derby DB from: " + url);
      db.getParentFile().mkdirs();
      if (URLs.isJar(url)) {
        final JarFile jarFile = new JarFile(URLs.getJarURL(url).getPath());
        final String path = URLs.getJarPath(url);
        ZipFiles.extract(jarFile, db.getParentFile(), f -> f.getName().startsWith(path));
      }
      else {
        FastFiles.copyAll(new File(url.getPath()).toPath(), db.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    }
    else {
      logger.info("Creating new Derby DB");
      new AuditConnection(DriverManager.getConnection("jdbc:derby:" + db.getPath() + ";create=true"));
    }

    new File(db, "tmp").mkdir();
  }

  @Override
  public Connection getConnection() throws IOException, SQLException {
    return new AuditConnection(DriverManager.getConnection("jdbc:derby:" + db.getPath()));
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