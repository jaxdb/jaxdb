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

package org.safris.xdb.entities.model;

import org.safris.xdb.entities.Subject;

public interface insert {
  public interface INSERT extends ExecuteUpdate {
  }

  public interface INSERT_SELECT<T extends Subject<?>> extends INSERT {
    public select._SELECT<T> SELECT(final select.SELECT<T> select);

    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final T ... entities);
  }
}