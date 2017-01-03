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

package org.safris.xdb.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import org.safris.xdb.entities.datatype.Char;
import org.safris.xdb.entities.datatype.DateTime;
import org.safris.xdb.entities.datatype.Numeric;
import org.safris.xdb.entities.datatype.Temporal;
import org.safris.xdb.entities.spec.delete;
import org.safris.xdb.entities.spec.expression;
import org.safris.xdb.entities.spec.insert;
import org.safris.xdb.entities.spec.select;
import org.safris.xdb.entities.spec.update;

public abstract class DML {
  /** Direction **/

  protected static abstract class Direction<T> extends Variable<T> {
    private final Variable<?> variable;

    public Direction(final Variable<T> variable) {
      super(variable.value);
      this.variable = variable;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      variable.serialize(this, serialization);
      serialization.sql.append(" ").append(getClass().getSimpleName());
    }

    @Override
    protected Entity owner() {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  protected static final class ASC<T> extends Direction<T> {
    protected ASC(final Variable<T> variable) {
      super(variable);
    }
  }

  public static <T>ASC<T> ASC(final Variable<T> variable) {
    return new ASC<T>(variable);
  }

  protected static final class DESC<T> extends Direction<T> {
    protected DESC(final Variable<T> variable) {
      super(variable);
    }
  }

  public static <T>DESC<T> DESC(final Variable<T> variable) {
    return new DESC<T>(variable);
  }

  /** NATURAL **/

  public static class NATURAL extends Keyword<Subject<?>> {
    @Override
    protected Keyword<Subject<?>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("NATURAL");
    }
  }

  public static final NATURAL NATURAL = new NATURAL();

  /** TYPE **/

  public static abstract class TYPE extends Keyword<Subject<?>> {
    @Override
    protected Keyword<Subject<?>> parent() {
      return null;
    }
  }

  public static final TYPE INNER = new TYPE() {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("INNER");
    }
  };

