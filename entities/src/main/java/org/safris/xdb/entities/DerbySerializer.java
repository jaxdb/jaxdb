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
import java.sql.SQLException;
import java.sql.Statement;

import org.safris.xdb.entities.Select.GROUP_BY;
import org.safris.xdb.entities.Select.SELECT;
import org.safris.xdb.schema.DBVendor;

final class DerbySerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
    final Statement statement = connection.createStatement();
    statement.execute("CREATE FUNCTION POWER(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME 'java.lang.Math.pow'");
    statement.execute("CREATE FUNCTION ROUND(a DOUBLE) RETURNS BIGINT PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME 'java.lang.Math.round'");
  }

  @Override
  protected void serialize(final NumericExpression<?> serializable, final Serialization serialization) throws IOException {
    serialization.append("(");
    serializable.a.serialize(serialization);
    serialization.append(" ").append(serializable.operator.toString()).append(" ");
    serializable.b.serialize(serialization);
    for (int i = 0; i < serializable.args.length; i++) {
      final Serializable arg = serializable.args[i];
      if (arg instanceof Interval) // FIXME: This needs to go to TemporalExpression serializer
        throw new UnsupportedOperationException("Derby does not support INTERVAL: https://db.apache.org/derby/docs/10.8/ref/rrefsql9241891.html");

      serialization.append(" ").append(serializable.operator.toString()).append(" ");
      arg.serialize(serialization);
    }
    serialization.append(")");
  }

  @Override
  protected void serialize(final Select.GROUP_BY<?> groupBy, final Serialization serialization) throws IOException {
    if (groupBy != null) {
      final SelectCommand command = (SelectCommand)serialization.command;
      groupBy.subjects.addAll(command.select().getEntitiesWithOwners());
      super.serialize(groupBy, serialization);
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void serialize(final Select.HAVING<?> having, final Serialization serialization) throws IOException {
    if (having != null) {
      final SELECT<?> select = (SELECT<?>)Keyword.getParentRoot(having.parent());
      final SelectCommand command = (SelectCommand)serialization.command;
      if (command.groupBy() == null) {
        final GROUP_BY<?> groupBy = new GROUP_BY(null, select.getEntitiesWithOwners());
        serialize(groupBy, serialization);
      }

      serialization.append(" HAVING ");
      having.condition.serialize(serialization);
    }
  }

  @Override
  protected void serialize(final Select.LIMIT<?> limit, final Select.OFFSET<?> offset, final Serialization serialization) {
    if (limit != null) {
      if (offset != null)
        serialization.append(" OFFSET ").append(offset.rows).append(" ROWS");

      serialization.append(" FETCH NEXT ").append(limit.rows).append(" ROWS ONLY");
    }
  }
}