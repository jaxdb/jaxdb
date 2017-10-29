/* Copyright (c) 2015 lib4j
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.vendor.DBVendor;

public final class MySQLCompiler extends Compiler {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<CreateStatement> triggers(final $ddlx_table table) {
    if (table._triggers() == null)
      return super.triggers(table);

    final String tableName = table._name$().text();
    final List<$ddlx_table._triggers._trigger> triggers = table._triggers().get(0)._trigger();
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    for (final $ddlx_table._triggers._trigger trigger : triggers) {
      String buffer = "";
      for (final String action : trigger._actions$().text()) {
        buffer += "DELIMITER |\n";
        buffer += "CREATE TRIGGER " + SQLDataTypes.getTriggerName(tableName, trigger, action) + " " + trigger._time$().text() + " " + action + " ON " + tableName + "\n";
        buffer += "  FOR EACH ROW\n";
        buffer += "  BEGIN\n";

        final String text = trigger.text().toString();
        // FIXME: This does not work because the whitespace is trimmed before we can check it
        int i = 0, j = -1;
        while (i < text.length()) {
          char c = text.charAt(i++);
          if (c == '\n' || c == '\r')
            continue;

          j++;
          if (c != ' ' && c != '\t')
            break;
        }

        buffer += "    " + text.trim().replace("\n" + text.substring(0, j), "\n    ") + "\n";
        buffer += "  END;\n";
        buffer += "|\n";
        buffer += "DELIMITER";
      }

      statements.add(new CreateStatement(buffer.toString()));
    }

    statements.addAll(super.triggers(table));
    return statements;
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return isAutoIncrement(column) ? $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text() : "";
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $ddlx_index._type$ type, final String tableName, final $ddlx_named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type.text() + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }
}