/* Copyright (c) 2017 Seva Safris
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

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.safris.commons.lang.Strings;
import org.safris.commons.xml.binding.Base64Binary;
import org.safris.commons.xml.binding.DateTime;
import org.safris.xdb.xdd.xe.$xdd_binary;
import org.safris.xdb.xdd.xe.$xdd_blob;
import org.safris.xdb.xdd.xe.$xdd_clob;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xdd.xe.$xdd_date;
import org.safris.xdb.xdd.xe.$xdd_dateTime;
import org.safris.xdb.xdd.xe.$xdd_decimal;
import org.safris.xdb.xdd.xe.$xdd_enum;
import org.safris.xdb.xdd.xe.$xdd_integer;
import org.safris.xdb.xdd.xe.$xdd_row;
import org.safris.xdb.xdd.xe.$xdd_time;
import org.safris.xsb.runtime.Binding;
import org.safris.xsb.runtime.Element;
import org.safris.xsb.runtime.QName;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;

public final class Entities {
  private static QName getName(final Class<?> cls) {
    return cls.getAnnotation(QName.class);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Entity toEntity(final $xdd_row row) {
    final Element element = (Element)row;
    final QName schemaName = getName(element.owner().getClass().getSuperclass());
    final QName entityName = getName(row.getClass().getSuperclass());

    try {
      final Class<?> binding = Class.forName(Entities.class.getPackage().getName() + "." + Strings.toInstanceCase(schemaName.localPart()) + "$" + Strings.toTitleCase(entityName.localPart()));
      final Entity entity = (Entity)binding.newInstance();
      final Iterator<? extends $xs_anySimpleType> attributeIterator = row.attributeIterator();
      while (attributeIterator.hasNext()) {
        final $xs_anySimpleType attribute = attributeIterator.next();
        if (attribute == null)
          continue;

        final Field field = binding.getField(Strings.toCamelCase(attribute.name().getLocalPart()));
        final type.DataType dataType = (type.DataType<?>)field.get(entity);
        final Object value = attribute.text();
        if (value == null)
          dataType.set(null);
        else if (attribute instanceof $xdd_integer) {
          // FIXME: Are we bounded by the size of long for BigInt here?
          if (dataType instanceof type.BIGINT)
            dataType.set(value);
          else if (dataType instanceof type.INT)
            dataType.set(((BigInteger)value).longValue());
          else if (dataType instanceof type.MEDIUMINT)
            dataType.set(((BigInteger)value).intValue());
          else if (dataType instanceof type.TINYINT)
            dataType.set(((BigInteger)value).shortValue());
        }
        else if (attribute instanceof $xdd_decimal)
          dataType.set(((BigDecimal)value).doubleValue());
        else if (attribute instanceof $xdd_date)
          dataType.set(LocalDate.parse((String)value));
        else if (attribute instanceof $xdd_time)
          dataType.set(LocalTime.parse((String)value));
        else if (attribute instanceof $xdd_dateTime)
          dataType.set(LocalDateTime.parse(((DateTime)value).toString()));
        else if (attribute instanceof $xdd_enum) {
          final String enumValue = ((String)value).toUpperCase().replace(' ', '_');
          for (final Object constant : ((type.ENUM)dataType).type().getEnumConstants())
            if (constant.toString().equals(enumValue))
              dataType.set(constant);
        }
        else if (attribute instanceof $xdd_binary)
          dataType.set(((Base64Binary)value).getBytes());
        else if (attribute instanceof $xdd_blob)
          dataType.set(new ByteArrayInputStream(((Base64Binary)value).getBytes()));
        else if (attribute instanceof $xdd_clob)
          dataType.set(new StringReader((String)value));
        else
          dataType.set(value);
      }

      return entity;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends Entity>T[] toEntities(final $xdd_data data) {
    final Iterator<Binding> iterator = data.elementIterator();
    final List<Entity> entities = new ArrayList<Entity>();
    while (iterator.hasNext())
      entities.add(toEntity(($xdd_row)iterator.next()));

    return (T[])entities.toArray(new Entity[entities.size()]);
  }

  private Entities() {
  }
}