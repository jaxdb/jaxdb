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

import java.util.Collection;

import org.safris.commons.lang.Arrays;
import org.safris.xdb.entities.binding.Interval;
import org.safris.xdb.entities.datatype.Char;
import org.safris.xdb.entities.datatype.DateTime;
import org.safris.xdb.entities.datatype.Decimal;
import org.safris.xdb.entities.datatype.Double;
import org.safris.xdb.entities.datatype.Enum;
import org.safris.xdb.entities.datatype.Numeric;
import org.safris.xdb.entities.datatype.Temporal;
import org.safris.xdb.entities.spec.delete;
import org.safris.xdb.entities.spec.expression;
import org.safris.xdb.entities.spec.insert;
import org.safris.xdb.entities.spec.select;
import org.safris.xdb.entities.spec.update;
import org.safris.xdb.xdd.xe.$xdd_data;

public final class DML {
  /** Ordering Specification **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <V extends Variable<T>,T>V ASC(final V variable) {
    final V wrapper = (V)variable.clone();
    wrapper.setWrapper(new OrderingSpec(Operator.ASC, variable));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <V extends Variable<T>,T>V DESC(final V variable) {
    final V wrapper = (V)variable.clone();
    wrapper.setWrapper(new OrderingSpec(Operator.DESC, variable));
    return wrapper;
  }

  /** NATURAL **/

