/* Copyright (c) 2021 JAX-DB
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

import java.io.IOException;
import java.sql.SQLException;

class operation {
  interface Operation {
  }

  interface Operation1<I,O> extends Operation {
    void compile(type.Column<?> a, Compilation compilation) throws IOException, SQLException;
    O evaluate(I a);
  }

  interface Operation2<I> extends Operation {
    void compile(type.Column<?> a, type.Column<?> b, final Compilation compilation) throws IOException, SQLException;
    I evaluate(I a, I b);
  }

  interface Operation3<IO,I2,I3> extends Operation {
    void compile(type.Column<?> a, type.Column<?> b, type.Column<?> c, final Compilation compilation) throws IOException, SQLException;
    IO evaluate(IO a, I2 b, I3 c);
  }
}