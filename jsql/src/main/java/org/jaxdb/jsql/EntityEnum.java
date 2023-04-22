/* Copyright (c) 2017 JAX-DB
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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EntityEnum extends CharSequence, Comparable<EntityEnum>, Serializable {
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Type {
    String value();
  }

  @Override
  default int length() {
    return toString().length();
  }

  byte ordinal();

  @Override
  default char charAt(final int index) {
    return toString().charAt(index);
  }

  @Override
  default CharSequence subSequence(final int start, final int end) {
    return toString().subSequence(start, end);
  }

  @Override
  default int compareTo(final EntityEnum o) {
    return toString().compareTo(o.toString());
  }
}