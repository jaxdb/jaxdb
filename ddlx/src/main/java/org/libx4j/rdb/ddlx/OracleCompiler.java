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

import org.libx4j.rdb.ddlx.HHuJd6JcA.$Column;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$ForeignKey;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$ForeignKey.OnDelete$;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$Index;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$Integer;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$Named;
import org.libx4j.rdb.ddlx.HHuJd6JcA.$Table;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OracleCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(OracleCompiler.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<DropStatement> drops(final $Table table) {
    final List<DropStatement> statements = super.drops(table);
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type)) {
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE " + SQLDataTypes.getSequenceName(table, type) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;"));
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER " + SQLDataTypes.getTriggerName(table, type) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;"));
          }
        }
      }
    }

    return statements;
  }

  @Override
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    // NOTE: Oracle's AUTO INCREMENT semantics are expressed via the CREATE SEQUENCE and CREATE TRIGGER statements, and nothing is needed in the CREATE TABLE statement
    return null;
  }

  @Override
  protected List<CreateStatement> types(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type)) {
            final String sequenceName = SQLDataTypes.getSequenceName(table, type);
            statements.add(0, new CreateStatement("CREATE SEQUENCE " + sequenceName + " START WITH 1"));
          }
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  protected List<CreateStatement> triggers(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type)) {
            final String sequenceName = SQLDataTypes.getSequenceName(table, type);
            statements.add(0, new CreateStatement("CREATE TRIGGER " + SQLDataTypes.getTriggerName(table, type) + " BEFORE INSERT ON " + table.getName$().text() + " FOR EACH ROW when (new." + column.getName$().text() + " IS NULL) BEGIN SELECT " + sequenceName + ".NEXTVAL INTO :new." + column.getName$().text() + " FROM dual; END;"));
          }
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  protected DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + table.getName$().text() + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return "";
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    if ($Index.Type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by Oracle's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }

  private int foreignKeys = 0;
  private int primaryKeys = 0;
  private int checkConstraints = 0;

  @Override
  protected String check(final $Table table) {
    return "CONSTRAINT " + table.getName$().text() + "_ck" + ++checkConstraints + " CHECK";
  }

  @Override
  protected String primaryKey(final $Table table) {
    return "CONSTRAINT " + table.getName$().text() + "_pk" + ++primaryKeys + " PRIMARY KEY";
  }

  @Override
  protected String foreignKey(final $Table table) {
    return "CONSTRAINT " + table.getName$().text() + "_fk" + ++foreignKeys + " FOREIGN KEY";
  }

  @Override
  protected String onDelete(final OnDelete$ onDelete) {
    return "RESTRICT".equals(onDelete.text()) ? null : super.onDelete(onDelete);
  }

  @Override
  protected String onUpdate(final $ForeignKey.OnUpdate$ onUpdate) {
    logger.warn("ON UPDATE is not supported");
    return null;
  }

  @Override
  protected String compileDate(final String value) {
    return "(date'" + value + "')";
  }

  @Override
  protected String compileDateTime(final String value) {
    return "(timestamp'" + value + "')";
  }

  @Override
  protected String compileTime(final String value) {
    return "INTERVAL '" + value + "' HOUR TO SECOND";
  }
}