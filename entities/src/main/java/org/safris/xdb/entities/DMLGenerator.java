package org.safris.xdb.entities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.safris.commons.lang.Classes;
import org.safris.xdb.entities.generator.Args;

public class DMLGenerator {
  private static final Class<?>[] types = new Class<?>[] {
    type.FLOAT.class,
    type.DOUBLE.class,
    type.TINYINT.class,
    type.SMALLINT.class,
    type.INT.class,
    type.BIGINT.class,
    type.DECIMAL.class
  };

  private static final Map<Args,Class<?>> scaledMap = new HashMap<Args,Class<?>>();
  private static final Map<Args,Class<?>> directMap = new HashMap<Args,Class<?>>();
  private static final Map<Class<?>,Class<?>> singleMap = new LinkedHashMap<Class<?>,Class<?>>();

  private static final void put(final Map<Args,Class<?>> map, final Class<?> r, final Class<?> a, final Class<?> b) {
    final Args args = new Args(a, b);
//    final Class<?> exists = map.get(args);
//    if (exists != null && exists != r)
//      System.err.println("WARNING: " + args + ": " + getName(exists) + " with " + getName(r));

    map.put(args, r);
    if (type.Numeric.class.isAssignableFrom(b))
      map.put(new Args(a, getGenericType(b)), r);

    if (type.Numeric.class.isAssignableFrom(a))
      map.put(new Args(b, getGenericType(a)), r);
  }

  private static final void putApprox(final Class<?> r, final Class<?> a, final Class<?> b, final boolean includeScaled) {
    if (includeScaled)
      put(scaledMap, r, a, b);

    put(directMap, r, a, b);
  }

  private static final void putApproxs(final Class<?> a, final Class<?> b, final Class<?> r) {
    put(a, b, r, true);
  }

  private static final void putDirect(final Class<?> a, final Class<?> b, final Class<?> r) {
    put(a, b, r, false);
  }

  private static final void put(final Class<?> a, final Class<?> b, final Class<?> r, final boolean includeScaled) {
    putApprox(r, a, b, includeScaled);

    final Class<?> ua = getUnsignedClass(a);
    final boolean bUnsigned = type.UNSIGNED.class.isAssignableFrom(b);
    final Class<?> ur = getUnsignedClass(r);
      putApprox(bUnsigned ? ur : r, ua, b, includeScaled);

    if (!bUnsigned) {
      final Class<?> ub = getUnsignedClass(b);
      putApprox(r, a, ub, includeScaled);
      putApprox(ur, ua, ub, includeScaled);
    }
  }

  private static Class<?> getGenericType(final Class<?> cls) {
    final Type[] genericTypes = Classes.getGenericSuperclasses(cls);
    final Class<?> generic = genericTypes != null ? (Class<?>)genericTypes[0] : getGenericType(cls.getSuperclass());
    return type.UNSIGNED.class.isAssignableFrom(cls) ? getUnsignedPrimitive(generic) : generic;
  }

  private static Class<?> getUnsignedPrimitive(final Class<?> cls) {
    return cls == Float.class ? UNSIGNED.Float.class : cls == Double.class ? UNSIGNED.Double.class : cls == BigDecimal.class ? UNSIGNED.BigDecimal.class : cls == Short.class ? UNSIGNED.Byte.class : cls == Integer.class ? UNSIGNED.Short.class : cls == Long.class ? UNSIGNED.Integer.class : cls == BigInteger.class ? UNSIGNED.Long.class : null;
  }

  private static Class<?> getUnsignedClass(final Class<?> cls) {
    final Class<?> unsignedClass = cls.getClasses()[0];
    assert(unsignedClass.getSimpleName().equals("UNSIGNED"));
    return unsignedClass;
  }

