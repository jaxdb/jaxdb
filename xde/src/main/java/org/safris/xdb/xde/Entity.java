/* Copyright (c) 2014 Seva Safris
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

public abstract class Entity extends Subject<Entity> {
  private final boolean wasSelected;

  protected Entity(final boolean wasSelected, final DataType<?>[] dataType, final DataType<?>[] primary) {
    this.wasSelected = wasSelected;
  }

  protected Entity(final Entity entity) {
    this.wasSelected = false;
  }

  protected Entity() {
    this.wasSelected = false;
  }

  protected Entity entity() {
    return null;
  }

  protected boolean wasSelected() {
    return wasSelected;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    serialization.sql.append(tableName(this, serialization));
    final String alias = tableAlias(this, true);
    if (serialization.getType() == Select.class)
      serialization.sql.append(" ").append(alias);
  }

  @SuppressWarnings("unchecked")
  protected Class<? extends Schema> schema() {
    return (Class<? extends Schema>)getClass().getEnclosingClass();
  }

  protected abstract String name();
  protected abstract DataType<?>[] column();
  protected abstract DataType<?>[] primary();
  protected abstract Entity newInstance();
}