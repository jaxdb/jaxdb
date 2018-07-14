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

package org.libx4j.rdb.ddlx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.AssertXml;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.ValidationException;
import org.libx4j.rdb.ddlx.runner.Derby;
import org.libx4j.rdb.ddlx.runner.VendorRunner;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.Schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.MarshalException;
import org.w3c.dom.Element;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class/*, SQLite.class*/})
//@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class ReverseTest extends DDLxTest {
  private static void assertEqual(final DBVendor vendor, final Binding expected, final Binding actual) throws XPathExpressionException {
    final Element controlElement = expected.toDOM();
    final Element testElement = actual.toDOM();
    final AssertXml builder = AssertXml.compare(controlElement, testElement);
    if (vendor == DBVendor.DERBY) {
      builder.remove(controlElement,
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
      );

      builder.replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:float']/@xsi:type", "ddlx:double");
      builder.replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:tinyint']/@xsi:type", "ddlx:smallint");
      builder.replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum' and @values='SEVEN EIGHT NINE']/@values", "length", "5");
      builder.addAttribute(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']", "varying", "true");
      builder.replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']/@xsi:type", "ddlx:char");

      builder.remove(testElement,
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

    builder.assertEqual();
  }

  private static final Comparator<Binding> hashCodeComparator = new Comparator<>() {
    @Override
    public int compare(final Binding o1, final Binding o2) {
      return Long.compare(o1.hashCode(), o2.hashCode());
    }
  };

  private static void sort(final Schema schema) {
    for (final $Table table : schema.getTable()) {
      if (table.getIndexes() != null && table.getIndexes().getIndex() != null && table.getIndexes().getIndex().size() > 0)
        table.getIndexes().getIndex().sort(hashCodeComparator);

      if (table.getConstraints() != null && table.getConstraints().getUnique() != null && table.getConstraints().getUnique().size() > 0)
        table.getConstraints().getUnique().sort(hashCodeComparator);
    }
  }

  @Test
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, SQLException, XPathExpressionException, ValidationException {
    final Schema expected = Schemas.flatten(recreateSchema(connection, "reverse", true));
    sort(expected);
//    logger.info(expected);
    Schema actual = Decompiler.createDDL(connection);
    // FIXME: Need to restrict which database/schema/tablespace we're looking at.
    final Iterator<$Table> iterator = actual.getTable().iterator();
    while (iterator.hasNext())
      if (!iterator.next().getName$().text().startsWith("t_"))
        iterator.remove();

    actual = Schemas.flatten(actual);

    sort(actual);
//    logger.info(actual);

    assertEqual(DBVendor.valueOf(connection.getMetaData()), expected, actual);
    final Map<String,String> schemaLocations = new HashMap<>();
    schemaLocations.put("http://rdb.libx4j.org/ddlx.xsd", "http://rdb.libx4j.org/ddlx.xsd");
//    logger.info(DOMs.domToString(actual.marshal(), schemaLocations, DOMStyle.INDENT));
  }
}