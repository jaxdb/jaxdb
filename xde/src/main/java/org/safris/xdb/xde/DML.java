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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.safris.commons.lang.Arrays;
import org.safris.xdb.xde.BooleanCondition.Operator;
import org.safris.xdb.xde.csql.delete.DELETE_WHERE;
import org.safris.xdb.xde.csql.expression.WHEN;
import org.safris.xdb.xde.csql.insert.INSERT;
import org.safris.xdb.xde.csql.select._SELECT;
import org.safris.xdb.xde.csql.update.UPDATE_SET;
import org.safris.xdb.xde.datatype.Char;

public abstract class DML {
  /** Direction **/

  protected static abstract class Direction<T> extends Field<T> {
    private final Field<?> field;

    public Direction(final Field<T> field) {
      super(null);
      this.field = field;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      field.serialize(serialization);
      serialization.sql.append(" ").append(getClass().getSimpleName());
    }

    @Override
    protected Entity entity() {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    protected T get(final ResultSet resultSet, final int columnIndex) throws SQLException {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  protected static class ASC<T> extends Direction<T> {
    protected ASC(final Field<T> field) {
      super(field);
    }
  }

  public static <T>ASC<T> ASC(final Field<T> field) {
    return new ASC<T>(field);
  }

  protected static class DESC<T> extends Direction<T> {
    protected DESC(final Field<T> field) {
      super(field);
    }
  }

  public static <T>DESC<T> DESC(final Field<T> field) {
    return new DESC<T>(field);
  }

  /** NATURAL **/

  public static class NATURAL extends Keyword<Data<?>> {
    @Override
    protected Keyword<Data<?>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("NATURAL");
    }
  }

  public static final NATURAL NATURAL = new NATURAL();

  /** TYPE **/

  public static abstract class TYPE extends Keyword<Data<?>> {
    @Override
    protected Keyword<Data<?>> parent() {
      return null;
    }
  }

  public static final TYPE INNER = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("INNER");
    }
  };

