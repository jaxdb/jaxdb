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
import java.sql.SQLException;

import org.safris.xdb.entities.DML.ALL;
import org.safris.xdb.entities.DML.DISTINCT;
import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.schema.DBVendor;

final class Insert extends SQLStatement {
  protected static final class INSERT<T extends Entity> extends Keyword<Subject<?>> implements org.safris.xdb.entities.model.insert.INSERT_SELECT<T> {
    protected final T[] entities;

    @SafeVarargs
    protected INSERT(final T ... entities) {
      super(null);
      this.entities = entities;
    }

    @Override
    protected final Command normalize() {
      final InsertCommand command = (InsertCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int[] execute(final Transaction transaction) throws SQLException {
      final Keyword<?> insert = getParentRoot(this);
      final Class<? extends Schema> schema = (((INSERT)insert).entities[0]).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = null;
//        final Serialization serialization = new Serialization(Insert.class, vendor, EntityRegistry.getStatementType(schema));
//        serialize(serialization);
        final int[] count = null;//serialization.executeUpdate(connection);

        if (transaction == null)
          connection.close();

        return count;
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }

    @Override
    public int[] execute() throws SQLException {
      return execute(null);
    }

    @Override
    public select._SELECT<T> SELECT(final select.SELECT<T> select) {
      // TODO:
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final T ... entities) {
      // TODO:
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final ALL all, final T ... entities) {
      // TODO:
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final DISTINCT distinct, final T ... entities) {
      // TODO:
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public select._SELECT<T> SELECT(final ALL all, final DISTINCT distinct, final T ... entities) {
      // TODO:
      throw new UnsupportedOperationException();
    }
  }
}