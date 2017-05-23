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

package org.safris.rdb.dmlx;

import org.lib4j.lang.Bytes;
import org.lib4j.util.Hexadecimal;
import org.safris.rdb.dmlx.xe.$dmlx_binary;
import org.safris.rdb.dmlx.xe.$dmlx_blob;
import org.safris.rdb.vendor.DBVendor;

final class PostgreSQLSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  private static String toOctalString(final Hexadecimal hex) {
    final short[] octals = Bytes.toOctal(hex.getBytes());
    final StringBuilder builder = new StringBuilder();
    for (final short octal : octals)
      builder.append("\\\\").append(octal);

    return "'" + builder + "'::BYTEA";
  }

  @Override
  protected String serialize(final $dmlx_blob attribute) {
    return toOctalString(new Hexadecimal(attribute.text()));
  }

  @Override
  protected String serialize(final $dmlx_binary attribute) {
    return toOctalString(new Hexadecimal(attribute.text()));
  }
}