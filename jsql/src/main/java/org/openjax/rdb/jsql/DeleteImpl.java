/* Copyright (c) 2015 OpenJAX
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

package org.openjax.rdb.jsql;

final class DeleteImpl {
  protected static final class WHERE extends BatchableKeyword<type.DataType<?>> implements Delete.DELETE {
    protected final Condition<?> condition;

    protected WHERE(final BatchableKeyword<type.DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    protected final Command normalize() {
      final DeleteCommand command = (DeleteCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class DELETE extends BatchableKeyword<type.DataType<?>> implements Delete._DELETE {
    protected final type.Entity entity;

    protected DELETE(final type.Entity entity) {
      super(null);
      this.entity = entity;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected final Command normalize() {
      return new DeleteCommand(this);
    }
  }
}