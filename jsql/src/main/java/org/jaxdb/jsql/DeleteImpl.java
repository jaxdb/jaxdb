/* Copyright (c) 2015 JAX-DB
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

package org.jaxdb.jsql;

final class DeleteImpl {
  static final class WHERE extends Executable.Keyword<type.DataType<?>> implements Delete.DELETE {
    final Condition<?> condition;

    WHERE(final Executable.Keyword<type.DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    final DeleteCommand buildCommand() {
      final DeleteCommand command = (DeleteCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  static final class DELETE extends Executable.Keyword<type.DataType<?>> implements Delete._DELETE {
    final type.Entity entity;

    DELETE(final type.Entity entity) {
      super(null);
      this.entity = entity;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    final DeleteCommand buildCommand() {
      return new DeleteCommand(this);
    }
  }
}