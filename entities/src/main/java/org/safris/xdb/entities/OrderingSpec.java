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

final class OrderingSpec<T extends Subject<?>> extends Subject<T> {
  private final Operator<OrderingSpec<?>> operator;
  private final Variable<?> variable;

  public OrderingSpec(final Operator<OrderingSpec<?>> operator, final Variable<T> variable) {
    this.operator = operator;
    this.variable = variable;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    serialization.addCaller(this);
    variable.serialize(serialization);
    serialization.append(" ").append(operator);
  }
}