  public static final TYPE LEFT = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("LEFT OUTER");
    }
  };

  public static final TYPE RIGHT = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("RIGHT OUTER");
    }
  };

  public static final TYPE FULL = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("FULL OUTER");
    }
  };

  public static final TYPE UNION = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("UNION");
    }
  };

  /** SetQualifier **/

  public static abstract class SetQualifier extends Keyword<Data<?>> {
    @Override
    protected Keyword<Data<?>> parent() {
      return null;
    }
  }

  public static class ALL extends SetQualifier {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("ALL");
    }
  }

  public static class DISTINCT extends SetQualifier {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("DISTINCT");
    }
  }

  public static final ALL ALL = new ALL();
  public static final DISTINCT DISTINCT = new DISTINCT();

  /** SELECT **/

  @SafeVarargs
  public static <T extends Data<?>>_SELECT<T> SELECT(final T ... entities) {
    return DML.<T>SELECT(null, null, entities);
  }

  @SafeVarargs
  public static <T extends Data<?>>_SELECT<T> SELECT(final ALL all, final T ... entities) {
    return DML.<T>SELECT(all, null, entities);
  }

  @SafeVarargs
  public static <T extends Data<?>>_SELECT<T> SELECT(final DISTINCT distinct, final T ... entities) {
    return DML.<T>SELECT(null, distinct, entities);
  }

  @SafeVarargs
  public static <T extends Data<?>>_SELECT<T> SELECT(final ALL all, final DISTINCT distinct, final T ... entities) {
    return new Select.SELECT<T>(all, distinct, entities);
  }

  /** CASE **/

  public static <T>WHEN<T> CASE_WHEN(final Condition<T> condition) {
    return new Case.CASE_WHEN<T>(condition);
  }

  /** DELETE **/

  public static <T>UPDATE_SET UPDATE(final Entity entity) {
    return new Update.UPDATE(entity);
  }

  public static <T extends Data<?>>DELETE_WHERE<T> DELETE(final Entity entity) {
    return new Delete.DELETE<T>(entity);
  }

  /** INSERT **/

  public static INSERT INSERT(final Entity entity) {
    return new Insert.INSERT(entity);
  }

  /** Aggregate **/

  public static class AVG<T> extends Aggregate<T> {
    protected AVG(final SetQualifier qualifier, final DataType<T> dataType) {
      super(qualifier, dataType);
    }

    protected AVG(final AVG<T> max) {
      super(max);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      tableAlias(dataType.entity(), true);
      serialization.sql.append("AVG(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(dataType).append(")");
    }
  }

  public static <T>AVG<T> AVG(final DataType<T> dataType) {
    return new AVG<T>(null, dataType);
  }

  public static <T>AVG<T> AVG(final DISTINCT distinct, final DataType<T> dataType) {
    return new AVG<T>(distinct, dataType);
  }

  public static <T>AVG<T> AVG(final ALL all, final DataType<T> dataType) {
    return new AVG<T>(all, dataType);
  }

  public static class MAX<T> extends Aggregate<T> {
    protected MAX(final SetQualifier qualifier, final DataType<T> dataType) {
      super(qualifier, dataType);
    }

    protected MAX(final MAX<T> max) {
      super(max);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      tableAlias(dataType.entity(), true);
      serialization.sql.append("MAX(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(dataType).append(")");
    }
  }

  public static <T>MAX<T> MAX(final DataType<T> dataType) {
    return new MAX<T>(null, dataType);
  }

  public static <T>MAX<T> MAX(final DISTINCT distinct, final DataType<T> dataType) {
    return new MAX<T>(distinct, dataType);
  }

  public static <T>MAX<T> MAX(final ALL all, final DataType<T> dataType) {
    return new MAX<T>(all, dataType);
  }

  public static class MIN<T> extends Aggregate<T> {
    protected MIN(final SetQualifier qualifier, final DataType<T> dataType) {
      super(qualifier, dataType);
    }

    protected MIN(final MIN<T> max) {
      super(max);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      tableAlias(dataType.entity(), true);
      serialization.sql.append("MIN(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(dataType).append(")");
    }
  }

  public static <T>MIN<T> MIN(final DataType<T> dataType) {
    return new MIN<T>(null, dataType);
  }

  public static <T>MIN<T> MIN(final DISTINCT distinct, final DataType<T> dataType) {
    return new MIN<T>(distinct, dataType);
  }

  public static <T>MIN<T> MIN(final ALL all, final DataType<T> dataType) {
    return new MIN<T>(all, dataType);
  }

  public static class SUM<T> extends Aggregate<T> {
    protected SUM(final SetQualifier qualifier, final DataType<T> dataType) {
      super(qualifier, dataType);
    }

    protected SUM(final SUM<T> max) {
      super(max);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      tableAlias(dataType.entity(), true);
      serialization.sql.append("SUM(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(dataType).append(")");
    }
  }

  public static <T>SUM<T> SUM(final DataType<T> dataType) {
    return new SUM<T>(null, dataType);
  }

  public static <T>SUM<T> SUM(final DISTINCT distinct, final DataType<T> dataType) {
    return new SUM<T>(distinct, dataType);
  }

  public static <T>SUM<T> SUM(final ALL all, final DataType<T> dataType) {
    return new SUM<T>(all, dataType);
  }

  public static class COUNT<T> extends Aggregate<T> {
    protected COUNT(final SetQualifier qualifier, final DataType<T> dataType) {
      super(qualifier, dataType);
    }

    protected COUNT(final COUNT<T> max) {
      super(max);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      tableAlias(dataType.entity(), true);
      serialization.sql.append("COUNT(");
      if (qualifier != null)
        serialization.sql.append(qualifier).append(" ");

      serialization.sql.append(dataType).append(")");
    }
  }

  public static <T>COUNT<T> COUNT(final DataType<T> dataType) {
    return new COUNT<T>(null, dataType);
  }

  public static <T>COUNT<T> COUNT(final DISTINCT distinct, final DataType<T> dataType) {
    return new COUNT<T>(distinct, dataType);
  }

  public static <T>COUNT<T> COUNT(final ALL all, final DataType<T> dataType) {
    return new COUNT<T>(all, dataType);
  }

  public static class PLUS<T> extends Function<T> {
    protected PLUS(final Field<T> a, final Field<T> b) {
      super(a, b);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      a.serialize(serialization);
      serialization.sql.append(" + ");
      b.serialize(serialization);
    }
  }

  public static <T>PLUS<T> PLUS(final Field<T> a, final Field<T> b) {
    return new PLUS<T>(a, b);
  }

  public static <T>PLUS<T> PLUS(final Field<T> a, final T b) {
    return new PLUS<T>(a, Field.valueOf(b));
  }

  public static <T>PLUS<T> PLUS(final T a, final Field<T> b) {
    return new PLUS<T>(Field.valueOf(a), b);
  }

  public static <T>PLUS<T> PLUS(final T a, final T b) {
    return new PLUS<T>(Field.valueOf(a), Field.valueOf(b));
  }

  public static class MINUS<T> extends Function<T> {
    protected MINUS(final Field<T> a, final Field<T> b) {
      super(a, b);
    }

    @Override
    protected void serialize(final Serialization serialization) {
      a.serialize(serialization);
      serialization.sql.append(" - ");
      b.serialize(serialization);
    }
  }

  public static <T>MINUS<T> MINUS(final Field<T> a, final Field<T> b) {
    return new MINUS<T>(a, b);
  }

  public static <T>MINUS<T> MINUS(final Field<T> a, final T b) {
    return new MINUS<T>(a, Field.valueOf(b));
  }

  public static <T>MINUS<T> MINUS(final T a, final Field<T> b) {
    return new MINUS<T>(Field.valueOf(a), b);
  }

  public static <T>MINUS<T> MINUS(final T a, final T b) {
    return new MINUS<T>(Field.valueOf(a), Field.valueOf(b));
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

  public static <T>LogicalCondition<T> GT(final Field<T> a, final Field<T> b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> GT(final Field<T> a, final T b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> GT(final T a, final Field<T> b) {
    return new LogicalCondition<T>(">", a, b);
  }

  public static <T>LogicalCondition<T> GTE(final Field<T> a, final Field<T> b) {
    return new LogicalCondition<T>(">=", a, b);
  }

  public static <T>LogicalCondition<T> GTE(final Field<T> a, final T b) {
    return new LogicalCondition<T>(">=", a, b);
  }

  public static <T>LogicalCondition<T> GTE(final T a, final Field<T> b) {
    return new LogicalCondition<T>(">=", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Field<T> a, final Field<T> b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Field<T> a, final T b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> EQ(final T a, final Field<T> b) {
    return new LogicalCondition<T>("=", a, b);
  }

  public static <T>LogicalCondition<T> LT(final Field<T> a, final Field<T> b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> LT(final Field<T> a, final T b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> LT(final T a, final Field<T> b) {
    return new LogicalCondition<T>("<", a, b);
  }

  public static <T>LogicalCondition<T> LTE(final Field<T> a, final Field<T> b) {
    return new LogicalCondition<T>("<=", a, b);
  }

  public static <T>LogicalCondition<T> LTE(final Field<T> a, final T b) {
    return new LogicalCondition<T>("<=", a, b);
  }

  public static <T>LogicalCondition<T> LTE(final T a, final Field<T> b) {
    return new LogicalCondition<T>("<=", a, b);
  }

  /** Predicate **/

  public static Predicate<String> LIKE(final Char a, final String b) {
    return new Predicate<String>("LIKE", a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, final Field<T> b0, final Field<T> ... b1) {
    final Object[] in = new Object[b1.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    return new Predicate<T>("IN", a, in);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, T b0, final T ... b1) {
    final Object[] in = new Object[b1.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    return new Predicate<T>("IN", a, in);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, final Field<T> b0, final T ... b1) {
    final Object[] in = new Object[b1.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    return new Predicate<T>("IN", a, in);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, final T b0, final Field<T> ... b1) {
    final Object[] in = new Object[b1.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    return new Predicate<T>("IN", a, in);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, final Field<T> b0, final Field<T>[] b1, final T ... b2) {
    final Object[] in = new Object[b1.length + b2.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    System.arraycopy(b2, 0, in, 1 + b1.length, b2.length);
    return new Predicate<T>("IN", a, in);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Field<T> a, final T b0, final T[] b1, final Field<T> ... b2) {
    final Object[] in = new Object[b1.length + b2.length + 1];
    in[0] = b0;
    System.arraycopy(b1, 0, in, 1, b1.length);
    System.arraycopy(b2, 0, in, 1 + b1.length, b2.length);
    return new Predicate<T>("IN", a, in);
  }
}