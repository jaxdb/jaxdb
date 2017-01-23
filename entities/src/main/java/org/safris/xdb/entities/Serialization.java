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

package org.safris.xdb.entities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.safris.xdb.schema.DBVendor;

final class Serialization {
  private final StringBuilder builder = new StringBuilder();
  private final List<DataType<?>> parameters = new ArrayList<DataType<?>>();
  private final boolean prepared;

  protected final Class<? extends SQLStatement> sqlStatementType;
  protected final DBVendor vendor;
  protected final Serializer serializer;

  protected Serialization(final Class<? extends SQLStatement> sqlStatementType, final DBVendor vendor, final Class<? extends Statement> statementType) {
    this.sqlStatementType = sqlStatementType;
    this.vendor = vendor;
    if (!(this.prepared = statementType == PreparedStatement.class) && statementType != Statement.class)
      throw new UnsupportedOperationException("Unsupported statement type: " + statementType.getName());

    this.serializer = Serializer.getSerializer(vendor);
  }

  private final Map<Subject<?>,Alias> aliases = new IdentityHashMap<Subject<?>,Alias>();

  protected Alias registerAlias(final Subject<?> subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(aliases.size()));

    return alias;
  }

  protected Alias getAlias(final Subject<?> subject) {
    return aliases.get(subject);
  }

  protected StringBuilder append(final CharSequence seq) {
    return builder.append(seq);
  }

  protected void addParameter(final DataType<?> dataType) throws IOException {
    if (prepared) {
      builder.append(Serializer.getSerializer(vendor).getPreparedStatementMark(dataType));
      parameters.add(dataType);
    }
    else {
      builder.append(dataType.serialize(vendor));
    }
  }

  protected ResultSet executeQuery(final Connection connection) throws SQLException {
    if (prepared) {
      final PreparedStatement statement = connection.prepareStatement(builder.toString());
      for (int i = 0; i < parameters.size(); i++)
        parameters.get(i).get(statement, i + 1);

      return statement.executeQuery();
    }

    final Statement statement = connection.createStatement();
    return statement.executeQuery(builder.toString());
  }
}