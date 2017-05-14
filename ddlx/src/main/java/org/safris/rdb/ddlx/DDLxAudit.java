package org.safris.rdb.ddlx;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.xml.validate.ValidationException;
import org.safris.rdb.ddlx.xe.$ddlx_columns;
import org.safris.rdb.ddlx.xe.$ddlx_named;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.ddlx.xe.ddlx_schema;
import org.safris.xsb.runtime.Bindings;
import org.safris.xsb.runtime.ParseException;
import org.xml.sax.InputSource;

public class DDLxAudit {
  public final Map<String,$ddlx_table> tableNameToTable = new HashMap<String,$ddlx_table>();

  private final ddlx_schema schema;

  public DDLxAudit(final ddlx_schema schema) {
    this.schema = schema;
    for (final $ddlx_table table : schema._table())
      tableNameToTable.put(table._name$().text(), table);
  }

  public DDLxAudit(final URL url) throws IOException, ParseException, ValidationException {
    this((ddlx_schema)Bindings.parse(new InputSource(url.openStream())));
  }

  public boolean isPrimary($ddlx_table table, final $ddlx_named column) {
    do {
      final $ddlx_columns constraint = table._constraints(0)._primaryKey(0);
      if (!constraint.isNull())
        for (final $ddlx_named col : constraint._column())
          if (column._name$().text().equals(col._name$().text()))
            return true;
    }
    while (!table._extends$().isNull() && (table = tableNameToTable.get(table._extends$().text())) != null);

    return false;
  }

  public boolean isUnique(final $ddlx_table table, final $ddlx_named column) {
    final $ddlx_columns constraint = table._constraints(0)._unique(0);
    if (constraint.isNull())
      return false;

    for (final $ddlx_named col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }

  public ddlx_schema schema() {
    return this.schema;
  }
}