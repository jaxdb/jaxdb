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
    @Override
    public Number generate(final DataType<Number> dataType) {
      if (dataType.get() == null)
        throw new XDERuntimeException("Value is missing");

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
  };

  public static GenerateOn<BaseLocal> TIMESTAMP = new GenerateOn<BaseLocal>() {
    @Override
    public BaseLocal generate(final DataType<BaseLocal> dataType) {
      final Class<?> type = (Class<?>)Classes.getGenericSuperclasses(dataType.getClass())[0];
      try {
        return (BaseLocal)type.newInstance();
      }
      catch (final IllegalAccessException | InstantiationException e) {
        throw new XDERuntimeException(e);
      }
    }
  };

  public static GenerateOn<String> UUID = new GenerateOn<String>() {
    @Override
    public String generate(final DataType<String> dataType) {
      return java.util.UUID.randomUUID().toString().toUpperCase();
    }
  };

  public abstract T generate(final DataType<T> dataType);
}