/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.schema.spec;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.safris.commons.lang.Numbers;
import org.safris.xdb.schema.GeneratorExecutionException;
import org.safris.xdb.schema.SQLDataTypes;
import org.safris.xdb.xds.xe.$xds_bigint;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_check;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_columns;
import org.safris.xdb.xds.xe.$xds_constraints;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_foreignKey;
import org.safris.xdb.xds.xe.$xds_int;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_smallint;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;
import org.safris.xdb.xds.xe.$xds_tinyint;
import org.safris.xdb.xds.xe.xds_schema;

public abstract class SQLSpec {
  protected abstract String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $xds_named ... columns);

  public abstract void init(final Connection connection) throws SQLException;

  public String createSchemaIfNotExists(final xds_schema schema) {
    return null;
  }

  public String createTableIfNotExists(final $xds_table table, final Map<String,$xds_column> columnNameToColumn) throws GeneratorExecutionException {
    final StringBuilder builder = new StringBuilder();
    final String tableName = table._name$().text();
    builder.append("CREATE TABLE ").append(tableName).append(" (\n");
    builder.append(createColumns(table));
    builder.append(createConstraints(tableName, columnNameToColumn, table));
    builder.append("\n)");
    return builder.toString();
  }

  private String createColumns(final $xds_table table) {
    final StringBuilder ddl = new StringBuilder();
    if (table._column() == null)
      return "";

    for (final $xds_column column : table._column())
      ddl.append(",\n  ").append(createColumn(table, column));

    return ddl.substring(2);
  }

  private String createColumn(final $xds_table table, final $xds_column column) {
    final StringBuilder ddl = new StringBuilder();
    ddl.append(column._name$().text()).append(" ");
    if (column instanceof $xds_char) {
      final $xds_char type = ($xds_char)column;
      ddl.append(declareChar(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $xds_clob) {
      final $xds_clob type = ($xds_clob)column;
      ddl.append(declareClob(type._length$().text()));
    }
    else if (column instanceof $xds_binary) {
      final $xds_binary type = ($xds_binary)column;
      ddl.append(declareBinary(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $xds_blob) {
      final $xds_blob type = ($xds_blob)column;
      ddl.append(declareBlob(type._length$().text()));
    }
    else if (column instanceof $xds_tinyint) {
      final $xds_tinyint type = ($xds_tinyint)column;
      ddl.append(declareInt8(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_smallint) {
      final $xds_smallint type = ($xds_smallint)column;
      ddl.append(declareInt16(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_int) {
      final $xds_int type = ($xds_int)column;
      ddl.append(declareInt32(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_bigint) {
      final $xds_bigint type = ($xds_bigint)column;
      if (!type._unsigned$().text() && type._precision$().text().intValue() > 19)
        throw new IllegalArgumentException("BIGINT maximum precision [0, 19] exceeded: " + type._precision$().text().intValue());

      ddl.append(declareInt64(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_float) {
      final $xds_float type = ($xds_float)column;
      ddl.append(declareFloat(type._double$().text(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_decimal) {
      final $xds_decimal type = ($xds_decimal)column;
      ddl.append(declareDecimal(type._precision$().text().shortValue(), type._scale$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_date) {
      ddl.append(declareDate());
    }
    else if (column instanceof $xds_time) {
      final $xds_time type = ($xds_time)column;
      ddl.append(declareTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $xds_dateTime) {
      final $xds_dateTime type = ($xds_dateTime)column;
      ddl.append(declareDateTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $xds_boolean) {
      ddl.append(declareBoolean());
    }
    else if (column instanceof $xds_enum) {
      final $xds_enum type = ($xds_enum)column;
      ddl.append(declareEnum(table, type));
    }

    final String defaultFragement = $default(table, column);
    if (defaultFragement != null && defaultFragement.length() > 0)
      ddl.append(" DEFAULT ").append(defaultFragement);

    final String nullFragment = $null(table, column);
    if (nullFragment != null && nullFragment.length() > 0)
      ddl.append(" ").append(nullFragment);

    if (column instanceof $xds_integer) {
      final String autoIncrementFragment = $autoIncrement(table, ($xds_integer)column);
      if (autoIncrementFragment != null && autoIncrementFragment.length() > 0)
        ddl.append(" ").append(autoIncrementFragment);
    }

    return ddl.toString();
  }

  private String createConstraints(final String tableName, final Map<String,$xds_column> columnNameToColumn, final $xds_table table) throws GeneratorExecutionException {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $xds_constraints constraints = table._constraints(0);

      // unique constraint
      final List<$xds_columns> uniques = constraints._unique();
      if (uniques != null) {
        String uniqueString = "";
        int uniqueIndex = 1;
        for (final $xds_columns unique : uniques) {
          final List<$xds_named> columns = unique._column();
          String columnsString = "";
          for (final $xds_named column : columns)
            columnsString += ", " + column._name$().text();

          uniqueString += ",\n  CONSTRAINT " + table._name$().text() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      // check constraint
      final List<$xds_check> checks = constraints._check();
      if (checks != null) {
        String checkString = "";
        for (final $xds_check check : checks) {
          final String checkClause = recurseCheckRule(check);
          checkString += ",\n  CHECK " + (checkClause.startsWith("(") ? checkClause : "(" + checkClause + ")");
        }

        contraintsBuffer.append(checkString);
      }

      // primary key constraint
      final $xds_columns primaryKey = constraints._primaryKey(0);
      if (!primaryKey.isNull()) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xds_named primaryColumn : primaryKey._column()) {
          final String primaryKeyColumn = primaryColumn._name$().text();
          final $xds_column column = columnNameToColumn.get(primaryKeyColumn);
          if (column._null$().text())
            throw new GeneratorExecutionException("Column " + tableName + "." + column._name$() + " must be NOT NULL to be a PRIMARY KEY.");

          primaryKeyBuffer.append(", ").append(primaryKeyColumn);
        }

        contraintsBuffer.append(",\n  PRIMARY KEY (").append(primaryKeyBuffer.substring(2)).append(")");
      }

      // foreign key constraint
      final List<$xds_table._constraints._foreignKey> foreignKeys = constraints._foreignKey();
      if (foreignKeys != null) {
        for (final $xds_table._constraints._foreignKey foreignKey : foreignKeys) {
          String columns = "";
          String referencedColumns = "";
          for (final $xds_table._constraints._foreignKey._column column : foreignKey._column()) {
            columns += ", " + column._name$().text();
            referencedColumns += ", " + column._column$().text();
          }

          contraintsBuffer.append(",\n  FOREIGN KEY (").append(columns.substring(2));
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          contraintsBuffer.append(" (").append(referencedColumns.substring(2)).append(")");
          if (!foreignKey._onDelete$().isNull())
            contraintsBuffer.append(" ON DELETE ").append(foreignKey._onDelete$().text());

          if (!foreignKey._onUpdate$().isNull()) {
            final String onUpdate = onUpdate(foreignKey._onUpdate$());
            if (onUpdate != null)
              contraintsBuffer.append(" ").append(onUpdate);
          }
        }
      }
    }

    if (table._column() != null) {
      for (final $xds_column column : table._column()) {
        if (column._foreignKey() != null) {
          final $xds_foreignKey foreignKey = column._foreignKey(0);
          contraintsBuffer.append(",\n  FOREIGN KEY (").append(column._name$().text());
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          contraintsBuffer.append(" (").append(foreignKey._column$().text()).append(")");
          if (!foreignKey._onDelete$().isNull())
            contraintsBuffer.append(" ON DELETE ").append(foreignKey._onDelete$().text());

          if (!foreignKey._onUpdate$().isNull()) {
            final String onUpdate = onUpdate(foreignKey._onUpdate$());
            if (onUpdate != null)
              contraintsBuffer.append(" ").append(onUpdate);
          }
        }
      }

      // Parse the min & max constraints of numeric types
      for (final $xds_column column : table._column()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $xds_integer) {
          if (column instanceof $xds_tinyint) {
            final $xds_tinyint type = ($xds_tinyint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_smallint) {
            final $xds_smallint type = ($xds_smallint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_int) {
            final $xds_int type = ($xds_int)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_bigint) {
            final $xds_bigint type = ($xds_bigint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else {
            throw new UnsupportedOperationException("Unexpected type: " + column.getClass().getName());
          }
        }
        else if (column instanceof $xds_float) {
          final $xds_float type = ($xds_float)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xds_decimal) {
          final $xds_decimal type = ($xds_decimal)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }

        if (minCheck != null)
          minCheck = column._name$().text() + " >= " + minCheck;

        if (maxCheck != null)
          maxCheck = column._name$().text() + " <= " + maxCheck;

        if (minCheck != null) {
          if (maxCheck != null)
            contraintsBuffer.append(",\n  CHECK (" + minCheck + " AND " + maxCheck + ")");
          else
            contraintsBuffer.append(",\n  CHECK (" + minCheck + ")");
        }
        else if (maxCheck != null) {
          contraintsBuffer.append(",\n  CHECK (" + maxCheck + ")");
        }
      }

      // parse the <check/> element per type
      for (final $xds_column column : table._column()) {
        String operator = null;
        String condition = null;
        if (column instanceof $xds_char) {
          final $xds_char type = ($xds_char)column;
          if (!type._check(0).isNull()) {
            operator = $xds_char._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_char._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : null;
            condition = "'" + type._check(0)._condition$().text() + "'";
          }
        }
        else if (column instanceof $xds_integer) {
          final $xds_integer type = ($xds_integer)column;
          if (!type._check(0).isNull()) {
            operator = $xds_integer._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_integer._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_integer._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_integer._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_integer._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_integer._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $xds_float) {
          final $xds_float type = ($xds_float)column;
          if (!type._check(0).isNull()) {
            operator = $xds_float._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_float._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_float._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_float._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_float._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_float._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $xds_decimal) {
          final $xds_decimal type = ($xds_decimal)column;
          if (!type._check(0).isNull()) {
            operator = $xds_decimal._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_decimal._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_decimal._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_decimal._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_decimal._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_decimal._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }

        if (operator != null) {
          if (condition != null)
            contraintsBuffer.append(",\n  CHECK (" + column._name$().text() + " " + operator + " " + condition + ")");
          else
            throw new UnsupportedOperationException("Unexpected 'null' condition encountered on column '" + column._name$().text());
        }
        else if (condition != null)
          throw new UnsupportedOperationException("Unexpected 'null' operator encountered on column '" + column._name$().text());
      }
    }

    return contraintsBuffer.toString();
  }

  protected String onUpdate(final $xds_foreignKey._onUpdate$ onUpdate) {
    return " ON UPDATE " + onUpdate.text();
  }

  private static String recurseCheckRule(final $xds_check check) {
    final String condition;
    if (check._column().size() == 2)
      condition = check._column(1).text();
    else if (!check._value(0).isNull())
      condition = Numbers.isNumber(check._value(0).text()) ? Numbers.roundInsignificant(check._value(0).text()) : "'" + check._value(0).text() + "'";
    else
      throw new UnsupportedOperationException("Unexpected condition on column '" + check._column(0).text() + "'");

    final String clause = check._column(0).text() + " " + check._operator(0).text() + " " + condition;
    if (!check._and(0).isNull())
      return "(" + clause + " AND " + recurseCheckRule(check._and(0)) + ")";

    if (!check._or(0).isNull())
      return "(" + clause + " OR " + recurseCheckRule(check._or(0)) + ")";

    return clause;
  }

  public List<String> triggers(final $xds_table table) {
    return new ArrayList<String>();
  }

  public List<String> indexes(final $xds_table table) {
    final List<String> statements = new ArrayList<String>();
    if (table._indexes() != null) {
      for (final $xds_table._indexes._index index : table._indexes(0)._index()) {
        statements.add(createIndex(!index._unique$().isNull() && index._unique$().text(), SQLDataTypes.getIndexName(table, index), index._type$().text(), table._name$().text(), index._column().toArray(new $xds_named[index._column().size()])));
      }
    }

    if (table._column() != null) {
      for (final $xds_column column : table._column()) {
        if (column._index() != null) {
          statements.add(createIndex(!column._index(0)._unique$().isNull() && column._index(0)._unique$().text(), SQLDataTypes.getIndexName(table, column._index(0), column), column._index(0)._type$().text(), table._name$().text(), column));
        }
      }
    }

    return statements;
  }

  public List<String> types(final $xds_table table) {
    return new ArrayList<String>();
  }

  protected abstract String dropIndexOnClause(final $xds_table table);

  public List<String> drops(final $xds_table table) {
    final List<String> statements = new ArrayList<String>();
    if (table._indexes() != null)
      for (final $xds_table._indexes._index index : table._indexes(0)._index())
        statements.add("DROP INDEX IF EXISTS " + SQLDataTypes.getIndexName(table, index) + dropIndexOnClause(table));

    if (table._column() != null)
      for (final $xds_column column : table._column())
        if (column._index() != null)
          statements.add("DROP INDEX IF EXISTS " + SQLDataTypes.getIndexName(table, column._index(0), column) + dropIndexOnClause(table));

    if (table._triggers() != null)
      for (final $xds_table._triggers._trigger trigger : table._triggers().get(0)._trigger())
        for (final String action : trigger._actions$().text())
          statements.add("DROP TRIGGER IF EXISTS " + SQLDataTypes.getTriggerName(table._name$().text(), trigger, action));

    final String dropTable = dropTableIfExists(table);
    if (dropTable != null)
      statements.add(dropTable);

    return statements;
  }

  public String dropTableIfExists(final $xds_table table) {
    return "DROP TABLE IF EXISTS " + table._name$().text();
  }

  private static void checkNumericDefault(final $xds_column type, final String defalt, final boolean positive, final Integer precision, final boolean unsigned) {
    if (!positive && unsigned)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type._name$().text() + "' DEFAULT " + defalt + " is negative, but type is declared UNSIGNED");

    if (precision != null && defalt.toString().length() > precision)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type._name$().text() + "' DEFAULT " + defalt + " is longer than declared PRECISION " + precision + ")");
  }

  public String $default(final $xds_table table, final $xds_column column) {
    if (column instanceof $xds_char) {
      final $xds_char type = ($xds_char)column;
      if (type._default$().isNull())
        return null;

      if (type._default$().text().length() > type._length$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column._name$().text() + "' DEFAULT '" + type._default$().text() + "' is longer than declared LENGTH(" + type._length$().text() + ")");

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $xds_binary) {
      final $xds_binary type = ($xds_binary)column;
      if (type._default$().isNull())
        return null;

      if (type._default$().text().getBytes().length > type._length$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column._name$().text() + "' DEFAULT '" + type._default$().text() + "' is longer than declared LENGTH " + type._length$().text());

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $xds_integer) {
      final BigInteger defalt;
      final Integer precision;
      final boolean unsigned;
      if (column instanceof $xds_tinyint) {
        final $xds_tinyint type = ($xds_tinyint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $xds_smallint) {
        final $xds_smallint type = ($xds_smallint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $xds_int) {
        final $xds_int type = ($xds_int)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else if (column instanceof $xds_bigint) {
        final $xds_bigint type = ($xds_bigint)column;
        defalt = type._default$().text();
        precision = type._precision$().text().intValue();
        unsigned = type._unsigned$().text();
      }
      else {
        throw new UnsupportedOperationException("Unexpected type: " + column.getClass().getName());
      }

      if (defalt == null)
        return null;

      checkNumericDefault(column, defalt.toString(), defalt.compareTo(BigInteger.ZERO) >= 0, precision, unsigned);
      return String.valueOf(defalt);
    }

    if (column instanceof $xds_float) {
      final $xds_float type = ($xds_float)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, null, type._unsigned$().text());
      return type._default$().text().toString();
    }

    if (column instanceof $xds_decimal) {
      final $xds_decimal type = ($xds_decimal)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, type._precision$().text().intValue(), type._unsigned$().text());
      return type._default$().text().toString();
    }

    if (column instanceof $xds_date) {
      final $xds_date type = ($xds_date)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $xds_time) {
      final $xds_time type = ($xds_time)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $xds_dateTime) {
      final $xds_dateTime type = ($xds_dateTime)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $xds_boolean) {
      final $xds_boolean type = ($xds_boolean)column;
      if (type._default$().isNull())
        return null;

      return type._default$().text().toString();
    }

    if (column instanceof $xds_enum) {
      final $xds_enum type = ($xds_enum)column;
      if (type._default$().isNull())
        return null;

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $xds_clob || column instanceof $xds_blob)
      return null;

    throw new UnsupportedOperationException("Unknown type: " + column.getClass().getName());
  }

  public static List<String> parseEnum(final String value) {
    final List<String> enums = new ArrayList<String>();
    final char[] chars = value.replace("\\\\", "\\").toCharArray();
    final StringBuilder builder = new StringBuilder();
    boolean escaped = false;
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      if (ch == '\\') {
        escaped = true;
      }
      else if (ch != ' ' || escaped) {
        escaped = false;
        builder.append(ch);
      }
      else if (builder.length() > 0) {
        enums.add(builder.toString());
        builder.setLength(0);
      }
    }

    enums.add(builder.toString());
    return enums;
  }

  public abstract String truncate(final String tableName);

  public abstract String $null(final $xds_table table, final $xds_column column);
  public abstract String $autoIncrement(final $xds_table table, final $xds_integer column);

  public abstract String declareFloat(final boolean doublePrecision, final boolean unsigned);
  public abstract String declareBoolean();
  public abstract String declareBinary(final boolean varying, final long length);
  public abstract String declareChar(final boolean varying, final long length);
  public abstract String declareClob(final long length);
  public abstract String declareBlob(final long length);
  public abstract String declareDecimal(final short precision, final short scale, final boolean unsigned);
  public abstract String declareDate();
  public abstract String declareDateTime(final short precision);
  public abstract String declareTime(final short precision);
  public abstract String declareInterval();
  public abstract String declareEnum(final $xds_table table, final $xds_enum type);

  public abstract String declareInt8(final short precision, final boolean unsigned);
  public abstract String declareInt16(final short precision, final boolean unsigned);
  public abstract String declareInt32(final short precision, final boolean unsigned);
  public abstract String declareInt64(final short precision, final boolean unsigned);
}