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

package org.safris.xdb.entities.datatype;

import org.safris.xdb.entities.DataType;
import org.safris.xdb.entities.Entity;
import org.safris.xdb.entities.GenerateOn;

public abstract class Numeric<T extends Number> extends DataType<T> {
  protected Numeric(final int sqlType, final Class<T> type, final Entity owner, final String specName, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
    super(sqlType, type, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected Numeric(final Numeric<T> dataType) {
    super(dataType);
  }
}