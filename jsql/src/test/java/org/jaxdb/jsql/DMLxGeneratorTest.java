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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;
import org.libj.lang.Classes;
import org.libj.util.ArrayUtil;

import com.google.common.base.Strings;

public class DMLxGeneratorTest {
  private static final Class<?>[] approxTypes = {
    type.FLOAT.class, type.FLOAT.class, type.FLOAT.class, type.DOUBLE.class, type.DECIMAL.class, type.FLOAT.class, type.DOUBLE.class
  };

  private static final Class<?>[] numericTypes = {
    type.TINYINT.class, type.SMALLINT.class, type.INT.class, type.BIGINT.class, type.DECIMAL.class, type.FLOAT.class, type.DOUBLE.class
  };

  private static final Class<?>[] numericClasses = {
    byte.class, short.class, int.class, long.class, BigDecimal.class, float.class, double.class
  };

  private static final Class<?>[][] numericCatalogs = {numericTypes, numericClasses};
  private static final Class<?>[] allNumericTypes = ArrayUtil.concat(numericCatalogs);

  private enum Returning {
    FIRST {
      @Override
      Class<?> getReturnType(final Class<?>[] parameters, final Class<?>[][] catalogs) {
        return Returning.getReturnType(parameters, 0, null, catalogs);
      }
    },
    SECOND {
      @Override
      Class<?> getReturnType(final Class<?>[] parameters, final Class<?>[][] catalogs) {
        return Returning.getReturnType(parameters, 1, null, catalogs);
      }
    },
    BOTH {
      @Override
      Class<?> getReturnType(final Class<?>[] parameters, final Class<?>[][] catalogs) {
        return Returning.getReturnType(parameters, -1, null, catalogs);
      }
    },
    SECOND_APPROX {
      @Override
      Class<?> getReturnType(final Class<?>[] parameters, final Class<?>[][] catalogs) {
        return Returning.getReturnType(parameters, 1, approxTypes, catalogs);
      }
    },
    BOTH_APPROX {
      @Override
      Class<?> getReturnType(final Class<?>[] parameters, final Class<?>[][] catalogs) {
        return Returning.getReturnType(parameters, -1, approxTypes, catalogs);
      }
    };

    abstract Class<?> getReturnType(Class<?>[] parameters, Class<?>[][] catalogs);

    private static Class<?> getReturnType(final Class<?>[] parameters, final int x, final Class<?>[] approx, final Class<?>[][] catalogs) {
      int j = 0;
      int z = -1;
      for (int k = 0; k < catalogs.length && z == -1; ++k) // [A]
        if (catalogs[k][0].getDeclaringClass() != null)
          z = k;

      final int start;
      final int end;
      if (x > -1) {
        start = x;
        end = x + 1;
      }
      else {
        start = 0;
        end = parameters.length;
      }

      for (int k = start; k < end; ++k) { // [N]
        int p = -1;
        int c = 0;
        for (c = 0; c < catalogs.length; ++c) // [A]
          if ((p = ArrayUtil.indexOf(catalogs[c], parameters[k])) > -1)
            break;

        j = Math.max(j, p);
      }

      return approx != null ? approx[j] : catalogs[z][j];
    }
  }

  private static String getCanonicalCompositeName(final Class<?> cls, final boolean genericParameters) {
    final String name = Classes.getCanonicalCompositeName(cls);
    final int len;
    if (!genericParameters || (len = cls.getTypeParameters().length) == 0)
      return name;

    final StringBuilder builder = new StringBuilder(name).append('<');
    for (int i = 0; i < len; ++i) { // [N]
      if (i > 0)
        builder.append(',');

      builder.append('?');
    }

    builder.append('>');
    return builder.toString();
  }

  private static String getParams(final String a, final String b) {
    final String param1 = Strings.isNullOrEmpty(a) ? "" : "final %s " + a;
    final String param2 = Strings.isNullOrEmpty(b) ? "" : "final %s " + b;
    return param1 + (Strings.isNullOrEmpty(a) || Strings.isNullOrEmpty(b) ? "" : ", ") + param2;
  }

  private static String getArgs(final String a, final String b) {
    final String arg1 = Strings.isNullOrEmpty(a) ? "" : a;
    final String arg2 = Strings.isNullOrEmpty(b) ? "" : b;
    return arg1 + (Strings.isNullOrEmpty(a) || Strings.isNullOrEmpty(b) ? "" : ", ") + arg2;
  }

