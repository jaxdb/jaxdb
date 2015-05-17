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

public class Inv {
  public static void main(final String[] args) {
    go("ABSOLUTE", "ACTION", "ADD", "AFTER", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "AREARRAY", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATIONBEFORE", "BEGIN", "BETWEEN", "BINARY", "BIT", "BLOB", "BOOLEAN", "BOTHBREADTH", "BYCALL", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTERCHECK", "CLOB", "CLOSE", "COLLATE", "COLLATION", "COLUMN", "COMMITCONDITION", "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTSCONSTRUCTOR", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBECURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUPCURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_PATH", "CURRENT_ROLECURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "CYCLEDATA", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULTDEFERRABLE", "DEFERRED", "DELETE", "DEPTH", "DEREF", "DESCDESCRIBE", "DESCRIPTOR", "DETERMINISTICDIAGNOSTICS", "DISCONNECT", "DISTINCT", "DO", "DOMAIN", "DOUBLEDROP", "DYNAMICEACH", "ELSE", "ELSEIF", "END", "END-EXEC", "EQUALS", "ESCAPE", "EXCEPTEXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNALFALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FREEFULL", "FUNCTIONGENERAL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "GROUPINGHANDLE", "HAVING", "HOLD", "HOURIDENTITY", "IF", "IMMEDIATE", "IN", "INDICATORINITIALLY", "INNER", "INOUT", "INPUT", "INSERT", "INT", "INTEGERINTERSECT", "INTERVAL", "INTO", "IS", "ISOLATIONJOINKEYLANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEAVE", "LEFTLEVEL", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATOR", "LOOPMAP", "MATCH", "METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTHNAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NESTING", "NEW", "NEXTNO", "NONE", "NOT", "NULL", "NUMERICOBJECT", "OF", "OLD", "ON", "ONLY", "OPEN", "OPTIONOR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "OVERLAPSPAD", "PARAMETER", "PARTIAL", "PATH", "PRECISIONPREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLICREAD", "READS", "REAL", "RECURSIVE", "REDO", "REF", "REFERENCES", "REFERENCINGRELATIVE", "RELEASE", "REPEAT", "RESIGNAL", "RESTRICT", "RESULT", "RETURNRETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINEROW", "ROWSSAVEPOINT", "SCHEMA", "SCROLL", "SEARCH", "SECOND", "SECTION", "SELECTSESSION", "SESSION_USER", "SET", "SETS", "SIGNAL", "SIMILAR", "SIZESMALLINT", "SOME", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTIONSQLSTATE", "SQLWARNING", "START", "STATE", "STATIC", "SYSTEM_USERTABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMPTIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTIONTRANSLATION", "TREAT", "TRIGGER", "TRUEUNDER", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL", "UPDATEUSAGE", "USER", "USINGVALUE", "VALUES", "VARCHAR", "VARYING", "VIEWWHEN", "WHENEVER", "WHERE", "WHILE", "WITH", "WITHOUT", "WORK", "WRITEYEARZONE");
  }

  public static void go(final String ... args) {
    String out = "";
    for (final String s : args)
      out += "\n" + inv(s.toLowerCase());

    System.out.println(out.substring(2));
  }

  private static String inv(final String s) {
    String a = "";
    String b = "";
    final char[] chars = s.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      final char ch = chars[i];
      final int more = chars.length - i - 1;
      final String rem = more == 1 ? ".?" : more == 0 ? "" : ".{0," + more + "}";
      b += "|(" + a + "[^" + ch + "]" + rem + ")";
      a += ch;
    }

    return b;
  }
}