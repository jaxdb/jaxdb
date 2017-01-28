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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;

import org.safris.xdb.entities.model.select;

final class InPredicate<T> extends Predicate<T> {
  protected final boolean positive;
  protected final DataType<T> dataType;
  protected final Serializable[] values;

  @SafeVarargs
  protected InPredicate(final boolean positive, final DataType<T> dataType, final T ... values) {
    this.positive = positive;
    this.dataType = dataType;
    this.values = new DataType<?>[values.length];
    for (int i = 0; i < values.length; i++)
      this.values[i] = DataType.wrap(values[i]);
  }

  @SuppressWarnings("unchecked")
  protected InPredicate(final boolean positive, final DataType<T> dataType, final Collection<T> values) {
    this(positive, dataType, values.toArray((T[])Array.newInstance(dataType.type(), values.size())));
  }

  protected InPredicate(final boolean positive, final DataType<T> dataType, final select.SELECT<? extends DataType<T>> query) {
    this.positive = positive;
    this.dataType = dataType;
    this.values = new Serializable[] {(Serializable)query};
  }

  @Override
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}