  private static String compose2(final String format, final Class<?>[] parameters, final Returning returning, final Class<?>[] ... catalogs) {
    final Class<?> returnType = returning.getReturnType(parameters, catalogs);
    final Object[] strings = new Object[parameters.length + 2];
    strings[0] = "exp." + returnType.getSimpleName();
    for (int i = 0, i$ = parameters.length; i < i$; ++i) // [A]
      strings[i + 1] = getCanonicalCompositeName(parameters[i], true);

    strings[strings.length - 1] = returnType.getSimpleName();
    return String.format(format, strings);
  }

  private static CharSequence compose2(final String format, final Returning returning, final Class<?>[] types, final Class<?>[] ... catalogs) {
    final StringBuilder builder = new StringBuilder();
    final Class<?>[] parameters = new Class[2];
    for (int i = 0, i$ = types.length; i < i$; ++i) { // [A]
      parameters[0] = types[i];
      for (int j = 0, j$ = types.length; j < j$; ++j) { // [A]
        parameters[1] = types[j];
        if (builder.length() > 0)
          builder.append('\n');

        builder.append(compose2(format, parameters, returning, catalogs));
      }
    }

    return builder;
  }

  private static StringBuilder compose1(final StringBuilder builder, final String function, final Returning returning, final Class<?> operatorClass, final Class<?>[] elements, final Class<?>[] ... catalogs) {
    return compose1(builder, function, returning, "a", null, operatorClass, elements, catalogs);
  }

  private static StringBuilder compose1(final StringBuilder builder, final String function, final Returning returning, final String a, final String b, final Class<?> operatorClass, final Class<?>[] elements, final Class<?>[] ... catalogs) {
    for (final Class<?> element : elements) // [A]
      builder.append(compose1(function, returning, a, b, operatorClass, new Class<?>[] {element}, catalogs)).append('\n');

    return builder;
  }

  private static CharSequence compose1(final String function, final Returning returning, final String a, final String b, final Class<?> operatorClass, final Class<?>[] parameters, final Class<?>[] ... catalogs) {
    final Class<?> returnType = returning.getReturnType(parameters, catalogs);
    final String params = getParams(a, b);
    final String args = getArgs(a, b);
    final Object[] stringArgs = new Object[parameters.length + 2];
    stringArgs[0] = "exp." + returnType.getSimpleName();
    for (int i = 0, i$ = parameters.length; i < i$; ++i) // [A]
      stringArgs[i + 1] = getCanonicalCompositeName(parameters[i], true);

    stringArgs[stringArgs.length - 1] = returnType.getSimpleName();
    return String.format("  public static %s " + function + "(" + params + ") { return new " + getCanonicalCompositeName(OperationImpl.Operation1.class, false) + ".%s(" + getCanonicalCompositeName(operatorClass, false) + "." + function + ", " + args + "); }", stringArgs);
  }

  private static CharSequence compose2(final String function, final Class<?> operatorClass, final Returning returning, final Class<?>[] types, final Class<?>[] ... catalogs) {
    return compose2(function, operatorClass, "a", "b", returning, types, catalogs);
  }

  private static CharSequence compose2(final String function, final Class<?> operatorClass, final String a, final String b, final Returning returning, final Class<?>[] types, final Class<?>[] ... catalogs) {
    final String params = getParams(a, b);
    final String args = getArgs(a, b);
    return compose2("  public static %s " + function + "(" + params + ") { return new " + getCanonicalCompositeName(OperationImpl.Operation2.class, false) + ".%s(" + getCanonicalCompositeName(operatorClass, false) + "." + function + ", " + args + "); }", returning, types, catalogs);
  }

  private static StringBuilder between(final StringBuilder builder, final int spaces) {
    builder.append('\n');
    final Class<?>[] numericTypes = {type.Numeric.class, Number.class};
    between(builder, spaces, BetweenPredicates.NumericBetweenPredicate.class, numericTypes).append('\n');

    final Class<?>[] textualTypes = {type.Textual.class, CharSequence.class};
    between(builder, spaces, BetweenPredicates.TextualBetweenPredicate.class, textualTypes).append('\n');

    final Class<?>[] temporalTypes = {type.DATE.class, type.DATETIME.class, LocalDate.class, LocalDateTime.class};
    between(builder, spaces, BetweenPredicates.TemporalBetweenPredicate.class, temporalTypes).append('\n');

    final Class<?>[] timeTypes = {type.TIME.class, LocalTime.class};
    between(builder, spaces, BetweenPredicates.TimeBetweenPredicate.class, timeTypes);

    return builder;
  }

  private static String toStringArg(final Object[] stringArgs, final Class<?> type, final int index, String generic) {
    stringArgs[index] = getCanonicalCompositeName(type, true);
    if (type.getDeclaringClass() != type.class) {
      // generic = "<V extends " + stringArgs[index] + ">";
      stringArgs[index] = stringArgs[index];
    }

    return generic;
  }

