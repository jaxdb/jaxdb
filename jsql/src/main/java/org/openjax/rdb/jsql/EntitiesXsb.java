package org.openjax.rdb.jsql;

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

import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Bigint;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Binary;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Blob;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Clob;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Date;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Datetime;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Enum;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Int;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Smallint;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Time;
import org.openjax.rdb.datatypes_0_3_9.xL5gluGCXYYJc.$Tinyint;
import org.openjax.rdb.jsql.kind.Numeric;
import org.openjax.rdb.sqlx.SQL;
import org.openjax.rdb.sqlx_0_3_9.xL0gluGCXYYJc.$Database;
import org.openjax.rdb.sqlx_0_3_9.xL0gluGCXYYJc.$Row;
import org.openjax.ext.util.Classes;
import org.openjax.ext.util.Identifiers;
import org.openjax.ext.xml.datatype.HexBinary;
import org.openjax.xsb.runtime.Attribute;
import org.openjax.xsb.runtime.Id;
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
        for (final Object constant : ((type.ENUM)dataType).type().getEnumConstants()) {
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

  @SuppressWarnings("unchecked")
  public static <T extends type.Entity>T[] toEntities(final $Database database) {
    try {
      final List<type.Entity> entities = new ArrayList<>();
      final Iterator<$Row> rowIterator = SQL.newRowIterator(database);
      if (!rowIterator.hasNext())
        return (T[])new type.Entity[0];

      while (rowIterator.hasNext())
        entities.add(toEntity(database, rowIterator.next()));

      return (T[])entities.toArray(new type.Entity[entities.size()]);
    }
    catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private EntitiesXsb() {
  }
}