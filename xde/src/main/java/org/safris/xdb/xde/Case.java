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
  protected static abstract class CASE<T> extends Expression<T> implements org.safris.xdb.xde.csql.expression.CASE<T> {
    protected void serialize(final Serialization serialization) {
      throw new Error("Have to override this");
    }
  }

  protected static final class CASE_WHEN<T> extends cSQL<T> implements org.safris.xdb.xde.csql.expression.WHEN<T> {
    private final Condition<T> condition;

    protected CASE_WHEN(final Condition<T> condition) {
      this.condition = condition;
    }

    public THEN<T> THEN(final Column<T> column) {
      return new THEN<T>(this, column);
    }

    public THEN<T> THEN(final T value) {
      return new THEN<T>(this, cSQL.valueOf(value));
    }

    protected cSQL<?> parent() {
      return condition;
    }

    protected void serialize(final Serialization serialization) {
      serialization.sql.append("CASE WHEN ");
      serialize(condition, serialization);
    }
  }

  protected static final class THEN<T> extends cSQL<T> implements org.safris.xdb.xde.csql.expression.THEN<T> {
    private final cSQL<T> parent;
    private final cSQL<?> value;

    protected <B>THEN(final cSQL<T> parent, final cSQL<B> value) {
      this.parent = parent;
      this.value = value;
    }

    public THEN<T> WHEN(final Condition<T> condition) {
      return new THEN<T>(this, condition);
    }

    public ELSE<T> ELSE(final Column<T> column) {
      return new ELSE<T>(this, column);
    }

    public ELSE<T> ELSE(final T value) {
      return new ELSE<T>(this, cSQL.valueOf(value));
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      serialize(parent, serialization);
      serialization.sql.append(" THEN ");
      serialize(value, serialization);
    }
  }

  protected static final class ELSE<T> extends CASE<T> implements org.safris.xdb.xde.csql.expression.ELSE<T> {
    private final THEN<T> parent;
    private final cSQL<?> value;

    protected <B>ELSE(final THEN<T> parent, final cSQL<B> value) {
      this.parent = parent;
      this.value = value;
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      serialize(parent, serialization);
      serialization.sql.append(" ELSE ");
      serialize(value, serialization);
    }
  }

  private Case() {
  }
}