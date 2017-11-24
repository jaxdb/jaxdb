package org.libx4j.rdb.ddlx;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Bigint;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Binary;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Blob;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Boolean;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Char;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Check;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Clob;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Column;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Constraints;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Date;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Datetime;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Decimal;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Double;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Float;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Int;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Integer;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Smallint;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Table;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Time;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Tinyint;
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
  protected $Column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $Column column;
    if (typeName.startsWith("BIGINT")) {
      final $Bigint type = newColumn($Bigint.class);
      if (size != 2000000000)
        type.setPrecision$(new $Bigint.Precision$((byte)size));

      if (typeName.endsWith("UNSIGNED"))
        type.setUnsigned$(new $Integer.Unsigned$(true));

      if (_default != null)
        type.setDefault$(new $Bigint.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if (typeName.startsWith("BINARY")) {
      final $Binary type = newColumn($Binary.class);
      final Long length = getLength(typeName);
      if (length != null)
        type.setLength$(new $Binary.Length$(length.intValue()));

      column = type;
    }
    else if (typeName.startsWith("BLOB")) {
      final $Blob type = newColumn($Blob.class);
      final Long length = getLength(typeName);
      if (length != null)
        type.setLength$(new $Blob.Length$(length));

      column = type;
    }
    else if ("BOOLEAN".equals(typeName)) {
      final $Boolean type = newColumn($Boolean.class);
      if (_default != null)
        type.setDefault$(new $Boolean.Default$(Boolean.parseBoolean(_default)));

      column = type;
    }
    else if (typeName.startsWith("VARCHAR") || typeName.startsWith("CHARACTER")) {
      final $Char type = newColumn($Char.class);
      if (typeName.startsWith("VARCHAR"))
        type.setVarying$(new $Char.Varying$(true));

      final Long length = getLength(typeName);
      if (length != null)
        type.setLength$(new $Char.Length$(length.intValue()));

      if (_default != null)
        type.setDefault$(new $Char.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if (typeName.startsWith("TEXT")) {
      final $Clob type = newColumn($Clob.class);
      final Long length = getLength(typeName);
      if (length != null)
        type.setLength$(new $Clob.Length$(length));

      column = type;
    }
    else if ("DATE".equals(typeName)) {
      final $Date type = newColumn($Date.class);
      if (_default != null)
        type.setDefault$(new $Date.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("DATETIME".equals(typeName)) {
      final $Datetime type = newColumn($Datetime.class);
      if (size != 2000000000)
        type.setPrecision$(new $Datetime.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Datetime.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if (typeName.startsWith("DECIMAL")) {
      final $Decimal type = newColumn($Decimal.class);
      if (!typeName.equals("DECIMAL(15, 0)")) {
        final int open = typeName.indexOf('(');
        if (open > 0) {
          final int comma = typeName.indexOf(',', open + 1);
          if (comma > open) {
            final int close = typeName.indexOf(')', comma + 1);
            if (close > comma) {
              type.setPrecision$(new $Decimal.Precision$(Short.parseShort(typeName.substring(open + 1, comma).trim())));
              type.setScale$(new $Decimal.Scale$(Short.parseShort(typeName.substring(comma + 1, close).trim())));
            }
          }
        }
      }

      if (_default != null)
        type.setDefault$(new $Decimal.Default$(new BigDecimal(_default)));

      column = type;
    }
    else if ("DOUBLE".equals(typeName)) {
      final $Double type = newColumn($Double.class);
      if (_default != null)
        type.setDefault$(new $Double.Default$(Double.valueOf(_default)));

      column = type;
    }
//    else if ("ENUM".equals(typeName)) {
//      final $Enum type = newColumn($Enum.class);
//      if (_default != null)
//        type.Default$(new $Enum.Default$(_default));
//
//      column = type;
//    }
    else if ("FLOAT".equals(typeName)) {
      final $Float type = newColumn($Float.class);
      if (_default != null)
        type.setDefault$(new $Float.Default$(Float.valueOf(_default)));

      column = type;
    }
    else if (typeName.startsWith("INT") || typeName.startsWith("MEDIUMINT")) {
      final $Int type = newColumn($Int.class);
      if (size != 2000000000)
        type.setPrecision$(new $Int.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Int.Default$(new BigInteger(_default)));

      if ("INTEGER".equals(typeName))
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $Smallint type = newColumn($Smallint.class);
      if (size != 2000000000)
        type.setPrecision$(new $Smallint.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Smallint.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("TIME".equals(typeName)) {
      final $Time type = newColumn($Time.class);
      if (size != 2000000000)
        type.setPrecision$(new $Time.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Time.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("TINYINT".equals(typeName)) {
      final $Tinyint type = newColumn($Tinyint.class);
      if (size != 2000000000)
        type.setPrecision$(new $Tinyint.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Tinyint.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else {
      throw new UnsupportedOperationException("Unsupported column type: " + typeName);
    }

    column.setName$(new $Column.Name$(columnName));
    if (nullable != null && !nullable)
      column.setNull$(new $Column.Null$(false));

    return column;
  }

  @Override
  protected Map<String,List<$Constraints.Unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Map<String,List<$Check>> getCheckConstraints(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Map<String,$Table.Indexes> getIndexes(final Connection connection) throws SQLException {
    throw new UnsupportedOperationException();
  }
}
