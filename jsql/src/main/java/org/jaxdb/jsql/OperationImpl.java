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

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public final class OperationImpl {
  public static class Operation1 {
    static final class TINYINT extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.TINYINT,data.TINYINT,Byte> implements exp.TINYINT {
      TINYINT(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      TINYINT(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class SMALLINT extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.SMALLINT,data.SMALLINT,Short> implements exp.SMALLINT {
      SMALLINT(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      SMALLINT(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class INT extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.INT,data.INT,Integer> implements exp.INT {
      INT(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      INT(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class BIGINT extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.BIGINT,data.BIGINT,Long> implements exp.BIGINT {
      BIGINT(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      BIGINT(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class DECIMAL extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.DECIMAL,data.DECIMAL,BigDecimal> implements exp.DECIMAL {
      DECIMAL(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      DECIMAL(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class FLOAT extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.FLOAT,data.FLOAT,Float> implements exp.FLOAT {
      FLOAT(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      FLOAT(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    static final class DOUBLE extends expression.NumericExpression1<operation.Operation1<Number,Number>,type.DOUBLE,data.DOUBLE,Double> implements exp.DOUBLE {
      DOUBLE(final operation.Operation1<Number,Number> o, final type.Numeric<?> a) {
        super(o, a);
      }

      DOUBLE(final operation.Operation1<Number,Number> o, final Number a) {
        super(o, data.wrap(a));
      }
    }

    private Operation1() {
    }
  }

  abstract static class Operation2<O extends operation.Operation2<? super V>,T extends type.Numeric<V>,D extends data.Numeric<V>,V extends Number> extends expression.Expression<T,D,V> {
    final O o;
    final type.Numeric<?> a;
    final type.Numeric<?> b;

    private Operation2(final O o, final type.Numeric<?> a, final type.Numeric<?> b) {
      this.o = o;
      this.a = a;
      this.b = b;
    }

    @Override
    final data.Table getTable() {
      return expression.getTable(a, b);
    }

    @Override
    data.Column<?> getColumn() {
      return expression.getColumn(a, b);
    }

    @Override
    boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return o.compile(a, b, compilation);
    }

    @Override
    final Number evaluate(final java.util.Set<Evaluable> visited) {
      if (!(this.a instanceof Evaluable) || !(this.b instanceof Evaluable))
        return null;

      final Number a = (Number)((Evaluable)this.a).evaluate(visited);
      if (a == null)
        return null;

      final Number b = (Number)((Evaluable)this.b).evaluate(visited);
      if (b == null)
        return null;

      return (Number)o.evaluate((V)a, (V)b);
    }

    static final class TINYINT extends Operation2<operation.Operation2<Number>,type.TINYINT,data.TINYINT,Byte> implements exp.TINYINT {
      TINYINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      TINYINT(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      TINYINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      TINYINT(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class SMALLINT extends Operation2<operation.Operation2<Number>,type.SMALLINT,data.SMALLINT,Short> implements exp.SMALLINT {
      SMALLINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      SMALLINT(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      SMALLINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      SMALLINT(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class INT extends Operation2<operation.Operation2<Number>,type.INT,data.INT,Integer> implements exp.INT {
      INT(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      INT(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      INT(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      INT(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class BIGINT extends Operation2<operation.Operation2<Number>,type.BIGINT,data.BIGINT,Long> implements exp.BIGINT {
      BIGINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      BIGINT(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      BIGINT(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      BIGINT(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class DECIMAL extends Operation2<operation.Operation2<Number>,type.DECIMAL,data.DECIMAL,BigDecimal> implements exp.DECIMAL {
      DECIMAL(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      DECIMAL(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      DECIMAL(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      DECIMAL(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class FLOAT extends Operation2<operation.Operation2<Number>,type.FLOAT,data.FLOAT,Float> implements exp.FLOAT {
      FLOAT(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      FLOAT(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      FLOAT(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      FLOAT(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }

    static final class DOUBLE extends Operation2<operation.Operation2<Number>,type.DOUBLE,data.DOUBLE,Double> implements exp.DOUBLE {
      DOUBLE(final operation.Operation2<Number> o, final type.Numeric<?> a, final type.Numeric<?> b) {
        super(o, a, b);
      }

      DOUBLE(final operation.Operation2<Number> o, final Number a, final type.Numeric<?> b) {
        super(o, data.wrap(a), b);
      }

      DOUBLE(final operation.Operation2<Number> o, final type.Numeric<?> a, final Number b) {
        super(o, a, data.wrap(b));
      }

      DOUBLE(final operation.Operation2<Number> o, final Number a, final Number b) {
        super(o, data.wrap(a), data.wrap(b));
      }
    }
  }

  private OperationImpl() {
  }
}