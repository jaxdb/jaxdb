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
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import org.jaxdb.ddlx.dt;
import org.jaxdb.ddlx.annotation.Column;
import org.jaxdb.ddlx.annotation.Schema;
import org.jaxdb.ddlx.annotation.Table;
import org.jaxdb.sqlx_0_4.Database;
import org.jaxdb.sqlx_0_4.Row;
import org.libj.lang.Identifiers;

final class EntitiesJaxb {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private static type.Entity toEntity(final Database database, final Row row) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    final Schema schema = database.getClass().getAnnotation(Schema.class);
    final Table table = row.getClass().getAnnotation(Table.class);
    // FIXME: This is brittle... Need to modularize it and make it clearer:
    final Class<?> binding = Class.forName(Entities.class.getPackage().getName() + "." + Identifiers.toInstanceCase(schema.name()) + "$" + Identifiers.toClassCase(table.name()));
    final type.Entity entity = (type.Entity)binding.getDeclaredConstructor().newInstance();
    for (final Method method : row.getClass().getMethods()) {
      if (method.getName().startsWith("get") && dt.DataType.class.isAssignableFrom(method.getReturnType())) {
        final dt.DataType<?> column = (dt.DataType<?>)method.invoke(row);
        if (column == null)
          continue;

        final Field field = binding.getField(Identifiers.toCamelCase(method.getAnnotation(Column.class).name()));
        final type.DataType dataType = (type.DataType<?>)field.get(entity);

        final Object value = column.get();
        // FIXME: Use primitive types
        if (value == null)
          dataType.set((Object)null);
        else if (column instanceof dt.BLOB)
          ((type.BLOB)dataType).set(new ByteArrayInputStream(((String)value).getBytes()));
        else if (column instanceof dt.BINARY)
          ((type.BINARY)dataType).set(((String)value).getBytes());
        else if (column instanceof dt.CLOB)
          ((type.CLOB)dataType).set(new StringReader((String)value));
        else if (column instanceof dt.DATE)
          ((type.DATE)dataType).set(LocalDate.parse((String)value));
        else if (column instanceof dt.DATETIME)
          ((type.DATETIME)dataType).set(LocalDateTime.parse((String)value));
        else if (column instanceof dt.TIME)
          ((type.TIME)dataType).set(LocalTime.parse((String)value));
        else if (column instanceof dt.ENUM) {
          for (final Object constant : dataType.type().getEnumConstants()) {
            if (constant.toString().equals(value)) {
              ((type.ENUM)dataType).set(constant);
              break;
            }
          }

          if (!dataType.wasSet())
            throw new IllegalArgumentException("'" + value + "' is not a valid value for " + dataType.name);
        }
        else {
          dataType.set(value);
        }
      }
    }

    return entity;
  }

  @SuppressWarnings("unchecked")
  public static type.Entity[] toEntities(final Database database) {
    try {
      final Class<?> cls = database.getClass();
      final List<type.Entity> entities = new ArrayList<>();
      final XmlType xmlType = cls.getAnnotation(XmlType.class);
      for (final String tableName : xmlType.propOrder())
        for (final Row row : (List<Row>)cls.getMethod("get" + Identifiers.toClassCase(tableName)).invoke(database))
          entities.add(toEntity(database, row));

      return entities.toArray(new type.Entity[entities.size()]);
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

  private EntitiesJaxb() {
  }
}