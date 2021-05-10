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

public interface Insert {
  interface CONFLICT_ACTION extends Executable.Modify.Insert {
  }

  interface ON_CONFLICT {
    CONFLICT_ACTION DO_UPDATE();
    CONFLICT_ACTION DO_NOTHING();
  }

  interface INSERT<T extends type.Entity<?>> extends CONFLICT_ACTION {
    ON_CONFLICT ON_CONFLICT();
  }

  interface _INSERT<T extends type.Entity<?>> extends INSERT<T> {
    INSERT<T> VALUES(Select.untyped.SELECT<?> select);
  }
}