/* Copyright (c) 2015 JAX-DB
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

package org.jaxdb.ddlx;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MySQLCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(MySQLCompiler.class);

  MySQLCompiler() {
    super(DBVendor.MY_SQL);
  }

  MySQLCompiler(final DBVendor vendor) {
    super(vendor);
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  List<CreateStatement> triggers(final $Table table) {
    if (table.getTriggers() == null)
      return super.triggers(table);

    final String tableName = table.getName$().text();
    final List<$Table.Triggers.Trigger> triggers = table.getTriggers().getTrigger();
    final List<CreateStatement> statements = new ArrayList<>();
    final StringBuilder builder = new StringBuilder();
    for (final $Table.Triggers.Trigger trigger : triggers) { // [L]
      for (final String action : trigger.getActions$().text()) {
        builder.append("DELIMITER |\n");
        builder.append("CREATE TRIGGER ").append(q(getTriggerName(tableName, trigger, action))).append(" ").append(trigger.getTime$().text()).append(" ").append(action).append(" ON ").append(q(tableName)).append('\n');
        builder.append("  FOR EACH ROW\n");
        builder.append("  BEGIN\n");

        final String text = trigger.text().toString();
        // FIXME: This does not work because the whitespace is trimmed before we can check it
        int k = -1;
        for (int j = 0; j < text.length();) { // [N]
          final char ch = text.charAt(j++);
          if (ch == '\n' || ch == '\r')
            continue;

          ++k;
          if (ch != ' ' && ch != '\t')
            break;
        }

        builder.append("    ").append(text.trim().replace("\n" + text.substring(0, k), "\n    ")).append('\n');
        builder.append("  END;\n");
        builder.append("|\n");
        builder.append("DELIMITER");
      }

      statements.add(new CreateStatement(builder.toString()));
      builder.setLength(0);
    }

    statements.addAll(super.triggers(table));
    return statements;
  }

  @Override
  String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    if (!Generator.isAuto(column))
      return "";

    final String _default = getAttr("default", column);
    final String min = getAttr("min", column);
    if (min != null && _default != null && logger.isWarnEnabled())
      logger.warn("AUTO_INCREMENT does not consider min=\"" + min + "\" -- Ignoring min spec.");

    final String max = getAttr("max", column);
    if (max != null && logger.isWarnEnabled())
      logger.warn("AUTO_INCREMENT does not consider max=\"" + max + "\" -- Ignoring max spec.");

    final String start = _default != null ? _default : min != null ? min : "1";
    alterStatements.add(new CreateStatement("ALTER TABLE " + q(table.getName$().text()) + " AUTO_INCREMENT = " + start));
    return "AUTO_INCREMENT";
  }

  @Override
  String dropIndexOnClause(final $Table table) {
    return " ON " + q(table.getName$().text());
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " USING " + type.text() + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getDialect(), columns) + ")");
  }
}