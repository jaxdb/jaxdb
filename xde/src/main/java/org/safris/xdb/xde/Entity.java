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
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.lang.reflect.Classes;
import org.safris.commons.sql.ConnectionProxy;

public abstract class Entity {
  private static void setValue(final PreparedStatement statement, final int index, final Object value, final int sqlType) throws SQLException {
    if (value == null)
      statement.setNull(index, sqlType);
    else if (sqlType == Types.BIGINT)
      statement.setObject(index, value);
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
    else if (sqlType == Types.INTEGER)
      statement.setInt(index, (Integer)value);
    else if (sqlType == Types.SMALLINT)
      statement.setShort(index, (Short)value);
    else if (sqlType == Types.TIME)
      statement.setTime(index, (Time)value);
    else if (sqlType == Types.TIMESTAMP)
      statement.setTimestamp(index, new Timestamp(((Date)value).getTime()));
    else if (sqlType == Types.VARCHAR)
      statement.setString(index, (String)value);
    else
      throw new IllegalArgumentException("Unknown type: " + sqlType);
  }

  private static ConnectionProxy getConnectionProxy(final Connection connection) {
    return connection instanceof ConnectionProxy ? (ConnectionProxy)connection : ConnectionProxy.getInstance(connection);
  }

  private static Map<Class<?>,Field[]> classToFields = new HashMap<Class<?>,Field[]>();

  private Field[] getDeclaredFields() {
    Field[] fields = classToFields.get(getClass());
    if (fields != null)
      return fields;

    synchronized (getClass()) {
      if ((fields = classToFields.get(getClass())) != null)
        return fields;

      classToFields.put(getClass(), fields = Classes.getDeclaredFieldsWithAnnotationDeep(getClass(), Column.class));
      return fields;
    }
  }


  /**
   * SELECT single row of column values for this Entity matching conditions of primary key(s)
   */
  public boolean select(final Connection connection) throws SQLException {
    try {
      final Table table = getClass().getAnnotation(Table.class);
      final Field[] fields = getDeclaredFields();
      String sql = "SELECT ";
      String columns = "";
      String where = "";
      for (final Field field : fields) {
        final Column column = field.getAnnotation(Column.class);
        if (column.primary())
          where += " AND " + column.name() + " = ?";
        else
          columns += ", " + column.name();
      }

      sql += columns.substring(2) + " FROM " + table.name() + " WHERE " + where.substring(5);
      final PreparedStatement statement = Entity.getConnectionProxy(connection).prepareStatement(sql);
      int index = 0;
      for (final Field field : fields) {
        field.setAccessible(true);
        final Column column = field.getAnnotation(Column.class);
        if (column.primary())
          setValue(statement, ++index, field.get(this), column.type());
      }

      System.err.println(statement.toString());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next())
        return false;

      index = 0;
      for (final Field field : fields) {
        final Column column = field.getAnnotation(Column.class);
        if (!column.primary())
          field.set(this, resultSet.getObject(++index, field.getType()));
      }

      return true;
    }
    catch (final IllegalAccessException e) {
      throw new Error(e);
    }
  }

  /**
   * INSERT single row of column values for this Entity
   */
  public int insert(final Connection connection) throws SQLException {
    try {
      final Table table = getClass().getAnnotation(Table.class);
      final Field[] fields = getDeclaredFields();
      String sql = "INSERT INTO " + table.name();
      String columns = "";
      String values = "";
      for (final Field field : fields) {
        final Column column = field.getAnnotation(Column.class);
        columns += ", " + column.name();
        values += ", ?";
      }

      sql += " (" + columns.substring(2) + ") VALUES (" + values.substring(2) + ")";
      final PreparedStatement statement = Entity.getConnectionProxy(connection).prepareStatement(sql);
      for (int i = 0; i < fields.length;) {
        final Field field = fields[i++];
        field.setAccessible(true);
        final Column column = field.getAnnotation(Column.class);
        setValue(statement, i, field.get(this), column.type());
      }

      System.err.println(statement.toString());
      return statement.executeUpdate();
    }
    catch (final IllegalAccessException e) {
      throw new Error(e);
    }
  }

  /**
   * UPDATE column values for this Entity matching conditions of primary key(s)
   */
  public int update(final Connection connection) throws SQLException {
    try {
      final Table table = getClass().getAnnotation(Table.class);
      final Field[] fields = getDeclaredFields();
      String sql = "UPDATE " + table.name() + " SET ";
      String columns = "";
      String where = "";
      for (final Field field : fields) {
        final Column column = field.getAnnotation(Column.class);
        if (column.primary())
          where += " AND " + column.name() + " = ?";
        else
          columns += ", " + column.name() + " = ?";
      }

      sql += columns.substring(2) + " WHERE " + where.substring(5);
      final PreparedStatement statement = Entity.getConnectionProxy(connection).prepareStatement(sql);
      // set the updated columns first
      int index = 0;
      for (final Field field : fields) {
        field.setAccessible(true);
        final Column column = field.getAnnotation(Column.class);
        if (!column.primary())
          setValue(statement, ++index, field.get(this), column.type());
      }

      // then the conditional columns
      for (final Field field : fields) {
        final Column column = field.getAnnotation(Column.class);
        if (column.primary())
          setValue(statement, ++index, field.get(this), column.type());
      }

      System.err.println(statement.toString());
      return statement.executeUpdate();
    }
    catch (final IllegalAccessException e) {
      throw new Error(e);
    }
  }
}