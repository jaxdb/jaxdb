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

class Evaluation<T> extends Subject<T> {
  protected final Object a;
  protected final Operator<Predicate<?>> operator;
  protected final Object[] args;
  protected final int startIndex;

  protected Evaluation(final Operator<Predicate<?>> operator, final Object a, final Object ... args) {
    this.a = a;
    this.operator = operator;
    this.args = args;
    this.startIndex = 0;
  }

  @SafeVarargs
  protected Evaluation(final Operator<Predicate<?>> operator, final Variable<T> ... args) {
    this.a = args[0];
    this.operator = operator;
    this.args = args;
    startIndex = 1;
  }
}