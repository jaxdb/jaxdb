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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.sql.StatementProxy;
import org.safris.xdb.xdl.DBVendor;

class Insert {
  protected static class INSERT extends Keyword<Data<?>> implements org.safris.xdb.xde.csql.insert.INSERT {
    protected final Entity entity;

    protected INSERT(final Entity entity) {
      this.entity = entity;
    }

    @Override
    protected Keyword<Data<?>> parent() {
      return null;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("INSERT INTO ").append(entity.name());
      final StringBuilder columns = new StringBuilder();
      final StringBuilder values = new StringBuilder();
      if (serialization.statementType == PreparedStatement.class) {
        for (final DataType dataType : entity.column()) {
          if (!dataType.wasSet()) {
            if (dataType.generateOnInsert == null)
              continue;

            dataType.set(dataType.generateOnInsert.generate(dataType));
          }

          columns.append(", ").append(dataType.name);
          values.append(", ").append(dataType.getPreparedStatementMark(serialization.vendor));
          serialization.addParameter(dataType.get());
        }
      }
      else if (serialization.statementType == Statement.class) {
        for (final DataType dataType : entity.column()) {
          if (!dataType.wasSet()) {
            if (dataType.generateOnInsert == null)
              continue;

            dataType.set(dataType.generateOnInsert.generate(dataType));
          }

          columns.append(", ").append(dataType.name);
          values.append(", ").append(FieldWrapper.toString(dataType.get()));
        }
      }
      else {
        throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
      }

      serialization.sql.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
    }

    @Override
    public int execute(final Transaction transaction) throws XDEException {
      final Keyword<?> insert = getParentRoot(this);
      final Class<? extends Schema> schema = (((INSERT)insert).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);

        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));
        serialize(serialization);
        Data.clearAliases();
        if (serialization.statementType == PreparedStatement.class) {
          final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString());
          serialization.set(statement);
          final int count = statement.executeUpdate();
          StatementProxy.close(statement);
          if (transaction == null)
            ConnectionProxy.close(connection);

          return count;
        }

        if (serialization.statementType == Statement.class) {
          final Statement statement = connection.createStatement();
          final int count = statement.executeUpdate(serialization.sql.toString());
          StatementProxy.close(statement);
          if (transaction == null)
            ConnectionProxy.close(connection);

          return count;
        }

        throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
      }
      catch (final SQLException e) {
        throw XDEException.lookup(e, vendor);
      }
    }

    @Override
    public int execute() throws XDEException {
      return execute(null);
    }
  }
}