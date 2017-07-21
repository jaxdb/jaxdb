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

import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lib4j.lang.Numbers;
import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_binary;
import org.libx4j.rdb.ddlx.xe.$ddlx_blob;
import org.libx4j.rdb.ddlx.xe.$ddlx_boolean;
import org.libx4j.rdb.ddlx.xe.$ddlx_changeAction;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_clob;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_columns;
import org.libx4j.rdb.ddlx.xe.$ddlx_constraints;
import org.libx4j.rdb.ddlx.xe.$ddlx_date;
import org.libx4j.rdb.ddlx.xe.$ddlx_dateTime;
import org.libx4j.rdb.ddlx.xe.$ddlx_decimal;
import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_time;
import org.libx4j.rdb.ddlx.xe.$ddlx_tinyint;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;

abstract class Compiler {
  private static final Compiler[] compilers = new Compiler[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemContextPackageLoader().loadPackage(Compiler.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Compiler.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Compiler compiler = (Compiler)cls.newInstance();
          compilers[compiler.getVendor().ordinal()] = compiler;
        }
      }
    }
    catch (final PackageNotFoundException | ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected static boolean isAutoIncrement(final $ddlx_integer column) {
    return !column._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text());
  }

  protected abstract DBVendor getVendor();

  protected abstract CreateStatement createIndex(final boolean unique, final String indexName, final $ddlx_index._type$ type, final String tableName, final $ddlx_named ... columns);

  protected abstract void init(final Connection connection) throws SQLException;

  protected CreateStatement createSchemaIfNotExists(final ddlx_schema schema) {
    return null;
  }

  protected CreateStatement createTableIfNotExists(final $ddlx_table table, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    final StringBuilder builder = new StringBuilder();
    final String tableName = table._name$().text();
    builder.append("CREATE TABLE ").append(tableName).append(" (\n");
    if (table._column() != null)
      builder.append(createColumns(table));

    final CreateStatement constraints = createConstraints(columnNameToColumn, table);
    if (constraints != null)
      builder.append(constraints);

    builder.append("\n)");
    return new CreateStatement(builder.toString());
  }

  private String createColumns(final $ddlx_table table) {
    final StringBuilder ddl = new StringBuilder();
    for (final $ddlx_column column : table._column())
      ddl.append(",\n  ").append(createColumn(table, column));

    return ddl.substring(2);
  }

  private CreateStatement createColumn(final $ddlx_table table, final $ddlx_column column) {
    final StringBuilder ddl = new StringBuilder();
    ddl.append(column._name$().text()).append(" ");
    if (column instanceof $ddlx_char) {
      final $ddlx_char type = ($ddlx_char)column;
      ddl.append(getVendor().getDialect().declareChar(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $ddlx_clob) {
      final $ddlx_clob type = ($ddlx_clob)column;
      ddl.append(getVendor().getDialect().declareClob(type._length$().text()));
    }
    else if (column instanceof $ddlx_binary) {
      final $ddlx_binary type = ($ddlx_binary)column;
      ddl.append(getVendor().getDialect().declareBinary(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $ddlx_blob) {
      final $ddlx_blob type = ($ddlx_blob)column;
      ddl.append(getVendor().getDialect().declareBlob(type._length$().text()));
    }
    else if (column instanceof $ddlx_integer) {
      ddl.append(createIntegerColumn(($ddlx_integer)column));
    }
    else if (column instanceof $ddlx_float) {
      final $ddlx_float type = ($ddlx_float)column;
      ddl.append(getVendor().getDialect().declareFloat(type._double$().text(), type._unsigned$().text()));
    }
    else if (column instanceof $ddlx_decimal) {
      final $ddlx_decimal type = ($ddlx_decimal)column;
      ddl.append(getVendor().getDialect().declareDecimal(type._precision$().text().shortValue(), type._scale$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $ddlx_date) {
      ddl.append(getVendor().getDialect().declareDate());
    }
    else if (column instanceof $ddlx_time) {
      final $ddlx_time type = ($ddlx_time)column;
      ddl.append(getVendor().getDialect().declareTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $ddlx_dateTime) {
      final $ddlx_dateTime type = ($ddlx_dateTime)column;
      ddl.append(getVendor().getDialect().declareDateTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $ddlx_boolean) {
      ddl.append(getVendor().getDialect().declareBoolean());
    }
    else if (column instanceof $ddlx_enum) {
      final $ddlx_enum type = ($ddlx_enum)column;
      ddl.append(getVendor().getDialect().declareEnum(table, type));
    }

    final String defaultFragement = $default(table, column);
    if (defaultFragement != null && defaultFragement.length() > 0)
      ddl.append(" DEFAULT ").append(defaultFragement);

    final String nullFragment = $null(table, column);
    if (nullFragment != null && nullFragment.length() > 0)
      ddl.append(" ").append(nullFragment);

    if (column instanceof $ddlx_integer) {
      final String autoIncrementFragment = $autoIncrement(table, ($ddlx_integer)column);
      if (autoIncrementFragment != null && autoIncrementFragment.length() > 0)
        ddl.append(" ").append(autoIncrementFragment);
    }

    return new CreateStatement(ddl.toString());
  }

  protected String createIntegerColumn(final $ddlx_integer column) {
    if (column instanceof $ddlx_tinyint) {
      final $ddlx_tinyint type = ($ddlx_tinyint)column;
      return getVendor().getDialect().declareInt8(type._precision$().text().shortValue(), type._unsigned$().text());
    }

    if (column instanceof $ddlx_smallint) {
      final $ddlx_smallint type = ($ddlx_smallint)column;
      return getVendor().getDialect().declareInt16(type._precision$().text().shortValue(), type._unsigned$().text());
    }

    if (column instanceof $ddlx_int) {
      final $ddlx_int type = ($ddlx_int)column;
      return getVendor().getDialect().declareInt32(type._precision$().text().shortValue(), type._unsigned$().text());
    }

    if (column instanceof $ddlx_bigint) {
      final $ddlx_bigint type = ($ddlx_bigint)column;
      if (!type._unsigned$().text() && type._precision$().text().intValue() > 19)
        throw new IllegalArgumentException("BIGINT maximum precision [0, 19] exceeded: " + type._precision$().text().intValue());

      return getVendor().getDialect().declareInt64(type._precision$().text().shortValue(), type._unsigned$().text());
    }

    throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
  }

  private CreateStatement createConstraints(final Map<String,$ddlx_column> columnNameToColumn, final $ddlx_table table) throws GeneratorExecutionException {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $ddlx_constraints constraints = table._constraints(0);

      // unique constraint
      final List<$ddlx_columns> uniques = constraints._unique();
      if (uniques != null) {
        String uniqueString = "";
        int uniqueIndex = 1;
        for (final $ddlx_columns unique : uniques) {
          final List<$ddlx_named> columns = unique._column();
          String columnsString = "";
          for (final $ddlx_named column : columns)
            columnsString += ", " + column._name$().text();

          uniqueString += ",\n  CONSTRAINT " + table._name$().text() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      // check constraint
      final List<$ddlx_check> checks = constraints._check();
      if (checks != null) {
        String checkString = "";
        for (final $ddlx_check check : checks) {
          final String checkClause = recurseCheckRule(check);
          checkString += ",\n  CHECK " + (checkClause.startsWith("(") ? checkClause : "(" + checkClause + ")");
        }

        contraintsBuffer.append(checkString);
      }

      // primary key constraint
      final String primaryKeyConstraint = blockPrimaryKey(table, constraints, columnNameToColumn);
      if (primaryKeyConstraint != null)
        contraintsBuffer.append(primaryKeyConstraint);

      // foreign key constraint
      final List<$ddlx_table._constraints._foreignKey> foreignKeys = constraints._foreignKey();
      if (foreignKeys != null) {
        for (final $ddlx_table._constraints._foreignKey foreignKey : foreignKeys) {
          String columns = "";
          String referencedColumns = "";
          for (final $ddlx_table._constraints._foreignKey._column column : foreignKey._column()) {
            columns += ", " + column._name$().text();
            referencedColumns += ", " + column._column$().text();
          }

          contraintsBuffer.append(",\n  ").append(foreignKey(table)).append(" (").append(columns.substring(2));
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          contraintsBuffer.append(" (").append(referencedColumns.substring(2)).append(")");
          if (!foreignKey._onDelete$().isNull()) {
            final String onDelete = onDelete(foreignKey._onDelete$());
            if (onDelete != null)
              contraintsBuffer.append(" ").append(onDelete);
          }

          if (!foreignKey._onUpdate$().isNull()) {
            final String onUpdate = onUpdate(foreignKey._onUpdate$());
            if (onUpdate != null)
              contraintsBuffer.append(" ").append(onUpdate);
          }
        }
      }
    }

    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column._foreignKey() != null) {
          final $ddlx_foreignKey foreignKey = column._foreignKey(0);
          contraintsBuffer.append(",\n  ").append(foreignKey(table)).append(" (").append(column._name$().text());
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          contraintsBuffer.append(" (").append(foreignKey._column$().text()).append(")");
          if (!foreignKey._onDelete$().isNull()) {
            final String onDelete = onDelete(foreignKey._onDelete$());
            if (onDelete != null)
              contraintsBuffer.append(" ").append(onDelete);
          }

          if (!foreignKey._onUpdate$().isNull()) {
            final String onUpdate = onUpdate(foreignKey._onUpdate$());
            if (onUpdate != null)
              contraintsBuffer.append(" ").append(onUpdate);
          }
        }
      }

      // Parse the min & max constraints of numeric types
      for (final $ddlx_column column : table._column()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $ddlx_integer) {
          if (column instanceof $ddlx_tinyint) {
            final $ddlx_tinyint type = ($ddlx_tinyint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $ddlx_smallint) {
            final $ddlx_smallint type = ($ddlx_smallint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $ddlx_int) {
            final $ddlx_int type = ($ddlx_int)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $ddlx_bigint) {
            final $ddlx_bigint type = ($ddlx_bigint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else {
            throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
          }
        }
        else if (column instanceof $ddlx_float) {
          final $ddlx_float type = ($ddlx_float)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $ddlx_decimal) {
          final $ddlx_decimal type = ($ddlx_decimal)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }

        if (minCheck != null)
          minCheck = column._name$().text() + " >= " + minCheck;

        if (maxCheck != null)
          maxCheck = column._name$().text() + " <= " + maxCheck;

        if (minCheck != null) {
          if (maxCheck != null)
            contraintsBuffer.append(",\n  ").append(check(table)).append(" (" + minCheck + " AND " + maxCheck + ")");
          else
            contraintsBuffer.append(",\n  ").append(check(table)).append(" (" + minCheck + ")");
        }
        else if (maxCheck != null) {
          contraintsBuffer.append(",\n  ").append(check(table)).append(" (" + maxCheck + ")");
        }
      }

      // parse the <check/> element per type
      for (final $ddlx_column column : table._column()) {
        String operator = null;
        String condition = null;
        if (column instanceof $ddlx_char) {
          final $ddlx_char type = ($ddlx_char)column;
          if (!type._check(0).isNull()) {
            operator = $ddlx_char._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $ddlx_char._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : null;
            condition = "'" + type._check(0)._condition$().text() + "'";
          }
        }
        else if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._check(0).isNull()) {
            operator = $ddlx_integer._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $ddlx_integer._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $ddlx_integer._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $ddlx_integer._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $ddlx_integer._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $ddlx_integer._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $ddlx_float) {
          final $ddlx_float type = ($ddlx_float)column;
          if (!type._check(0).isNull()) {
            operator = $ddlx_float._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $ddlx_float._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $ddlx_float._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $ddlx_float._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $ddlx_float._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $ddlx_float._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $ddlx_decimal) {
          final $ddlx_decimal type = ($ddlx_decimal)column;
          if (!type._check(0).isNull()) {
            operator = $ddlx_decimal._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $ddlx_decimal._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $ddlx_decimal._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $ddlx_decimal._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $ddlx_decimal._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $ddlx_decimal._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }

        if (operator != null) {
          if (condition != null)
            contraintsBuffer.append(",\n  ").append(check(table)).append(" (" + column._name$().text() + " " + operator + " " + condition + ")");
          else
            throw new UnsupportedOperationException("Unsupported 'null' condition encountered on column '" + column._name$().text());
        }
        else if (condition != null)
          throw new UnsupportedOperationException("Unsupported 'null' operator encountered on column '" + column._name$().text());
      }
    }

    return new CreateStatement(contraintsBuffer.toString());
  }

  protected String check(final $ddlx_table table) {
    return "CHECK";
  }

  protected String blockPrimaryKey(final $ddlx_table table, final $ddlx_constraints constraints, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    final $ddlx_columns primaryKey = constraints._primaryKey(0);
    if (primaryKey.isNull())
      return "";

    final StringBuffer primaryKeyBuffer = new StringBuffer();
    for (final $ddlx_named primaryColumn : primaryKey._column()) {
      final String primaryKeyColumn = primaryColumn._name$().text();
      final $ddlx_column column = columnNameToColumn.get(primaryKeyColumn);
      if (column._null$().text())
        throw new GeneratorExecutionException("Column " + column._name$() + " must be NOT NULL to be a PRIMARY KEY.");

      primaryKeyBuffer.append(", ").append(primaryKeyColumn);
    }

    return ",\n  " + primaryKey(table) + " (" + primaryKeyBuffer.substring(2) + ")";
  }

  protected String foreignKey(final $ddlx_table table) {
    return "FOREIGN KEY";
  }

  protected String primaryKey(final $ddlx_table table) {
    return "PRIMARY KEY";
  }

  protected String onDelete(final $ddlx_foreignKey._onDelete$ onDelete) {
    return "ON DELETE " + changeAction(onDelete);
  }

  protected String onUpdate(final $ddlx_foreignKey._onUpdate$ onUpdate) {
    return "ON UPDATE " + changeAction(onUpdate);
  }

  protected String changeAction(final $ddlx_changeAction changeAction) {
    return changeAction.text();
  }

  private static String recurseCheckRule(final $ddlx_check check) {
    final String condition;
    if (check._column().size() == 2)
      condition = check._column(1).text();
    else if (!check._value(0).isNull())
      condition = Numbers.isNumber(check._value(0).text()) ? Numbers.roundInsignificant(check._value(0).text()) : "'" + check._value(0).text() + "'";
    else
      throw new UnsupportedOperationException("Unsupported condition on column '" + check._column(0).text() + "'");

    final String clause = check._column(0).text() + " " + check._operator(0).text() + " " + condition;
    if (!check._and(0).isNull())
      return "(" + clause + " AND " + recurseCheckRule(check._and(0)) + ")";

    if (!check._or(0).isNull())
      return "(" + clause + " OR " + recurseCheckRule(check._or(0)) + ")";

    return clause;
  }

  protected List<CreateStatement> triggers(final $ddlx_table table) {
    return new ArrayList<CreateStatement>();
  }

  protected List<CreateStatement> indexes(final $ddlx_table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table._indexes() != null) {
      for (final $ddlx_table._indexes._index index : table._indexes(0)._index()) {
        final CreateStatement createIndex = createIndex(!index._unique$().isNull() && index._unique$().text(), SQLDataTypes.getIndexName(table, index), index._type$(), table._name$().text(), index._column().toArray(new $ddlx_named[index._column().size()]));
        if (createIndex != null)
          statements.add(createIndex);
      }
    }

    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column._index() != null) {
          final CreateStatement createIndex = createIndex(!column._index(0)._unique$().isNull() && column._index(0)._unique$().text(), SQLDataTypes.getIndexName(table, column._index(0), column), column._index(0)._type$(), table._name$().text(), column);
          if (createIndex != null)
            statements.add(createIndex);
        }
      }
    }

    return statements;
  }

  protected List<CreateStatement> types(final $ddlx_table table) {
    return new ArrayList<CreateStatement>();
  }

  protected abstract String dropIndexOnClause(final $ddlx_table table);

  protected List<DropStatement> drops(final $ddlx_table table) {
    final List<DropStatement> statements = new ArrayList<DropStatement>();
    // FIXME: Explicitly dropping indexes on tables that may not exist will throw errors!
//    if (table._indexes() != null)
//      for (final $ddlx_table._indexes._index index : table._indexes(0)._index())
//        statements.add(dropIndexIfExists(SQLDataTypes.getIndexName(table, index) + dropIndexOnClause(table)));

//    if (table._column() != null)
//      for (final $ddlx_column column : table._column())
//        if (column._index() != null)
//          statements.add(dropIndexIfExists(SQLDataTypes.getIndexName(table, column._index(0), column) + dropIndexOnClause(table)));

    if (table._triggers() != null)
      for (final $ddlx_table._triggers._trigger trigger : table._triggers().get(0)._trigger())
        for (final String action : trigger._actions$().text())
          statements.add(new DropStatement("DROP TRIGGER IF EXISTS " + SQLDataTypes.getTriggerName(table._name$().text(), trigger, action)));

    final DropStatement dropTable = dropTableIfExists(table);
    if (dropTable != null)
      statements.add(dropTable);

    return statements;
  }

  protected DropStatement dropTableIfExists(final $ddlx_table table) {
    return new DropStatement("DROP TABLE IF EXISTS " + table._name$().text());
  }

  protected DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("DROP INDEX IF EXISTS " + indexName);
  }

  private static void checkNumericDefault(final $ddlx_column type, final String defalt, final boolean positive, final Integer precision, final boolean unsigned) {
    if (!positive && unsigned)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type._name$().text() + "' DEFAULT " + defalt + " is negative, but type is declared UNSIGNED");

    if (precision != null && defalt.toString().length() > precision)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type._name$().text() + "' DEFAULT " + defalt + " is longer than declared PRECISION " + precision + ")");
  }

  protected String $default(final $ddlx_table table, final $ddlx_column column) {
    if (column instanceof $ddlx_char) {
      final $ddlx_char type = ($ddlx_char)column;
      if (type._default$().isNull())
        return null;

      if (type._default$().text().length() > type._length$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column._name$().text() + "' DEFAULT '" + type._default$().text() + "' is longer than declared LENGTH(" + type._length$().text() + ")");

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $ddlx_binary) {
      final $ddlx_binary type = ($ddlx_binary)column;
      if (type._default$().isNull())
        return null;

      if (type._default$().text().getBytes().length > type._length$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column._name$().text() + "' DEFAULT '" + type._default$().text() + "' is longer than declared LENGTH " + type._length$().text());

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $ddlx_integer) {
      final BigInteger defalt;
      final Integer precision;
      final boolean unsigned;
      if (column instanceof $ddlx_tinyint) {
        final $ddlx_tinyint type = ($ddlx_tinyint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $ddlx_smallint) {
        final $ddlx_smallint type = ($ddlx_smallint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $ddlx_int) {
        final $ddlx_int type = ($ddlx_int)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $ddlx_bigint) {
        final $ddlx_bigint type = ($ddlx_bigint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else {
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
      }

      if (defalt == null)
        return null;

      checkNumericDefault(column, defalt.toString(), defalt.compareTo(BigInteger.ZERO) >= 0, precision, unsigned);
      return String.valueOf(defalt);
    }

    if (column instanceof $ddlx_float) {
      final $ddlx_float type = ($ddlx_float)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, null, type._unsigned$().text());
      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_decimal) {
      final $ddlx_decimal type = ($ddlx_decimal)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, type._precision$().text().intValue(), type._unsigned$().text());
      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_date) {
      final $ddlx_date type = ($ddlx_date)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_time) {
      final $ddlx_time type = ($ddlx_time)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_dateTime) {
      final $ddlx_dateTime type = ($ddlx_dateTime)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_boolean) {
      final $ddlx_boolean type = ($ddlx_boolean)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $ddlx_enum) {
      final $ddlx_enum type = ($ddlx_enum)column;
      if (type._default$().isNull())
        return null;

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $ddlx_clob || column instanceof $ddlx_blob)
      return null;

    throw new UnsupportedOperationException("Unknown type: " + column.getClass().getName());
  }

  protected String truncate(final String tableName) {
    return "DELETE FROM " + tableName;
  }

  protected abstract String $null(final $ddlx_table table, final $ddlx_column column);
  protected abstract String $autoIncrement(final $ddlx_table table, final $ddlx_integer column);
}