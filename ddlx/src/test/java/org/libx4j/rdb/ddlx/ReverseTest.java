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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_columns;
import org.libx4j.rdb.ddlx.xe.$ddlx_constraints;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.ddlx.runner.Derby;
import org.libx4j.rdb.ddlx.runner.MySQL;
import org.libx4j.rdb.ddlx.runner.Oracle;
import org.libx4j.rdb.ddlx.runner.PostgreSQL;
import org.libx4j.rdb.ddlx.runner.VendorRunner;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class/*, SQLite.class*/})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class ReverseTest extends DDLxTest {
  private static final Logger logger = LoggerFactory.getLogger(ReverseTest.class);

  private static String getNames(final List<? extends $ddlx_named> nameds) {
    final StringBuilder builder = new StringBuilder();
    for (final $ddlx_named named : nameds)
      builder.append(", ").append(named._name$().text());

    return builder.substring(1);
  }

  private static void checkNamesEqual(final String label, final $ddlx_named named, final List<? extends $ddlx_named> expected, final List<? extends $ddlx_named> actual) {
    if (expected.size() != expected.size())
      Assert.fail("Expected number of " + label + (named == null ? " " : " in " + named._name$().text()) + " [" + expected.size() + "] does not equal actual [" + actual.size() + "]\n" + getNames(expected) + "\n" + getNames(actual));

    for (int j = 0; j < expected.size(); j++) {
      final $ddlx_named name1 = expected.get(j);
      final $ddlx_named name2 = actual.get(j);
      if (!name1._name$().text().equals(name2._name$().text()))
        Assert.fail("Expected " + label + (named == null ? " " : " in " + named._name$().text()) + " name [" + name1._name$().text() + "] does not equal actual [" + name2._name$().text() + "]\n" + getNames(expected) + "\n" + getNames(actual));
    }
  }

  private static void assertSize(final String label, final $ddlx_named named, final List<? extends Binding> expected, final List<? extends Binding> actual) {
    if (expected == null ? actual != null : actual == null || actual.size() != expected.size())
      Assert.fail("Expected " + (expected == null ? "null" : expected.size()) + " " + label + " in " + named._name$().text() + ", but got " + (actual == null ? "null" : actual.size()) + " in actual");
  }

  private static void assertEqual(final String label, final $ddlx_named named, final Binding expected, final Binding actual) {
    if (expected == null ? actual != null : actual == null || !actual.equals(expected))
      Assert.fail("Expected " + label + " in " + named._name$().text() + " not equal to actual " + (actual == null ? "null" : actual));
  }

  private static final Comparator<Binding> columnsComparator = new Comparator<Binding>() {
    @Override
    public int compare(final Binding o1, final Binding o2) {
      return Long.compare(o1.hashCode(), o2.hashCode());
    }
  };

  private static void assertEqual(final ddlx_schema expected, final ddlx_schema actual) {
    checkNamesEqual("table", null, expected._table(), actual._table());
    for (int i = 0; i < expected._table().size(); i++) {
      final $ddlx_table expectedTable = expected._table().get(i);
      final $ddlx_table actualTable = actual._table().get(i);
      checkNamesEqual("column", expectedTable, expectedTable._column(), actualTable._column());

      if (expectedTable._constraints() != null && expectedTable._constraints().size() > 0) {
        assertSize("constraints", expectedTable, expectedTable._constraints(), actualTable._constraints());
        final $ddlx_constraints expectedConstraints = expectedTable._constraints(0);
        final $ddlx_constraints actualConstraints = actualTable._constraints(0);
        if (expectedConstraints._primaryKey() != null && expectedConstraints._primaryKey().size() > 0) {
          assertSize("primaryKeys", expectedTable, expectedConstraints._primaryKey(), actualConstraints._primaryKey());
          final $ddlx_columns expectedPrimaryKeys = expectedConstraints._primaryKey(0);
          final $ddlx_columns actualPrimaryKeys = actualConstraints._primaryKey(0);
          if (expectedPrimaryKeys._column() != null && expectedPrimaryKeys._column().size() > 0) {
            assertSize("primaryKey columns", expectedTable, expectedPrimaryKeys._column(), actualPrimaryKeys._column());
            checkNamesEqual("primaryKey", expectedTable, expectedPrimaryKeys._column(), actualPrimaryKeys._column());
          }
        }

        if (expectedConstraints._unique() != null && expectedConstraints._unique().size() > 0) {
          assertSize("unique constraints", expectedTable, expectedConstraints._unique(), actualConstraints._unique());
          final List<$ddlx_columns> expectedUniques = new ArrayList<$ddlx_columns>(expectedConstraints._unique());
          expectedUniques.sort(columnsComparator);
          final List<$ddlx_columns> actualUniques = new ArrayList<$ddlx_columns>(actualConstraints._unique());
          actualUniques.sort(columnsComparator);
          for (int j = 0; j < expectedUniques.size(); j++) {
            final $ddlx_columns expectedUnique = expectedUniques.get(j);
            final $ddlx_columns actualUnique = actualUniques.get(j);
            if (expectedUnique._column() != null && expectedUnique._column().size() > 0) {
              assertSize("unique constraint columns", expectedTable, expectedUnique._column(), actualUnique._column());
              checkNamesEqual("unique constraint column", expectedTable, expectedUnique._column(), actualUnique._column());
            }
          }
        }

        if (expectedConstraints._check() != null && expectedConstraints._check().size() > 0) {
          assertSize("check constraints", expectedTable, expectedConstraints._check(), actualConstraints._check());
          final List<$ddlx_check> expectedChecks = new ArrayList<$ddlx_check>(expectedConstraints._check());
          expectedChecks.sort(columnsComparator);
          final List<$ddlx_check> actualChecks = new ArrayList<$ddlx_check>(actualConstraints._check());
          actualChecks.sort(columnsComparator);
          for (int j = 0; j < expectedConstraints._check().size(); j++) {
            final $ddlx_check expectedCheck = expectedChecks.get(j);
            final $ddlx_check actualCheck = actualChecks.get(j);
            assertEqual("check", expectedTable, expectedCheck, actualCheck);
          }
        }
      }
    }
  }

  @Test
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, ParseException, SQLException, ValidationException {
    final ddlx_schema expected = Schemas.flatten(recreateSchema(connection, "reverse"));
    ddlx_schema actual = Generator.createDDL(connection);
    // FIXME: Need to restrict which database/schema/tablespace we're looking at.
    final Iterator<$ddlx_table> iterator = actual._table().iterator();
    while (iterator.hasNext())
      if (!iterator.next()._name$().text().startsWith("t_"))
        iterator.remove();

    actual = Schemas.flatten(actual);

    assertEqual(expected, actual);
    final Map<String,String> schemaLocations = new HashMap<String,String>();
    schemaLocations.put("http://rdb.libx4j.org/ddlx.xsd", "http://rdb.libx4j.org/ddlx.xsd");
    logger.info(DOMs.domToString(actual.marshal(), schemaLocations, DOMStyle.INDENT));
  }
}