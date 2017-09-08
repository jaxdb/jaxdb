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

package org.libx4j.rdb.jsql;

import org.lib4j.lang.Classes;

public abstract class Subject<T> extends Evaluable {
  @SuppressWarnings("unchecked")
  protected static <T extends Subject<?>>T as(final Keyword<T> select) {
    try {
      Keyword<T> keyword = select;
      do {
        if (keyword instanceof SelectImpl.untyped.SELECT) {
          final Class<?> cls = Classes.getGreatestCommonSuperclass(((SelectImpl.untyped.SELECT<?>)keyword).entities);
          final T as = (T)cls.newInstance();
          as.wrapper(new As<T>(select, as, false));
          return as;
        }
      }
      while ((keyword = keyword.parent()) != null);
      return null;
    }
    catch (final IllegalAccessException | InstantiationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private Evaluable wrapper;

  protected final Evaluable wrapper() {
    return wrapper;
  }

  protected Subject<T> wrapper(final Evaluable wrapper) {
    this.wrapper = wrapper;
    return this;
  }
}