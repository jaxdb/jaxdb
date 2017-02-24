/* Copyright (c) 2017 Seva Safris
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

package org.safris.dbx.jsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.safris.dbx.ddlx.DBVendor;

abstract class ConditionWrapper<T> extends Condition<T> {
  @Override
  protected final String declare(final DBVendor vendor) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected final int sqlType() {
    throw new UnsupportedOperationException();
  }

  @Override
  protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected type.DataType<?> scaleTo(final type.DataType<?> dataType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final type.DataType<T> clone() {
    throw new UnsupportedOperationException();
  }
}