  public static class NATURAL extends Provision<Subject<?>> {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.addCaller(this);
      serialization.append("NATURAL");
    }
  }

  public static final NATURAL NATURAL = new NATURAL();

  /** TYPE **/

  public static abstract class TYPE extends Provision<Subject<?>> {
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

  public static abstract class SetQualifier extends Provision<Subject<?>> {
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

  public static <T extends Subject<?>>expression.WHEN<T> CASE_WHEN(final Condition<T> condition) {
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

  /** String Functions **/

  public static Char CONCAT(final Char a, final Char b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Char c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Char c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Char d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Char d, final CharSequence e) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Enum<?> b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Enum<?> c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Enum<?> c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Enum<?> d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Enum<?> d, final CharSequence e) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Enum<?> b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Enum<?> c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Enum<?> c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Enum<?> d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Enum<?> d, final CharSequence e) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Char b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Char c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Char c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Char d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Char d, final CharSequence e) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b) {
    final Char wrapper = new Char(a.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(b.owner());
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  /** Math Functions **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ABS(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ABS", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SIGN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SIGN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T FLOOR(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("FLOOR", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T CEIL(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("CEIL", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T POW(final T x, final double y) {
    final T wrapper = (T)x.clone();
    wrapper.setWrapper(new NumericFunction("POWER", x, y));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ROUND(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ROUND", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ROUND(final T dataType, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ROUND", dataType, decimal));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SQRT(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SQRT", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SIN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ASIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ASIN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T COS(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("COS", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ACOS(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ACOS", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T TAN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("TAN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T ATAN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("ATAN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Number>Numeric<T> ATAN2(final Numeric<T> a, final Numeric<T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericFunction("ATAN2", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T EXP(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("EXP", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("LN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG(final T a, final T b) {
    final T wrapper = (T)a.clone();
    wrapper.setWrapper(new NumericFunction("LOG", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG(final T a, final Number b) {
    final T wrapper = (T)a.clone();
    wrapper.setWrapper(new NumericFunction("LOG", a, b));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG2(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("LOG2", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T LOG10(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("COS", dataType));
    return wrapper;
  }

  /** Aggregate **/

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new NumericFunction("COUNT", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final DISTINCT distinct, final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new NumericFunction("COUNT", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static org.safris.xdb.entities.datatype.Long COUNT(final ALL all, final DataType<?> dataType) {
    final org.safris.xdb.entities.datatype.Long wrapper = new org.safris.xdb.entities.datatype.Long();
    wrapper.setWrapper(new NumericFunction("COUNT", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SUM", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SUM", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T SUM(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("SUM", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("AVG", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("AVG", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends Numeric<?>>T AVG(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("AVG", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MAX", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MAX", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MAX(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MAX", all, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MIN", dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final DISTINCT distinct, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MIN", distinct, dataType));
    return wrapper;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T extends DataType<?>>T MIN(final ALL all, final T dataType) {
    final T wrapper = (T)dataType.clone();
    wrapper.setWrapper(new NumericFunction("MIN", all, dataType));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.DIVIDE, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.DIVIDE, args[0], args[1], (Object[])Arrays.subArray(args, 2)));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.DIVIDE, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.DIVIDE, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.DIVIDE, a, args[0], (Object[])Arrays.subArray(args, 1)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MULTIPLY, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MULTIPLY, args[0], args[1], (Object[])Arrays.subArray(args, 2)));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MULTIPLY, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MULTIPLY, a, args[0], (Object[])Arrays.subArray(args, 1)));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<T> a, final Numeric<? super T> b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MOD, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MOD, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.PLUS, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.PLUS, args[0], args[1], (Object[])Arrays.subArray(args, 2)));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.PLUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.PLUS, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.PLUS, a, args[0], (Object[])Arrays.subArray(args, 1)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    return ADD(a, b, args);
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    return ADD(args[0], args[1], Arrays.subArray(args, 2));
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Number b) {
    return ADD(a, b);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    return ADD(a, b, args);
  }

  public static <T extends Number>Numeric<T> PLUS(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    return ADD(a, args[0], Arrays.subArray(args, 1));
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.PLUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.PLUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> PLUS(final Temporal<T> a, final Interval interval) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.PLUS, a, interval));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, args[0], args[1], (Object[])Arrays.subArray(args, 2)));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = (Numeric<T>)a.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Number a, Numeric<T> b) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = (Numeric<T>)b.clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, a, b, (Object[])args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = (Numeric<T>)args[0].clone();
    wrapper.setWrapper(new NumericExpression<Numeric<T>>(Operator.MINUS, a, args[0], (Object[])Arrays.subArray(args, 1)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    return SUB(a, b, args);
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    return SUB(args[0], args[1], Arrays.subArray(args, 2));
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Number b) {
    return SUB(a, b);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    return SUB(a, b, args);
  }

  public static <T extends Number>Numeric<T> MINUS(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    return SUB(a, args[0], Arrays.subArray(args, 1));
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Temporal<? super T> b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final T b) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.MINUS, a, b));
    return wrapper;
  }

  public static <T extends java.time.temporal.Temporal>Temporal<T> MINUS(final Temporal<T> a, final Interval interval) {
    final Temporal<T> wrapper = (Temporal<T>)a.clone();
    wrapper.setWrapper(new TemporalExpression<Temporal<T>>(Operator.MINUS, a, interval));
    return wrapper;
  }

  private static class NOW extends DateTime {
    protected NOW() {
      super();
      this.wrapper = new TemporalFunction<Temporal<?>>("NOW");
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
      this.wrapper = new NumericFunction<Double>("PI");
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
  public static BooleanCondition<?> AND(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.AND, a, b, conditions);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanCondition(Operator.AND, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanCondition(Operator.AND, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanCondition(Operator.AND, array[0], array[1], Arrays.subArray(array, 2));
  }

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanCondition(Operator.OR, a, b, conditions);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanCondition(Operator.OR, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanCondition(Operator.OR, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BooleanCondition<?> OR(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanCondition(Operator.OR, array[0], array[1], Arrays.subArray(array, 2));
  }

  public static <T>ComparisonPredicate<T> GT(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GT(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GT(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GT(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GT(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GT(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> GTE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(b != null ? Operator.EQ : Operator.IS, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(b != null ? Operator.EQ : Operator.IS, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>ComparisonPredicate<T> EQ(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(b != null ? Operator.NE : Operator.IS_NOT, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(b != null ? Operator.NE : Operator.IS_NOT, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>ComparisonPredicate<T> NE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LT(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final Variable<? extends T> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final select.SELECT<? extends Variable<T>> a, final Variable<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final Variable<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final select.SELECT<? extends Variable<T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final T a, final Variable<T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>ComparisonPredicate<T> LTE(final T a, final select.SELECT<? extends Variable<T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  /** Predicate **/

  public static Predicate<String> LIKE(final Char a, final CharSequence b) {
    return new LikePredicate(true, a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final Variable<T> a, final T ... b) {
    return new InPredicate<T>(true, a, b);
  }

  public static <T>Predicate<T> IN(final Variable<T> a, final select.SELECT<? extends Variable<T>> b) {
    return new InPredicate<T>(true, a, b);
  }

  public static class NOT {
    public static Predicate<String> LIKE(final Char a, final String b) {
      return new LikePredicate(false, a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final Variable<T> a, final T ... b) {
      return new InPredicate<T>(false, a, b);
    }

    public static <T>Predicate<T> IN(final Variable<T> a, final select.SELECT<? extends Variable<T>> b) {
      return new InPredicate<T>(false, a, b);
    }
  }

  private DML() {
  }
}