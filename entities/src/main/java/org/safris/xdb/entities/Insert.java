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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.schema.DBVendor;

final class Insert extends SQLStatement {
  protected static final class INSERT extends Keyword<Subject<?>> implements org.safris.xdb.entities.model.insert.INSERT {
    protected final Entity[] entities;
    protected final select.SELECT<?> select;

    @SafeVarargs
    protected INSERT(final Entity ... entities) {
      super(null);
      this.entities = entities;
      this.select = null;
    }

    protected INSERT(final select.SELECT<?> select) {
      super(null);
      this.entities = null;
      this.select = select;
    }

    @Override
    protected final Command normalize() {
      return new InsertCommand(this);
    }

    @Override
    public int[] execute(final Transaction transaction) throws IOException, SQLException {
      final InsertCommand command = (InsertCommand)normalize();

      final Class<? extends Schema> schema;
      if (command.insert().entities != null) {
        schema = command.insert().entities[0].schema();
      }
      else if (command.insert().select != null) {
        final SelectCommand selectCommand = (SelectCommand)((Keyword<?>)command.insert().select).normalize();
        schema = selectCommand.from().tables.iterator().next().schema();
      }
      else {
        throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
      }

      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        final DBVendor vendor = Schema.getDBVendor(connection);

        final Serialization serialization = new Serialization(command, vendor, EntityRegistry.getStatementType(schema));
        command.serialize(serialization);
        final int[] count = serialization.execute(connection);
        if (transaction == null)
          connection.close();

        return count;
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }

    @Override
    public int[] execute() throws IOException, SQLException {
      return execute(null);
    }
  }
}