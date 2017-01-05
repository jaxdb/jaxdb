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

package org.safris.xdb.entities;

class Case {
  protected static abstract class CASE<T> extends Expression<Variable<T>> implements org.safris.xdb.entities.spec.expression.CASE<T> {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      throw new Error("Have to override this");
    }
  }

  protected static final class CASE_WHEN<T> extends Keyword<Variable<T>> implements org.safris.xdb.entities.spec.expression.WHEN<T> {
    private final Condition<T> condition;

    protected CASE_WHEN(final Condition<T> condition) {
      this.condition = condition;
    }

    @Override
    public THEN<T> THEN(final Variable<T> variable) {
      return new THEN<T>(this, variable);
    }

    @Override
    public THEN<T> THEN(final T value) {
      return new THEN<T>(this, Variable.valueOf(value));
    }

    @Override
    protected Keyword<Variable<T>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.append("CASE WHEN ");
      throw new UnsupportedOperationException("implement this");
      //serialize(condition, serialization);
    }
  }

  protected static final class THEN<T> extends Keyword<Variable<T>> implements org.safris.xdb.entities.spec.expression.THEN<T> {
    private final Keyword<Variable<T>> parent;
    private final Variable<T> value;

    protected THEN(final Keyword<Variable<T>> parent, final Variable<T> value) {
      this.parent = parent;
      this.value = value;
    }

    @Override
    public THEN<T> WHEN(final Condition<T> condition) {
      throw new UnsupportedOperationException("implement this");
      //return new THEN<T>(this, condition);
    }

    @Override
    public ELSE<T> ELSE(final Variable<T> variable) {
      return new ELSE<T>(this, variable);
    }

    @Override
    public ELSE<T> ELSE(final T value) {
      return new ELSE<T>(this, Variable.valueOf(value));
    }

    @Override
    protected Keyword<Variable<T>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      //serialize(parent, serialization);
      serialization.append(" THEN ");
      value.serialize(this, serialization);
      throw new UnsupportedOperationException("implement this");
    }
  }

  protected static final class ELSE<T> extends CASE<T> implements org.safris.xdb.entities.spec.expression.ELSE<T> {
    private final THEN<T> parent;
    private final Variable<T> value;

    protected ELSE(final THEN<T> parent, final Variable<T> value) {
      this.parent = parent;
      this.value = value;
    }

    @Override
    protected Keyword<Variable<T>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
//      serialize(parent, serialization);
      serialization.append(" ELSE ");
      value.serialize(this, serialization);
      throw new UnsupportedOperationException("implement this");
    }
  }

  private Case() {
  }
}