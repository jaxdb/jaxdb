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

import org.safris.xdb.xde.BooleanCondition.Operator;
import org.safris.xdb.xde.csql.delete.DELETE_WHERE;
import org.safris.xdb.xde.csql.expression.WHEN;
import org.safris.xdb.xde.csql.insert.INSERT;
import org.safris.xdb.xde.csql.select.ORDER_BY;
import org.safris.xdb.xde.csql.select.SELECT;
import org.safris.xdb.xde.csql.select.SELECT_FROM;
import org.safris.xdb.xde.csql.update.UPDATE_SET;

public abstract class DML extends cSQL<Object> {
  /** Direction **/

  protected static abstract class Direction<T> implements ORDER_BY.Column<T> {
    private final Column<?> column;

    public Direction(final Column<?> column) {
      this.column = column;
    }

    protected void serialize(final Serialization serialization) {
      column.serialize(serialization);
      serialization.sql.append(" ").append(getClass().getSimpleName());
    }
  }

  protected static class ASC<T> extends Direction<T> {
    protected ASC(final Column<?> column) {
      super(column);
    }
  }

  public static <T>ASC<T> ASC(final Column<?> column) {
    return new ASC<T>(column);
  }

  protected static class DESC<T> extends Direction<T> {
    protected DESC(final Column<?> column) {
      super(column);
    }
  }

  public static <T>DESC<T> DESC(final Column<?> column) {
    return new DESC<T>(column);
  }

  /** NATURAL **/

  public static class NATURAL extends cSQL<Object> {
    protected cSQL<?> parent() {
      return null;
    }

    protected void serialize(final Serialization serialization) {
      serialization.sql.append("NATURAL");
    }
  }

  public static final NATURAL NATURAL = new NATURAL();

  /** TYPE **/

  public static abstract class TYPE extends cSQL<Object> {
    protected cSQL<?> parent() {
      return null;
    }
  }

