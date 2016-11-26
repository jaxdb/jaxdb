/* Copyright (c) 2016 Seva Safris
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

import org.joda.time.base.BaseLocal;
import org.safris.commons.lang.reflect.Classes;

public abstract class GenerateOn<T> {
  public static GenerateOn<Number> INCREMENT = new GenerateOn<Number>() {
    private Number generate(final DataType<Number> dataType) {
      if (dataType.get() == null)
        throw new IllegalArgumentException("value is missing");

      if (dataType.get() instanceof Long)
        return (long)(dataType.get().longValue() + 1l);

      if (dataType.get() instanceof Integer)
        return (int)(dataType.get().intValue() + 1);

      if (dataType.get() instanceof Double)
        return (double)(dataType.get().doubleValue() + 1d);

      if (dataType.get() instanceof Float)
        return (float)(dataType.get().floatValue() + 1f);

      if (dataType.get() instanceof Short)
        return (short)(dataType.get().shortValue() + 1);

      return (byte)(dataType.get().byteValue() + 1);
    }

    @Override
    public Number generateStatic(final DataType<Number> dataType) {
      return generate(dataType);
    }

    @Override
    public String generateDynamic(final Serialization serialization, final DataType<Number> dataType) {
      return dataType.name + " + 1";
    }
  };

  public static GenerateOn<BaseLocal> TIMESTAMP = new GenerateOn<BaseLocal>() {
    private BaseLocal generate(final DataType<BaseLocal> dataType) {
      final Class<?> type = (Class<?>)Classes.getGenericSuperclasses(dataType.getClass())[0];
      try {
        return (BaseLocal)type.newInstance();
      }
      catch (final ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public BaseLocal generateStatic(final DataType<BaseLocal> dataType) {
      return generate(dataType);
    }

    @Override
    public String generateDynamic(final Serialization serialization, final DataType<BaseLocal> dataType) {
      dataType.set(generate(dataType));
      serialization.addParameter(dataType);
      return dataType.getPreparedStatementMark(serialization.vendor);
    }
  };

  public static GenerateOn<String> UUID = new GenerateOn<String>() {
    private String generate(final DataType<String> dataType) {
      return java.util.UUID.randomUUID().toString().toUpperCase();
    }

    @Override
    public String generateStatic(final DataType<String> dataType) {
      return generate(dataType);
    }

    @Override
    public String generateDynamic(final Serialization serialization, final DataType<String> dataType) {
      return generate(dataType);
    }
  };

  public abstract T generateStatic(final DataType<T> dataType);
  public abstract String generateDynamic(final Serialization serialization, final DataType<T> dataType);
}