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

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.lang.Strings;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.SimpleNamespaceContext;
import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.xAA.$Table;
import org.libx4j.rdb.ddlx.xAA.Schema;
import org.libx4j.rdb.ddlx.runner.Derby;
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

  public static void addAttribute(final Element element, final String xpath, final String name, final String value) throws XPathExpressionException {
    final XPathExpression expr = newXPath().compile(xpath);
    final NodeList nodes2 = (NodeList)expr.evaluate(element, XPathConstants.NODESET);
    for (int i = 0; i < nodes2.getLength(); ++i) {
      final Node node = nodes2.item(i);
      if (!(node instanceof Element))
        throw new UnsupportedOperationException("Only support addition of attributes to elements");

      final Element target = (Element)node;
      final int colon = name.indexOf(':');
      final String namespaceURI = colon == -1 ? node.getNamespaceURI() : node.getOwnerDocument().lookupNamespaceURI(name.substring(0, colon));
      target.setAttributeNS(namespaceURI, name, value);
    }
  }

  public static void replace(final Element element, final String xpath, final String name, final String value) throws XPathExpressionException {
    final XPathExpression expr = newXPath().compile(xpath);
    final NodeList nodes2 = (NodeList)expr.evaluate(element, XPathConstants.NODESET);
    for (int i = 0; i < nodes2.getLength(); ++i) {
      final Node node = nodes2.item(i);
      if (node instanceof Attr) {
        final Attr attr = (Attr)node;
        if (name == null) {
          attr.setValue(value);
        }
        else {
          final int colon = name.indexOf(':');
          final String namespaceURI = colon == -1 ? attr.getNamespaceURI() : attr.getOwnerDocument().lookupNamespaceURI(name.substring(0, colon));
          final Element owner = attr.getOwnerElement();
          owner.removeAttributeNode(attr);
          owner.setAttributeNS(namespaceURI, name, value);
        }
      }
      else {
        throw new UnsupportedOperationException("Only support replacement of attribute values");
      }
    }
  }

  public static void replace(final Element element, final String xpath, final String value) throws XPathExpressionException {
    replace(element, xpath, null, value);
  }

  private static void assertEqual(final DBVendor vendor, final Binding expected, final Binding actual) throws XPathExpressionException {
    final Element controlElement = expected.toDOM();
    final Element testElement = actual.toDOM();
    if (vendor == DBVendor.DERBY) {
      remove(controlElement,
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

      replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:float']/@xsi:type", "ddlx:double");
      replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:tinyint']/@xsi:type", "ddlx:smallint");
      replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum' and @values='SEVEN EIGHT NINE']/@values", "length", "5");
      addAttribute(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']", "varying", "true");
      replace(controlElement, "//ddlx:schema/ddlx:table/ddlx:column[@xsi:type='ddlx:enum']/@xsi:type", "ddlx:char");

      remove(testElement,
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

    final String controlXML = DOMs.domToString(controlElement, DOMStyle.INDENT);
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
        logger.info(Strings.printColumns("Expected: " + controlXPath + "\n" + controlEval, "Actual: " + testXPath + "\n" + testEval));
        Assert.fail("Found a difference: " + comparison);
      }
    });
    diffEngine.compare(controlSource, testSource);
  }

  private static final Comparator<Binding> hashCodeComparator = new Comparator<Binding>() {
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
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, ParseException, SQLException, ValidationException, XPathExpressionException {
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
    final Map<String,String> schemaLocations = new HashMap<String,String>();
    schemaLocations.put("http://rdb.libx4j.org/ddlx.xsd", "http://rdb.libx4j.org/ddlx.xsd");
//    logger.info(DOMs.domToString(actual.marshal(), schemaLocations, DOMStyle.INDENT));
  }
}