package org.safris.xdb.xde;

public abstract class Column<T> extends Basis {
  protected final int sqlType;
  protected final Class<T> type;
  protected final Entity owner;
  protected final String cqlName;
  protected final String name;
  protected final boolean unique;
  protected final boolean primary;
  protected final boolean nullable;

  public Column(final int sqlType, final Class<T> type, final Entity owner, final String cqlName, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable) {
    this.sqlType = sqlType;
    this.type = type;
    this.owner = owner;
    this.cqlName = cqlName;
    this.name = name;
    this.value = _default;
    this.unique = unique;
    this.primary = primary;
    this.nullable = nullable;
  }

  protected T value;

  public void set(final T value) {
    this.value = value;
  }

  public T get() {
    return value;
  }
}