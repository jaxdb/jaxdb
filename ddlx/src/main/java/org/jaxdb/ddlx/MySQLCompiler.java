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

import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxsb.runtime.BindingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MySQLCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(MySQLCompiler.class);

  MySQLCompiler() {
    super(DbVendor.MY_SQL);
  }

  MySQLCompiler(final DbVendor vendor) {
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
    final BindingList<$Table.Triggers.Trigger> triggers = table.getTriggers().getTrigger();
    final List<CreateStatement> statements = new ArrayList<>();
    final StringBuilder b = new StringBuilder();
    for (int i = 0, i$ = triggers.size(); i < i$; ++i) { // [RA]
      final $Table.Triggers.Trigger trigger = triggers.get(i);
      final List<String> actions = trigger.getActions$().text();
      for (int j = 0, j$ = actions.size(); j < j$; ++j) { // [RA]
        final String action = actions.get(j);
        b.append("DELIMITER |\n");
        b.append("CREATE TRIGGER ");
        q(b, getTriggerName(tableName, trigger, action)).append(' ').append(trigger.getTime$().text()).append(' ').append(action).append(" ON ");
        q(b, tableName).append('\n');
        b.append("  FOR EACH ROW\n");
        b.append("  BEGIN\n");

        final String text = trigger.text().toString();
        // FIXME: This does not work because the whitespace is trimmed before we can check it
        int l = -1;
        for (int k = 0, k$ = text.length(); k < k$;) { // [N]
          final char ch = text.charAt(k++);
          if (ch == '\n' || ch == '\r')
            continue;

          ++l;
          if (ch != ' ' && ch != '\t')
            break;
        }

        b.append("    ").append(text.trim().replace("\n" + text.substring(0, l), "\n    ")).append('\n'); // FIXME: StringBuilder
        b.append("  END;\n");
        b.append("|\n");
        b.append("DELIMITER");
      }

      statements.add(new CreateStatement(b.toString()));
      b.setLength(0);
    }

    statements.addAll(super.triggers(table));
    return statements;
  }

  @Override
  StringBuilder $null(final StringBuilder b, final $Table table, final $Column column) {
    if (column.getNull$() != null)
      b.append(column.getNull$().text() ? " NULL" : " NOT NULL");

    return b;
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    if (!Generator.isAuto(column))
      return null;

    final String _default = getAttr("default", column);
    final String min = getAttr("min", column);
    if (min != null && _default != null && logger.isWarnEnabled()) logger.warn("AUTO_INCREMENT does not consider min=\"" + min + "\" -- Ignoring min spec.");

    final String max = getAttr("max", column);
    if (max != null && logger.isWarnEnabled()) logger.warn("AUTO_INCREMENT does not consider max=\"" + max + "\" -- Ignoring max spec.");

    final String start = _default != null ? _default : min != null ? min : "1";
    final StringBuilder b = new StringBuilder("ALTER TABLE ");
    q(b, table.getName$().text()).append(" AUTO_INCREMENT = ").append(start);
    alterStatements.add(new CreateStatement(b.toString()));
    return "AUTO_INCREMENT";
  }

  @Override
  StringBuilder dropIndexOnClause(final $Table table) {
    return q(new StringBuilder(" ON "), table.getName$().text());
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $IndexType type, final String tableName, final $Named ... columns) {
    final StringBuilder b = new StringBuilder("CREATE ");
    if (unique)
      b.append("UNIQUE ");

    b.append("INDEX ");
    q(b, indexName).append(" USING ").append(type.text()).append(" ON ");
    q(b, tableName).append(" (").append(SQLDataTypes.csvNames(getDialect(), columns)).append(')');
    return new CreateStatement(b.toString());
  }
}