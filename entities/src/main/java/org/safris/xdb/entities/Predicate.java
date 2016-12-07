/* Copyright (c) 2015 Seva Safris
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

final class Predicate<T> extends Condition<Subject<T>> {
  private final String predicate;
  private final Variable<T> variable;
  private final Object[] condition;

  protected Predicate(final String predicate, final Variable<T> variable, final Object ... condition) {
    if (variable == null)
      throw new NullPointerException("variable == null");

    this.predicate = predicate;
    this.variable = variable;
    this.condition = condition;
  }

  protected Predicate(final String predicate, final Object ... condition) {
    this.predicate = predicate;
    this.variable = null;
    this.condition = condition;
  }

  @Override
  protected Keyword<Subject<Subject<T>>> parent() {
    return null;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    if (variable != null) {
      format(this, variable, serialization);
      serialization.sql.append(" ");
    }

    serialization.sql.append(predicate).append(" ");
    format(this, condition, serialization);
  }
}