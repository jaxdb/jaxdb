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
import java.util.Map;

public final class Notification {
  public abstract static class Action implements Comparable<Action>, Serializable {
    public static final class INSERT extends Action {
      private INSERT(final String name, final byte ordinal) {
        super(name, ordinal);
      }
    }

    public static final INSERT INSERT = new INSERT("INSERT", (byte)0);

    public static class UP extends Action {
      private UP(final String name, final byte ordinal) {
        super(name, ordinal);
      }
    }

    public static final class UPDATE extends UP {
      private UPDATE() {
        super("UPDATE", (byte)1);
      }
    }

    public static final class UPGRADE extends UP {
      private UPGRADE() {
        super("UPGRADE", (byte)1);
      }
    }

    // NOTE: UPDATE and UPGRADE have the same ordinal, so that they cannot both specified alongside each other
    public static final UPDATE UPDATE = new UPDATE();
    public static final UPGRADE UPGRADE = new UPGRADE();

    public static final class DELETE extends Action {
      private DELETE(final String name, final byte ordinal) {
        super(name, ordinal);
      }
    }

    public static final DELETE DELETE = new DELETE("DELETE", (byte)2);

    public static Action valueOf(final String name) {
      return "INSERT".equals(name) ? INSERT : "UPDATE".equals(name) ? UPDATE : "UPGRADE".equals(name) ? UPGRADE : "DELETE".equals(name) ? DELETE : null;
    }

    private final byte ordinal;
    private final String name;

    private Action(final String name, final byte ordinal) {
      this.ordinal = ordinal;
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

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  public interface InsertListener<T extends data.Table> extends Listener<T> {
    T onInsert(T row);
  }

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  public interface UpdateListener<T extends data.Table> extends Listener<T> {
    T onUpdate(T row);
  }

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  public interface UpgradeListener<T extends data.Table> extends Listener<T> {
    T onUpgrade(T row, Map<String,String> keyForUpdate);
  }

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  public interface DeleteListener<T extends data.Table> extends Listener<T> {
    T onDelete(T row);
  }

  @SuppressWarnings("rawtypes")
  public interface Listener<T extends data.Table> {
  }

  private Notification() {
  }
}