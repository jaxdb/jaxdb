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

import org.lib4j.xml.ValidationException;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Columns;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Indexes;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Named;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.Schema;
import org.libx4j.xsb.runtime.Bindings;
import org.libx4j.xsb.runtime.ParseException;

public class DDLxAudit {
  protected static DDLxAudit makeAudit(final URL url) throws IOException, ValidationException {
    if (url == null)
      throw new IllegalArgumentException("url == null");

    try (final InputStream in = url.openStream()) {
      return new DDLxAudit(url);
    }
  }

  public final Map<String,$Table> tableNameToTable;
  private final Schema schema;

  protected DDLxAudit(final Schema schema) {
    this.schema = schema;
    this.tableNameToTable = new HashMap<String,$Table>();
    for (final $Table table : schema.getTable())
      tableNameToTable.put(table.getName$().text(), table);
  }

  public DDLxAudit(final URL url) throws IOException, ParseException, ValidationException {
    this((Schema)Bindings.parse(url));
  }

  protected DDLxAudit(final DDLxAudit copy) {
    this.schema = copy.schema;
    this.tableNameToTable = copy.tableNameToTable;
  }

  public boolean isPrimary($Table table, final $Named column) {
    do {
      if (table.getConstraints() != null && table.getConstraints().getPrimaryKey() != null)
        for (final $Named col : table.getConstraints().getPrimaryKey().getColumn())
          if (column.getName$().text().equals(col.getName$().text()))
            return true;
    }
    while (table.getExtends$() != null && (table = tableNameToTable.get(table.getExtends$().text())) != null);

    return false;
  }

  public boolean isUnique(final $Table table, final $Named column) {
    if (table.getConstraints() != null && table.getConstraints().getUnique() != null)
      for (final $Columns unique : table.getConstraints().getUnique())
        if (unique.getColumn().size() == 1 && column.getName$().text().equals(unique.getColumn(0).getName$().text()))
          return true;

    if (table.getIndexes() != null && table.getIndexes().getIndex() != null)
      for (final $Indexes.Index index : table.getIndexes().getIndex())
        if (index.getUnique$() != null && index.getUnique$().text() && index.getColumn().size() == 1 && column.getName$().text().equals(index.getColumn(0).getName$().text()))
          return true;

    return false;
  }

  public Schema schema() {
    return this.schema;
  }
}