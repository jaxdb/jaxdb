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

package org.safris.dbx.jsql;

import java.io.IOException;

public abstract class Entity extends Subject<Entity> {
  private final boolean wasSelected;

  protected Entity(final boolean wasSelected, final type.DataType<?>[] column, final type.DataType<?>[] primary) {
    this.wasSelected = wasSelected;
  }

  protected Entity(final Entity entity) {
    this.wasSelected = false;
  }

  protected Entity() {
    this.wasSelected = false;
  }

  protected final boolean wasSelected() {
    return wasSelected;
  }

  @SuppressWarnings("unchecked")
  protected final Class<? extends Schema> schema() {
    return (Class<? extends Schema>)getClass().getEnclosingClass();
  }

  @Override
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }

  protected abstract String name();
  protected abstract type.DataType<?>[] column();
  protected abstract type.DataType<?>[] primary();
  protected abstract Entity newInstance();
}