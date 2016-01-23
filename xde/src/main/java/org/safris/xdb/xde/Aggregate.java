/* Copyright (c) 2015 Seva Safris
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.safris.xdb.xdl.DBVendor;

public abstract class Aggregate<T> extends Column<T> {
  protected final DML.SetQualifier qualifier;
  protected final Column<T> column;

  protected Aggregate(final DML.SetQualifier qualifier, final Column<T> column) {
    super(column);
    this.qualifier = qualifier;
    this.column = column;
  }

  protected Aggregate(final Aggregate<T> aggregate) {
    super(aggregate.column);
    this.qualifier = aggregate.qualifier;
    this.column = aggregate.column;
  }

  protected cSQL<?> parent() {
    return column;
  }

  protected String getPreparedStatementMark(final DBVendor vendor) {
    return column.getPreparedStatementMark(vendor);
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    column.set(statement, parameterIndex);
  }

  protected T get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    return column.get(resultSet, columnIndex);
  }
}