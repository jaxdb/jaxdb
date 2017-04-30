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

package org.safris.rdb.jsql.model;

import org.safris.rdb.jsql.Subject;

public interface insert {
  public interface VALUES<T extends Subject<?>> extends ExecuteUpdate {
  }

  public interface INSERT<T extends Subject<?>> extends ExecuteUpdate {
  }

  public interface INSERT_VALUES<T extends Subject<?>> extends INSERT<T> {
    public VALUES<T> VALUES(final select.SELECT<T> select);
  }
}