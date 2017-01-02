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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.safris.xdb.entities.DML.ALL;
import org.safris.xdb.entities.DML.DISTINCT;
import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.spec.select;
import org.safris.xdb.schema.DBVendor;
import org.safris.xdb.xdd.xe.$xdd_xdd;

class Insert {
  protected static class INSERT<T extends Subject<?>> extends Keyword<Subject<?>> implements org.safris.xdb.entities.spec.insert.INSERT<T> {
    private final Entity parent;
    private final T[] entities;
    private final T entity;
    private final $xdd_xdd xdd;

    @SafeVarargs
    protected INSERT(final Entity parent, final T ... entities) {
      this.parent = parent;
      this.entities = entities;
      this.entity = null;
      this.xdd = null;
    }

    protected INSERT(final T entity) {
      this.parent = (Entity)entity;
      this.entity = entity;
      this.entities = null;
      this.xdd = null;
    }

    protected INSERT(final $xdd_xdd xdd) {
      this.parent = null;
      this.entity = null;
      this.entities = null;
      this.xdd = xdd;
    }

    @Override
    protected Keyword<Subject<?>> parent() {
      return null;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void serialize(final Serializable caller, final Serialization serialization) {
      if (entity != null) {
        serialization.sql.append("INSERT INTO ");
        entity.serialize(this, serialization);
        final StringBuilder columns = new StringBuilder();
        final StringBuilder values = new StringBuilder();
        if (serialization.statementType == PreparedStatement.class) {
          if (entity instanceof Entity) {
            for (final DataType dataType : ((Entity)entity).column()) {
              if (!dataType.wasSet()) {
                if (dataType.generateOnInsert == null)
                  continue;

                dataType.value = dataType.generateOnInsert.generateStatic(dataType);
              }

              columns.append(", ").append(dataType.name);
              values.append(", ").append(dataType.getPreparedStatementMark(serialization.vendor));
              serialization.addParameter(dataType);
            }
          }
          else {
            throw new UnsupportedOperationException();
          }
        }
        else if (serialization.statementType == Statement.class) {
          if (entity instanceof Entity) {
            for (final DataType dataType : ((Entity)entity).column()) {
              if (!dataType.wasSet()) {
                if (dataType.generateOnInsert == null)
                  continue;

                dataType.value = dataType.generateOnInsert.generateStatic(dataType);
              }

              columns.append(", ").append(dataType.name);
              values.append(", ").append(VariableWrapper.toString(dataType.get()));
            }
          }
          else {
            throw new UnsupportedOperationException();
          }
        }
        else {
          throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
        }

        serialization.sql.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
      }
      else if (entities != null) {
        throw new UnsupportedOperationException("INSERT of individual columns is not yet implemented");
      }
      else if (xdd != null) {
        throw new UnsupportedOperationException("INSERT of $xdd_data is not yet implemented");
      }
      else {
        throw new RuntimeException("How did we get here?");
      }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int execute(final Transaction transaction) throws SQLException {
      if (entity != null) {
        final Keyword<?> insert = getParentRoot(this);
        final Class<? extends Schema> schema = ((Entity)(((INSERT)insert).entity)).schema();
        DBVendor vendor = null;
        try {
          final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
          vendor = Schema.getDBVendor(connection);
          final Serialization serialization = new Serialization(Insert.class, vendor, EntityRegistry.getStatementType(schema));
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
          throw SQLExceptionCatalog.lookup(e);
        }
      }
      else if (entities != null) {
        throw new UnsupportedOperationException("INSERT of individual columns is not yet implemented");
      }
      else if (xdd != null) {
        throw new UnsupportedOperationException("INSERT of $xdd_data is not yet implemented");
      }
      else {
        throw new RuntimeException("How did we get here?");
      }
    }

    @Override
    public int execute() throws SQLException {
      return execute(null);
    }

    @Override
    public select._SELECT<T> SELECT(final select.SELECT<T> select) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final T ... entities) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final ALL all, final T ... entities) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final DISTINCT distinct, final T ... entities) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final ALL all, final DISTINCT distinct, final T ... entities) {
      throw new UnsupportedOperationException();
    }
  }
}