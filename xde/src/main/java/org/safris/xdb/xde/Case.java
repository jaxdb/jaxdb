/* Copyright (c) 2015 Seva Safris
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

class Case {
  protected static abstract class CASE<T> extends Expression<Field<T>> implements org.safris.xdb.xde.csql.expression.CASE<T> {
    @Override
    protected void serialize(final Serialization serialization) {
      throw new Error("Have to override this");
    }
  }

  protected static final class CASE_WHEN<T> extends Keyword<Field<T>> implements org.safris.xdb.xde.csql.expression.WHEN<T> {
    private final Condition<T> condition;

    protected CASE_WHEN(final Condition<T> condition) {
      this.condition = condition;
    }

    @Override
    public THEN<T> THEN(final Field<T> field) {
      return new THEN<T>(this, field);
    }

    @Override
    public THEN<T> THEN(final T value) {
      return new THEN<T>(this, Field.valueOf(value));
    }

    @Override
    protected Keyword<Field<T>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("CASE WHEN ");
      throw new RuntimeException("implement this");
      //serialize(condition, serialization);
    }
  }

  protected static final class THEN<T> extends Keyword<Field<T>> implements org.safris.xdb.xde.csql.expression.THEN<T> {
    private final Keyword<Field<T>> parent;
    private final Field<T> value;

    protected THEN(final Keyword<Field<T>> parent, final Field<T> value) {
      this.parent = parent;
      this.value = value;
    }

    @Override
    public THEN<T> WHEN(final Condition<T> condition) {
      throw new RuntimeException("implement this");
      //return new THEN<T>(this, condition);
    }

    @Override
    public ELSE<T> ELSE(final Field<T> field) {
      return new ELSE<T>(this, field);
    }

    @Override
    public ELSE<T> ELSE(final T value) {
      return new ELSE<T>(this, Field.valueOf(value));
    }

    @Override
    protected Keyword<Field<T>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      //serialize(parent, serialization);
      serialization.sql.append(" THEN ");
      value.serialize(serialization);
      throw new RuntimeException("implement this");
    }
  }

  protected static final class ELSE<T> extends CASE<T> implements org.safris.xdb.xde.csql.expression.ELSE<T> {
    private final THEN<T> parent;
    private final Field<T> value;

    protected ELSE(final THEN<T> parent, final Field<T> value) {
      this.parent = parent;
      this.value = value;
    }

    @Override
    protected Keyword<Field<T>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serialization serialization) {
//      serialize(parent, serialization);
      serialization.sql.append(" ELSE ");
      value.serialize(serialization);
      throw new RuntimeException("implement this");
    }
  }

  private Case() {
  }
}