  private static StringBuilder between(final StringBuilder builder, final int spaces, final Class<?> predicateClass, final Class<?>[] types) {
    final Class<?>[] parameters = new Class[2];
    for (final Class<?> type : types) { // [A]
      final boolean isTypeClass = type.getDeclaringClass() == type.class;
      for (int i = 0, i$ = types.length; i < i$; ++i) { // [A]
        parameters[0] = types[i];
        for (int j = 0, j$ = types.length; j < j$; ++j) { // [A]
          parameters[1] = types[j];

          if (!isTypeClass) {
            boolean hasOneTypeClass = false;
            for (final Class<?> parameter : parameters) // [A]
              if (hasOneTypeClass = (parameter.getDeclaringClass() == type.class))
                break;

            if (!hasOneTypeClass)
              continue;
          }

          final Object[] stringArgs = new Object[parameters.length + 1];
          String generic = toStringArg(stringArgs, type, 0, "");
          for (int k = 1, k$ = stringArgs.length; k < k$; ++k) // [A]
            generic = toStringArg(stringArgs, parameters[k - 1], k, generic);

          builder.append(String.format(Strings.repeat(" ", spaces) + "public static " + generic + getCanonicalCompositeName(data.BOOLEAN.class, false) + " BETWEEN(final %s v, final %s l, final %s r) { return new " + getCanonicalCompositeName(predicateClass, false) + "<>(v, l, r); }", stringArgs)).append('\n');
        }
      }
    }

    return builder;
  }

  @Test
  public void generate() throws IOException {
    final StringBuilder dml = new StringBuilder();

    final Class<?> operator2 = function.NumericOperator2.class;
    dml.append(compose2("ADD", operator2, Returning.BOTH, allNumericTypes, numericCatalogs)).append("\n\n");
    dml.append(compose2("SUB", operator2, Returning.BOTH, allNumericTypes, numericCatalogs)).append("\n\n");
    dml.append(compose2("MUL", operator2, Returning.BOTH, allNumericTypes, numericCatalogs)).append("\n\n");
    dml.append(compose2("DIV", operator2, Returning.BOTH, allNumericTypes, numericCatalogs)).append("\n\n");

    // -- 1 param --
    final Class<?> function1 = function.Function1.class;
    compose1(dml, "ABS", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "ACOS", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "ASIN", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "ATAN", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "CEIL", Returning.BOTH, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "COS", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "DEGREES", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "EXP", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "FLOOR", Returning.BOTH, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "LN", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "LOG10", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "LOG2", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "RADIANS", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "ROUND", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "SIGN", Returning.BOTH, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "SIN", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "SQRT", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');
    compose1(dml, "TAN", Returning.BOTH_APPROX, function1, allNumericTypes, numericCatalogs).append('\n');

    // -- 2 param --
    final Class<?> function2 = function.Function2.class;
    dml.append(compose2("ATAN2", function2, "y", "x", Returning.BOTH_APPROX, allNumericTypes, numericCatalogs)).append('\n');
    dml.append(compose2("LOG", function2, "b", "n", Returning.SECOND_APPROX, allNumericTypes, numericCatalogs)).append('\n');
    dml.append(compose2("MOD", function2, "n", "m", Returning.FIRST, allNumericTypes, numericCatalogs)).append('\n');
    dml.append(compose2("POW", function2, "a", "p", Returning.BOTH_APPROX, allNumericTypes, numericCatalogs)).append('\n');
    dml.append(compose2("ROUND", function2, "a", "b", Returning.BOTH_APPROX, allNumericTypes, numericCatalogs)).append('\n');

    // -- between --
    between(dml, 2).append('\n');

    final File dmlJavaFile = new File("src/test/java", DMLx.class.getName().replace('.', '/') + ".java");
    final String source = new String(Files.readAllBytes(dmlJavaFile.toPath()))
      .replace("/**[", "")
      .replace("]**/", "")
      .replace("/*** public ***/ final class DMLx {", "public final class DML {")
      .replace("private DMLx() {", "private DML() {")
      .replace("/**** DMLx ****/", dml.toString().trim());

    assertMatch(source, DML.class, true);
    assertMatch(source.replace(" DML", " TestDML").replace("new Command.", "new TestCommand."), TestDML.class, false);
  }

  private static void assertMatch(final String source, final Class<?> c, final boolean mainOrTest) throws IOException {
    final File controlMainFile = new File("src/" + (mainOrTest ? "main" : "test") + "/java", c.getName().replace('.', '/') + ".java");
    final String controlSource = controlMainFile.exists() ? new String(Files.readAllBytes(controlMainFile.toPath())) : null;
    if (!source.equals(controlSource)) {
      System.err.println(source);
      assertEquals(controlSource, source);
    }
  }
}