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

package org.jaxdb.ddlx.runner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.jar.JarFile;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.jaxdb.vendor.DBVendor;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.libj.sql.AuditConnection;
import org.libj.util.zip.ZipFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Derby extends Vendor {
  static final Logger logger = LoggerFactory.getLogger(Derby.class);
  private static final File[] dbPaths = {new File("target/classes/derby.db"), new File("target/test-classes/derby.db")};

  private final File location;

  public Derby() throws IOException, SQLException {
    this(new File("target/generated-test-resources/jaxdb/derby.db"), false);
  }

  @SuppressWarnings("unused")
  public Derby(final File location, final boolean forceCreate) throws IOException, SQLException {
    this.location = location;
    new EmbeddedDriver();
    for (final File dbPath : dbPaths)
      if (dbPath.exists() && !FileUtil.deleteAll(dbPath.toPath()))
        throw new IOException("Unable to delete " + dbPath.getPath());

    if (location.exists() && new File(location, "seg0").exists())
      return;

    final URL url;
    if (!forceCreate && (url = ClassLoader.getSystemClassLoader().getResource("derby.db")) != null) {
      logger.info("Copying Derby DB from: " + url);
      location.getParentFile().mkdirs();
      if (URLs.isJar(url)) {
        final JarFile jarFile = new JarFile(URLs.getJarURL(url).getPath());
        final String path = URLs.getJarPath(url);
        ZipFiles.extract(jarFile, location.getParentFile(), f -> f.getName().startsWith(path));
      }
      else {
        FileUtil.copyAll(new File(url.getPath()).toPath(), location.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    }
    else {
      logger.info("Creating new Derby DB");
      new AuditConnection(DriverManager.getConnection("jdbc:derby:" + location.getPath() + ";create=true"));
    }

    new File(location, "tmp").mkdir();
  }

  @Override
  public Connection getConnection() throws IOException, SQLException {
    return new AuditConnection(DriverManager.getConnection("jdbc:derby:" + location.getPath()));
  }

  @Override
  @SuppressWarnings("unused")
  public void destroy() throws SQLException {
    try {
      new EmbeddedDriver();
      DriverManager.getConnection("jdbc:derby:;shutdown=true").close();
    }
    catch (final SQLException e) {
      if (!"XJ015".equals(e.getSQLState()) && !"08001".equals(e.getSQLState()))
        throw e;
    }

    new File("derby.log").deleteOnExit();
  }

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.DERBY;
  }
}