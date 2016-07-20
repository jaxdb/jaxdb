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

import org.safris.xdb.xde.spec.delete;
import org.safris.xdb.xdl.DBVendor;

class Delete {
  private static abstract class Execute extends Keyword<DataType<?>> implements delete.DELETE {
    @Override
    public int execute(final Transaction transaction) throws SQLErrorSpecException {
      final Keyword<?> delete = getParentRoot(this);
      final Class<? extends Schema> schema = (((DELETE)delete).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(vendor, EntityRegistry.getStatementType(schema));
        serialize(this, serialization);
        Data.clearAliases();
        if (serialization.statementType == PreparedStatement.class) {
          final int count;
          try (final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString())) {
            serialization.set(statement);
            count = statement.executeUpdate();
          }

          if (transaction == null)
            connection.close();

          return count;
        }

        if (serialization.statementType == Statement.class) {
          final int count;
          try (final Statement statement = connection.createStatement()) {
            count = statement.executeUpdate(serialization.sql.toString());
          }

          if (transaction == null)
            connection.close();

          return count;
        }

        throw new UnsupportedOperationException("Unsupported Statement type: " + serialization.statementType.getName());
      }
      catch (final SQLException e) {
        throw SQLErrorSpecException.lookup(e, vendor);
      }
    }

    @Override
    public int execute() throws SQLErrorSpecException {
      return execute(null);
    }
  }

  protected final static class WHERE extends Execute implements delete.DELETE {
    private final Keyword<DataType<?>> parent;
    private final Condition<?> condition;

    protected WHERE(final Keyword<DataType<?>> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      parent.serialize(this, serialization);
      serialization.sql.append(" WHERE ");
      condition.serialize(this, serialization);
    }
  }

  protected final static class DELETE extends Execute implements delete.DELETE_WHERE {
    protected final Entity entity;

    protected DELETE(final Entity entity) {
      this.entity = entity;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      if (getClass() != DELETE.class) // means that there are subsequent clauses
        throw new Error("Need to override this");

      if (entity.primary().length == 0)
        throw new UnsupportedOperationException("Entity '" + entity.name() + "' does not have a primary key");

      if (caller == this && !entity.wasSelected())
        throw new UnsupportedOperationException("Entity '" + entity.name() + "' did not come from a SELECT");

      serialization.sql.append("DELETE FROM ");
      entity.serialize(this, serialization);

      if (caller == this) {
        final StringBuilder whereClause = new StringBuilder();
        for (final DataType<?> dataType : entity.primary()) {
          serialization.addParameter(dataType);
          whereClause.append(" AND ").append(dataType.name).append(" = ?");
        }

        serialization.sql.append(" WHERE ").append(whereClause.substring(5));
      }
    }
  }
}