  public static final TYPE INNER = new TYPE() {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("INNER");
    }
  };

  public static final TYPE LEFT_OUTER = new TYPE() {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("LEFT OUTER");
    }
  };

  public static final TYPE RIGHT_OUTER = new TYPE() {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("RIGHT OUTER");
    }
  };

  public static final TYPE FULL_OUTER = new TYPE() {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("FULL OUTER");
    }
  };

  public static final TYPE UNION = new TYPE() {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("UNION");
    }
  };

  /** SetQualifier **/

  public static abstract class SetQualifier extends cSQL<Object> {
    protected cSQL<?> parent() {
      return null;
    }
  }

  public static class ALL extends SetQualifier {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("ALL");
    }
  }

  public static class DISTINCT extends SetQualifier {
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("DISTINCT");
    }
  }

  public static final ALL ALL = new ALL();
  public static final DISTINCT DISTINCT = new DISTINCT();

  /** SELECT **/

  @SafeVarargs
  public static <T extends SELECT.Column<?>>SELECT_FROM<T> SELECT(final T ... columns) {
    return DML.<T>SELECT(null, null, columns);
  }

  @SafeVarargs
  public static <T extends SELECT.Column<?>>SELECT_FROM<T> SELECT(final ALL all, final T ... columns) {
    return DML.<T>SELECT(all, null, columns);
  }

  @SafeVarargs
  public static <T extends SELECT.Column<?>>SELECT_FROM<T> SELECT(final DISTINCT distinct, final T ... columns) {
    return DML.<T>SELECT(null, distinct, columns);
  }

  @SafeVarargs
  public static <T extends SELECT.Column<?>>SELECT_FROM<T> SELECT(final ALL all, final DISTINCT distinct, final T ... columns) {
    return new Select.SELECT<T>(all, distinct, columns);
  }

  /** CASE **/

  public static <T>WHEN<T> CASE_WHEN(final Condition<T> condition) {
    return new Case.CASE_WHEN<T>(condition);
  }

  /** DELETE **/

  public static <T>UPDATE_SET<T> UPDATE(final Table table) {
    return new Update.UPDATE<T>(table);
  }

  public static <T>DELETE_WHERE<T> DELETE(final Table table) {
    return new Delete.DELETE<T>(table);
  }

  /** INSERT **/

  public static INSERT INSERT(final Table table) {
    return new Insert.INSERT(table);
  }

  /** Aggregate **/

  public static class AVG<T> extends Aggregate<T> {
    protected AVG(final SetQualifier qualifier, final Column<T> column) {
      super(qualifier, column);
    }

    protected void serialize(final Serialization serialization) {
      tableAlias(column.owner, true);
      serialization.sql.append("AVG(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(column).append(")");
    }
  }

  public static <T>AVG<T> AVG(final Column<T> column) {
    return new AVG<T>(null, column);
  }

  public static <T>Aggregate<T> AVG(final DISTINCT distinct, final Column<T> column) {
    return new AVG<T>(distinct, column);
  }

  public static <T>Aggregate<T> AVG(final ALL all, final Column<T> column) {
    return new AVG<T>(all, column);
  }

  public static class MAX<T> extends Aggregate<T> {
    protected MAX(final SetQualifier qualifier, final Column<T> column) {
      super(qualifier, column);
    }

    protected void serialize(final Serialization serialization) {
      tableAlias(column.owner, true);
      serialization.sql.append("MAX(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(column).append(")");
    }
  }

  public static <T>MAX<T> MAX(final Column<T> column) {
    return new MAX<T>(null, column);
  }

  public static <T>MAX<T> MAX(final DISTINCT distinct, final Column<T> column) {
    return new MAX<T>(distinct, column);
  }

  public static <T>MAX<T> MAX(final ALL all, final Column<T> column) {
    return new MAX<T>(all, column);
  }

  public static class MIN<T> extends Aggregate<T> {
    protected MIN(final SetQualifier qualifier, final Column<T> column) {
      super(qualifier, column);
    }

    protected void serialize(final Serialization serialization) {
      tableAlias(column.owner, true);
      serialization.sql.append("MIN(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(column).append(")");
    }
  }

  public static <T>MIN<T> MIN(final Column<T> column) {
    return new MIN<T>(null, column);
  }

  public static <T>MIN<T> MIN(final DISTINCT distinct, final Column<T> column) {
    return new MIN<T>(distinct, column);
  }

  public static <T>MIN<T> MIN(final ALL all, final Column<T> column) {
    return new MIN<T>(all, column);
  }

  public static class SUM<T> extends Aggregate<T> {
    protected SUM(final SetQualifier qualifier, final Column<T> column) {
      super(qualifier, column);
    }

    protected void serialize(final Serialization serialization) {
      tableAlias(column.owner, true);
      serialization.sql.append("SUM(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(column).append(")");
    }
  }

  public static <T>SUM<T> SUM(final Column<T> column) {
    return new SUM<T>(null, column);
  }

  public static <T>SUM<T> SUM(final DISTINCT distinct, final Column<T> column) {
    return new SUM<T>(distinct, column);
  }

  public static <T>SUM<T> SUM(final ALL all, final Column<T> column) {
    return new SUM<T>(all, column);
  }

  public static class COUNT<T> extends Aggregate<T> {
    protected COUNT(final SetQualifier qualifier, final Column<T> column) {
      super(qualifier, column);
    }

    protected void serialize(final Serialization serialization) {
      tableAlias(column.owner, true);
      serialization.sql.append("COUNT(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(column).append(")");
    }
  }

  public static <T>COUNT<T> COUNT(final Column<T> column) {
    return new COUNT<T>(null, column);
  }

  public static <T>Aggregate<T> COUNT(final DISTINCT distinct, final Column<T> column) {
    return new COUNT<T>(distinct, column);
  }

  public static <T>Aggregate<T> COUNT(final ALL all, final Column<T> column) {
    return new COUNT<T>(all, column);
  }

  public static class PLUS<T> extends Function<T> {
    protected PLUS(final cSQL<T> a, final cSQL<T> b) {
      super(a, b);
    }

    protected void serialize(final Serialization serialization) {
      serialize(a, serialization);
      serialization.sql.append(" + ");
      serialize(b, serialization);
    }
  }

  public static <T>PLUS<T> PLUS(final Column<T> a, final Column<T> b) {
    return new PLUS<T>(a, b);
  }

  public static <T>PLUS<T> PLUS(final Column<T> a, final T b) {
    return new PLUS<T>(a, cSQL.valueOf(b));
  }

  public static <T>PLUS<T> PLUS(final T a, final Column<T> b) {
    return new PLUS<T>(cSQL.valueOf(a), b);
  }

  public static <T>PLUS<T> PLUS(final T a, final T b) {
    return new PLUS<T>(cSQL.valueOf(a), cSQL.valueOf(b));
  }

  public static class MINUS<T> extends Function<T> {
    protected MINUS(final cSQL<T> a, final cSQL<T> b) {
      super(a, b);
    }

    protected void serialize(final Serialization serialization) {
      serialize(a, serialization);
      serialization.sql.append(" - ");
      serialize(b, serialization);
    }
  }

  public static <T>MINUS<T> MINUS(final Column<T> a, final Column<T> b) {
    return new MINUS<T>(a, b);
  }

  public static <T>MINUS<T> MINUS(final Column<T> a, final T b) {
    return new MINUS<T>(a, cSQL.valueOf(b));
  }

  public static <T>MINUS<T> MINUS(final T a, final Column<T> b) {
    return new MINUS<T>(cSQL.valueOf(a), b);
  }

  public static <T>MINUS<T> MINUS(final T a, final T b) {
    return new MINUS<T>(cSQL.valueOf(a), cSQL.valueOf(b));
  }

  /** Condition **/

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.AND, conditions);
  }

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.OR, conditions);
  }

  public static <T>LogicalCondition<T> GT(final Column<T> a, final Column<T> b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> GT(final Column<T> a, final T b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> GT(final T a, final Column<T> b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> LT(final Column<T> a, final Column<T> b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> LT(final Column<T> a, final T b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> LT(final T a, final Column<T> b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Column<T> a, final Column<T> b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Column<T> a, final T b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final T a, final Column<T> b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> LTEQ(final Column<T> a, final Column<T> b) {
    return new LogicalCondition<T>("<=", a, b);
  }

  public static <T>LogicalCondition<T> LTEQ(final Column<T> a, final T b) {
    return new LogicalCondition<T>("<=", a, b);
  }

  public static <T>LogicalCondition<T> LTEQ(final T a, final Column<T> b) {
    return new LogicalCondition<T>("<=", a, b);
  }
}