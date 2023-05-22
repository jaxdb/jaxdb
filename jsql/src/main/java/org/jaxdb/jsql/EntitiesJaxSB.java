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
import java.lang.reflect.Constructor;
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
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

final class EntitiesJaxSB {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private static data.Table toEntity(final Schema schema, final $Row row) throws IllegalAccessException, InvocationTargetException {
    final String rowId = row.id();
    final String tableName = rowId.substring(0, rowId.lastIndexOf('-'));
    final data.Table table = schema.getTable(tableName).newInstance();

    for (final Method method : Classes.getDeclaredMethodsDeep(row.getClass())) { // [A]
      final Class<?> returnType = method.getReturnType();
      if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(returnType))
        continue;

      final Id id = returnType.getAnnotation(Id.class);
      if (id == null)
        continue;

      final $AnySimpleType type = ($AnySimpleType)method.invoke(row);
      if (type == null)
        continue;

      final String idValue = id.value();
      final int d1 = idValue.indexOf('-');
      final int d2 = idValue.indexOf('-', d1 + 1);

      final data.Column column = table.getColumn(d2 > -1 ? idValue.substring(d1 + 1, d2) : idValue.substring(d1 + 1));

      final Class<? extends $AnySimpleType> simpleReturnType = (Class<? extends $AnySimpleType>)returnType;
      final Object value = type.text();
      if (value == null)
        column.set(null);
      else if ($Binary.class.isAssignableFrom(simpleReturnType))
        column.set(((HexBinary)value).getBytes());
      else if ($Blob.class.isAssignableFrom(simpleReturnType))
        column.set(new ByteArrayInputStream(((HexBinary)value).getBytes()));
      else if (value instanceof String)
        column.set(column.parseString(null, (String)value)); // FIXME: Setting vendor to null here... need to review this pattern
      else
        column.set(value);
    }

    return table;
  }

  public static data.Table[] toEntities(final $Database database) {
    try {
      final Iterator<$Row> iterator = SQL.newRowIterator(database);
      if (!iterator.hasNext())
        return new data.Table[0];

      final Constructor<?> constructor = Class.forName(Entities.class.getPackage().getName() + "." + Identifiers.toClassCase(database.id())).getDeclaredConstructor();
      constructor.setAccessible(true);
      final Schema schema = (Schema)constructor.newInstance();
      final List<data.Table> entities = new ArrayList<>();
      while (iterator.hasNext())
        entities.add(toEntity(schema, iterator.next()));

      return entities.toArray(new data.Table[entities.size()]);
    }
    catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
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