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

import javax.xml.transform.Source;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.lang.Strings;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.SimpleNamespaceContext;
import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.ddlx.runner.Derby;
import org.libx4j.rdb.ddlx.runner.MySQL;
import org.libx4j.rdb.ddlx.runner.Oracle;
import org.libx4j.rdb.ddlx.runner.PostgreSQL;
import org.libx4j.rdb.ddlx.runner.VendorRunner;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonListener;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class/*, SQLite.class*/})
//@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class ReverseTest extends DDLxTest {
  private static final Logger logger = LoggerFactory.getLogger(ReverseTest.class);
  private static final Map<String,String> prefixToNamespaceURI = new HashMap<String,String>();

  static {
    prefixToNamespaceURI.put("ddlx", "http://rdb.libx4j.org/ddlx.xsd");
    prefixToNamespaceURI.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
  }

  private static XPath newXPath() {
    final XPath xPath = XPathFactory.newInstance().newXPath();
    xPath.setNamespaceContext(new SimpleNamespaceContext(prefixToNamespaceURI));
    return xPath;
  }

  private static String evalXPath(final Element element, final String xpath) {
    try {
      final NodeList nodes = (NodeList)newXPath().evaluate(xpath, element, XPathConstants.NODESET);
      final StringBuilder builder = new StringBuilder();
      for (int i = 0; i < nodes.getLength(); ++i) {
        final Node node = nodes.item(i);
        builder.append('\n').append(DOMs.domToString(node, DOMStyle.INDENT));
      }

      return builder.length() == 0 ? null : builder.substring(1);
    }
    catch (final XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  public static void remove(final Element element, final String ... xpaths) throws XPathExpressionException {
    for (final String xpath : xpaths) {
      final XPathExpression expr = newXPath().compile(xpath);
      final NodeList nodes2 = (NodeList)expr.evaluate(element, XPathConstants.NODESET);
      for (int i = 0; i < nodes2.getLength(); ++i) {
        final Node node = nodes2.item(i);
        if (node instanceof Attr) {
          final Attr attr = (Attr)node;
          attr.getOwnerElement().removeAttributeNode(attr);
        }
        else {
          node.getParentNode().removeChild(node);
        }
      }
    }
  }

  private static void assertEqual(final DBVendor vendor, final Binding expected, final Binding actual) throws XPathExpressionException {
    final Element controlElement = expected.toDOM();
    if (vendor == DBVendor.DERBY) {
      final String[] remove = {
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:bigint']/@precision",
        "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:bigint']/@unsigned"
      };

      remove(controlElement, remove);
    }

    final String controlXML = DOMs.domToString(controlElement, DOMStyle.INDENT);

    final Element testElement = actual.toDOM();
    final String testXML = DOMs.domToString(testElement, DOMStyle.INDENT);

    final Source controlSource = Input.fromString(controlXML).build();
    final Source testSource = Input.fromString(testXML).build();

    final DifferenceEngine diffEngine = new DOMDifferenceEngine();
    diffEngine.addDifferenceListener(new ComparisonListener() {
      @Override
      public void comparisonPerformed(final Comparison comparison, final ComparisonResult outcome) {
        final String controlXPath = comparison.getControlDetails().getXPath() == null ? null : comparison.getControlDetails().getXPath().replaceAll("/([^@])", "/ddlx:$1");
        if (controlXPath == null || controlXPath.matches("^.*\\/@[:a-z]+$") || controlXPath.contains("text()"))
          return;

        final String controlEval = evalXPath(controlElement, controlXPath);

        final String testXPath = comparison.getTestDetails().getXPath() == null ? null : comparison.getTestDetails().getXPath().replaceAll("/([^@])", "/ddlx:$1");
        final String testEval = testXPath == null ? null : evalXPath(testElement, testXPath);
        System.err.println(Strings.printColumns("Expected: " + controlXPath + "\n" + controlEval, "Actual: " + testXPath + "\n" + testEval));
//        Assert.fail("found a difference: " + comparison);
      }
    });
    diffEngine.compare(controlSource, testSource);
  }

  public static boolean go = false;
  private static final Comparator<Binding> hashCodeComparator = new Comparator<Binding>() {
    @Override
    public int compare(final Binding o1, final Binding o2) {
      if (go)
        System.err.println(o1.hashCode() + " " + o2.hashCode());
      return Long.compare(o1.hashCode(), o2.hashCode());
    }
  };

  private static void sort(final ddlx_schema schema) {
    for (final $ddlx_table table : schema._table()) {
      if (table._name$().text().equals("t_bigint")) {
        go = true;
      }

      if (table._indexes() != null && table._indexes().size() > 0 && table._indexes(0)._index() != null && table._indexes(0)._index().size() > 0)
        table._indexes(0)._index().sort(hashCodeComparator);

      go = false;
    }
  }

  @Test
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, ParseException, SQLException, ValidationException, XPathExpressionException {
    final ddlx_schema expected = Schemas.flatten(recreateSchema(connection, "reverse"));
    sort(expected);
//    System.out.println(expected);
    ddlx_schema actual = Generator.createDDL(connection);
    // FIXME: Need to restrict which database/schema/tablespace we're looking at.
    final Iterator<$ddlx_table> iterator = actual._table().iterator();
    while (iterator.hasNext())
      if (!iterator.next()._name$().text().startsWith("t_"))
        iterator.remove();

    actual = Schemas.flatten(actual);

    sort(actual);
//    System.out.println(actual);

    assertEqual(DBVendor.valueOf(connection.getMetaData()), expected, actual);
    final Map<String,String> schemaLocations = new HashMap<String,String>();
    schemaLocations.put("http://rdb.libx4j.org/ddlx.xsd", "http://rdb.libx4j.org/ddlx.xsd");
//    logger.info(DOMs.domToString(actual.marshal(), schemaLocations, DOMStyle.INDENT));
  }
}