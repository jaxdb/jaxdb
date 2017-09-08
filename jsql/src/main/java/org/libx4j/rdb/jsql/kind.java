package org.libx4j.rdb.jsql;

interface kind {
  public interface Subject<T> {
  }

  public interface Entity<T> extends Subject<T> {
  }

  public interface DataType<T> extends Subject<T> {
  }

  public interface Numeric<T> extends DataType<T> {
    public static interface UNSIGNED {
    }
  }

  public interface ExactNumeric<T> extends Numeric<T> {
  }

  public interface ApproxNumeric<T> extends Numeric<T> {
  }

  public interface Serial<T> extends DataType<T> {
  }

  public interface Textual<T> extends DataType<T> {
  }

  public interface LargeObject<T> extends DataType<T> {
  }

  public interface Temporal<T> extends DataType<T> {
  }

  public interface ARRAY<T> extends DataType<T> {
  }

  public interface BIGINT<T> extends ExactNumeric<T> {
    public interface UNSIGNED<T> extends ExactNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface BINARY<T> extends Serial<T> {
  }

  public interface BLOB<T> extends DataType<T> {
  }

  public interface BOOLEAN<T> extends DataType<T> {
  }

  public interface CHAR<T> extends Textual<T> {
  }

  public interface CLOB<T> extends DataType<T> {
  }

  public interface DATE<T> extends Temporal<T> {
  }

  public interface DATETIME<T> extends Temporal<T> {
  }

  public interface DECIMAL<T> extends ExactNumeric<T> {
    public interface UNSIGNED<T> extends ApproxNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface DOUBLE<T> extends ApproxNumeric<T> {
    public interface UNSIGNED<T> extends ApproxNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface ENUM<T> extends Textual<T> {
  }

  public interface FLOAT<T> extends ApproxNumeric<T> {
    public interface UNSIGNED<T> extends ApproxNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface INT<T> extends ExactNumeric<T> {
    public interface UNSIGNED<T> extends ExactNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface SMALLINT<T> extends ExactNumeric<T> {
    public interface UNSIGNED<T> extends ExactNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }

  public interface TIME<T> extends Temporal<T> {
  }

  public interface TINYINT<T> extends ExactNumeric<T> {
    public interface UNSIGNED<T> extends ExactNumeric<T>, ExactNumeric.UNSIGNED {
    }
  }
}