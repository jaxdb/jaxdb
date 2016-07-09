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

package org.safris.xdb.xde.csql;

import org.safris.xdb.xde.Condition;
import org.safris.xdb.xde.Data;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xde.Field;
import org.safris.xdb.xde.DML.NATURAL;
import org.safris.xdb.xde.DML.TYPE;

public interface select {
  public interface _LIMIT<T extends Data<?>> {
    public LIMIT<T> LIMIT(final int limit);
  }

  public interface LIMIT<T extends Data<?>> extends SELECT<T> {
  }

  public interface _ORDER_BY<T extends Data<?>> {
    public ORDER_BY<T> ORDER_BY(final Field<?> ... column);
  }

  public interface ORDER_BY<T extends Data<?>> extends SELECT<T>, _LIMIT<T> {
  }

  public interface _GROUP_BY<T extends Data<?>> {
    public GROUP_BY<T> GROUP_BY(final Field<?> field);
  }

  public interface GROUP_BY<T extends Data<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
  }

  public interface _HAVING<T extends Data<?>> {
    public HAVING<T> HAVING(final Condition<?> condition);
  }

  public interface HAVING<T extends Data<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
  }

  public interface _WHERE<T extends Data<?>> {
    public WHERE<T> WHERE(final Condition<?> condition);
  }

  public interface WHERE<T extends Data<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
  }

  public interface _ON<T extends Data<?>> {
    public ON<T> ON(final Condition<?> condition);
  }

  public interface ON<T extends Data<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
  }

  public interface _JOIN<T extends Data<?>> {
    public JOIN<T> JOIN(final Entity entity);
    public JOIN<T> JOIN(final TYPE type, final Entity entity);
    public JOIN<T> JOIN(final NATURAL natural, final Entity entity);
    public JOIN<T> JOIN(final NATURAL natural, final TYPE type, final Entity entity);
  }

  public interface JOIN<T extends Data<?>> extends _ON<T> {
  }

  public interface _FROM<T extends Data<?>> {
    public FROM<T> FROM(final Entity ... tables);
  }

  public interface FROM<T extends Data<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
  }

  public interface _SELECT<T extends Data<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
  }

  public interface SELECT<T extends Data<?>> extends ExecuteQuery<T> {
  }
}