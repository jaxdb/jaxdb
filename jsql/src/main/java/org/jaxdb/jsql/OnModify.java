/* Copyright (c) 2023 JAX-DB
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

/**
 * Interface used define runtime actions to be made on {@link data.Column}s upon modification.
 *
 * @param <T> The type parameter of the {@link data.Table}.
 */
public interface OnModify<T extends data.Table> {
  /**
   * Called when a change is made to the "cur" value of the {@link data.Column} in context.
   *
   * @param table A reference to {@code Table.this} for the {@link data.Column} in context of the caller.
   */
  default void changeCur(T table) {
  }

  /**
   * Called when a change is made to the "old" value of the {@link data.Column} in context.
   *
   * @param table A reference to {@code Table.this} for the {@link data.Column} in context of the caller.
   */
  default void changeOld(T table) {
  }

  /**
   * Called when an UPDATE to the {@link data.Column} in context is committed in lieu of NOTIFY.
   *
   * @param table A reference to {@code Table.this} for the {@link data.Column} in context of the caller.
   */
  void update(T table);
}