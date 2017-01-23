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
import java.math.BigInteger;

import org.safris.commons.io.Streams;
import org.safris.xdb.entities.data.Blob;
import org.safris.xdb.entities.data.Enum;
import org.safris.xdb.schema.DBVendor;
import org.safris.xdb.schema.spec.PostgreSQLSpec;

final class PostreSQLSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  protected String getPreparedStatementMark(final DataType<?> dataType) {
    return dataType instanceof Enum ? "?::" + PostgreSQLSpec.getTypeName(dataType.owner.name(), dataType.name) : "?";
  }

  @Override
  public String serialize(final Blob dataType) throws IOException {
    if (dataType.get() == null)
      return "NULL";

    final BigInteger integer = new BigInteger(Streams.getBytes(dataType.get()));
    return "E'\\" + integer.toString(8); // FIXME: This is only half done
  }
}