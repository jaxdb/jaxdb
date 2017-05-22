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

package org.safris.rdb.ddlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.safris.rdb.ddlx.xe.$ddlx_column;
import org.safris.rdb.ddlx.xe.$ddlx_foreignKey;
import org.safris.rdb.ddlx.xe.$ddlx_foreignKey._onDelete$;
import org.safris.rdb.ddlx.xe.$ddlx_integer;
import org.safris.rdb.ddlx.xe.$ddlx_named;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OracleSerializer extends Serializer {
  private static final Logger logger = LoggerFactory.getLogger(OracleSerializer.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<DropStatement> drops(final $ddlx_table table) {
    final List<DropStatement> statements = super.drops(table);
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text())) {
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE " + SQLDataTypes.getSequenceName(table, type) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;"));
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER " + SQLDataTypes.getTriggerName(table, type) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;"));
          }
        }
      }
    }

    return statements;
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    // NOTE: Oracle's AUTO INCREMENT semantics are expressed via the CREATE SEQUENCE and CREATE TRIGGER statements, and nothing is needed in the CREATE TABLE statement
    return null;
  }

  @Override
  protected List<CreateStatement> types(final $ddlx_table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text())) {
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
  protected List<CreateStatement> triggers(final $ddlx_table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text())) {
            final String sequenceName = SQLDataTypes.getSequenceName(table, type);
            statements.add(0, new CreateStatement("CREATE TRIGGER " + SQLDataTypes.getTriggerName(table, type) + " BEFORE INSERT ON " + table._name$().text() + " FOR EACH ROW when (new." + column._name$().text() + " IS NULL) BEGIN SELECT " + sequenceName + ".NEXTVAL INTO :new." + column._name$().text() + " FROM dual; END;"));
          }
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  protected DropStatement dropTableIfExists(final $ddlx_table table) {
    return new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + table._name$().text() + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return "";
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $ddlx_named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }

  private int foreignKeys = 0;
  private int primaryKeys = 0;
  private int checkConstraints = 0;

  @Override
  protected String check(final $ddlx_table table) {
    return "CONSTRAINT " + table._name$().text() + "_ck" + ++checkConstraints + " CHECK";
  }

  @Override
  protected String primaryKey(final $ddlx_table table) {
    return "CONSTRAINT " + table._name$().text() + "_pk" + ++primaryKeys + " PRIMARY KEY";
  }

  @Override
  protected String foreignKey(final $ddlx_table table) {
    return "CONSTRAINT " + table._name$().text() + "_fk" + ++foreignKeys + " FOREIGN KEY";
  }

  @Override
  protected String onDelete(final _onDelete$ onDelete) {
    return "RESTRICT".equals(onDelete.text()) ? null : super.onDelete(onDelete);
  }

  @Override
  protected String onUpdate(final $ddlx_foreignKey._onUpdate$ onUpdate) {
    logger.warn("ON UPDATE is not supported");
    return null;
  }
}