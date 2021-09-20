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

package org.jaxdb.ddlx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.MarshalException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.test.AssertXml;
import org.xml.sax.SAXException;

@RunWith(DBTestRunner.class)
public abstract class ReverseTest extends DDLxTest {
  @Ignore("FIXME")
  @DB(value=Derby.class, parallel=2)
  // @VendorRunner.Vendor(SQLite.class) // FIXME: Enable SQLite
  public static class IntegrationTest extends ReverseTest {
  }

  @Ignore("Not implemented")
  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends ReverseTest {
  }

  private static void assertEqual(final DBVendor vendor, final Binding expected, final Binding actual) throws XPathExpressionException {
    final AssertXml builder = AssertXml.compare(expected.toDOM(), actual.toDOM());
    if (vendor == DBVendor.DERBY) {
      builder.removeFromControl(
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:binary']/@default",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:bigint']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:bigint']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:datetime']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:decimal']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:double']/@unsigned",
        "//ddlx:schema/ddlx:table[@name='t_enum']",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:float']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:int']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:int']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:smallint']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:smallint']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:time']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:tinyint']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:tinyint']/@unsigned",
        "//ddlx:schema/ddlx:table/ddlx:column/ddlx:foreignKey[@onDelete='SET DEFAULT']/@onDelete",
        "//ddlx:schema/ddlx:table/ddlx:column/ddlx:foreignKey[@onUpdate='SET DEFAULT']/@onUpdate",
        "//ddlx:schema/ddlx:table/ddlx:column/ddlx:foreignKey[@onUpdate='SET NULL']/@onUpdate",
        "//ddlx:schema/ddlx:table/ddlx:column/ddlx:foreignKey[@onUpdate='RESTRICT']/@onUpdate",
        "//ddlx:schema/ddlx:table/ddlx:column/ddlx:foreignKey[@onUpdate='CASCADE']/@onUpdate"
      )

      .replaceAttrInControl("//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:float']/@xsi:type", "ddlx:double")
      .replaceAttrInControl("//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:tinyint']/@xsi:type", "ddlx:smallint")
      .replaceAttrInControl("//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum' and @values='SEVEN EIGHT NINE']/@values", "length", "5")
      .addAttrToControl("//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']", "varying", "true")
      .replaceAttrInControl("//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']/@xsi:type", "ddlx:char")

      .removeFromTest(
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:binary' and @length='2147483647']/@length",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:blob' and @length='2147483647']/@length",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:clob' and @length='2147483647']/@length",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:decimal' and not(@length)]/@length",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:decimal' and @name='c_implicit']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:decimal' and @name='c_implicit']/@scale",
        "//ddlx:schema/ddlx:table[@name='t_enum']",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:time']/@precision"
      );
    }

    builder.assertEqual(true);
  }

  private static final Comparator<Binding> hashCodeComparator = Comparator.comparingLong(Binding::hashCode);

  private static void sort(final Schema schema) {
    for (final $Table table : schema.getTable()) {
      if (table.getIndexes() != null && table.getIndexes().getIndex() != null && table.getIndexes().getIndex().size() > 0)
        table.getIndexes().getIndex().sort(hashCodeComparator);

      if (table.getConstraints() != null && table.getConstraints().getUnique() != null && table.getConstraints().getUnique().size() > 0)
        table.getConstraints().getUnique().sort(hashCodeComparator);
    }
  }

  @Test
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, SAXException, SQLException, TransformerException, XPathExpressionException {
    final Schema expected = recreateSchema(connection, "reverse", true);
    sort(expected);
//    logger.info(expected);
    Schema actual = Decompiler.createDDL(connection);
    // FIXME: Need to restrict which database/schema/tablespace we're looking at.
    final Iterator<$Table> iterator = actual.getTable().iterator();
    while (iterator.hasNext())
      if (!iterator.next().getName$().text().startsWith("t_"))
        iterator.remove();

    sort(actual);
//    logger.info(actual);

    assertEqual(DBVendor.valueOf(connection.getMetaData()), expected, actual);
//    logger.info(DOMs.domToString(actual.marshal(), Collections.singletonMap("http://www.jaxdb.org/ddlx.xsd", "http://www.jaxdb.org/ddlx.xsd"), DOMStyle.INDENT));
  }
}