  static {
    for (final Class<?> cls : types) {
      if (type.ApproxNumeric.class.isAssignableFrom(cls)) {
        singleMap.put(cls, cls);
        final Class<?> unsignedType = getUnsignedClass(cls);
        singleMap.put(unsignedType, unsignedType);
      }
    }

    singleMap.put(type.TINYINT.class, type.FLOAT.class);
    singleMap.put(type.TINYINT.UNSIGNED.class, type.FLOAT.UNSIGNED.class);
    singleMap.put(type.SMALLINT.class, type.FLOAT.class);
    singleMap.put(type.SMALLINT.UNSIGNED.class, type.FLOAT.UNSIGNED.class);
    singleMap.put(type.INT.class, type.FLOAT.class);
    singleMap.put(type.INT.UNSIGNED.class, type.DOUBLE.UNSIGNED.class);
    singleMap.put(type.BIGINT.class, type.DOUBLE.class);
    singleMap.put(type.BIGINT.UNSIGNED.class, type.DOUBLE.UNSIGNED.class);
    singleMap.put(type.DECIMAL.class, type.DECIMAL.class);
    singleMap.put(type.DECIMAL.UNSIGNED.class, type.DECIMAL.UNSIGNED.class);
    assert(singleMap.size() == 14);

    putApproxs(type.FLOAT.class, type.FLOAT.class, type.FLOAT.class);
    putApproxs(type.FLOAT.class, type.DOUBLE.class, type.DOUBLE.class);
    putApproxs(type.FLOAT.class, type.TINYINT.class, type.FLOAT.class);
    putApproxs(type.FLOAT.class, type.SMALLINT.class, type.FLOAT.class);
    putApproxs(type.FLOAT.class, type.INT.class, type.FLOAT.class);
    putApproxs(type.FLOAT.class, type.INT.UNSIGNED.class, type.DOUBLE.class);
    putApproxs(type.FLOAT.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.FLOAT.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.DOUBLE.class, type.DOUBLE.class, type.DOUBLE.class);
    putApproxs(type.DOUBLE.class, type.TINYINT.class, type.DOUBLE.class);
    putApproxs(type.DOUBLE.class, type.SMALLINT.class, type.DOUBLE.class);
    putApproxs(type.DOUBLE.class, type.INT.class, type.DOUBLE.class);
    putApproxs(type.DOUBLE.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.DOUBLE.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.TINYINT.class, type.TINYINT.class, type.FLOAT.class);
    putApproxs(type.TINYINT.class, type.SMALLINT.class, type.FLOAT.class);
    putApproxs(type.TINYINT.class, type.INT.class, type.FLOAT.class);
    putApproxs(type.TINYINT.class, type.INT.UNSIGNED.class, type.DOUBLE.class);
    putApproxs(type.TINYINT.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.TINYINT.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.SMALLINT.class, type.SMALLINT.class, type.FLOAT.class);
    putApproxs(type.SMALLINT.class, type.INT.class, type.FLOAT.class);
    putApproxs(type.SMALLINT.class, type.INT.UNSIGNED.class, type.DOUBLE.class);
    putApproxs(type.SMALLINT.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.SMALLINT.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.INT.class, type.INT.class, type.FLOAT.class);
    putApproxs(type.INT.class, type.INT.UNSIGNED.class, type.DOUBLE.class);
    putApproxs(type.INT.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.INT.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.BIGINT.class, type.BIGINT.class, type.DOUBLE.class);
    putApproxs(type.BIGINT.class, type.DECIMAL.class, type.DECIMAL.class);

    putApproxs(type.DECIMAL.class, type.DECIMAL.class, type.DECIMAL.class);

    putDirect(type.TINYINT.class, type.TINYINT.class, type.TINYINT.class);
    putDirect(type.TINYINT.class, type.SMALLINT.class, type.SMALLINT.class);
    putDirect(type.TINYINT.class, type.INT.class, type.INT.class);
    putDirect(type.TINYINT.class, type.BIGINT.class, type.BIGINT.class);

    putDirect(type.SMALLINT.class, type.SMALLINT.class, type.SMALLINT.class);
    putDirect(type.SMALLINT.class, type.INT.class, type.INT.class);
    putDirect(type.SMALLINT.class, type.BIGINT.class, type.BIGINT.class);

    putDirect(type.INT.class, type.INT.class, type.INT.class);
    putDirect(type.INT.class, type.BIGINT.class, type.BIGINT.class);

    putDirect(type.BIGINT.class, type.BIGINT.class, type.BIGINT.class);
  }

