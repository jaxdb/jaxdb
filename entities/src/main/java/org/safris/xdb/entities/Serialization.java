/* Copyright (c) 2016 Seva Safris
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.schema.DBVendor;

public class Serialization {
  private final List<Variable<?>> parameters = new ArrayList<Variable<?>>();

  protected final Class<?> type;
  protected final DBVendor vendor;
  protected final Class<? extends Statement> statementType;
  protected final StringBuilder sql = new StringBuilder();

  protected Serialization(final Class<?> type, final DBVendor vendor, final Class<? extends Statement> statementType) {
    this.type = type;
    this.vendor = vendor;
    this.statementType = statementType;
  }

  public Class<?> getType() {
    return type;
  }

  protected void addParameter(final Variable<?> parameter) {
    if (parameter == null)
      throw new IllegalArgumentException("parameter cannot be null");

    parameters.add(parameter);
  }

  protected void set(final PreparedStatement statement) throws SQLException {
    for (int i = 0; i < parameters.size(); i++)
      parameters.get(i).get(statement, i + 1);
  }
}