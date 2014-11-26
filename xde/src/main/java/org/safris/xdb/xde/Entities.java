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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.safris.commons.lang.Numbers;

public final class Entities {
  private static List<Column<?>> getSelection(final String string, final Map<String,Class<? extends Entity>> aliases) {
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

  private static Map<String,Class<? extends Entity>> getAliases(final String string) {
    try {
      final Map<String,Class<? extends Entity>> aliases = new HashMap<String,Class<? extends Entity>>();

      final StringTokenizer tokenizer = new StringTokenizer(string, " \t\n\r\f,()");
      final List<String> selection = new ArrayList<String>();
      boolean isInFrom = false;

      Class<? extends Entity> entityClass = null;
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
          entityClass = (Class<? extends Entity>)Class.forName("xdb.xde." + token.replace('.', '$'));
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

  private static String listColumns(final Class<? extends Entity> entity, final String alias) {
    final Entity identity = identity(entity);
    String out = "";
    for (final Column<?> column : identity.column())
      out += ", " + alias + "." + column.name;

    return out.substring(2);
  }

  private static final Entity identity(final Class<? extends Entity> entity) {
    try {
      if (entity == null) {
        int i = 0;
      }
      final Field field = entity.getDeclaredField("identity");
      field.setAccessible(true);
      return (Entity)field.get(null);
    }
    catch (final Exception e) {
      throw new Error(e);
    }
  }

  private static String transform(final String string, final Map<String,Class<? extends Entity>> aliases) {
    try {
      final String delims = " \t\n\r\f(),";
      final StringTokenizer tokenizer = new StringTokenizer(string, delims, true);
      Zone zone = null;
      String out = "";
      Class<? extends Entity> entityClass = null;
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

          final Class<? extends Entity> entity = aliases.get(token);
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
            entityClass = (Class<? extends Entity>)Class.forName("xdb.xde." + token.replace('.', '$'));
            final Entity identity = identity(entityClass);
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
          final Class<? extends Entity> entity = aliases.get(alias);
          final Entity identity = identity(entity);
          for (final Column<?> col : identity.column()) {
            if (col.cqlName.equals(field)) {
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

  public static List<Entity[]> executeQuery(final String string) throws SQLException {
    final Map<String,Class<? extends Entity>> aliases = getAliases(string);
    final List<Column<?>> columns = getSelection(string, aliases);
    Entity lastIdentity = null;
    int numEntities = 0;
    for (final Column<?> column : columns) {
      if (lastIdentity == null || lastIdentity != column.owner) {
        ++numEntities;
      }

      lastIdentity = column.owner;
    }

    final Connection connection = Schema.getConnection((Class<? extends Schema>)aliases.values().iterator().next().getEnclosingClass());
    final Statement statement = connection.createStatement();
    final String sql = transform(string, aliases);
    System.err.println(sql);
    final ResultSet resultSet = statement.executeQuery(sql);
    int index = 0;
    int colCount = 0;
    Entity current = null;
    final List<Entity[]> entities = new ArrayList<Entity[]>();
    try {
      while (resultSet.next()) {
        int cursor = 0;
        final Entity[] row = new Entity[numEntities];
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

    return entities;
  }

  public static int select(final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      if (e.select())
        ++count;

    return count;
  }

  public static int select(final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      if (e.select())
        ++count;

    return count;
  }

  public static int insert(final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      count += e.insert();

    return count;
  }

  public static int insert(final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      count += e.insert();

    return count;
  }

  public static int update(final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      count += e.update();

    return count;
  }

  public static int update(final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      count += e.update();

    return count;
  }

  private Entities() {
  }
}