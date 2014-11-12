/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class Entities {
  public static int select(final Connection connection, final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      if (e.select(connection))
        ++count;

    return count;
  }

  public static int select(final Connection connection, final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      if (e.select(connection))
        ++count;

    return count;
  }

  public static int insert(final Connection connection, final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      count += e.insert(connection);

    return count;
  }

  public static int insert(final Connection connection, final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      count += e.insert(connection);

    return count;
  }

  public static int update(final Connection connection, final Entity ... entity) throws SQLException {
    int count = 0;
    for (final Entity e : entity)
      count += e.update(connection);

    return count;
  }

  public static int update(final Connection connection, final Collection<Entity> entities) throws SQLException {
    int count = 0;
    for (final Entity e : entities)
      count += e.update(connection);

    return count;
  }

  private Entities() {
  }
}