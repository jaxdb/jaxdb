/* Copyright (c) 2022 JAX-DB
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

package org.jaxdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

abstract class Notifiable {
  /**
   * Called when a new {@link Connection} is established for the context of a {@link data.Table}.
   *
   * @param connection The {@link Connection}.
   * @param table The {@link data.Table}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  abstract void onConnect(Connection connection, data.Table table) throws IOException, SQLException;

  /**
   * Called when an unhandled failure is encountered.
   *
   * @param sessionId The session ID.
   * @param timestamp The timestamp (in microseconds) of the NOTIFY invocation.
   * @param table The {@link data.Table}.
   * @param e The unhandled {@link Exception}.
   */
  abstract void onFailure(String sessionId, long timestamp, data.Table table, Exception e);

  abstract void onSelect(data.Table row);
  abstract void onInsert(String sessionId, long timestamp, data.Table row);
  abstract void onUpdate(String sessionId, long timestamp, data.Table row, Map<String,String> keyForUpdate);
  abstract void onDelete(String sessionId, long timestamp, data.Table row);
}