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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class TestConnectionFactory implements ConnectionFactory {
  private static final ThreadLocal<Boolean> called = new ThreadLocal<Boolean>() {
    @Override
    protected Boolean initialValue() {
      return Boolean.FALSE;
    }
  };

  public static boolean called() {
    if (!called.get())
      return false;

    called.set(Boolean.FALSE);
    return true;
  }

  public abstract Connection getTestConnection(Transaction.Isolation isolation) throws IOException, SQLException;

  @Override
  public final Connection getConnection(final Transaction.Isolation isolation) throws IOException, SQLException {
    called.set(true);
    return getTestConnection(isolation);
  }
}