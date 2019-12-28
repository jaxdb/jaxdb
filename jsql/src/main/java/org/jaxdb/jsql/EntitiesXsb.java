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
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxdb.jsql.kind.Numeric;
import org.jaxdb.sqlx.SQL;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Bigint;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Binary;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Blob;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Clob;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Date;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Datetime;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Enum;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Int;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Smallint;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Time;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Tinyint;
import org.jaxdb.www.sqlx_0_4.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_4.xLygluGCXAA.$Row;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.Id;
import org.libj.lang.Identifiers;
import org.libj.util.Classes;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

final class EntitiesXsb {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private static type.Entity toEntity(final $Database database, final $Row row) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    // FIXME: This is brittle... Need to modularize it and make it clearer:
    final Class<?> binding = Class.forName(Entities.class.getPackage().getName() + "." + Identifiers.toInstanceCase(database.id()) + "$" + Identifiers.toClassCase(row.id()));
    final type.Entity entity = (type.Entity)binding.getDeclaredConstructor().newInstance();
    for (final Method method : Classes.getDeclaredMethodsWithAnnotationDeep(row.getClass(), Id.class)) {
      if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
        continue;

      final Class<? extends $AnySimpleType> type = (Class<? extends $AnySimpleType>)method.getReturnType();
      final Id id = type.getAnnotation(Id.class);
      final $AnySimpleType column = ($AnySimpleType)method.invoke(row);
      if (column == null)
        continue;

      final Field field = binding.getField(Identifiers.toCamelCase(id.value().substring(id.value().indexOf('-') + 1)));
      final type.DataType dataType = (type.DataType<?>)field.get(entity);

      final Object value = column.text();
      if (value == null)
        dataType.set(null);
      else if ($Bigint.class.isAssignableFrom(type))
        dataType.set(dataType instanceof Numeric.UNSIGNED ? (BigInteger)value : ((BigInteger)value).longValue());
      else if ($Binary.class.isAssignableFrom(type))
        dataType.set(((HexBinary)value).getBytes());
      else if ($Blob.class.isAssignableFrom(type))
        dataType.set(new ByteArrayInputStream(((HexBinary)value).getBytes()));
      else if ($Clob.class.isAssignableFrom(type))
        dataType.set(new StringReader((String)value));
      else if ($Date.class.isAssignableFrom(type))
        dataType.set(LocalDate.parse((String)value));
      else if ($Datetime.class.isAssignableFrom(type))
        dataType.set(LocalDateTime.parse((String)value));
      else if ($Enum.class.isAssignableFrom(type)) {
        for (final Object constant : dataType.type().getEnumConstants()) {
          if (constant.toString().equals(value)) {
            dataType.set(constant);
            break;
          }
        }

        if (!dataType.wasSet())
          throw new IllegalArgumentException("'" + value + "' is not a valid value for " + dataType.name);
      }
      else if ($Int.class.isAssignableFrom(type))
        dataType.set(dataType instanceof Numeric.UNSIGNED ? ((BigInteger)value).longValue() : ((BigInteger)value).intValue());
      else if ($Smallint.class.isAssignableFrom(type))
        dataType.set(dataType instanceof Numeric.UNSIGNED ? ((BigInteger)value).intValue() : ((BigInteger)value).shortValue());
      else if ($Time.class.isAssignableFrom(type))
        dataType.set(LocalTime.parse((String)value));
      else if ($Tinyint.class.isAssignableFrom(type))
        dataType.set(dataType instanceof Numeric.UNSIGNED ? ((BigInteger)value).shortValue() : ((BigInteger)value).byteValue());
      else
        dataType.set(value);
    }

    return entity;
  }

  public static type.Entity[] toEntities(final $Database database) {
    try {
      final Iterator<$Row> iterator = SQL.newRowIterator(database);
      if (!iterator.hasNext())
        return new type.Entity[0];

      final List<type.Entity> entities = new ArrayList<>();
      while (iterator.hasNext())
        entities.add(toEntity(database, iterator.next()));

      return entities.toArray(new type.Entity[entities.size()]);
    }
    catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private EntitiesXsb() {
  }
}