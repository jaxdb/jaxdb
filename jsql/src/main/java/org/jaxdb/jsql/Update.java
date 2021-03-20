/* Copyright (c) 2015 JAX-DB
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

public interface Update {
  interface UPDATE extends Executable.Update {
  }

  interface _SET extends UPDATE {
    <T>SET SET(type.DataType<? extends T> column, type.DataType<? extends T> to);
    <T>SET SET(type.DataType<T> column, T to);
  }

  interface SET extends _SET {
    UPDATE WHERE(Condition<?> condition);
  }
}