/* Copyright (c) 2012 Seva Safris
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

package org.safris.xdb.xdl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.namespace.QName;

import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.w3.x2001.xmlschema.$xs_complexType;
import org.w3.x2001.xmlschema.$xs_simpleType;
import org.w3.x2001.xmlschema.xs_schema;

public final class XSDTransform extends XDLTransformer {
  public static void main(final String[] args) throws Exception {
    createXSD(new File(args[0]), null);
  }

  public static void createXSD(final File xdlFile, final File outDir) {
    final xdl_database database;
    try {
      database = parseArguments(xdlFile.toURI().toURL(), outDir);
    }
    catch (final MalformedURLException e) {
      throw new Error(e);
    }

    try {
      final XSDTransform creator = new XSDTransform(database);
      final xs_schema schema = creator.parse();

      writeOutput(DOMs.domToString(schema.marshal(), DOMStyle.INDENT), outDir != null ? new File(outDir, creator.unmerged._name$().text() + ".xsd") : null);
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private XSDTransform(final xdl_database database) throws Exception {
    super(database);
  }

  private xs_schema parse() {
    final xs_schema schema = new xs_schema();
    schema._targetNamespace$(new xs_schema._targetNamespace$(unmerged._targetNamespace$().text()));

    for (final $xdl_tableType table : unmerged._table()) {
      if (table._abstract$().text()) {
        final $xs_complexType complexType = parseTable(table, new xs_schema._complexType());
        complexType._name$(new $xs_complexType._name$(table._name$().text()));
        schema._complexType(complexType);
      }
      else {
        final xs_schema._element element = new xs_schema._element();
        element._type$(new xs_schema._element._type$(new QName(unmerged._targetNamespace$().text(), table._name$().text(), unmerged._name$().text())));
        element._name$(new xs_schema._element._name$(table._name$().text()));
        schema._element(element);

        final $xs_complexType complexType = parseTable(table, new xs_schema._element._complexType());
        complexType._name$(new $xs_complexType._name$(table._name$().text()));
        schema._complexType(complexType);
      }
    }

    return schema;
  }

  private $xs_complexType parseTable(final $xdl_tableType table, $xs_complexType complexType) {
    final $xs_complexType retType = complexType;
    if (!table._extends$().isNull()) {
      final $xs_complexType._complexContent complexContent = new $xs_complexType._complexContent();
      complexType._complexContent(complexContent);

      final $xs_complexType._complexContent._extension extension = new $xs_complexType._complexContent._extension();
      complexContent._extension(extension);
      extension._base$(new $xs_complexType._complexContent._extension._base$(new QName(unmerged._targetNamespace$().text(), table._extends$().text(), unmerged._name$().text())));

      complexType = extension;
    }

    if (table._column() == null)
      return retType;

    for (final $xdl_columnType column : table._column()) {
      final $xs_complexType._attribute attribute = new $xs_complexType._attribute();
      attribute._name$(new $xs_complexType._attribute._name$(column._name$().text()));

      if (column instanceof $xdl_enum) {
        final $xs_simpleType._restriction restriction = new $xs_simpleType._restriction();
        restriction._base$(new $xs_simpleType._restriction._base$(new QName(NamespaceURI.XS.getNamespaceURI(), "string")));
        final List<String> values = (($xdl_enum)column)._values$().text();
        for (final String value : values) {
          final $xs_simpleType._restriction._enumeration enumeration = new $xs_simpleType._restriction._enumeration();
          enumeration._value$(new $xs_simpleType._restriction._enumeration._value$(value));
          restriction._enumeration(enumeration);
        }

        final $xs_simpleType simpleType = new $xs_complexType._attribute._simpleType();
        simpleType._restriction(restriction);

        attribute._simpleType(simpleType);
      }
      else {
        final QName type;
        if (column instanceof $xdl_boolean)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "boolean");
        else if (column instanceof $xdl_varchar)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "string");
        else if (column instanceof $xdl_smallint || column instanceof $xdl_tinyint)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "short");
        else if (column instanceof $xdl_decimal)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "double");
        else if (column instanceof $xdl_int || column instanceof $xdl_mediumint)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "integer");
        else if (column instanceof $xdl_bigint)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "long");
        else if (column instanceof $xdl_date)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "date");
        else if (column instanceof $xdl_dateTime)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "time");
        else if (column instanceof $xdl_time)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "dateTime");
        else if (column instanceof $xdl_blob)
          type = new QName(NamespaceURI.XS.getNamespaceURI(), "base64Binary");
        else
          type = null;

        attribute._type$(new $xs_complexType._attribute._type$(type));
      }

      if (!column._null$().text())
        attribute._use$(new $xs_complexType._attribute._use$($xs_complexType._attribute._use$.REQUIRED));

      complexType._attribute(attribute);
    }

    return retType;
  }
}