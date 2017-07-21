/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql.generator;

import org.libx4j.rdb.ddlx.DDLxAudit;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.jsql.xe.$jsql_binary;
import org.libx4j.rdb.jsql.xe.$jsql_blob;
import org.libx4j.rdb.jsql.xe.$jsql_boolean;
import org.libx4j.rdb.jsql.xe.$jsql_char;
import org.libx4j.rdb.jsql.xe.$jsql_clob;
import org.libx4j.rdb.jsql.xe.$jsql_date;
import org.libx4j.rdb.jsql.xe.$jsql_dateTime;
import org.libx4j.rdb.jsql.xe.$jsql_decimal;
import org.libx4j.rdb.jsql.xe.$jsql_enum;
import org.libx4j.rdb.jsql.xe.$jsql_float;
import org.libx4j.rdb.jsql.xe.$jsql_integer;
import org.libx4j.rdb.jsql.xe.$jsql_time;

public class JSQLAudit extends DDLxAudit {
  protected JSQLAudit(final DDLxAudit copy) {
    super(copy);
  }

  public boolean isKeyForUpdate(final $ddlx_column column) {
    if (column instanceof $jsql_char)
      return (($jsql_char)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_clob)
      return (($jsql_clob)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_binary)
      return (($jsql_binary)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_blob)
      return (($jsql_blob)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_integer)
      return (($jsql_integer)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_float)
      return (($jsql_float)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_decimal)
      return (($jsql_decimal)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_date)
      return (($jsql_date)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_time)
      return (($jsql_time)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_dateTime)
      return (($jsql_dateTime)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_boolean)
      return (($jsql_boolean)column).jsql_keyForUpdate$().text();

    if (column instanceof $jsql_enum)
      return (($jsql_enum)column).jsql_keyForUpdate$().text();

    throw new UnsupportedOperationException("Unsupported column type: " + column.getClass().getName());
  }
}