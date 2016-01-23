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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.xde.csql.Entity;
import org.safris.xdb.xdl.DBVendor;

public abstract class Column<T> extends cSQL<T> implements Cloneable, Entity {
  private static final Map<Type,Method> typeToSetter = new HashMap<Type,Method>();

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemPackageLoader().loadPackage(Column.class.getPackage().getName() + ".column");
      for (final Class<?> cls : classes) {
        final Method[] methods = cls.getDeclaredMethods();
        for (final Method method : methods) {
          if (Modifier.isStatic(method.getModifiers()) && "set".equals(method.getName())) {
            typeToSetter.put(cls == org.safris.xdb.xde.column.Enum.class ? Enum.class : Classes.getGenericSuperclasses(cls)[0], method);
            method.setAccessible(true);
            continue;
          }
        }
      }
    }
    catch (final Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static void set(final PreparedStatement statement, final int parameterIndex, final Class<?> type, final Object value) {
    try {
      typeToSetter.get(type.isEnum() ? Enum.class : type).invoke(null, statement, parameterIndex, value);
    }
    catch (final IllegalAccessException e) {
      throw new Error(e);
    }
    catch (final InvocationTargetException e) {
      throw new Error(e);
    }
  }

  protected final int sqlType;
  protected final Class<T> type;
  protected final Table owner;
  protected final String csqlName;
  protected final String name;
  protected final boolean unique;
  protected final boolean primary;
  protected final boolean nullable;
  protected final GenerateOn<T> generateOnInsert;
  protected final GenerateOn<T> generateOnUpdate;

  protected Column(final int sqlType, final Class<T> type, final Table owner, final String csqlName, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<T> generateOnInsert, final GenerateOn<T> generateOnUpdate) {
    this.sqlType = sqlType;
    this.type = type;
    this.owner = owner;
    this.csqlName = csqlName;
    this.name = name;
    this.value = _default;
    this.unique = unique;
    this.primary = primary;
    this.nullable = nullable;
    this.generateOnInsert = generateOnInsert;
    this.generateOnUpdate = generateOnUpdate;
  }

  protected Column(final Column<T> column) {
    this(column.sqlType, column.type, column.owner, column.csqlName, column.name, column.value, column.unique, column.primary, column.nullable, column.generateOnInsert, column.generateOnUpdate);
    this.value = column.value;
  }

  private boolean wasSet = false;

  protected boolean wasSet() {
    return wasSet;
  }

  private T value;

  public T set(final T value) {
    this.wasSet = true;
    return this.value = value;
  }

  public T get() {
    return value;
  }

  protected cSQL<?> parent() {
    return owner;
  }

  protected void serialize(final Serialization serialization) {
    if (serialization.statementType == PreparedStatement.class) {
      if (tableAlias(owner, false) == null) {
        serialization.addParameter(get());
        serialization.sql.append(getPreparedStatementMark(serialization.vendor));
      }
      else {
        serialization.sql.append(toString());
      }
    }
    else if (serialization.statementType == Statement.class) {
      serialization.sql.append(toString());
    }
    else {
      throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
    }
  }

  protected abstract String getPreparedStatementMark(final DBVendor vendor);
  protected abstract void set(final PreparedStatement statement, final int parameterIndex) throws SQLException;
  protected abstract T get(final ResultSet resultSet, final int columnIndex) throws SQLException;

  public Column<T> clone() {
    try {
      final Constructor<Column<T>> constructor = (Constructor<Column<T>>)getClass().getDeclaredConstructor(getClass());
      constructor.setAccessible(true);
      return (Column<T>)constructor.newInstance(this);
    }
    catch (final InstantiationException e) {
      throw new Error(e);
    }
    catch (final ReflectiveOperationException e) {
      throw new Error(e);
    }
  }

  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (obj.getClass() != getClass())
      return false;

    final T get = get();
    return get != null ? get.equals(((Column<?>)obj).get()) : ((Column<?>)obj).get() == null;
  }

  public String toString() {
    final String alias = cSQL.tableAlias(owner, false);
    return alias != null ? alias + "." + name : String.valueOf(get());
  }
}