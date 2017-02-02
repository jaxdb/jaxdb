/* Copyright (c) 2016 Seva Safris
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

final class Operator<T extends Serializable> {
  public static final Operator<BooleanTerm> AND = new Operator<BooleanTerm>("AND");
  public static final Operator<BooleanTerm> OR = new Operator<BooleanTerm>("OR");

  public static final Operator<ComparisonPredicate<?>> EQ = new Operator<ComparisonPredicate<?>>("=");
  public static final Operator<ComparisonPredicate<?>> LT = new Operator<ComparisonPredicate<?>>("<");
  public static final Operator<ComparisonPredicate<?>> LTE = new Operator<ComparisonPredicate<?>>("<=");
  public static final Operator<ComparisonPredicate<?>> GT = new Operator<ComparisonPredicate<?>>(">");
  public static final Operator<ComparisonPredicate<?>> GTE = new Operator<ComparisonPredicate<?>>(">=");
  public static final Operator<ComparisonPredicate<?>> NE = new Operator<ComparisonPredicate<?>>("<>");
  public static final Operator<ComparisonPredicate<?>> IS = new Operator<ComparisonPredicate<?>>("IS");
  public static final Operator<ComparisonPredicate<?>> NOT = new Operator<ComparisonPredicate<?>>("IS NOT");

  public static final Operator<NumericExpression> PLUS = new Operator<NumericExpression>("+");
  public static final Operator<NumericExpression> MINUS = new Operator<NumericExpression>("-");
  public static final Operator<NumericExpression> MULTIPLY = new Operator<NumericExpression>("*");
  public static final Operator<NumericExpression> DIVIDE = new Operator<NumericExpression>("/");

  public static final Operator<StringExpression> CONCAT = new Operator<StringExpression>("||");

  public static final Operator<OrderingSpec> ASC = new Operator<OrderingSpec>("ASC");
  public static final Operator<OrderingSpec> DESC = new Operator<OrderingSpec>("DESC");

  private final String symbol;

  private Operator(final String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }
}