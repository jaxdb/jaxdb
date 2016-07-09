package org.safris.xdb.xde;

public class Predicate<T> extends Condition<Data<T>> {
  private final String predicate;
  private final Field<T> field;
  private final Object[] condition;

  protected Predicate(final String predicate, final Field<T> field, final Object ... condition) {
    this.predicate = predicate;
    this.field = field;
    this.condition = condition;
  }

  @Override
  protected Keyword<Data<Data<T>>> parent() {
    return null;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    format(this, field, serialization);
    serialization.sql.append(" ").append(predicate).append(" ");
    format(this, condition, serialization);
  }
}