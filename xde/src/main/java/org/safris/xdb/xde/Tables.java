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

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.safris.commons.lang.Numbers;

public final class Tables {
  private static List<Column<?>> getSelection(final String string, final Map<String,Class<? extends Table>> aliases) {
    final StringTokenizer tokenizer = new StringTokenizer(string, " \t\n\r\f,");
    final List<String> selection = new ArrayList<String>();
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      if ("SELECT".equalsIgnoreCase(token) || "ALL".equalsIgnoreCase(token) || "DISTINCT".equalsIgnoreCase(token))
        continue;

      if ("FROM".equalsIgnoreCase(token))
        break;

      selection.add(token);
    }

    final List<Column<?>> columns = new ArrayList<Column<?>>(selection.size());
    for (final String select : selection)
      for (final Column<?> column : identity(aliases.get(select)).column())
        columns.add(column);

    return columns;
  }

  private static Map<String,Class<? extends Table>> getAliases(final String string) {
    try {
      final Map<String,Class<? extends Table>> aliases = new HashMap<String,Class<? extends Table>>();

      final StringTokenizer tokenizer = new StringTokenizer(string, " \t\n\r\f,()");
      final List<String> selection = new ArrayList<String>();
      boolean isInFrom = false;

      Class<? extends Table> entityClass = null;
      while (tokenizer.hasMoreTokens()) {
        final String token = tokenizer.nextToken();
        if (!isInFrom) {
          if ("FROM".equalsIgnoreCase(token) || "JOIN".equalsIgnoreCase(token))
            isInFrom = true;

          continue;
        }

        if ("LEFT".equalsIgnoreCase(token) || "RIGHT".equalsIgnoreCase(token) || "INNER".equalsIgnoreCase(token) || "OUTER".equalsIgnoreCase(token) || "ON".equalsIgnoreCase(token)) {
          isInFrom = false;
          continue;
        }

        if ("WHERE".equalsIgnoreCase(token))
          break;

        if (entityClass == null) {
          entityClass = (Class<? extends Table>)Class.forName("xdb.xde." + token.replace('.', '$'));
        }
        else {
          aliases.put(token, entityClass);
          entityClass = null;
        }
      }

      return aliases;
    }
    catch (final ClassNotFoundException e) {
      throw new Error(e);
    }
  }

  private static enum Zone {
    ALIAS,
    ENTITY,
    COLUMN,
  }

  protected static String listColumns(final Class<? extends Table> entity, final String alias) {
    final Table identity = identity(entity);
    String out = "";
    for (final Column<?> column : identity.column())
      out += ", " + alias + "." + column.name;

    return out.substring(2);
  }

  private static final Table identity(final Class<? extends Table> entity) {
    try {
      final Field field = entity.getDeclaredField("identity");
      field.setAccessible(true);
      return (Table)field.get(null);
    }
    catch (final Exception e) {
      throw new Error(e);
    }
  }

  private static String transform(final String string, final Map<String,Class<? extends Table>> aliases) {
    try {
      final String delims = " \t\n\r\f(),";
      final StringTokenizer tokenizer = new StringTokenizer(string, delims, true);
      Zone zone = null;
      String out = "";
      Class<? extends Table> entityClass = null;
      while (tokenizer.hasMoreTokens()) {
        final String token = tokenizer.nextToken();
        if (token.length() == 1 && delims.contains(token)) {
          out += token;
          continue;
        }

        if (zone == null) {
          if ("SELECT".equalsIgnoreCase(token))
            zone = Zone.ALIAS;

          out += token;
          continue;
        }

        if (zone == Zone.ALIAS) {
          if ("ALL".equalsIgnoreCase(token) || "DISTINCT".equalsIgnoreCase(token)) {
            out += token;
            continue;
          }

          if ("FROM".equalsIgnoreCase(token)) {
            out += token;
            zone = Zone.ENTITY;
            continue;
          }

          final Class<? extends Table> entity = aliases.get(token);
          if (entity == null)
            throw new Error("Unknown entity for: " + token);

          out += listColumns(entity, token);
        }
        else if (zone == Zone.ENTITY) {
          if ("LEFT".equalsIgnoreCase(token) || "OUTER".equalsIgnoreCase(token) || "INNER".equalsIgnoreCase(token) || "JOIN".equalsIgnoreCase(token)) {
            out += token;
            continue;
          }

          if ("ON".equalsIgnoreCase(token) || "WHERE".equalsIgnoreCase(token)) {
            out += token;
            zone = Zone.COLUMN;
            continue;
          }

          if (entityClass == null) {
            entityClass = (Class<? extends Table>)Class.forName("xdb.xde." + token.replace('.', '$'));
            final Table identity = identity(entityClass);
            out += identity.name();
          }
          else {
            entityClass = null;
            out += token;
          }

          continue;
        }
        else if (zone == Zone.COLUMN) {
          int dot = token.indexOf('.');
          if (dot < 0 || Numbers.isNumber(token) || token.startsWith("'") || token.startsWith("\"")) {
            out += token;
            continue;
          }

          final String alias = token.substring(0, dot);
          final String field = token.substring(dot + 1);
          final Class<? extends Table> entity = aliases.get(alias);
          final Table identity = identity(entity);
          for (final Column<?> col : identity.column()) {
            if (col.csqlName.equals(field)) {
              out += alias + "." + col.name;
              break;
            }
          }
        }
      }

      return out;
    }
    catch (final ClassNotFoundException e) {
      throw new Error(e);
    }
  }

  public static <T extends cSQL>List<T[]> executeQuery(final String string) throws SQLException {
    final Map<String,Class<? extends Table>> aliases = getAliases(string);
    final List<Column<?>> columns = getSelection(string, aliases);
    Table lastIdentity = null;
    int numEntities = 0;
    for (final Column<?> column : columns) {
      if (lastIdentity == null || lastIdentity != column.owner) {
        ++numEntities;
      }

      lastIdentity = column.owner;
    }

    final String sql = transform(string, aliases);
    System.err.println(sql);
    try (
      final Connection connection = Schema.getConnection((Class<? extends Schema>)aliases.values().iterator().next().getEnclosingClass());
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery(sql);
    ) {
      int index = 0;
      int colCount = 0;
      Table current = null;
      final List entities = new ArrayList();
      try {
        while (resultSet.next()) {
          int cursor = 0;
          final Table[] row = new Table[numEntities];
          entities.add(row);
          for (final Column<?> column : columns) {
            if (current == null || lastIdentity == null || lastIdentity != column.owner) {
              row[cursor++] = current = column.owner.getClass().newInstance();
              colCount = 0;
            }

            // FIXME: Copy of code in Entity.select()
            final Column col = ((Column<Object>)current.column()[colCount++]);
            if (col.type.isEnum())
              col.set(Enum.valueOf(col.type, (String)resultSet.getObject(++index, String.class)));
            else if (((Column<?>)col).type == BigInteger.class) {
              final Object value = resultSet.getObject(++index);
              col.set(value instanceof BigInteger ? value : value instanceof Long ? BigInteger.valueOf((Long)value) : new BigInteger(String.valueOf(value)));
            }
            else
              col.set(resultSet.getObject(++index, col.type));

            lastIdentity = column.owner;
          }
        }
      }
      catch (final Exception e) {
        throw new Error(e);
      }

      return (List<T[]>)entities;
    }
  }

  /*public static int select(final Table ... entity) throws SQLException {
    int count = 0;
    for (final Table e : entity)
      if (e.select())
        ++count;

    return count;
  }

  public static int select(final Collection<Table> entities) throws SQLException {
    int count = 0;
    for (final Table e : entities)
      if (e.select())
        ++count;

    return count;
  }

  public static int insert(final Table ... entity) throws SQLException {
    int count = 0;
    for (final Table e : entity)
      count += e.insert();

    return count;
  }

  public static int insert(final Collection<Table> entities) throws SQLException {
    int count = 0;
    for (final Table e : entities)
      count += e.insert();

    return count;
  }

  public static int update(final Table ... entity) throws SQLException {
    int count = 0;
    for (final Table e : entity)
      count += e.update();

    return count;
  }

  public static int update(final Collection<Table> entities) throws SQLException {
    int count = 0;
    for (final Table e : entities)
      count += e.update();

    return count;
  }*/

  private Tables() {
  }
}