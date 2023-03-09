/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb.jsql;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxdb.sqlx.SQL;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Binary;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Blob;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Row;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.Id;
import org.libj.io.SerializableInputStream;
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

final class EntitiesJaxSB {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private static data.Table toEntity(final $Database database, final $Row row) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    // FIXME: This is brittle... Need to modularize it and make it clearer:
    final String tableName = row.id().substring(0, row.id().lastIndexOf('-'));
    final Class<?> binding = Class.forName(Entities.class.getPackage().getName() + "." + Identifiers.toInstanceCase(database.id()) + "$" + Identifiers.toClassCase(tableName));
    final data.Table table = (data.Table)binding.getDeclaredConstructor().newInstance();
    for (final Method method : Classes.getDeclaredMethodsDeep(row.getClass())) { // [A]
      if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
        continue;

      final Id id = method.getReturnType().getAnnotation(Id.class);
      if (id == null)
        continue;

      final $AnySimpleType type = ($AnySimpleType)method.invoke(row);
      if (type == null)
        continue;

      final String idValue = id.value();
      final int d1 = idValue.indexOf('-');
      final int d2 = idValue.indexOf('-', d1 + 1);
      final Field field = binding.getField(Identifiers.toCamelCase(d2 > -1 ? idValue.substring(d1 + 1, d2) : idValue.substring(d1 + 1)));
      final data.Column column = (data.Column<?>)field.get(table);

      final Class<? extends $AnySimpleType> returnType = (Class<? extends $AnySimpleType>)method.getReturnType();
      final Serializable value = (Serializable)type.text(); // FIXME: Should all xsb text be forced as serializable?
      if (value == null)
        column.set(null);
      else if ($Binary.class.isAssignableFrom(returnType))
        column.set(((HexBinary)value).getBytes());
      else if ($Blob.class.isAssignableFrom(returnType))
        column.set(new SerializableInputStream(new ByteArrayInputStream(((HexBinary)value).getBytes())));
      else if (value instanceof String)
        column.set(column.parseString(null, (String)value)); // FIXME: Setting vendor to null here... need to review this pattern
      else
        column.set(value);
    }

    return table;
  }

  public static data.Table<?>[] toEntities(final $Database database) {
    try {
      final Iterator<$Row> iterator = SQL.newRowIterator(database);
      if (!iterator.hasNext())
        return new data.Table[0];

      final List<data.Table<?>> entities = new ArrayList<>();
      while (iterator.hasNext())
        entities.add(toEntity(database, iterator.next()));

      return entities.toArray(new data.Table[entities.size()]);
    }
    catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  private EntitiesJaxSB() {
  }
}