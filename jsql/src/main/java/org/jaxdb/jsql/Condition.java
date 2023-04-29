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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Condition<V extends Serializable> extends data.Primitive<V> {
  Condition(final data.Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends data.Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
    super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
  }

  Condition(final data.Table owner, final boolean mutable, final Condition<V> copy) {
    super(owner, mutable, copy);
  }

  Condition(final data.Table owner, final boolean mutable) {
    super(owner, mutable);
  }

  abstract void collectColumns(ArrayList<data.Column<?>> list);
}