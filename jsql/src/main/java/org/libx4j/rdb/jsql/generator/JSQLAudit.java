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
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Binary;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Blob;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Boolean;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Char;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Clob;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Date;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Datetime;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Decimal;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Enum;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Float;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Integer;
import org.libx4j.rdb.jsql098.xLzAluECXYQJdhA.$Time;

public class JSQLAudit extends DDLxAudit {
  protected JSQLAudit(final DDLxAudit copy) {
    super(copy);
  }

  public boolean isKeyForUpdate(final xLzAluECXYQJdhA.$Column column) {
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