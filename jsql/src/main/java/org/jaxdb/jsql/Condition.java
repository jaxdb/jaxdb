/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql;

public abstract class Condition<T> extends type.Primitive<T> {
  Condition(final type.Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
    super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
  }

  Condition(final Condition<T> copy) {
    super(copy);
  }

  Condition(final boolean mutable) {
    super(mutable);
  }
}