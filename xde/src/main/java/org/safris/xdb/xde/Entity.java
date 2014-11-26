/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.safris.xdb.xdl.DBVendor;
import org.safris.xdb.xdl.DDL;

public abstract class Entity {
  protected Entity(final Column<?>[] column, final Column<?>[] primary) {
  }

  protected Entity(final Entity entity) {
  }

  protected Entity() {
  }

  private static void setValue(final PreparedStatement statement, final int index, final Object value, final int sqlType) throws SQLException {
    if (value == null)
      statement.setNull(index, sqlType);
    else if (sqlType == Types.BLOB)
      statement.setBinaryStream(index, new ByteArrayInputStream((byte[])value));
    else if (sqlType == Types.BOOLEAN)
      statement.setBoolean(index, (Boolean)value);
    else if (sqlType == Types.CHAR || value.getClass().isEnum())
      statement.setString(index, value.toString());
    else if (sqlType == Types.DATE)
      statement.setDate(index, (Date)value);
    else if (sqlType == Types.DECIMAL || sqlType == Types.DOUBLE)
      statement.setDouble(index, (Double)value);
    else if (sqlType == Types.FLOAT)
      statement.setFloat(index, (Float)value);
    else if (sqlType == Types.TINYINT)
      statement.setShort(index, (Short)value);
    else if (sqlType == Types.SMALLINT)
      statement.setInt(index, (Integer)value);
    else if (sqlType == Types.INTEGER)
      statement.setLong(index, (Long)value);
    else if (sqlType == Types.BIGINT)
      statement.setObject(index, value);
    else if (sqlType == Types.TIME)
      statement.setTime(index, (Time)value);
    else if (sqlType == Types.TIMESTAMP)
      statement.setTimestamp(index, (Timestamp)value);
    else if (sqlType == Types.VARCHAR)
      statement.setString(index, (String)value);
    else
      throw new IllegalArgumentException("Unknown type: " + sqlType);
  }

  /**
   * SELECT single row of column values for this Entity matching conditions of primary key(s)
   */
  public boolean select() throws SQLException {
    final Column<?>[] columns = column();
    String sql = "SELECT ";
    String select = "";
    String where = "";
    for (final Column<?> column : columns)
      if (column.primary)
        where += " AND " + column.name + " = ?";
      else
        select += ", " + column.name;

    sql += select.substring(2) + " FROM " + name() + " WHERE " + where.substring(5);
    final PreparedStatement statement = Schema.getConnection(schema()).prepareStatement(sql);
    int index = 0;
    for (final Column<?> column : columns)
      if (column.primary)
        setValue(statement, ++index, column.get(), column.sqlType);

    System.err.println(statement.toString());
    final ResultSet resultSet = statement.executeQuery();
    if (!resultSet.next())
      return false;

    index = 0;
    for (final Column column : columns)
      if (!column.primary)
        if (column.type.isEnum())
          column.set(Enum.valueOf(column.type, (String)resultSet.getObject(++index, String.class)));
        else if (column.type == BigInteger.class) {
          final Object value = resultSet.getObject(++index);
          column.set(value instanceof BigInteger ? value : value instanceof Long ? BigInteger.valueOf((Long)value) : new BigInteger(String.valueOf(value)));
        }
        else
          column.set(resultSet.getObject(++index, column.type));

    return true;
  }

  /**
   * INSERT single row of column values for this Entity
   */
  public int insert() throws SQLException {
    String sql = "INSERT INTO " + name();
    String columns = "";
    String values = "";
    for (final Column<?> column : column()) {
      columns += ", " + column.name;
      values += ", ?";
    }

    sql += " (" + columns.substring(2) + ") VALUES (" + values.substring(2) + ")";
    final PreparedStatement statement = Schema.getConnection(schema()).prepareStatement(sql);
    for (int i = 0; i < column().length;) {
      final Column<?> column = column()[i++];
      setValue(statement, i, column.get(), column.sqlType);
    }

    System.err.println(statement.toString());
    return statement.executeUpdate();
  }

  /**
   * UPDATE column values for this Entity matching conditions of primary key(s)
   */
  public int update() throws SQLException {
    String sql = "UPDATE " + name() + " SET ";
    String columns = "";
    String where = "";
    for (final Column<?> column : column())
      if (column.primary)
        where += " AND " + column.name + " = ?";
      else
        columns += ", " + column.name + " = ?";

    sql += columns.substring(2) + " WHERE " + where.substring(5);
    final PreparedStatement statement = Schema.getConnection(schema()).prepareStatement(sql);
    // set the updated columns first
    int index = 0;
    for (final Column<?> column : column())
      if (!column.primary)
        setValue(statement, ++index, column.get(), column.sqlType);

    // then the conditional columns
    for (final Column<?> column : column())
      if (column.primary)
        setValue(statement, ++index, column.get(), column.sqlType);

    System.err.println(statement.toString());
    return statement.executeUpdate();
  }

  protected abstract String name();
  protected abstract Column<?>[] column();
  protected abstract Column<?>[] primary();
  protected abstract DDL[] ddl();

  protected Class<? extends Schema> schema() {
    return (Class<? extends Schema>)getClass().getEnclosingClass();
  }

  public String[] ddl(final DBVendor vendor, final DDL.Type type) {
    return ddl()[vendor.ordinal()].get(type);
  }
}