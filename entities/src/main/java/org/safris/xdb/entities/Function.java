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

package org.safris.xdb.entities;

public class Function<T> extends Subject<T> {
  private final String function;
  protected final DML.SetQualifier qualifier;
  protected final DataType<T> dataType;

  protected Function(final String function, final DML.SetQualifier qualifier, final DataType<T> dataType) {
    this.function = function;
    this.qualifier = qualifier;
    this.dataType = dataType;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    serialization.append(function).append("(");
    if (dataType != null) {
      tableAlias(dataType.owner(), true);
      if (qualifier != null)
        serialization.append(qualifier.toString()).append(" ");

      serialization.append(dataType.toString());
    }

    serialization.append(")");
  }
}