package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Decimal extends Column<Double> {
  public final int precision;
  public int decimal;
  public final boolean unsigned;
  public final Number min;
  public final Number max;

  public Decimal(final Entity owner, final String cqlName, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final int decimal, final boolean unsigned, final Number min, final Number max) {
    this(Types.DECIMAL, Double.class, owner, cqlName, name, _default, unique, primary, nullable, precision, decimal, unsigned, min, max);
  }

  public Decimal(final int sqlType, final Class<Double> type, final Entity owner, final String cqlName, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final int decimal, final boolean unsigned, final Number min, final Number max) {
    super(sqlType, type, owner, cqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }
}