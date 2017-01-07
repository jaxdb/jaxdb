/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import static org.safris.xdb.entities.DML.INSERT;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.JarFile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.io.Files;
import org.safris.commons.io.JarFiles;
import org.safris.commons.jci.CompilationException;
import org.safris.commons.jci.JavaCompiler;
import org.safris.commons.lang.PackageNotFoundException;
import org.safris.commons.lang.Resource;
import org.safris.commons.lang.Resources;
import org.safris.commons.net.URLs;
import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.entities.generator.Generator;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public class EntitiesTest {
  private static final FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(final File pathname) {
      return pathname.getName().endsWith(".java");
    }
  };

  private static Connection connection;

  @BeforeClass
  @SuppressWarnings("unchecked")
  public static void create() throws ClassNotFoundException, CompilationException, IOException, PackageNotFoundException, SQLException, XMLException {
    final Resource resource = Resources.getResource("test-db");
    final File db = new File("target/generated-test-resources/test-db");
    if (URLs.isJar(resource.getURL())) {
      final JarFile jarFile = new JarFile(URLs.getParentJar(resource.getURL()).getPath());
      final String path = URLs.getPathInJar(resource.getURL());
      JarFiles.extract(jarFile, path, db.getParentFile());
    }
    else {
      Files.copy(new File(resource.getURL().getPath()), db);
    }

    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final File xdeDestDir = new File("target/generated-test-sources/xdb");
    Generator.generate(xds, xdeDestDir);

    final JavaCompiler compiler = new JavaCompiler(new File("target/test-classes"));
    compiler.compile(Files.listAll(xdeDestDir, fileFilter));

    connection = new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath()));
    final Statement s = connection.createStatement();
    s.executeQuery("SELECT * from Office");
    EntityRegistry.register((Class<? extends Schema>)Class.forName("xdb.ddl.classicmodels"), PreparedStatement.class, new EntityDataSource() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });
  }

  @Test
  public void testEntities() throws IOException, SQLException, XMLException {
    final URL xdd = Resources.getResource("classicmodels.xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    INSERT(data).execute();
  }

  @AfterClass
  public static void destroy() {
    new File("derby.log").deleteOnExit();
  }
}