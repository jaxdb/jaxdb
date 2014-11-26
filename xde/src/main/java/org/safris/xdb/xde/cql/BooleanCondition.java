package org.safris.xdb.xde.cql;

public class BooleanCondition extends Condition {
  public static BooleanCondition AND(final LogicalCondition a, final LogicalCondition b) {
    return new BooleanCondition(Operator.AND, a, b);
  }

  public static BooleanCondition AND(final LogicalCondition a, final BooleanCondition b) {
    return new BooleanCondition(Operator.AND, a, b);
  }

  public static BooleanCondition AND(final BooleanCondition a, final LogicalCondition b) {
    return new BooleanCondition(Operator.AND, a, b);
  }

  public static BooleanCondition AND(final BooleanCondition a, final BooleanCondition b) {
    return new BooleanCondition(Operator.AND, a, b);
  }

  public static BooleanCondition OR(final LogicalCondition a, final LogicalCondition b) {
    return new BooleanCondition(Operator.OR, a, b);
  }

  public static BooleanCondition OR(final LogicalCondition a, final BooleanCondition b) {
    return new BooleanCondition(Operator.OR, a, b);
  }

  public static BooleanCondition OR(final BooleanCondition a, final LogicalCondition b) {
    return new BooleanCondition(Operator.OR, a, b);
  }

  public static BooleanCondition OR(final BooleanCondition a, final BooleanCondition b) {
    return new BooleanCondition(Operator.OR, a, b);
  }

  protected enum Operator {
    AND("AND"),
    OR("OR");

    private final String symbol;

    Operator(final String symbol) {
      this.symbol = symbol;
    }

    public String toString() {
      return symbol;
    }
  }

  private static String formatBarce(final Operator operator, final BooleanCondition condition) {
    return operator == condition.operator ? condition.toString() : "(" + condition + ")";
  }

  protected final Operator operator;

  protected BooleanCondition(final Operator operator, final BooleanCondition a, final BooleanCondition b) {
    super(formatBarce(operator, a) + " " + operator + " " + formatBarce(operator, b));
    this.operator = operator;
  }

  protected BooleanCondition(final Operator operator, final BooleanCondition a, final LogicalCondition b) {
    super(formatBarce(operator, a) + " " + operator + " " + b);
    this.operator = operator;
  }

  protected BooleanCondition(final Operator operator, final LogicalCondition a, final BooleanCondition b) {
    super(a + " " + operator + " " + formatBarce(operator, b));
    this.operator = operator;
  }

  protected BooleanCondition(final Operator operator, final LogicalCondition a, final LogicalCondition b) {
    super(a + " " + operator + " " + b);
    this.operator = operator;
  }
}