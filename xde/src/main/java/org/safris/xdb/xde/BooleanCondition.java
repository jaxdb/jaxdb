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

package org.safris.xdb.xde;

public class BooleanCondition<T extends Data<?>> extends Condition<T> {
  protected static enum Operator {
    AND("AND"),
    OR("OR");

    private final String symbol;

    Operator(final String symbol) {
      this.symbol = symbol;
    }

    @Override
    public String toString() {
      return symbol;
    }
  }

  private static <T extends Data<?>>void formatBraces(final Operator operator, final Condition<?> condition, final Serialization serialization) {
    if (condition instanceof LogicalCondition) {
      condition.serialize(serialization);
    }
    else if (condition instanceof BooleanCondition) {
      if (operator == ((BooleanCondition<T>)condition).operator) {
        condition.serialize(serialization);
      }
      else {
        serialization.sql.append("(");
        condition.serialize(serialization);
        serialization.sql.append(")");
      }
    }
    else {
      throw new Error("Unknown condition type: " + condition.getClass().getName());
    }
  }

  protected final Operator operator;
  private final Condition<?>[] conditions;

  @SafeVarargs
  protected BooleanCondition(final Operator operator, final Condition<?> ... conditions) {
    this.operator = operator;
    this.conditions = conditions;
  }

  @Override
  protected Keyword<Data<T>> parent() {
    return null;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    for (int i = 0; i < conditions.length; i++) {
      formatBraces(operator, conditions[i], serialization);
      serialization.sql.append(i < conditions.length - 1 ? " " + operator + " " : "");
    }
  }
}