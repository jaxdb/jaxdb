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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.xe.$ddlx_columns;
import org.libx4j.rdb.ddlx.xe.$ddlx_indexes;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.xsb.runtime.Bindings;
import org.libx4j.xsb.runtime.ParseException;

public class DDLxAudit {
  protected static DDLxAudit makeAudit(final URL url) throws IOException {
    if (url == null)
      throw new IllegalArgumentException("url == null");

    try (final InputStream in = url.openStream()) {
      return new DDLxAudit(url);
    }
  }

  public final Map<String,$ddlx_table> tableNameToTable;
  private final ddlx_schema schema;

  protected DDLxAudit(final ddlx_schema schema) {
    this.schema = schema;
    this.tableNameToTable = new HashMap<String,$ddlx_table>();
    for (final $ddlx_table table : schema._table())
      tableNameToTable.put(table._name$().text(), table);
  }

  public DDLxAudit(final URL url) throws IOException, ParseException, ValidationException {
    this((ddlx_schema)Bindings.parse(url));
  }

  protected DDLxAudit(final DDLxAudit copy) {
    this.schema = copy.schema;
    this.tableNameToTable = copy.tableNameToTable;
  }

  public boolean isPrimary($ddlx_table table, final $ddlx_named column) {
    do {
      if (table._constraints() != null && table._constraints()._primaryKey() != null)
        for (final $ddlx_named col : table._constraints()._primaryKey()._column())
          if (column._name$().text().equals(col._name$().text()))
            return true;
    }
    while (table._extends$() != null && (table = tableNameToTable.get(table._extends$().text())) != null);

    return false;
  }

  public boolean isUnique(final $ddlx_table table, final $ddlx_named column) {
    if (table._constraints() != null && table._constraints()._unique() != null)
      for (final $ddlx_columns unique : table._constraints()._unique())
        if (unique._column().size() == 1 && column._name$().text().equals(unique._column(0)._name$().text()))
          return true;

    if (table._indexes() != null && table._indexes()._index() != null)
      for (final $ddlx_indexes._index index : table._indexes()._index())
        if (index._unique$() != null && index._unique$().text() && index._column().size() == 1 && column._name$().text().equals(index._column(0)._name$().text()))
          return true;

    return false;
  }

  public ddlx_schema schema() {
    return this.schema;
  }
}