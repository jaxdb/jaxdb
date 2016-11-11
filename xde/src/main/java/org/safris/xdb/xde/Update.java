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
import java.util.logging.Logger;

import org.safris.xdb.xde.spec.expression.CASE;
import org.safris.xdb.xde.spec.update;
import org.safris.xdb.xdl.DBVendor;

class Update {
  private static final Logger logger = Logger.getLogger(Update.class.getName());

  private static abstract class Execute extends Keyword<DataType<?>> implements update.UPDATE {
    @Override
    public int execute(final Transaction transaction) throws SQLException {
      final Keyword<?> update = getParentRoot(this);
      final Class<? extends Schema> schema = (((UPDATE)update).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(Update.class, vendor, EntityRegistry.getStatementType(schema));
        serialize(this, serialization);
        Subject.clearAliases();
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

        throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
      }
      catch (final SQLException e) {
        throw SQLErrorSpecException.lookup(e, vendor);
      }
    }

    @Override
    public int execute() throws SQLException {
      return execute(null);
    }
  }

  private abstract static class UPDATE_SET extends Execute implements update.UPDATE_SET {
    @Override
    public final <T>SET SET(final DataType<T> set, final CASE<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final Variable<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final T to) {
      return new SET(this, set, Variable.valueOf(to));
    }
  }

  protected final static class WHERE extends Execute implements update.UPDATE {
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

  protected final static class UPDATE extends UPDATE_SET implements update.UPDATE_SET {
    protected final Entity entity;

    protected UPDATE(final Entity entity) {
      this.entity = entity;
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return null;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void serialize(final Serializable caller, final Serialization serialization) {
      if (getClass() != UPDATE.class) // means that there are subsequent clauses
        throw new Error("Need to override this");

      if (!(caller instanceof Update.SET) && entity.primary().length == 0)
        throw new UnsupportedOperationException("Entity '" + entity.name() + "' does not have a primary key, nor was WHERE clause specified");

      serialization.sql.append("UPDATE ");
      entity.serialize(this, serialization);
      if (!(caller instanceof Update.SET)) {
        final StringBuilder setClause = new StringBuilder();
        for (final DataType dataType : entity.column()) {
          if (!dataType.primary) {
            if (!dataType.wasSet()) {
              if (dataType.generateOnUpdate == null)
                continue;

              dataType.value = dataType.generateOnUpdate.generate(dataType);
            }

            serialization.addParameter(dataType);
            setClause.append(", ").append(dataType.name).append(" = ").append(dataType.getPreparedStatementMark(serialization.vendor));
          }
        }

        serialization.sql.append(" SET ").append(setClause.substring(2));
        final StringBuilder whereClause = new StringBuilder();
        for (final DataType dataType : entity.column()) {
          if (dataType.primary) {
            if (dataType.generateOnUpdate != null)
              dataType.value = dataType.generateOnUpdate.generate(dataType);

            serialization.addParameter(dataType);
            whereClause.append(" AND ").append(dataType).append(" = ").append(dataType.getPreparedStatementMark(serialization.vendor));
          }
        }

        serialization.sql.append(" WHERE ").append(whereClause.substring(5));
      }
    }

    @Override
    public int execute() throws SQLException {
      if (false) {
        final UPDATE update = (UPDATE)getParentRoot(this);
        final Class<? extends Schema> schema = update.entity.schema();
        DBVendor vendor = null;
        try {
          try (final Connection connection = Schema.getConnection(schema)) {
            vendor = Schema.getDBVendor(connection);
            final Serialization serialization = new Serialization(Update.class, vendor, EntityRegistry.getStatementType(schema));
            serialize(this, serialization);
            logger.info(serialization.sql.toString());
            if (true)
              return 0;

            try (final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString())) {
              // set the updated columns first
              int index = 0;
              for (final DataType<?> dataType : update.entity.column())
                if (!dataType.primary)
                  dataType.get(statement, ++index);

              // then the conditional columns
              for (final DataType<?> dataType : update.entity.column())
                if (dataType.primary)
                  dataType.get(statement, ++index);

              logger.info(statement.toString());
              return statement.executeUpdate();
            }
          }
        }
        catch (final SQLException e) {
          throw SQLErrorSpecException.lookup(e, vendor);
        }
      }

      Subject.clearAliases();
      return super.execute();
    }
  }

  protected final static class SET extends UPDATE_SET implements update.SET {
    private final Keyword<DataType<?>> parent;
    private final DataType<?> set;
    private final Serializable to;

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final Evaluation<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final Aggregate<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    @SuppressWarnings("unchecked")
    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final CASE<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = (Expression<Variable<T>>)to;
    }

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final Variable<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final T to) {
      this.parent = parent;
      this.set = set;
      this.to = Variable.valueOf(to);
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      parent.serialize(this, serialization);
      if (parent instanceof Update.UPDATE)
        serialization.sql.append(" SET ");

      set.serialize(this, serialization);
      serialization.sql.append(" = ");
      format(this, to, serialization);
      if (caller instanceof Update.SET)
        serialization.sql.append(", ");
    }
  }
}