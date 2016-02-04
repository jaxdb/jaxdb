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