  public static final TYPE LEFT = new TYPE() {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("LEFT OUTER");
    }
  };

  public static final TYPE RIGHT = new TYPE() {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("RIGHT OUTER");
    }
  };

  public static final TYPE FULL = new TYPE() {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("FULL OUTER");
    }
  };

  public static final TYPE UNION = new TYPE() {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("UNION");
    }
  };

  /** SetQualifier **/

  public static abstract class SetQualifier extends Keyword<Subject<?>> {
    @Override
    protected Keyword<Subject<?>> parent() {
      return null;
    }
  }

  public static class ALL extends SetQualifier {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("ALL");
    }
  }

  public static class DISTINCT extends SetQualifier {
    @Override
    protected void serialize(final Serializable caller, final Serialization serialization) {
      serialization.sql.append("DISTINCT");
    }
  }

  public static final ALL ALL = new ALL();
  public static final DISTINCT DISTINCT = new DISTINCT();

  /** SELECT **/

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final T ... entities) {
    return DML.<T>SELECT(null, null, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final ALL all, final T ... entities) {
    return DML.<T>SELECT(all, null, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final DISTINCT distinct, final T ... entities) {
    return DML.<T>SELECT(null, distinct, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final ALL all, final DISTINCT distinct, final T ... entities) {
    return new Select.SELECT<T>(all, distinct, entities);
  }

  /** CASE **/

  public static <T>expression.WHEN<T> CASE_WHEN(final Condition<T> condition) {
    return new Case.CASE_WHEN<T>(condition);
  }

  /** DELETE **/

  public static update.UPDATE_SET UPDATE(final Entity entity) {
    return new Update.UPDATE(entity);
  }

  public static delete.DELETE_WHERE DELETE(final Entity entity) {
    return new Delete.DELETE(entity);
  }

  /** INSERT **/

  @SafeVarargs
  public static <T extends Entity>insert.INSERT<T> INSERT(final T ... entities) {
    return new Insert.INSERT<T>(entities);
  }

  /** Aggregate **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new Function("COUNT", null, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final DISTINCT distinct, final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new Function("COUNT", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final ALL all, final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new Function("COUNT", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SUM", null, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SUM", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SUM", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("AVG", null, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("AVG", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("AVG", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MAX", null, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MAX", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MAX", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MIN", null, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MIN", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("MIN", all, dataType));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Numeric<? super T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.PLUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.PLUS, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final T b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.PLUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.PLUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Duration duration) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.PLUS, duration));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Numeric<? super T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.MINUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.MINUS, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final T b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.MINUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.MINUS, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Duration duration) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(a, Operator.MINUS, duration));
    return wrapper;
  }

  private static class NOW extends DateTime {
    protected NOW() {
      super();
      this.wrapper = new Function<LocalDateTime>("NOW", null, null);
    }
  }

  private static final NOW NOW = new NOW();

  public static NOW NOW() {
    return NOW;
  }

  /** Condition **/

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.AND, conditions);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Collection<Condition<?>> conditions) {
    return new BooleanCondition(Operator.AND, conditions.toArray(new Condition<?>[conditions.size()]));
  }

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.OR, conditions);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Collection<Condition<?>> conditions) {
    return new BooleanCondition(Operator.OR, conditions.toArray(new Condition<?>[conditions.size()]));
  }

  public static <T>LogicalCondition<T> GT(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GT(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GT(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GT(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GT(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GT(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.GT, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> GTE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.GTE, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.EQ, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.EQ, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(b != null ? Operator.EQ : Operator.IS, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(b != null ? Operator.EQ : Operator.IS, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.EQ, a, b);
  }

  public static <T>LogicalCondition<T> EQ(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.EQ, a, b);
  }

  public static <T>LogicalCondition<T> NE(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.NE, a, b);
  }

  public static <T>LogicalCondition<T> NE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.NE, a, b);
  }

  public static <T>LogicalCondition<T> NE(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(b != null ? Operator.NE : Operator.IS_NOT, a, b);
  }

  public static <T>LogicalCondition<T> NE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(b != null ? Operator.NE : Operator.IS_NOT, a, b);
  }

  public static <T>LogicalCondition<T> NE(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.NE, a, b);
  }

  public static <T>LogicalCondition<T> NE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.NE, a, b);
  }

  public static <T>LogicalCondition<T> LT(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LT(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LT(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LT(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LT(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LT(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.LT, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final Variable<T> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final Variable<T> a, final T b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final T a, final Variable<T> b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  public static <T>LogicalCondition<T> LTE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new LogicalCondition<T>(Operator.LTE, a, b);
  }

  /** Predicate **/

  public static Predicate<String> LIKE(final Char a, final String b) {
    return new Predicate<String>("LIKE", a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Variable<T> a, final Variable<? super T> ... b) {
    return new Predicate<T>("IN", a, (Object[])b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> ... b) {
    return new Predicate<T>("IN", a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Variable<T> a, final T ... b) {
    return new Predicate<T>("IN", a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final T ... b) {
    return new Predicate<T>("IN", a, b);
  }

  public static <T>Predicate<T> IN(final Variable<T> a, final Collection<T> b) {
    return new Predicate<T>("IN", a, b.toArray());
  }

  public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final Collection<T> b) {
    return new Predicate<T>("IN", a, b.toArray());
  }

  public static <T>Predicate<T> IN(final Variable<T> a, final select.SELECT<? extends Variable<T>> b) {
    return new Predicate<T>("IN", a, b);
  }

  public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final select.SELECT<? extends Variable<T>> b) {
    return new Predicate<T>("IN", a, b);
  }

  public static <T>Predicate<T> EXISTS(final select.SELECT<? extends Variable<T>> b) {
    return new Predicate<T>("EXISTS", b);
  }

  public static class NOT {
    public static Predicate<String> LIKE(final Char a, final String b) {
      return new Predicate<String>("NOT LIKE", a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final Variable<T> a, final Variable<? super T> ... b) {
      return new Predicate<T>("NOT IN", a, (Object[])b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> ... b) {
      return new Predicate<T>("NOT IN", a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final Variable<T> a, final T ... b) {
      return new Predicate<T>("NOT IN", a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final T ... b) {
      return new Predicate<T>("NOT IN", a, b);
    }

    public static <T>Predicate<T> IN(final Variable<T> a, final Collection<T> b) {
      return new Predicate<T>("NOT IN", a, b.toArray());
    }

    public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final Collection<T> b) {
      return new Predicate<T>("NOT IN", a, b.toArray());
    }

    public static <T>Predicate<T> IN(final Variable<T> a, final select.SELECT<? extends Variable<T>> b) {
      return new Predicate<T>("NOT IN", a, b);
    }

    public static <T>Predicate<T> IN(final select.SELECT<? extends Variable<T>> a, final select.SELECT<? extends Variable<T>> b) {
      return new Predicate<T>("NOT IN", a, b);
    }

    public static <T>Predicate<T> EXISTS(final select.SELECT<? extends Variable<T>> b) {
      return new Predicate<T>("NOT EXISTS", b);
    }
  }
}