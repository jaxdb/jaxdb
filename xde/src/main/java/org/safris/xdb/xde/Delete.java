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

import org.safris.xdb.xdl.DBVendor;

class Delete {
  private static final Logger logger = Logger.getLogger(Delete.class.getName());

  private static abstract class Execute<T extends Data<?>> extends Keyword<T> {
    public int execute() throws XDEException {
      final Entity entity = null;//getParentRoot(this);
      final Class<? extends Schema> schema = entity.schema();
      DBVendor vendor = null;
      try {
        try (final Connection connection = Schema.getConnection(schema)) {
          vendor = Schema.getDBVendor(connection);
          final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));
          serialize(serialization);
          Data.clearAliases();
          if (serialization.statementType == PreparedStatement.class) {
            try (final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString())) {
              serialization.set(statement);
              return statement.executeUpdate();
            }
          }

          if (serialization.statementType == Statement.class) {
            try (final Statement statement = connection.createStatement()) {
              return statement.executeUpdate(serialization.sql.toString());
            }
          }

          throw new UnsupportedOperationException("Unsupported Statement type: " + serialization.statementType.getName());
        }
      }
      catch (final SQLException e) {
        throw XDEException.lookup(e, vendor);
      }
    }
  }

  protected final static class WHERE<T extends Data<?>> extends Execute<T> implements org.safris.xdb.xde.csql.delete.DELETE<T> {
    private final Field<T> parent;
    private final Condition<?> condition;

    protected WHERE(final Field<T> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    @Override
    protected Keyword<T> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      parent.serialize(serialization);
      serialization.sql.append(" WHERE ");
      condition.serialize(serialization);
    }
  }

  protected final static class DELETE<T extends Data<?>> extends Execute<T> implements org.safris.xdb.xde.csql.delete.DELETE_WHERE<T> {
    protected final Entity entity;

    protected DELETE(final Entity entity) {
      this.entity = entity;
    }

    @Override
    public WHERE<T> WHERE(final Condition<?> condition) {
      return null;//new WHERE<T>(this, condition);
    }

    @Override
    protected Keyword<T> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("UPDATE ");
      entity.serialize(serialization);
    }

    protected String encodeSingle(final Serialization serialization) {
      if (getClass() != DELETE.class) // means that there are subsequent clauses
        throw new Error("Need to override this");

      String sql = "UPDATE " + entity.name() + " SET ";
      String columns = "";
      String where = "";
      for (final DataType<?> dataType : entity.column()) {
        if (dataType.primary)
          where += " AND " + dataType.name + " = ?";
        else
          columns += ", " + dataType.name + " = ?";
      }

      sql += columns.substring(2) + " WHERE " + where.substring(5);
      logger.info(sql);
      return sql;
    }

    @Override
    public int execute() throws XDEException {
      if (false) {
        final Entity entity = null;//getParentRoot(this);
        final Class<? extends Schema> schema = entity.schema();
        DBVendor vendor = null;
        try {
          try (final Connection connection = Schema.getConnection(schema)) {
            vendor = Schema.getDBVendor(connection);
            final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));
            final String sql = encodeSingle(serialization);
            logger.info(sql);
            if (true)
              return 0;

            try (final PreparedStatement statement = connection.prepareStatement(sql)) {
              // set the updated columns first
              int index = 0;
              for (final DataType<?> dataType : entity.column())
                if (!dataType.primary)
                  dataType.set(statement, ++index);

              // then the conditional columns
              for (final DataType<?> dataType : entity.column())
                if (dataType.primary)
                  dataType.set(statement, ++index);

              logger.info(statement.toString());
              return statement.executeUpdate();
            }
          }
        }
        catch (final SQLException e) {
          throw XDEException.lookup(e, vendor);
        }
      }

      Data.clearAliases();
      return super.execute();
    }
  }
}