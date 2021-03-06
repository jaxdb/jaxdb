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

final class InsertImpl {
  static final class VALUES<T extends type.Subject<?>> extends BatchableKeyword<T> implements Insert.VALUES<T> {
    final Select.untyped.SELECT<?> select;

    VALUES(final BatchableKeyword<T> parent, final Select.untyped.SELECT<?> select) {
      super(parent);
      this.select = select;
    }

    @Override
    InsertCommand buildCommand() {
      final InsertCommand command = (InsertCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  static final class INSERT<T extends type.Subject<?>> extends BatchableKeyword<T> implements Insert._INSERT<T> {
    final type.Entity entity;
    final type.DataType<?>[] columns;

    INSERT(final type.Entity entity) {
      super(null);
      this.entity = entity;
      this.columns = null;
    }

    @SafeVarargs
    INSERT(final type.DataType<?> ... columns) {
      super(null);
      this.entity = null;
      this.columns = columns;
      final type.Entity entity = columns[0].owner;
      if (entity == null)
        throw new IllegalArgumentException("DataType must belong to an Entity");

      for (int i = 1; i < columns.length; ++i)
        if (!columns[i].owner.equals(entity))
          throw new IllegalArgumentException("All columns must belong to the same Entity");
    }

    @Override
    final InsertCommand buildCommand() {
      return new InsertCommand(this);
    }

    @Override
    public Insert.VALUES<T> VALUES(final Select.untyped.SELECT<?> select) {
      return new VALUES<>(this, select);
    }
  }
}