  private static final String[] singleParamFunctions = new String[] {
    "$1 ROUND(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Round(a, 0));\n}",
    "$1 ROUND(final $2 a, final int scale) {\n  return ($1)$n1.wrapper(new function.numeric.Round(a, scale));\n}",
    "$1 ABS(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Abs(a));\n}",
    "$1 FLOOR(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Floor(a));\n}",
    "$1 CEIL(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Ceil(a));\n}",
    "$1 SQRT(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Sqrt(a));\n}",
    "$1 EXP(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Exp(a));\n}",
    "$1 LN(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Ln(a));\n}",
    "$1 LOG2(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Log2(a));\n}",
    "$1 LOG10(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Log10(a));\n}",
    "$1 SIN(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Sin(a));\n}",
    "$1 ASIN(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Asin(a));\n}",
    "$1 COS(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Cos(a));\n}",
    "$1 ACOS(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Acos(a));\n}",
    "$1 TAN(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Tan(a));\n}",
    "$1 ATAN(final $2 a) {\n  return ($1)$n1.wrapper(new function.numeric.Atan(a));\n}"
  };

  private static final String[] doubleParamFunctions = new String[] {
    "$1 POW(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new function.numeric.Pow(a, b));\n}",
    "$1 MOD(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new function.numeric.Mod(a, b));\n}",
    "$1 LOG(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new function.numeric.Log(a, b));\n}",
    "$1 ATAN2(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new function.numeric.Atan2(a, b));\n}"
  };

  private static final String[] numericExpressionsDirect = new String[] {
    "$1 ADD(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new NumericExpression(Operator.PLUS, a, b));\n}",
    "$1 SUB(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new NumericExpression(Operator.MINUS, a, b));\n}",
    "$1 MUL(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));\n}"
  };

  private static final String[] numericExpressionsScaled = new String[] {
    "$1 DIV(final $2 a, final $3 b) {\n  return ($1)$n1.wrapper(new NumericExpression(Operator.DIVIDE, a, b));\n}"
  };

  public static String getName(final Class<?> cls) {
    if (cls == Float.class)
      return "float";

    if (cls == Double.class)
      return "double";

    if (cls == Byte.class)
      return "byte";

    if (cls == Short.class)
      return "short";

    if (cls == Integer.class)
      return "int";

    if (cls == Long.class)
      return "long";

    if (cls == BigInteger.class || cls == BigDecimal.class)
      return cls.getSimpleName();

    int index = cls.getName().indexOf("type$");
    final String strictName = Classes.getStrictName(cls);
    if (index != -1)
      return strictName.substring(index) + (cls == type.Numeric.class ? "<?>" : "");

    index = cls.getName().indexOf("UNS");
    return strictName.substring(index);
  }

  private static String newInstance(final Class<?> a, final Class<?> b, final Class<?> c) {
    if (a == type.FLOAT.class || a == type.DOUBLE.class || a == type.DECIMAL.class) {
      if (b == type.FLOAT.class || b == type.DOUBLE.class || b == type.DECIMAL.class) {
        if (c == null || c == type.FLOAT.class || c == type.DOUBLE.class || c == type.DECIMAL.class || type.UNSIGNED.class.isAssignableFrom(c)) {
          final String ub = c == type.FLOAT.class || c == type.DOUBLE.class || c == type.DECIMAL.class ? " && b.unsigned()" : "";
          return "(a.unsigned()" + ub + " ? new " + getName(a) + ".UNSIGNED($p) : new " + getName(a) + "($p))";
        }
      }

      if (type.UNSIGNED.class.isAssignableFrom(b))
        return "new " + getName(a) + ".UNSIGNED($p)";
    }

    return "new " + getName(a) + "($p)";
  }

  private static String compile(final String function, final Class<?> a, final Class<?> b, final Class<?> c, final boolean checkBothUnsigned) {
    final boolean bIsNumeric = type.Numeric.class.isAssignableFrom(b);
    String compiled = "public static final " + function.replace("$n1", newInstance(a, b, checkBothUnsigned ? c : null)).replace("$1", getName(a)).replace("$2", getName(b)) + "\n";
    if (c != null)
      compiled = compiled.replace("$3", getName(c));

    final String numericVar = bIsNumeric ? "a" : "b";
    return a == type.DECIMAL.class || a == type.DECIMAL.UNSIGNED.class ? compiled.replace("$p", numericVar + ".precision(), " + numericVar + ".scale()") : type.ExactNumeric.class.isAssignableFrom(a) ? compiled.replace("$p", numericVar + ".precision()") : compiled.replace("$p", "");
  }

  private static void printSingles() {
    for (final String function : singleParamFunctions)
      for (final Map.Entry<Class<?>,Class<?>> entry : singleMap.entrySet())
        System.out.println(compile(function, entry.getValue(), entry.getKey(), null, false));
  }

