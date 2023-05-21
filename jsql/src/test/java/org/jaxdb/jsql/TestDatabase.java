/* Copyright (c) 2023 JAX-DB
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

import org.libj.util.ConcurrentNullHashMap;

public class TestDatabase extends Database {
  public static Database get(final Schema schema, final String dataSourceId) {
    ConcurrentNullHashMap<String,Database> localGlobal = schemaClassToLocalGlobal.get(schema);
    Database database;
    if (localGlobal == null)
      schemaClassToLocalGlobal.put(schema, localGlobal = new ConcurrentNullHashMap<>(2));
    else if ((database = localGlobal.get(dataSourceId)) != null)
      return database;

    localGlobal.put(dataSourceId, database = new TestDatabase(schema, dataSourceId));
    return database;
  }

  public static Database get(final Schema schema) {
    return get(schema, null);
  }

  TestDatabase(final Schema schema, final String dataSourceId) {
    super(schema, dataSourceId);
  }

  @Override
  Connector newConnector(final ConnectionFactory connectionFactory, final boolean isPrepared) {
    return new TestConnector(schema, dataSourceId, connectionFactory, isPrepared);
  }
}