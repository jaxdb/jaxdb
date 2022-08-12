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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints.PrimaryKey;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnDelete$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnUpdate$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
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
    super(DBVendor.ORACLE);
  }

  @Override
  void init(final Connection connection) throws SQLException {
    try {
      final ClassLoader classLoader = OracleCompiler.class.getClassLoader();
      Resources.walk(classLoader, "org/jaxdb/oracle", (root, entry, isDirectory) -> {
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
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE " + q(getSequenceName(table, type)) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;"));
            statements.add(new DropStatement("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER " + q(getTriggerName(table, type)) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;"));
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
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    // NOTE: Oracle's AUTO INCREMENT semantics are expressed via the CREATE SEQUENCE and CREATE TRIGGER statements, and nothing is needed in the CREATE TABLE statement
    return null;
  }

  @Override
  String primaryKey(final $Table table, final int[] columns, final PrimaryKey.Using$ using) {
    return super.primaryKey(table, columns, null);
  }

  @Override
  List<CreateStatement> types(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final List<CreateStatement> statements = new ArrayList<>();
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Integer) {
          final $Integer integer = ($Integer)column;
          if (Generator.isAuto(integer)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("CREATE SEQUENCE ").append(q(getSequenceName(table, integer)));
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

    statements.addAll(super.types(table, tableNameToEnumToOwner));
    return statements;
  }

  @Override
  List<CreateStatement> triggers(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<>();
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (Generator.isAuto(type)) {
            final String sequenceName = getSequenceName(table, type);
            final String columnName = q(column.getName$().text());
            statements.add(0, new CreateStatement("CREATE TRIGGER " + q(getTriggerName(table, type)) + " BEFORE INSERT ON " + q(table.getName$().text()) + " FOR EACH ROW WHEN (new." + columnName + " IS NULL) BEGIN SELECT " + q(sequenceName) + ".NEXTVAL INTO " + ":new." + columnName + " FROM dual; END;"));
          }
        }
      }
    }

    statements.addAll(super.triggers(table));
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
    if ($Index.Type$.HASH.text().equals(type.text()) && logger.isWarnEnabled())
      logger.warn("HASH index type specification is not explicitly supported by Oracle's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getDialect(), columns) + ")");
  }

  @Override
  String onDelete(final OnDelete$ onDelete) {
    return "RESTRICT".equals(onDelete.text()) ? null : super.onDelete(onDelete);
  }

  @Override
  String onUpdate(final OnUpdate$ onUpdate) {
    if (logger.isWarnEnabled())
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