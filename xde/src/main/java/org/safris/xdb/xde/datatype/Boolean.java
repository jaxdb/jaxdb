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

package org.safris.xdb.xde.datatype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xde.DataType;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xdl.DBVendor;

public final class Boolean extends DataType<java.lang.Boolean> {
  protected static final int sqlType = Types.BOOLEAN;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final java.lang.Boolean value) throws SQLException {
    statement.setBoolean(parameterIndex, value);
  }

  public Boolean(final Entity owner, final String csqlName, final String name, final java.lang.Boolean _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<java.lang.Boolean> generateOnInsert, final GenerateOn<java.lang.Boolean> generateOnUpdate) {
    super(sqlType, java.lang.Boolean.class, owner, csqlName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected Boolean(final Boolean column) {
    super(column);
  }

  @Override
  protected String getPreparedStatementMark(final DBVendor vendor) {
    return "?";
  }

  @Override
  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  @Override
  protected java.lang.Boolean get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final boolean value = resultSet.getBoolean(columnIndex);
    return resultSet.wasNull() ? null : value;
  }
}