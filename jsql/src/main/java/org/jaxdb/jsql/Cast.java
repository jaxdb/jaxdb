/* Copyright (c) 2017 JAX-DB
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
import java.sql.SQLException;
import java.util.Set;

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.data.Table;

public final class Cast {
  static final class AS extends Provision {
    final type.Column<?> column;
    final data.Column<?> cast;
    final Integer length;
    final Integer scale;

    AS(final type.Column<?> column, final data.Column<?> castAs, final Integer length) {
      this.column = column;
      this.cast = castAs;
      this.length = length;
      this.scale = null;
    }

    AS(final type.Column<?> column, final data.Column<?> castAs) {
      this.column = column;
      this.cast = castAs;
      this.length = null;
      this.scale = null;
    }

    AS(final type.Column<?> column, final data.Column<?> castAs, final Integer length, final Integer scale) {
      this.column = column;
      this.cast = castAs;
      this.length = length;
      this.scale = scale;
    }

    @Override
    Table table() {
      return ((Subject)column).table();
    }

    @Override
    Column<?> column() {
      return ((Subject)column).column();
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compileCast(this, compilation);
    }

    @Override
    Object evaluate(final Set<Evaluable> visited) {
      return column instanceof Evaluable ? ((Evaluable)column).evaluate(visited) : null;
    }
  }

  public static final class BOOLEAN {
    public final class AS {
      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final Integer length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BOOLEAN value;

    public BOOLEAN(final type.BOOLEAN value) {
      this.value = value;
    }
  }

  public static final class FLOAT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public FLOAT(final type.FLOAT value) {
      this.value = value;
    }
  }

  public static final class DOUBLE {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public DOUBLE(final type.DOUBLE value) {
      this.value = value;
    }
  }

  public static final class DECIMAL {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public DECIMAL(final type.DECIMAL value) {
      this.value = value;
    }
  }

  public static final class TINYINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public TINYINT(final type.TINYINT value) {
      this.value = value;
    }
  }

  public static final class SMALLINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public SMALLINT(final type.SMALLINT value) {
      this.value = value;
    }
  }

  public static final class INT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public INT(final type.INT value) {
      this.value = value;
    }
  }

  public static final class BIGINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public BIGINT(final type.BIGINT value) {
      this.value = value;
    }
  }

  public static final class CHAR {
    public final class AS {
      public data.DECIMAL DECIMAL() {
        final data.DECIMAL cast = new data.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.TINYINT TINYINT() {
        final data.TINYINT cast = new data.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final Integer precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT() {
        final data.SMALLINT cast = new data.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.SMALLINT SMALLINT(final Integer precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT() {
        final data.INT cast = new data.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.INT INT(final Integer precision) {
        final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT() {
        final data.BIGINT cast = new data.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.BIGINT BIGINT(final Integer precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final Integer length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.DATE DATE() {
        final data.DATE cast = new data.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME(final Integer precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME(final Integer precision) {
        final data.DATETIME cast = new data.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME() {
        final data.DATETIME cast = new data.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Textual<?> value;

    public CHAR(final type.Textual<?> value) {
      this.value = value;
    }
  }

  public static final class DATE {
    public final class AS {
      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DATE value;

    public DATE(final type.DATE value) {
      this.value = value;
    }
  }

  public static final class TIME {
    public final class AS {
      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.TIME TIME(final Integer precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.TIME value;

    public TIME(final type.TIME value) {
      this.value = value;
    }
  }

  public static final class DATETIME {
    public final class AS {
      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.DATE DATE() {
        final data.DATE cast = new data.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME(final Integer precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME(final Integer precision) {
        final data.DATETIME cast = new data.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME() {
        final data.DATETIME cast = new data.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DATETIME value;

    public DATETIME(final type.DATETIME value) {
      this.value = value;
    }
  }

  public static final class CLOB {
    public final class AS {
      public data.CHAR CHAR(final Integer length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final Integer length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.CLOB value;

    public CLOB(final type.CLOB value) {
      this.value = value;
    }
  }

  public static final class BLOB {
    public final class AS {
      public data.BLOB BLOB(final Integer length) {
        final data.BLOB cast = new data.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BLOB value;

    public BLOB(final type.BLOB value) {
      this.value = value;
    }
  }

  public static final class BINARY {
    public final class AS {
      public data.BLOB BLOB(final Integer length) {
        final data.BLOB cast = new data.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.BINARY BINARY(final int length) {
        final data.BINARY cast = new data.BINARY(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BINARY value;

    public BINARY(final type.BINARY value) {
      this.value = value;
    }
  }

  private Cast() {
  }
}