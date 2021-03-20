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
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey.OnDelete$;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey.OnUpdate$;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class OracleCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(OracleCompiler.class);

  @Override
  public DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  LinkedHashSet<DropStatement> dropTypes(final $Table table) {
    final LinkedHashSet<DropStatement> statements = super.dropTypes(table);
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type)) {
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE " + q(SQLDataTypes.getSequenceName(table, type)) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;"));
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER " + q(SQLDataTypes.getTriggerName(table, type)) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;"));
          }
        }
      }
    }

    return statements;
  }

  @Override
  String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  String $autoIncrement(final $Table table, final $Integer column) {
    // NOTE: Oracle's AUTO INCREMENT semantics are expressed via the CREATE SEQUENCE and CREATE TRIGGER statements, and nothing is needed in the CREATE TABLE statement
    return null;
  }

  @Override
  List<CreateStatement> types(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<>();
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer integer = ($Integer)column;
          if (isAutoIncrement(integer)) {
            final String sequenceName = SQLDataTypes.getSequenceName(table, integer);
            final StringBuilder builder = new StringBuilder("CREATE SEQUENCE " + q(sequenceName));
            final String min = getAttr("min", integer);
            if (min != null)
              builder.append(" MINVALUE ").append(min);

            final String max = getAttr("max", integer);
            if (max != null)
              builder.append(" MAXVALUE ").append(max);

            final String _default = getAttr("default", integer);
            if (_default != null)
              builder.append(" START ").append(_default);

            builder.append(" CYCLE");
            statements.add(0, new CreateStatement(builder.toString()));
          }
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  List<CreateStatement> triggers(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<>();
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type)) {
            final String sequenceName = SQLDataTypes.getSequenceName(table, type);
            statements.add(0, new CreateStatement("CREATE TRIGGER " + q(SQLDataTypes.getTriggerName(table, type)) + " BEFORE INSERT ON " + q(table.getName$().text()) + " FOR EACH ROW when (" + "new." + q(column.getName$().text()) + " IS NULL) BEGIN SELECT " + q(sequenceName) + ".NEXTVAL INTO " + ":new." + q(column.getName$().text()) + " FROM dual; END;"));
          }
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + q(table.getName$().text()) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
  }

  @Override
  String dropIndexOnClause(final $Table table) {
    return "";
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    if ($Index.Type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by Oracle's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getVendor().getDialect(), columns) + ")");
  }

  @Override
  String onDelete(final OnDelete$ onDelete) {
    return "RESTRICT".equals(onDelete.text()) ? null : super.onDelete(onDelete);
  }

  @Override
  String onUpdate(final OnUpdate$ onUpdate) {
    logger.warn("ON UPDATE is not supported");
    return null;
  }

  @Override
  String compileDate(final String value) {
    return "(date'" + value + "')";
  }

  @Override
  String compileDateTime(final String value) {
    return "(timestamp'" + value + "')";
  }

  @Override
  String compileTime(final String value) {
    return "INTERVAL '" + value + "' HOUR TO SECOND";
  }
}