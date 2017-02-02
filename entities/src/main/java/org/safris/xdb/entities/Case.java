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

final class Case extends SQLStatement {
  protected static abstract class CASE<T> extends Keyword<Subject<T>> implements org.safris.xdb.entities.model.expression.CASE<T> {
    protected CASE() {
      super(null);
    }
  }

  protected static final class CASE_WHEN extends Keyword<Subject<?>> implements org.safris.xdb.entities.model.expression.WHEN {
    private final Condition<?> condition;

    protected CASE_WHEN(final Condition<?> condition) {
      super(null);
      this.condition = condition;
    }

    @Override
    public <T>THEN THEN(final type.DataType<T> dataType) {
      return new THEN(this, dataType);
    }

    @Override
    public <T>THEN THEN(final T value) {
      return new THEN(this, type.DataType.wrap(value));
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class THEN extends Keyword<Subject<?>> implements org.safris.xdb.entities.model.expression.THEN {
    private final type.DataType<?> value;

    protected THEN(final Keyword<Subject<?>> parent, final type.DataType<?> value) {
      super(parent);
      this.value = value;
    }

    @Override
    public <T>THEN WHEN(final Condition<T> condition) {
      throw new UnsupportedOperationException("implement this");
      //return new THEN<T>(this, condition);
    }

    @Override
    public <T>ELSE<T> ELSE(final type.DataType<T> dataType) {
      return new ELSE<T>(this, dataType);
    }

    @Override
    public <T>ELSE<T> ELSE(final T value) {
      return new ELSE<T>(this, type.DataType.wrap(value));
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class ELSE<T> extends CASE<T> implements org.safris.xdb.entities.model.expression.ELSE<T> {
    private final type.DataType<T> value;

    protected ELSE(final Keyword<?> parent, final type.DataType<T> value) {
      this.value = value;
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  private Case() {
  }
}