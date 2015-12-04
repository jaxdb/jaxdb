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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.safris.xdb.xdl.DBVendor;

public abstract class cSQL<T> implements org.safris.xdb.xde.csql.cSQL<T> {
  protected static class cSQLObject<T> extends cSQL<T> {
    protected static String toString(final Object obj) {
      if (obj == null)
        return "NULL";

      if (obj instanceof String)
        return "'" + obj + "'";

      if (obj instanceof LocalDate)
        return "'" + dateFormat.get().format(((LocalDate)obj).toDate()) + "'";

      if (obj instanceof LocalDateTime)
        return "'" + dateTimeFormat.get().format(((LocalDateTime)obj).toDate()) + "'";

      return obj.toString();
    }

    private final T object;

    public cSQLObject(final T object) {
      this.object = object;
    }

    protected cSQL<?> parent() {
      return null;
    }

    protected void serialize(final Serialization serialization) {
      if (object == null) {
        serialization.sql.append("NULL");
      }
      else if (serialization.prototype == PreparedStatement.class) {
        serialization.parameters.add(object);
        serialization.sql.append("?");
      }
      else {
        serialization.sql.append(toString(object));
      }
    }

    public String toString() {
      return toString(object);
    }
  }

  protected static <T>cSQL<T> valueOf(final T object) {
    return new cSQLObject<T>(object);
  }

  private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
    protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd");
    }
  };

  private static final ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>() {
    protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }
  };

  // This is implemented as a ThreadLocal variable, because of one main reason:
  // The CQL query construct crosses into static space when applying Condition(s),
  // such as AND, OR, EQ, etc. A ThreadLocal reference to the aliases list works,
  // because the CQL query is rendered immediately upon construct. As it is
  // guaranteed to run linearly in a single thread, a ThreadLocal variable fits.
  private static final ThreadLocal<List<Table>> aliases = new ThreadLocal<List<Table>>() {
    protected List<Table> initialValue() {
      return new ArrayList<Table>();
    }
  };

  protected static void format(final Object obj, final Serialization serialization) {
    if (obj instanceof Column<?>) {
      ((Column<?>)obj).serialize(serialization);
    }
    else if (serialization.prototype == PreparedStatement.class) {
      if (obj == null) {
        serialization.sql.append("NULL");
      }
      else {
        serialization.parameters.add(obj);
        serialization.sql.append("?");
      }
    }
    else if (serialization.prototype == Statement.class) {
      serialization.sql.append(cSQLObject.toString(obj));
    }
    else {
      throw new UnsupportedOperationException("Unsupported Statement prototype class: " + serialization.prototype.getName());
    }
  }

  protected static <B>Object columnRef(final Column<B> column) {
    return tableAlias(column.owner, false) == null ? column.get() : column;
  }

  protected static void clearAliases() {
    aliases.get().clear();
  }

  protected static String tableName(final Table table, final Serialization serialization) {
    if (serialization.vendor == DBVendor.MY_SQL) {
      return table.getClass().getEnclosingClass().getSimpleName() + "." + table.name();
    }

    if (serialization.vendor == DBVendor.POSTGRE_SQL) {
      return table.name();
    }

    throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
  }

  protected static String tableAlias(final Table entity, final boolean register) {
    final List<Table> list = aliases.get();
    int i;
    for (i = 0; i < list.size(); i++)
      if (list.get(i) == entity)
        return String.valueOf((char)('a' + i));

    if (!register)
      return null;

    list.add(entity);
    return String.valueOf((char)('a' + i));
  }

  protected static void serialize(final org.safris.xdb.xde.csql.cSQL<?> csql, final Serialization serialization) {
    if (!(csql instanceof cSQL<?>))
      throw new Error(csql.getClass().getName() + " is not an instance of cSQL");

    ((cSQL<?>)csql).serialize(serialization);
  }

  protected cSQL<?> getParentRoot(cSQL<?> csql) {
    while (csql.parent() != null)
      csql = csql.parent();

    return csql;
  }

  protected abstract cSQL<?> parent();
  protected abstract void serialize(final Serialization serialization);
}