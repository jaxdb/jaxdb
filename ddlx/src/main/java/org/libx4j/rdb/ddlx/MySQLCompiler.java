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

import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Column;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Index;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Integer;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Named;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.vendor.DBVendor;

class MySQLCompiler extends Compiler {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<CreateStatement> triggers(final $Table table) {
    if (table.getTriggers() == null)
      return super.triggers(table);

    final String tableName = table.getName$().text();
    final List<$Table.Triggers.Trigger> triggers = table.getTriggers().getTrigger();
    final List<CreateStatement> statements = new ArrayList<>();
    for (final $Table.Triggers.Trigger trigger : triggers) {
      String buffer = "";
      for (final String action : trigger.getActions$().text()) {
        buffer += "DELIMITER |\n";
        buffer += "CREATE TRIGGER " + q(SQLDataTypes.getTriggerName(tableName, trigger, action)) + " " + trigger.getTime$().text() + " " + action + " ON " + q(tableName) + "\n";
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
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    return isAutoIncrement(column) ? $Integer.GenerateOnInsert$.AUTO_5FINCREMENT.text() : "";
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return " ON " + q(table.getName$().text());
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " USING " + type.text() + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getVendor().getDialect(), columns) + ")");
  }
}