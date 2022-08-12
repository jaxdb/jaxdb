package org.jaxdb.ddlx;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Tinyint;

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
  $Column makeColumn(final String columnName, final String typeName, final long size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $Column column;
    if (typeName.startsWith("BIGINT")) {
      final $Bigint type = newColumn($Bigint.class);
      if (size != 2000000000)
        type.setPrecision$(new $Bigint.Precision$((byte)size));

      // FIXME: Add min check?
      // if (typeName.endsWith("UNSIGNED"))
      //   type.setUnsigned$(new $Integer.Unsigned$(true));

      if (_default != null)
        type.setDefault$(new $Bigint.Default$(Long.valueOf(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Bigint.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if (typeName.startsWith("BINARY")) {
      final $Binary type = newColumn($Binary.class);
      final Long length = getLength(typeName);
      if (length != null)
        type.setLength$(new $Binary.Length$(length));

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
        type.setLength$(new $Char.Length$(length));

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
      if (!"DECIMAL(15,0)".equals(typeName)) {
        final int open = typeName.indexOf('(');
        if (open > 0) {
          final int comma = typeName.indexOf(',', open + 1);
          if (comma > open) {
            final int close = typeName.indexOf(')', comma + 1);
            if (close > comma) {
              type.setPrecision$(new $Decimal.Precision$(Integer.valueOf(typeName.substring(open + 1, comma).trim())));
              type.setScale$(new $Decimal.Scale$(Integer.valueOf(typeName.substring(comma + 1, close).trim())));
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
        type.setDefault$(new $Int.Default$(Integer.valueOf(_default)));

      if ("INTEGER".equals(typeName))
        type.setGenerateOnInsert$(new $Int.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $Smallint type = newColumn($Smallint.class);
      if (size != 2000000000)
        type.setPrecision$(new $Smallint.Precision$((byte)size));

      if (_default != null)
        type.setDefault$(new $Smallint.Default$(Short.valueOf(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Smallint.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

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
        type.setDefault$(new $Tinyint.Default$(Byte.valueOf(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Tinyint.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

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
  <L extends List<$Table.Constraints.Unique> & RandomAccess>Map<String,L> getUniqueConstraints(final Connection connection) {
    throw new UnsupportedOperationException();
  }

  @Override
  <L extends List<$CheckReference> & RandomAccess>Map<String,L> getCheckConstraints(final Connection connection) {
    throw new UnsupportedOperationException();
  }

  @Override
  Map<String,$Table.Indexes> getIndexes(final Connection connection) {
    throw new UnsupportedOperationException();
  }
}
