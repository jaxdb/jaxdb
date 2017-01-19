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

import org.safris.xdb.entities.spec.select;

final class InPredicate<T> extends Predicate<T> {
  private final boolean positive;
  private final Variable<T> variable;
  private final T[] values;
  private final select.SELECT<? extends Variable<T>> query;

  @SafeVarargs
  protected InPredicate(final boolean positive, final Variable<T> variable, final T ... values) {
    this.positive = positive;
    this.variable = variable;
    this.values = values;
    this.query = null;
  }

  protected InPredicate(final boolean positive, final Variable<T> variable, final select.SELECT<? extends Variable<T>> query) {
    this.positive = positive;
    this.variable = variable;
    this.values = null;
    this.query = query;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    serialization.addCaller(this);
    format(variable, serialization);
    serialization.append(" ");
    if (!positive)
      serialization.append("NOT ");

    serialization.append("IN").append(" ");
    format(values != null ? values : query, serialization);
  }
}
