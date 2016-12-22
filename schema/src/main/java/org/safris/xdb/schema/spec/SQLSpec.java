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
import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.schema.SQLDataTypes;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;

public abstract class SQLSpec {
  protected abstract String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $xds_named ... columns);

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

    statements.add("DROP TABLE IF EXISTS " + table._name$().text());
    return statements;
  }

  public abstract String type(final $xds_table table, final $xds_char type);
  public abstract String type(final $xds_table table, final $xds_clob type);
  public abstract String type(final $xds_table table, final $xds_binary type);
  public abstract String type(final $xds_table table, final $xds_blob type);
  public abstract String type(final $xds_table table, final $xds_integer type);
  public abstract String type(final $xds_table table, final $xds_float type);
  public abstract String type(final $xds_table table, final $xds_decimal type);
  public abstract String type(final $xds_table table, final $xds_date type);
  public abstract String type(final $xds_table table, final $xds_time type);
  public abstract String type(final $xds_table table, final $xds_dateTime type);
  public abstract String type(final $xds_table table, final $xds_boolean type);
  public abstract String type(final $xds_table table, final $xds_enum type);

  private static void checkNumericDefault(final $xds_column type, final String defalt, final boolean positive, final int precision, final boolean unsigned) {
    if (!positive && unsigned)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type._name$().text() + "' DEFAULT " + defalt + " is negative, but type is declared UNSIGNED");

    if (defalt.toString().length() > precision)
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

      if (type._default$().text().length() > type._length$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column._name$().text() + "' DEFAULT '" + type._default$().text() + "' is longer than declared LENGTH " + type._length$().text());

      return "'" + type._default$().text() + "'";
    }

    if (column instanceof $xds_integer) {
      final $xds_integer type = ($xds_integer)column;
      if (type._default$().isNull())
        return null;

      final BigInteger defalt = type._default$().text();
      checkNumericDefault(type, defalt.toString(), defalt.compareTo(BigInteger.ZERO) >= 0, type._precision$().text(), type._unsigned$().text());
      return String.valueOf(type._default$().text());
    }

    if (column instanceof $xds_float) {
      final $xds_float type = ($xds_float)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, type._precision$().text(), type._unsigned$().text());
      return type._default$().text().toString();
    }

    if (column instanceof $xds_decimal) {
      final $xds_decimal type = ($xds_decimal)column;
      if (type._default$().isNull())
        return null;

      checkNumericDefault(type, type._default$().text().toString(), type._default$().text().doubleValue() > 0, type._precision$().text(), type._unsigned$().text());
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

    throw new UnsupportedOperationException("Unknown type: " + column.getClass().getName());
  }

  public abstract String $null(final $xds_table table, final $xds_column column);
  public abstract String $autoIncrement(final $xds_table table, final $xds_integer column);
}