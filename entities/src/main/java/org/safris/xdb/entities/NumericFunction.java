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

package org.safris.xdb.entities;

import org.safris.xdb.entities.datatype.Numeric;

final class NumericFunction<T extends Numeric<?>> extends Expression<T> {
  protected final String function;
  protected final DML.SetQualifier qualifier;
  protected final DataType<T> a;
  protected final Object b;

  protected NumericFunction(final String function, final DML.SetQualifier qualifier, final DataType<T> dataType) {
    this.function = function;
    this.qualifier = qualifier;
    this.a = dataType;
    this.b = null;
  }

  protected NumericFunction(final String function, final DataType<T> dataType) {
    this(function, (DML.SetQualifier)null, dataType);
  }

  protected NumericFunction(final String function) {
    this(function, (DML.SetQualifier)null, null);
  }

  protected NumericFunction(final String function, final DataType<T> a, final Object b) {
    this.function = function;
    this.qualifier = null;
    this.a = a;
    this.b = b;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    serialization.addCaller(this);
    serialization.append(function).append("(");
    if (a != null) {
      if (b != null) {
        Keyword.format(a, serialization);
        serialization.append(", ");
        if (b instanceof DataType)
          Keyword.format(b, serialization);
        else
          serialization.append(String.valueOf(b));
      }
      else {
        if (qualifier != null)
          serialization.append(qualifier.toString()).append(" ");

        Keyword.format(a, serialization);
      }
    }

    serialization.append(")");
  }
}