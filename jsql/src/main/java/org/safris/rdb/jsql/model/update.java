/* Copyright (c) 2015 lib4j
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

import org.safris.rdb.jsql.Condition;
import org.safris.rdb.jsql.type;

public interface update {
  public interface UPDATE extends ExecuteUpdate {
  }

  public interface UPDATE_SET extends UPDATE {
    public <T>SET SET(final type.DataType<? extends T> column, final type.DataType<? extends T> to);
    public <T>SET SET(final type.DataType<T> column, final T to);
  }

  public interface SET extends UPDATE_SET {
    public UPDATE WHERE(final Condition<?> condition);
  }
}