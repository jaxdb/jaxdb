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
import org.libx4j.rdb.ddlx_0_9_8.xLzgluGCXYYJc;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Binary;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Blob;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Boolean;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Char;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Clob;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Date;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Datetime;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Decimal;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Enum;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Float;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Integer;
import org.libx4j.rdb.jsql_0_9_8.xLzgluGCXYYJc.$Time;

public class JSQLAudit extends DDLxAudit {
  protected JSQLAudit(final DDLxAudit copy) {
    super(copy);
  }

  public boolean isKeyForUpdate(final xLzgluGCXYYJc.$Column column) {
    if (column instanceof $Char)
      return (($Char)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Clob)
      return (($Clob)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Binary)
      return (($Binary)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Blob)
      return (($Blob)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Integer)
      return (($Integer)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Float)
      return (($Float)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Decimal)
      return (($Decimal)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Date)
      return (($Date)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Time)
      return (($Time)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Datetime)
      return (($Datetime)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Boolean)
      return (($Boolean)column).getJsqlKeyForUpdate$().text();

    if (column instanceof $Enum)
      return (($Enum)column).getJsqlKeyForUpdate$().text();

    throw new UnsupportedOperationException("Unsupported column type: " + column.getClass().getName());
  }
}