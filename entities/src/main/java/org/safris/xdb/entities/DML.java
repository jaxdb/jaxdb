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
import org.safris.xdb.entities.datatype.Decimal;
import org.safris.xdb.entities.datatype.Numeric;
import org.safris.xdb.entities.datatype.Temporal;
import org.safris.xdb.entities.spec.delete;
import org.safris.xdb.entities.spec.expression;
import org.safris.xdb.entities.spec.insert;
import org.safris.xdb.entities.spec.select;
import org.safris.xdb.entities.spec.update;
import org.safris.xdb.xdd.xe.$xdd_data;

public abstract class DML {
  /** Direction **/

  protected static abstract class Direction<T> extends Variable<T> {
    private final Variable<?> variable;

    public Direction(final Variable<T> variable) {
      super(variable.value);
      this.variable = variable;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append(serialize());
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

    @Override
    protected String serialize() {
      return variable.serialize() + " " + getClass().getSimpleName();
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
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("NATURAL");
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
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("INNER");
    }
  };

  public static final TYPE LEFT = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("LEFT OUTER");
    }
  };

  public static final TYPE RIGHT = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("RIGHT OUTER");
    }
  };

  public static final TYPE FULL = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("FULL OUTER");
    }
  };

  public static final TYPE UNION = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("UNION");
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
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("ALL");
    }
  }

  public static class DISTINCT extends SetQualifier {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("DISTINCT");
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
  public static <T extends Entity>insert.INSERT_SELECT<T> INSERT(final T ... entities) {
    return new Insert.INSERT<T>(entities);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static insert.INSERT INSERT(final $xdd_data data) {
    return new Insert.INSERT(Entities.toEntities(data));
  }

  /** Math Functions **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SIGN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SIGN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T FLOOR(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("FLOOR", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T CEIL(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("CEIL", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T POW(final T x, final double y) {
    final T wrapper = (T)x.clone();
    wrapper.setWrapper(new Function("POWER", x, y));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ROUND(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("ROUND", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ROUND(final T dataType, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("ROUND", dataType, decimal));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SQRT(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SQRT", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("SIN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ASIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("ASIN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T COS(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("COS", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ACOS(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("ACOS", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T TAN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("TAN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ATAN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("ATAN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Number>Numeric<T> ATAN2(final Numeric<T> a, final Numeric<T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Function("ATAN2", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T EXP(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("EXP", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("LN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG(final T a, final T b) {
    final T wrapper = (T)a.clone();
    wrapper.setWrapper(new Function("LOG", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG(final T a, final Number b) {
    final T wrapper = (T)a.clone();
    wrapper.setWrapper(new Function("LOG", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG2(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("LOG2", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG10(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new Function("COS", dataType));
    return wrapper;
  }

  /** Aggregate **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new Function("COUNT", dataType));
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
    wrapper.setWrapper(new Function("SUM", dataType));
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
    wrapper.setWrapper(new Function("AVG", dataType));
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
    wrapper.setWrapper(new Function("MAX", dataType));
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
    wrapper.setWrapper(new Function("MIN", dataType));
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

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Numeric<T> ... args) {
    if (args.length <= 1)
      throw new IllegalArgumentException("args.length <= 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.DIVIDE, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.DIVIDE, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Number a, final Numeric<T> ... b) {
    if (b.length == 0)
      throw new IllegalArgumentException("b.length == 0");

    final Numeric<T> wrapper = (Numeric<T>)b[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.DIVIDE, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Numeric<T> ... args) {
    if (args.length <= 1)
      throw new IllegalArgumentException("args.length <= 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MULTIPLY, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Number a, final Numeric<T> ... b) {
    if (b.length == 0)
      throw new IllegalArgumentException("b.length == 0");

    final Numeric<T> wrapper = (Numeric<T>)b[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<T> a, final Numeric<? super T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MOD, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MOD, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Numeric<T> ... args) {
    if (args.length <= 1)
      throw new IllegalArgumentException("args.length <= 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Number a, final Numeric<T> ... b) {
    if (b.length ==0 )
      throw new IllegalArgumentException("b.length == 0");

    final Numeric<T> wrapper = (Numeric<T>)b[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> ... args) {
    return ADD(args);
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Number b) {
    return ADD(a, b);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Number a, final Numeric<T> ... b) {
    return ADD(a, b);
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Duration duration) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.PLUS, a, duration));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Numeric<T> ... args) {
    if (args.length <= 1)
      throw new IllegalArgumentException("args.length <= 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Number a, Numeric<T> b) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Number a, final Numeric<T> ... b) {
    if (b.length == 0)
      throw new IllegalArgumentException("b.length == 0");

    final Numeric<T> wrapper = (Numeric<T>)b[0].clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> ... args) {
    return SUB(args);
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Number b) {
    return SUB(a, b);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Number a, final Numeric<T> ... b) {
    return SUB(a, b);
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Duration duration) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new Evaluation<T>(Operator.MINUS, a, duration));
    return wrapper;
  }

  private static class NOW extends DateTime {
    protected NOW() {
      super();
      this.wrapper = new Function<LocalDateTime>("NOW");
    }

    @Override
    public NOW clone() {
      return new NOW();
    }
  }

  private static final NOW NOW = new NOW();

  public static NOW NOW() {
    return NOW;
  }

  private static class PI extends Decimal {
    protected PI() {
      super();
      this.wrapper = new Function<Double>("PI");
    }

    @Override
    public PI clone() {
      return new PI();
    }
  }

  private static final PI PI = new PI();

  public static PI PI() {
    return PI;
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