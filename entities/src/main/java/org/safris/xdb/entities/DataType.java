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

package org.safris.xdb.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.entities.datatype.Char;
import org.safris.xdb.schema.DBVendor;

public abstract class DataType<T> extends Variable<T> implements Cloneable {
  private static final Map<Type,Method> typeToGetter = new HashMap<Type,Method>();
  private static final Map<Type,Method> typeToSetter = new HashMap<Type,Method>();

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemPackageLoader().loadPackage(Char.class.getPackage());
      if (classes.size() == 0)
        throw new ExceptionInInitializerError("No classes found, wrong package?");

      for (final Class<?> cls : classes) {
        final Method[] methods = cls.getDeclaredMethods();
        for (final Method method : methods) {
          if (Modifier.isStatic(method.getModifiers())) {
            if ("get".equals(method.getName())) {
              typeToGetter.put(cls == org.safris.xdb.entities.datatype.Enum.class ? Enum.class : Classes.getGenericSuperclasses(cls)[0], method);
              method.setAccessible(true);
              continue;
            }

            if ("set".equals(method.getName())) {
              typeToSetter.put(cls == org.safris.xdb.entities.datatype.Enum.class ? Enum.class : Classes.getGenericSuperclasses(cls)[0], method);
              method.setAccessible(true);
              continue;
            }
          }
        }
      }
    }
    catch (final Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static <T>boolean canGet(final Class<T> type) {
    return (type.isEnum() ? typeToGetter.get(Enum.class) : typeToGetter.get(type)) != null;
  }

  @SuppressWarnings("unchecked")
  protected static <T>T get(final Class<T> type, final ResultSet resultSet, final int columnIndex) {
    try {
      return type.isEnum() ? (T)typeToGetter.get(Enum.class).invoke(null, resultSet, columnIndex, type) : (T)typeToGetter.get(type).invoke(null, resultSet, columnIndex);
    }
    catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  protected static <T>boolean canSet(final Class<T> type) {
    return (type.isEnum() ? typeToSetter.get(Enum.class) : typeToSetter.get(type)) != null;
  }

  protected static <T>void set(final PreparedStatement statement, final int parameterIndex, final Class<T> type, final T value) {
    try {
      typeToSetter.get(type.isEnum() ? Enum.class : type).invoke(null, statement, parameterIndex, value);
    }
    catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  protected final int sqlType;
  protected final Class<T> type;
  protected final Entity entity;
  protected final String specName;
  protected final String name;
  protected final boolean unique;
  protected final boolean primary;
  protected final boolean nullable;
  protected final GenerateOn<? super T> generateOnInsert;
  protected final GenerateOn<? super T> generateOnUpdate;

  protected DataType(final int sqlType, final Class<T> type, final Entity owner, final String specName, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
    super(_default);
    this.sqlType = sqlType;
    this.type = type;
    this.entity = owner;
    this.specName = specName;
    this.name = name;
    this.unique = unique;
    this.primary = primary;
    this.nullable = nullable;
    this.generateOnInsert = generateOnInsert;
    this.generateOnUpdate = generateOnUpdate;
  }

  protected DataType(final DataType<T> dataType) {
    this(dataType.sqlType, dataType.type, dataType.entity, dataType.specName, dataType.name, dataType.get(), dataType.unique, dataType.primary, dataType.nullable, dataType.generateOnInsert, dataType.generateOnUpdate);
  }

  protected DataType() {
    this(0, null, null, null, null, null, false, false, false, null, null);
  }

  @Override
  protected Entity owner() {
    return entity;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    if (serialization.statementType == PreparedStatement.class) {
      if (Entity.tableAlias(entity, false) == null) {
        serialization.addParameter(this);
        serialization.sql.append(getPreparedStatementMark(serialization.vendor));
      }
      else if (serialization.getType() != Select.class) {
        serialization.sql.append(name);
      }
      else {
        serialization.sql.append(toString());
      }
    }
    else if (serialization.statementType == Statement.class) {
      final String alias = Entity.tableAlias(entity, false);
      serialization.sql.append(alias == null ? String.valueOf(get()) : serialization.getType() == Select.class ? alias + "." + name : name);
    }
    else {
      throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
    }
  }

  protected abstract String getPreparedStatementMark(final DBVendor vendor);

  @Override
  @SuppressWarnings("unchecked")
  public DataType<T> clone() {
    try {
      final Constructor<DataType<T>> constructor = (Constructor<DataType<T>>)getClass().getDeclaredConstructor(getClass());
      constructor.setAccessible(true);
      return constructor.newInstance(this);
    }
    catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (obj.getClass() != getClass())
      return false;

    final T get = get();
    return get != null ? get.equals(((DataType<?>)obj).get()) : ((DataType<?>)obj).get() == null;
  }

  @Override
  public String toString() {
    final String alias = Entity.tableAlias(entity, false);
    return alias != null ? alias + "." + name : String.valueOf(get());
  }
}