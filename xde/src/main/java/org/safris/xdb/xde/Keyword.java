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
  protected static void format(final Serializable caller, final Object obj, final Serialization serialization) {
    if (obj instanceof DataType<?>) {
      ((DataType<?>)obj).serialize(caller, serialization);
    }
    else if (serialization.statementType == PreparedStatement.class) {
      if (obj == null) {
        serialization.sql.append("NULL");
      }
      else if (obj instanceof Object[]) {
        serialization.sql.append("(");
        final Object[] arr = (Object[])obj;
        if (arr.length > 0) {
          if (arr[0] != null) {
            serialization.addParameter(FieldWrapper.valueOf(arr[0]));
            serialization.sql.append("?");
          }
          else {
            serialization.sql.append("NULL");
          }

          for (int i = 1; i < arr.length; i++) {
            if (arr[i] != null) {
              serialization.addParameter(FieldWrapper.valueOf(arr[i]));
              serialization.sql.append(", ?");
            }
            else {
              serialization.sql.append("NULL");
            }
          }
        }

        serialization.sql.append(")");
      }
      else {
        serialization.addParameter(FieldWrapper.valueOf(obj));
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

  protected static Keyword<?> getParentRoot(Keyword<?> keyword) {
    while (keyword.parent() != null)
      keyword = keyword.parent();

    return keyword;
  }

  protected abstract Keyword<T> parent();
}