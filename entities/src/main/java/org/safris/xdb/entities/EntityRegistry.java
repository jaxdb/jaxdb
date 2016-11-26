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

package org.safris.xdb.entities;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public final class EntityRegistry {
  private static final Map<Class<? extends Schema>,EntityDataSource> dataSources = new HashMap<Class<? extends Schema>,EntityDataSource>();
  private static final Map<Class<? extends Schema>,Class<? extends Statement>> statementTypes = new HashMap<Class<? extends Schema>,Class<? extends Statement>>();

  public static void register(final Class<? extends Schema> schema, final Class<? extends Statement> statementType, final EntityDataSource dataSource) {
    dataSources.put(schema, dataSource);
    statementTypes.put(schema, statementType);
  }

  protected static EntityDataSource getDataSource(final Class<? extends Schema> schema) {
    return dataSources.get(schema);
  }

  protected static Class<? extends Statement> getStatementType(final Class<? extends Schema> schema) {
    return statementTypes.get(schema);
  }

  private EntityRegistry() {
  }
}