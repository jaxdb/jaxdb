/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.jsql;

import org.openjax.rdb.sqlx_0_9_9.Database;
import org.openjax.rdb.sqlx_0_9_9.xL0gluGCXYYJc.$Database;

public class Entities {
  public static <T extends type.Entity>T[] toEntities(final Database database) {
    return EntitiesJaxb.toEntities(database);
  }

  public static <T extends type.Entity>T[] toEntities(final $Database database) {
    return EntitiesXsb.toEntities(database);
  }
}