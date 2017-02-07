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

package org.safris.xdb.entities.model;

import org.safris.xdb.entities.Condition;
import org.safris.xdb.entities.Entity;
import org.safris.xdb.entities.Select;
import org.safris.xdb.entities.Subject;
import org.safris.xdb.entities.type;

public interface select {
  public interface OFFSET<T extends Subject<?>> extends SELECT<T> {
  }

  public interface _LIMIT<T extends Subject<?>> {
    public LIMIT<T> LIMIT(final int rows);
  }

  public interface LIMIT<T extends Subject<?>> extends SELECT<T> {
    public OFFSET<T> OFFSET(final int rows);
  }

  public interface _ORDER_BY<T extends Subject<?>> {
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
  }

  public interface ORDER_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T> {
  }

  public interface _GROUP_BY<T extends Subject<?>> {
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
  }

  public interface GROUP_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
  }

  public interface _HAVING<T extends Subject<?>> {
    public HAVING<T> HAVING(final Condition<?> condition);
  }

  public interface HAVING<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
  }

  public interface _WHERE<T extends Subject<?>> {
    public WHERE<T> WHERE(final Condition<?> condition);
  }

  public interface WHERE<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
  }

  public interface _ON<T extends Subject<?>> {
    public Select.ON<T> ON(final Condition<?> condition);
  }

  public interface ON<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
  }

  public interface _JOIN<T extends Subject<?>> {
    public interface CROSS<T extends Subject<?>> {
      public ADV_JOIN<T> JOIN(final Entity table);
    }

    public interface NATURAL<T extends Subject<?>> {
      public ADV_JOIN<T> JOIN(final Entity table);
    }

    public interface OUTER<T extends Subject<?>> {
      public JOIN<T> JOIN(final Entity table);
    }

    public JOIN<T> JOIN(final Entity table);
  }

  public interface JOIN<T extends Subject<?>> extends _JOIN<T>, _ON<T> {
  }

  public interface ADV_JOIN<T extends Subject<?>> extends SELECT<T>, _JOIN<T> {
  }

  public interface _FROM<T extends Subject<?>> {
    public Select.FROM<T> FROM(final Entity ... tables);
  }

  public interface FROM<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
  }

  public interface _SELECT<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
  }

  public interface _UNION<T extends Subject<?>> {
    public UNION<T> UNION(final SELECT<T> union);

    public interface ALL<T extends Subject<?>> {
      public UNION<T> ALL(final SELECT<T> union);
    }

    public ALL<T> UNION();
  }

  public interface UNION<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T> {
  }

  public interface SELECT<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T> {
    public T AS(final T as);
  }
}