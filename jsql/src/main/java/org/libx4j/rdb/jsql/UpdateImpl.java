/* Copyright (c) 2015 lib4j
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

package org.libx4j.rdb.jsql;

final class UpdateImpl {
  private static abstract class UPDATE_SET extends BatchableKeyword<type.DataType<?>> implements Update._SET {
    protected UPDATE_SET(final BatchableKeyword<type.DataType<?>> parent) {
      super(parent);
    }

    @Override
    public final <T>SET SET(final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
      return new SET(this, column, to);
    }

    @Override
    public final <T>SET SET(final type.DataType<T> column, final T to) {
      final type.DataType<T> wrap = type.DataType.wrap(to);
      return new SET(this, column, wrap);
    }
  }

  protected static final class UPDATE extends UPDATE_SET implements Update._SET {
    protected final type.Entity entity;

    protected UPDATE(final type.Entity entity) {
      super(null);
      this.entity = entity;
    }

    @Override
    protected final Command normalize() {
      return new UpdateCommand(this);
    }
  }

  protected static final class SET extends UPDATE_SET implements Update.SET {
    protected final type.DataType<?> column;
    protected final Compilable to;

    protected <T>SET(final BatchableKeyword<type.DataType<?>> parent, final type.DataType<? extends T> column, final Case.CASE<? extends T> to) {
      super(parent);
      this.column = column;
      this.to = (Provision)to;
    }

    protected <T>SET(final BatchableKeyword<type.DataType<?>> parent, final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
      super(parent);
      this.column = column;
      this.to = to;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected final Command normalize() {
      final UpdateCommand command = (UpdateCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class WHERE extends BatchableKeyword<type.DataType<?>> implements Update.UPDATE {
    protected final Condition<?> condition;

    protected WHERE(final BatchableKeyword<type.DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    protected final Command normalize() {
      final UpdateCommand command = (UpdateCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }
}