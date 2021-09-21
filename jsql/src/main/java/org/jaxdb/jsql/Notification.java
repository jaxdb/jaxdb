/* Copyright (c) 2021 JAX-DB
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

import java.io.Serializable;

public class Notification {
  public static class Action implements Comparable<Action>, Serializable {
    private static final long serialVersionUID = -331494081209439532L;

    public static final class INSERT extends Action {
      private static final long serialVersionUID = -3312391997642865716L;

      private INSERT(final String name) {
        super(name);
      }
    }

    public static final INSERT INSERT;

    public static final class UPDATE extends Action {
      private static final long serialVersionUID = 8510578744535577243L;

      private UPDATE(final String name) {
        super(name);
      }
    }

    public static final UPDATE UPDATE;

    public static final class DELETE extends Action {
      private static final long serialVersionUID = 3787235180101056533L;

      private DELETE(final String name) {
        super(name);
      }
    }

    public static final DELETE DELETE;

    private static byte index = 0;

    private static final Action[] values = {
      INSERT = new INSERT("INSERT"),
      UPDATE = new UPDATE("UPDATE"),
      DELETE = new DELETE("DELETE")
    };

    public static Action[] values() {
      return values;
    }

    public static Action valueOf(final String name) {
      return "INSERT".equals(name) ? INSERT : "UPDATE".equals(name) ? UPDATE : "DELETE".equals(name) ? DELETE : null;
    }

    private final byte ordinal;
    private final String name;

    private Action(final String name) {
      this.ordinal = index++;
      this.name = name;
    }

    public byte ordinal() {
      return ordinal;
    }

    @Override
    public int compareTo(final Action o) {
      return name.compareTo(o.name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  @FunctionalInterface
  public interface Listener<T extends data.Table<?>> {
    public abstract void notification(Action action, T row);
  }
}