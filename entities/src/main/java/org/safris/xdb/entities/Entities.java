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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.safris.commons.lang.Strings;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xdd.xe.$xdd_xdd;
import org.safris.xsb.runtime.Binding;
import org.safris.xsb.runtime.Element;
import org.safris.xsb.runtime.QName;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;

public final class Entities {
  private static QName getName(final Class<?> cls) {
    return cls.getAnnotation(QName.class);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Entity toEntity(final $xdd_data data) {
    final Element element = (Element)data;
    final $xs_anySimpleType owner = element.owner();
    final QName schemaName = getName(owner.getClass().getSuperclass());
    final QName entityName = getName(data.getClass().getSuperclass());

    try {
      final Class<?> binding = Class.forName("xdb.ddl." + Strings.toInstanceCase(schemaName.localPart()) + "." + Strings.toTitleCase(entityName.localPart()));
      final Entity entity = (Entity)binding.newInstance();
      final Iterator<? extends $xs_anySimpleType> attributeIterator = data.attributeIterator();
      while (attributeIterator.hasNext()) {
        final $xs_anySimpleType attribute = attributeIterator.next();
        final Field field = binding.getField(Strings.toCamelCase(attribute.name().getLocalPart()));
        final DataType dataType = (DataType<?>)field.get(entity);
        dataType.set(attribute.text());
      }

      return entity;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends Entity>T[] toEntities(final $xdd_xdd xdd) {
    final Iterator<Binding> iterator = xdd.elementIterator();
    final List<Entity> entities = new ArrayList<Entity>();
    while (iterator.hasNext())
      entities.add(toEntity(($xdd_data)iterator.next()));

    final T[] array = (T[])Array.newInstance(entities.get(0).getClass(), entities.size());
    return entities.toArray(array);
  }

  private Entities() {
  }
}