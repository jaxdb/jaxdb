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

package org.safris.xdb.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.safris.xdb.schema.DBVendor;

public abstract class Aggregate<T> extends DataType<T> {
  protected final DML.SetQualifier qualifier;
  protected final DataType<T> dataType;

  protected Aggregate(final DML.SetQualifier qualifier, final DataType<T> dataType) {
    super(dataType);
    this.qualifier = qualifier;
    this.dataType = dataType;
  }

  protected Aggregate(final Aggregate<T> aggregate) {
    super(aggregate.dataType);
    this.qualifier = aggregate.qualifier;
    this.dataType = aggregate.dataType;
  }

  @Override
  protected Entity owner() {
    return dataType.owner();
  }

  @Override
  protected String getPreparedStatementMark(final DBVendor vendor) {
    return dataType.getPreparedStatementMark(vendor);
  }

  @Override
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    dataType.get(statement, parameterIndex);
  }

  @Override
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    dataType.set(resultSet, columnIndex);
    this.value = dataType.get();
  }
}