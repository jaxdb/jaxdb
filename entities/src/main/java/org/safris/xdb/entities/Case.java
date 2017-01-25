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

import java.io.IOException;

final class Case extends SQLStatement {
  protected static abstract class CASE<T> extends Keyword<Subject<T>> implements org.safris.xdb.entities.spec.expression.CASE<T> {
    protected CASE() {
      super(null);
    }
  }

  protected static final class CASE_WHEN<T> extends Keyword<Subject<T>> implements org.safris.xdb.entities.spec.expression.WHEN<T> {
    private final Condition<T> condition;

    protected CASE_WHEN(final Condition<T> condition) {
      super(null);
      this.condition = condition;
    }

    @Override
    public THEN<T> THEN(final DataType<T> dataType) {
      return new THEN<T>(this, dataType);
    }

    @Override
    public THEN<T> THEN(final T value) {
      return new THEN<T>(this, DataType.wrap(value));
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    protected CASE_WHEN<T> clone(final Keyword<Subject<T>> parent) {
      return new CASE_WHEN<T>(condition);
    }
  }

  protected static final class THEN<T> extends Keyword<Subject<T>> implements org.safris.xdb.entities.spec.expression.THEN<T> {
    private final DataType<T> value;

    protected THEN(final Keyword<Subject<T>> parent, final DataType<T> value) {
      super(parent);
      this.value = value;
    }

    @Override
    public THEN<T> WHEN(final Condition<T> condition) {
      throw new UnsupportedOperationException("implement this");
      //return new THEN<T>(this, condition);
    }

    @Override
    public ELSE<T> ELSE(final DataType<T> dataType) {
      return new ELSE<T>(this, dataType);
    }

    @Override
    public ELSE<T> ELSE(final T value) {
      return new ELSE<T>(this, DataType.wrap(value));
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    protected THEN<T> clone(final Keyword<Subject<T>> parent) {
      return new THEN<T>(parent, value);
    }
  }

  protected static final class ELSE<T> extends CASE<T> implements org.safris.xdb.entities.spec.expression.ELSE<T> {
    private final DataType<T> value;

    protected ELSE(final Keyword<?> parent, final DataType<T> value) {
      this.value = value;
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    protected ELSE<T> clone(final Keyword<Subject<T>> parent) {
      return new ELSE<T>(parent, value);
    }
  }

  private Case() {
  }
}