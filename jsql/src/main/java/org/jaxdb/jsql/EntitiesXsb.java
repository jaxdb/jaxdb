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
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxdb.sqlx.SQL;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Binary;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Blob;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Clob;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Date;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Datetime;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Enum;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Time;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Row;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.Id;
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

final class EntitiesXsb {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private static data.Table toEntity(final $Database database, final $Row row) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    // FIXME: This is brittle... Need to modularize it and make it clearer:
    final Class<?> binding = Class.forName(Entities.class.getPackage().getName() + "." + Identifiers.toInstanceCase(database.id()) + "$" + Identifiers.toClassCase(row.id()));
    final data.Table table = (data.Table)binding.getDeclaredConstructor().newInstance();
    for (final Method method : Classes.getDeclaredMethodsWithAnnotationDeep(row.getClass(), Id.class)) {
      if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
        continue;

      final $AnySimpleType Type = ($AnySimpleType)method.invoke(row);
      if (Type == null)
        continue;

      final Class<? extends $AnySimpleType> returnType = (Class<? extends $AnySimpleType>)method.getReturnType();
      final String id = returnType.getAnnotation(Id.class).value();
      final int d1 = id.indexOf('-');
      final int d2 = id.indexOf('-', d1 + 1);
      final Field field = binding.getField(Identifiers.toCamelCase(d2 > -1 ? id.substring(d1 + 1, d2) : id.substring(d1 + 1)));
      final data.Column column = (data.Column<?>)field.get(table);

      final Object value = Type.text();
      if (value == null)
        column.set(null);
      else if ($Binary.class.isAssignableFrom(returnType))
        column.set(((HexBinary)value).getBytes());
      else if ($Blob.class.isAssignableFrom(returnType))
        column.set(new ByteArrayInputStream(((HexBinary)value).getBytes()));
      else if ($Clob.class.isAssignableFrom(returnType))
        column.set(new StringReader((String)value));
      else if ($Date.class.isAssignableFrom(returnType))
        column.set(LocalDate.parse((String)value));
      else if ($Datetime.class.isAssignableFrom(returnType))
        column.set(LocalDateTime.parse((String)value));
      else if ($Time.class.isAssignableFrom(returnType))
        column.set(LocalTime.parse((String)value));
      else if ($Enum.class.isAssignableFrom(returnType)) {
        for (final Object constant : column.type().getEnumConstants()) {
          if (constant.toString().equals(value)) {
            column.set(constant);
            break;
          }
        }

        if (!column.wasSet())
          throw new IllegalArgumentException("'" + value + "' is not a valid value for " + column.name);
      }
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

      final List<data.Table> entities = new ArrayList<>();
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

  private EntitiesXsb() {
  }
}