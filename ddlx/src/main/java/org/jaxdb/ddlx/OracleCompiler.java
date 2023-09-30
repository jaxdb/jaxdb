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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Constraints.PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxsb.runtime.BindingList;
import org.libj.lang.Resources;
import org.libj.math.FastMath;
import org.libj.net.URLs;
import org.libj.util.function.Throwing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class OracleCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(OracleCompiler.class);

  OracleCompiler() {
    super(DbVendor.ORACLE);
  }

  @Override
  void init(final Connection connection) throws SQLException {
    try {
      final ClassLoader classLoader = OracleCompiler.class.getClassLoader();
      Resources.walk(classLoader, "org/jaxdb/oracle", (final URL root, final String entry, final boolean isDirectory) -> {
        if (!isDirectory) {
          try (final Statement statement = connection.createStatement()) {
            statement.execute(new String(URLs.readBytes(classLoader.getResource(entry))));
          }
          catch (final IOException | SQLException ie) {
            Throwing.rethrow(ie);
          }
        }

        return true;
      });
    }
    catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  LinkedHashSet<DropStatement> dropTypes(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final LinkedHashSet<DropStatement> statements = super.dropTypes(table, tableNameToEnumToOwner);
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (Generator.isAuto(type)) {
            statements.add(new DropStatement(q(new StringBuilder("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE "), getSequenceName(table, type)).append("'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;").toString()));
            statements.add(new DropStatement(q(new StringBuilder("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER "), getTriggerName(table, type)).append("'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;").toString()));
          }
        }
      }
    }

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
    // NOTE: Oracle's AUTO INCREMENT semantics are expressed via the CREATE SEQUENCE and CREATE TRIGGER statements,
    // and nothing is needed in the CREATE TABLE statement
    return null;
  }

  @Override
  StringBuilder primaryKey(final StringBuilder b, final $Table table, final int[] columns, final PrimaryKey.Using$ using) {
    return super.primaryKey(b, table, columns, null);
  }

  @Override
  ArrayList<CreateStatement> types(final $Table table, final HashMap<String,String> enumTemplateToValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final ArrayList<CreateStatement> statements = new ArrayList<>();
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Integer) {
          final $Integer integer = ($Integer)column;
          if (Generator.isAuto(integer)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("CREATE SEQUENCE ");
            q(builder, getSequenceName(table, integer));
            builder.append(" INCREMENT BY 1");

            final String startWith = getAttr("default", integer);
            if (startWith != null)
              builder.append(" START WITH ").append(startWith);

            final String max = getAttr("max", integer);
            final Byte precision;
            builder.append(" MAXVALUE ");
            builder.append(max != null ? max : (precision = getPrecision(integer)) != null ? FastMath.longE10[precision] : Long.MAX_VALUE);

            String min = getAttr("min", integer);
            if (min == null)
              min = startWith;

            if (min != null)
              builder.append(" MINVALUE ").append(min);
            else
              builder.append(" NOMINVALUE ");

            builder.append(" CYCLE NOCACHE");
            statements.add(0, new CreateStatement(builder.toString()));
          }
        }
      }
    }

    statements.addAll(super.types(table, enumTemplateToValues, tableNameToEnumToOwner));
    return statements;
  }

  @Override
  ArrayList<CreateStatement> triggers(final $Table table) {
    final ArrayList<CreateStatement> statements = new ArrayList<>();
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (Generator.isAuto(type)) {
            final String sequenceName = getSequenceName(table, type);
            final StringBuilder b = new StringBuilder("CREATE TRIGGER ");
            q(b, getTriggerName(table, type)).append(" BEFORE INSERT ON ");
            q(b, table.getName$().text()).append(" FOR EACH ROW WHEN (new.");
            q(b, column.getName$().text()).append(" IS NULL) BEGIN SELECT ");
            q(b, sequenceName).append(".NEXTVAL INTO :new.");
            q(b, column.getName$().text()).append(" FROM dual; END;");
            statements.add(0, new CreateStatement(b.toString()));
          }
        }
      }
    }

    statements.addAll(super.triggers(table));
    return statements;
  }

  @Override
  DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement(q(new StringBuilder("BEGIN EXECUTE IMMEDIATE 'DROP TABLE "), table.getName$().text()).append("'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;").toString());
  }

  @Override
  StringBuilder dropIndexOnClause(final $Table table) {
    return new StringBuilder(0);
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $IndexType type, final String tableName, final $Named ... columns) {
    if ($IndexType.HASH.text().equals(type.text()) && logger.isWarnEnabled())
      logger.warn("HASH index type specification is not explicitly supported by Oracle's CREATE INDEX syntax. Creating index with default type.");

    final StringBuilder b = new StringBuilder("CREATE ");
    if (unique)
      b.append("UNIQUE ");

    b.append("INDEX ");
    q(b, indexName).append(" ON ");
    q(b, tableName).append(" (").append(SQLDataTypes.csvNames(getDialect(), columns)).append(')');
    return new CreateStatement(b.toString());
  }

  @Override
  StringBuilder onDelete(final StringBuilder b, final $ChangeRule onDelete) {
    if (!"RESTRICT".equals(onDelete.text()))
      super.onDelete(b, onDelete);

    return b;
  }

  @Override
  StringBuilder onUpdate(final StringBuilder b, final $ChangeRule onUpdate) {
    if (logger.isWarnEnabled()) { logger.warn("ON UPDATE is not supported"); }
    return b;
  }

  @Override
  StringBuilder compileDate(final StringBuilder b, final String value) {
    return b.append("(date'").append(value).append("')");
  }

  @Override
  StringBuilder compileDateTime(final StringBuilder b, final String value) {
    return b.append("(timestamp'").append(value).append("')");
  }

  @Override
  StringBuilder compileTime(final StringBuilder b, final String value) {
    return b.append("INTERVAL '").append(value).append("' HOUR TO SECOND");
  }
}