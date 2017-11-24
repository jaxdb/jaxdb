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

package org.libx4j.rdb.sqlx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashSet;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.lib4j.jci.CompilationException;
import org.lib4j.jci.JavaCompiler;
import org.lib4j.lang.ClassLoaders;
import org.lib4j.lang.Resources;
import org.lib4j.util.JavaIdentifiers;
import org.lib4j.xml.jaxb.JaxbUtil;
import org.lib4j.xml.jaxb.XJCompiler;
import org.libx4j.rdb.ddlx.Schemas;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.Schema;
import org.libx4j.rdb.sqlx_0_9_8.Database;
import org.libx4j.xsb.runtime.Bindings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SQLxTest {
  private static final File sourcesDestDir = new File("target/generated-test-sources/jaxb");
  protected static final File resourcesDestDir = new File("target/generated-test-resources/rdb");
  protected static final File testClassesDir = new File("target/test-classes");

  public static void createSchemas(final String name) throws CompilationException, IOException, JAXBException, TransformerException {
    final URL ddlx = Resources.getResource(name + ".ddlx").getURL();
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddl2xsd(ddlx, destFile);

    final XJCompiler.Command command = new XJCompiler.Command();
    command.setExtension(true);
    command.setDestDir(sourcesDestDir);

    final LinkedHashSet<URL> xjbs = new LinkedHashSet<URL>();
    xjbs.add(Resources.getResource("sqlx.xjb").getURL());
    command.setXJBs(xjbs);

    final LinkedHashSet<URL> schemas = new LinkedHashSet<URL>();
    schemas.add(destFile.toURI().toURL());
    command.setSchemas(schemas);

    final URL[] mainClasspath = ClassLoaders.getClassPath();
    final URL[] testClasspath = ClassLoaders.getTestClassPath();
    final File[] classpath = new File[mainClasspath.length + testClasspath.length];
    for (int i = 0; i < mainClasspath.length; i++)
      classpath[i] = new File(mainClasspath[i].getFile());

    for (int i = 0; i < testClasspath.length; i++)
      classpath[i + mainClasspath.length] = new File(testClasspath[i].getFile());

    XJCompiler.compile(command, classpath);
    new JavaCompiler(testClassesDir, classpath).compile(command.getDestDir());
  }

  public static int[] loadData(final Connection connection, final String name) throws ClassNotFoundException, IOException, SAXException, SQLException {
    final Schema schema;
    try (final InputStream in = Resources.getResource(name + ".ddlx").getURL().openStream()) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.truncate(connection, Schemas.flatten(schema).getTable());

    final URL sqlx = Resources.getResource(name + ".sqlx").getURL();
    final Database database;
    try (final InputStream in = sqlx.openStream()) {
      database = (Database)JaxbUtil.parse(Class.forName(name + ".sqlx." + JavaIdentifiers.toClassCase(name)), sqlx, false);
    }

    return SQL.INSERT(connection, database);
  }
}