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

package org.libx4j.rdb.dmlx;

import org.lib4j.util.Hexadecimal;
import org.libx4j.rdb.dmlx.xe.$dmlx_binary;
import org.libx4j.rdb.dmlx.xe.$dmlx_blob;
import org.libx4j.rdb.dmlx.xe.$dmlx_boolean;
import org.libx4j.rdb.dmlx.xe.$dmlx_char;
import org.libx4j.rdb.dmlx.xe.$dmlx_date;
import org.libx4j.rdb.dmlx.xe.$dmlx_dateTime;
import org.libx4j.rdb.dmlx.xe.$dmlx_time;
import org.libx4j.rdb.vendor.DBVendor;

final class OracleSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  protected String serialize(final $dmlx_char attribute) {
    final String value = attribute.text().replace("'", "''");
    return value.length() == 0 || value.charAt(0) == ' ' ? "' " + value + "'" : "'" + value + "'";
  }

  @Override
  protected String serialize(final $dmlx_blob attribute) {
    return "HEXTORAW('" + new Hexadecimal(attribute.text()) + "')";
  }

  @Override
  protected String serialize(final $dmlx_binary attribute) {
    return "HEXTORAW('" + new Hexadecimal(attribute.text()) + "')";
  }

  @Override
  protected String serialize(final $dmlx_date attribute) {
    return "TO_DATE('" + attribute.text() + "','YYYY-MM-DD')";
  }

  @Override
  protected String serialize(final $dmlx_time attribute) {
    return "'0 " + attribute.text() + "'";
  }

  @Override
  protected String serialize(final $dmlx_dateTime attribute) {
    return "TO_TIMESTAMP('" + attribute.text() + "', 'YYYY-MM-DD HH24:MI:SS.FF')";
  }

  @Override
  protected String serialize(final $dmlx_boolean attribute) {
    return attribute.text() ? "1" : "0";
  }
}