  private static void printDoubles() {
    for (final String function : doubleParamFunctions) {
      for (final Map.Entry<Args,Class<?>> entry : scaledMap.entrySet()) {
        System.out.println(compile(function, entry.getValue(), entry.getKey().a, entry.getKey().b, false));
      }
    }
  }

  private static void printNumericExpressions() {
    for (final String function : numericExpressionsDirect)
      for (final Map.Entry<Args,Class<?>> entry : directMap.entrySet()) {
        System.out.println(compile(function, entry.getValue(), entry.getKey().a, entry.getKey().b, true));
      }

    for (final String function : numericExpressionsScaled)
      for (final Map.Entry<Args,Class<?>> entry : scaledMap.entrySet()) {
        System.out.println(compile(function, entry.getValue(), entry.getKey().a, entry.getKey().b, true));
      }
  }

  private static void filter(final Map<Args,Class<?>> map) {
    final Set<Args> removes = new HashSet<Args>();
    for (final Map.Entry<Args,Class<?>> entry : map.entrySet()) {
      final Args args = entry.getKey();
      if (!type.Numeric.class.isAssignableFrom(args.b)) {
        if (args.b == Float.class && map.get(new Args(args.a, Double.class)) == entry.getValue())
          removes.add(args);
        if (args.b == UNSIGNED.Float.class && map.get(new Args(args.a, UNSIGNED.Double.class)) == entry.getValue())
          removes.add(args);
        if (args.b == Byte.class && map.get(new Args(args.a, Short.class)) == entry.getValue())
          removes.add(args);
        if (args.b == UNSIGNED.Byte.class && map.get(new Args(args.a, UNSIGNED.Short.class)) == entry.getValue())
          removes.add(args);
        if (args.b == Short.class && map.get(new Args(args.a, Integer.class)) == entry.getValue())
          removes.add(args);
        if (args.b == UNSIGNED.Short.class && map.get(new Args(args.a, UNSIGNED.Integer.class)) == entry.getValue())
          removes.add(args);
        if (args.b == Integer.class && map.get(new Args(args.a, Long.class)) == entry.getValue())
          removes.add(args);
        if (args.b == UNSIGNED.Integer.class && map.get(new Args(args.a, UNSIGNED.Long.class)) == entry.getValue())
          removes.add(args);

        if (!type.UNSIGNED.class.isAssignableFrom(entry.getValue()) && UNSIGNED.class.isAssignableFrom(args.b))
          removes.add(args);
      }

      if (!type.UNSIGNED.class.isAssignableFrom(entry.getValue()) && (args.a == type.FLOAT.UNSIGNED.class || args.a == type.DOUBLE.UNSIGNED.class || args.a == type.DECIMAL.UNSIGNED.class))
        removes.add(args);

      if (!type.UNSIGNED.class.isAssignableFrom(entry.getValue()) && (args.b == type.FLOAT.UNSIGNED.class || args.b == type.DOUBLE.UNSIGNED.class || args.b == type.DECIMAL.UNSIGNED.class))
        removes.add(args);

      if (!type.UNSIGNED.class.isAssignableFrom(entry.getValue()) && (UNSIGNED.UnsignedNumber.class.isAssignableFrom(args.a) || UNSIGNED.UnsignedNumber.class.isAssignableFrom(args.b)))
        removes.add(args);
    }

    for (final Args args : removes)
      map.remove(args);
  }

  private static void trans(final Map<Args,Class<?>> map) {
    final Map<Args,Class<?>> trans = new HashMap<Args,Class<?>>();
    for (final Map.Entry<Args,Class<?>> entry : map.entrySet()) {
      final Args args = entry.getKey();
      trans.put(new Args(args.b, args.a), entry.getValue());
    }

    map.putAll(trans);
  }

  public static void main(final String[] args) {
    filter(scaledMap);
    trans(scaledMap);
    filter(directMap);
    trans(directMap);

//    int total = 0;
//    for (final Map.Entry<Args,Class<?>> entry : scaledMap.entrySet())
//      System.out.println(getName(entry.getValue()) + " (" + getName(entry.getKey().a) + ", " + getName(entry.getKey().b) + ")");

//    System.err.println(scaledMap.size());
//    printSingles();
    printDoubles();
//    printNumericExpressions();
  }
}