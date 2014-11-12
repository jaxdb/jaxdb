/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.safris.commons.lang.Resources;
import org.safris.commons.lang.Strings;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.xde.EntityBridgeUtil.Assignment;
import org.safris.xdb.xdl.DBVendor;
import org.safris.xdb.xdl.DDLTransform;
import org.safris.xdb.xdl.xdl_database;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.xml.sax.InputSource;

import xdb.xde.Livecare;

import com.livecare.entity.livecare_patient;
import com.livecare.entity.livecare_provider;
import com.livecare.entity.livecare_provider_patient;

public class EntitiesTest {
  private static Connection createConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:derby:memory:" + Strings.getRandomAlphaString(6) + ";create=true");
  }

  private static void x() throws SQLException {
    final Connection connection = createConnection();
    final Statement statement = connection.createStatement();
    System.err.println(connection.getWarnings());
    statement.executeUpdate("CREATE TABLE foo (bar VARCHAR(255))");
    statement.executeUpdate("INSERT INTO foo VALUES ('success')");
    final ResultSet resultSet = statement.executeQuery("SELECT * FROM foo");
    while (resultSet.next())

      System.out.println(resultSet.getString(1));

    statement.close();
    connection.close();
  }

  private static void createSchema(final Connection connection, final String ddl) throws SQLException {
    final String[] sqls = ddl.split(";\n");
    final Statement statement = connection.createStatement();
    sqls[sqls.length - 1] = sqls[sqls.length - 1].substring(0, sqls[sqls.length - 1].length() - 1);
    int i = -1;
    try {
      while (++i < sqls.length)
        if ((sqls[i] = sqls[i].trim()).length() > 0) {
          System.err.println(sqls[i]);
          statement.executeUpdate(sqls[i]);
        }
    }
    catch (final Exception e) {
      throw new SQLException(sqls[--i], e);
    }
  }

  @Test
  @Ignore
  public void test2() throws Exception {
    final URL url = Resources.getResource("/entity.xdl").getURL();
    final DDLTransform ddlTransform = DDLTransform.transformDDL(url);
    final String ddl = ddlTransform.parse(DBVendor.DERBY);

    final Connection connection = createConnection();
    //createSchema(connection, ddl);

    Statement statement = connection.createStatement();
    String y = "CREATE SCHEMA livecare\n";
    statement.executeUpdate(y);

    String x = "CREATE TABLE diagnosis (\n";
    x += "icd9 VARCHAR(16) NOT NULL,\n";
    x += "description VARCHAR(255) NOT NULL,\n";
    x += "PRIMARY KEY (icd9)\n";
    x += ")";
    statement.executeUpdate(x);

    final Livecare.Diagnosis diagnosis = new Livecare.Diagnosis();
    diagnosis.setIcd9("icd9");
    diagnosis.setDescription("description");
    diagnosis.insert(connection);
    diagnosis.setDescription("changed");
    diagnosis.update(connection);
    diagnosis.select(connection);
  }

  @Test
  public void testEntities2() throws Exception {
    final String sql = "SELECT pr, pp, p FROM Provider pr, ProviderPatient pp, Patient p WHERE pr.gender = 'M' AND pr.id = pp.providerId AND p.id = pp.patientId";
    System.err.println(EntityUtil.compile(Livecare.class, sql));
  }

  @Test
  @Ignore
  public void testEntities() throws Exception {
    final xdl_database database = (xdl_database)Bindings.parse(new InputSource(Resources.getResource("/entity.xdl").getURL().openStream()));
    final String sql = "SELECT patient.*, pr.given_name, pr.website, pr.*, pp.*, pp.id, pp.emergency_contact_name FROM provider pr, provider_patient pp, patient WHERE pr.gender = 'M' AND pr.id = pp.provider_id AND patient.id = pp.patient_id";
    final Map<String,Class<?>> aliasToBinding = EntityBridgeUtil.parseBindings(database, sql);
    Assert.assertEquals(livecare_provider.class, aliasToBinding.get("pr"));
    Assert.assertEquals(livecare_provider_patient.class, aliasToBinding.get("pp"));
    Assert.assertEquals(livecare_patient.class, aliasToBinding.get("patient"));

    final EntityBridge entities = EntityBridge.getInstance(database);
    final List<Assignment> assignments = entities.parseAssignments(sql);
    Assert.assertEquals(5, assignments.size());
    Assignment assignment = assignments.get(0);
    Assert.assertEquals(livecare_patient.class, assignment.binding);
    Assert.assertEquals(17, assignment.entries.size());
    assignment = assignments.get(1);
    Assert.assertEquals(livecare_provider.class, assignment.binding);
    Assert.assertEquals(2, assignment.entries.size());
    assignment = assignments.get(2);
    Assert.assertEquals(livecare_provider.class, assignment.binding);
    Assert.assertEquals(42, assignment.entries.size());
    assignment = assignments.get(3);
    Assert.assertEquals(livecare_provider_patient.class, assignment.binding);
    Assert.assertEquals(31, assignment.entries.size());
    assignment = assignments.get(4);
    Assert.assertEquals(livecare_provider_patient.class, assignment.binding);
    Assert.assertEquals(2, assignment.entries.size());
    // FIXME: Weak tests here! :| Should check the order of columns too
  }
}