/* Copyright (c) 2016 Seva Safris
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

import java.sql.PreparedStatement;
import java.sql.Statement;

public abstract class Keyword<T extends Data<?>> extends Serializable {
  protected static void format(final Object obj, final Serialization serialization) {
    if (obj instanceof DataType<?>) {
      ((DataType<?>)obj).serialize(serialization);
    }
    else if (serialization.statementType == PreparedStatement.class) {
      if (obj == null) {
        serialization.sql.append("NULL");
      }
      else {
        serialization.addParameter(obj);
        serialization.sql.append("?");
      }
    }
    else if (serialization.statementType == Statement.class) {
      serialization.sql.append(FieldWrapper.toString(obj));
    }
    else {
      throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
    }
  }

  protected Keyword<?> getParentRoot(Keyword<?> keyword) {
    while (keyword.parent() != null)
      keyword = keyword.parent();

    return keyword;
  }

  protected abstract Keyword<T> parent();
}