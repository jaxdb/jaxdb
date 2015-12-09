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

class Insert {
  protected static class INSERT extends cSQL<Table> implements org.safris.xdb.xde.csql.insert.INSERT {
    protected final Table table;

    protected INSERT(final Table table) {
      this.table = table;
    }

    protected cSQL<?> parent() {
      return null;
    }

    protected void serialize(final Serialization serialization) {
      serialization.sql.append("INSERT INTO ").append(table.name());
      String columns = "";
      String values = "";
      if (serialization.prototype == PreparedStatement.class) {
        for (final Column column : table.column()) {
          columns += ", " + column.name;
          values += ", ?";
          serialization.parameters.add(column.get());
        }
      }
      else if (serialization.prototype == Statement.class) {
        for (final Column<?> column : table.column()) {
          columns += ", " + column.name;
          values += ", " + cSQLObject.toString(column.get());
        }
      }
      else {
        throw new UnsupportedOperationException("Unsupported Statement prototype class: " + serialization.prototype.getName());
      }

      serialization.sql.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
    }

    public int execute() throws SQLException {
      final cSQL<?> insert = getParentRoot(this);
      final Class<? extends Schema> schema = (((INSERT)insert).table).schema();
      try (final Connection connection = Schema.getConnection(schema)) {
        final Serialization serialization = new Serialization(Schema.getDBVendor(connection), EntityDataSources.getPrototype(schema));
        serialize(serialization);
        clearAliases();
        if (serialization.prototype == PreparedStatement.class) {
          try (final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString())) {
            serialization.set(statement);
            return statement.executeUpdate();
          }
        }

        if (serialization.prototype == Statement.class) {
          try (final Statement statement = connection.createStatement()) {
            return statement.executeUpdate(serialization.sql.toString());
          }
        }

        throw new UnsupportedOperationException("Unsupported Statement prototype class: " + serialization.prototype.getName());
      }
    }
  }
}