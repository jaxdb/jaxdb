package org.libx4j.rdb.ddlx;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_binary;
import org.libx4j.rdb.ddlx.xe.$ddlx_blob;
import org.libx4j.rdb.ddlx.xe.$ddlx_boolean;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_clob;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_constraints;
import org.libx4j.rdb.ddlx.xe.$ddlx_date;
import org.libx4j.rdb.ddlx.xe.$ddlx_datetime;
import org.libx4j.rdb.ddlx.xe.$ddlx_decimal;
import org.libx4j.rdb.ddlx.xe.$ddlx_double;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_time;
import org.libx4j.rdb.ddlx.xe.$ddlx_tinyint;
import org.libx4j.rdb.vendor.DBVendor;

class SQLiteDecompiler extends Decompiler {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.SQLITE;
  }

  private static Long getLength(final String typeName) {
    final int open = typeName.indexOf('(');
    if (open < 0)
      return null;

    final int close = typeName.indexOf(')', open + 1);
    return close <= open ? null : Long.parseLong(typeName.substring(open + 1, close).trim());
  }

  @Override
  protected $ddlx_column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $ddlx_column column;
    if (typeName.startsWith("BIGINT")) {
      final $ddlx_bigint type = newColumn($ddlx_bigint.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_bigint._precision$((byte)size));

      if (typeName.endsWith("UNSIGNED"))
        type._unsigned$(new $ddlx_integer._unsigned$(true));

      if (_default != null)
        type._default$(new $ddlx_bigint._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if (typeName.startsWith("BINARY")) {
      final $ddlx_binary type = newColumn($ddlx_binary.class);
      final Long length = getLength(typeName);
      if (length != null)
        type._length$(new $ddlx_binary._length$(length.intValue()));

      column = type;
    }
    else if (typeName.startsWith("BLOB")) {
      final $ddlx_blob type = newColumn($ddlx_blob.class);
      final Long length = getLength(typeName);
      if (length != null)
        type._length$(new $ddlx_blob._length$(length));

      column = type;
    }
    else if ("BOOLEAN".equals(typeName)) {
      final $ddlx_boolean type = newColumn($ddlx_boolean.class);
      if (_default != null)
        type._default$(new $ddlx_boolean._default$(Boolean.parseBoolean(_default)));

      column = type;
    }
    else if (typeName.startsWith("VARCHAR") || typeName.startsWith("CHARACTER")) {
      final $ddlx_char type = newColumn($ddlx_char.class);
      if (typeName.startsWith("VARCHAR"))
        type._varying$(new $ddlx_char._varying$(true));

      final Long length = getLength(typeName);
      if (length != null)
        type._length$(new $ddlx_char._length$(length.intValue()));

      if (_default != null)
        type._default$(new $ddlx_char._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if (typeName.startsWith("TEXT")) {
      final $ddlx_clob type = newColumn($ddlx_clob.class);
      final Long length = getLength(typeName);
      if (length != null)
        type._length$(new $ddlx_clob._length$(length));

      column = type;
    }
    else if ("DATE".equals(typeName)) {
      final $ddlx_date type = newColumn($ddlx_date.class);
      if (_default != null)
        type._default$(new $ddlx_date._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("DATETIME".equals(typeName)) {
      final $ddlx_datetime type = newColumn($ddlx_datetime.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_datetime._precision$((byte)size));

      if (_default != null)
        type._default$(new $ddlx_datetime._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if (typeName.startsWith("DECIMAL")) {
      final $ddlx_decimal type = newColumn($ddlx_decimal.class);
      if (!typeName.equals("DECIMAL(15, 0)")) {
        final int open = typeName.indexOf('(');
        if (open > 0) {
          final int comma = typeName.indexOf(',', open + 1);
          if (comma > open) {
            final int close = typeName.indexOf(')', comma + 1);
            if (close > comma) {
              type._precision$(new $ddlx_decimal._precision$(Short.parseShort(typeName.substring(open + 1, comma).trim())));
              type._scale$(new $ddlx_decimal._scale$(Short.parseShort(typeName.substring(comma + 1, close).trim())));
            }
          }
        }
      }

      if (_default != null)
        type._default$(new $ddlx_decimal._default$(new BigDecimal(_default)));

      column = type;
    }
    else if ("DOUBLE".equals(typeName)) {
      final $ddlx_double type = newColumn($ddlx_double.class);
      if (_default != null)
        type._default$(new $ddlx_double._default$(Double.valueOf(_default)));

      column = type;
    }
//    else if ("ENUM".equals(typeName)) {
//      final $ddlx_enum type = newColumn($ddlx_enum.class);
//      if (_default != null)
//        type._default$(new $ddlx_enum._default$(_default));
//
//      column = type;
//    }
    else if ("FLOAT".equals(typeName)) {
      final $ddlx_float type = newColumn($ddlx_float.class);
      if (_default != null)
        type._default$(new $ddlx_float._default$(Float.valueOf(_default)));

      column = type;
    }
    else if (typeName.startsWith("INT") || typeName.startsWith("MEDIUMINT")) {
      final $ddlx_int type = newColumn($ddlx_int.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_int._precision$((byte)size));

      if (_default != null)
        type._default$(new $ddlx_int._default$(new BigInteger(_default)));

      if ("INTEGER".equals(typeName))
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $ddlx_smallint type = newColumn($ddlx_smallint.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_smallint._precision$((byte)size));

      if (_default != null)
        type._default$(new $ddlx_smallint._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("TIME".equals(typeName)) {
      final $ddlx_time type = newColumn($ddlx_time.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_time._precision$((byte)size));

      if (_default != null)
        type._default$(new $ddlx_time._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("TINYINT".equals(typeName)) {
      final $ddlx_tinyint type = newColumn($ddlx_tinyint.class);
      if (size != 2000000000)
        type._precision$(new $ddlx_tinyint._precision$((byte)size));

      if (_default != null)
        type._default$(new $ddlx_tinyint._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else {
      throw new UnsupportedOperationException("Unsupported column type: " + typeName);
    }

    column._name$(new $ddlx_column._name$(columnName));
    if (nullable != null && !nullable)
      column._null$(new $ddlx_column._null$(false));

    return column;
  }

  @Override
  protected Map<String,List<$ddlx_constraints._unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Map<String,List<$ddlx_check>> getCheckConstraints(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Map<String,$ddlx_table._indexes> getIndexes(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }
}
