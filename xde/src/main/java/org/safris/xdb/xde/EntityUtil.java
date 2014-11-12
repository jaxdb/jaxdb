package org.safris.xdb.xde;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.safris.commons.lang.reflect.Classes;

public final class EntityUtil {
  private static Map<String,Class<?>> parseBindings(final Class<? extends Database> database, final String sql) {
    try {
      final String ucsql = sql.toUpperCase();
      final String from = sql.substring(ucsql.indexOf("FROM") + 4, ucsql.indexOf("WHERE")).trim();
      final String[] parts = from.split(",");
      final Map<String,Class<?>> map = new HashMap<String,Class<?>>();
      for (final String part : parts) {
        final String[] alias = part.trim().split("\\s+");
        if (alias.length != 2)
          throw new IllegalArgumentException("Table " + alias[0] + " is missing an alias declaration.");

        map.put(alias[1], Class.forName(database.getName() + "$" + alias[0]));
      }

      return map;
    }
    catch (final ClassNotFoundException e) {
      throw new Error(e);
    }
  }

  private static String lookupColumnName(final Class<?> tableClass, final String fieldName) {
    final Field field = Classes.getDeclaredFieldDeep(tableClass, fieldName);
    if (field == null)
      throw new IllegalArgumentException("Field " + fieldName + " not found on " + tableClass.getSimpleName());

    return field.getAnnotation(Column.class).name();
  }

  private static String convertToSQL(final String string, final Map<String,Class<?>> aliasToBinding) {
    final StringTokenizer tokenizer = new StringTokenizer(string, " \t\n\r\f=<>-+", true);
    String sub = "";
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      char ch;
      if (token.length() != 1 || ((ch = token.charAt(0)) != ' ' && ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f' && ch != '=' && ch != '<' && ch != '>' && ch != '-' && ch != '+')) {
        for (final Map.Entry<String,Class<?>> entry : aliasToBinding.entrySet()) {
          if (token.startsWith(entry.getKey() + ".")) {
            final int index = entry.getKey().length();
            token = token.substring(0, index + 1) + EntityUtil.lookupColumnName(entry.getValue(), token.substring(index + 1));
          }
        }
      }

      sub += token;
    }

    return sub;
  }

  private static String getColumnsList(final Class<?> tableClass) {
    final Table table = tableClass.getAnnotation(Table.class);
    final Field[] fields = Classes.getDeclaredFieldsWithAnnotationDeep(tableClass, Column.class);
    String columns = "";
    for (final Field field : fields) {
      final Column column = field.getAnnotation(Column.class);
      columns += ", " + table.name() + "." + column.name();
    }

    return columns.substring(2);
  }

  protected static String compile(final Class<? extends Database> database, final String sql) {
    try {
      final String ucsql = sql.toUpperCase();
      int start = ucsql.indexOf("SELECT");
      if (start == -1)
        throw new IllegalArgumentException("Query is missing SELECT keyword: " + sql);

      int end = ucsql.indexOf("FROM", start + 1);

      // convert SELECT clause
      final String selectClause = sql.substring(start + 7, end).trim();
      final Map<String,Class<?>> aliasToBinding = EntityUtil.parseBindings(database, sql);
      String selectSQL = "";
      final String[] selects = selectClause.split(",");
      for (final String select : selects)
        selectSQL += ", " + EntityUtil.getColumnsList(aliasToBinding.get(select.trim()));

      // convert FROM clause
      start = ucsql.indexOf("WHERE", end + 6);
      final String fromClause = sql.substring(end + 5, start);
      String fromSQL = "";
      final String[] froms = fromClause.split(",");
      for (final String from : froms) {
        final String[] tableAlias = from.trim().split("\\s+");
        final Class<?> tableClass = Class.forName(database.getName() + "$" + tableAlias[0]);
        final Table table = tableClass.getAnnotation(Table.class);
        fromSQL += ", " + table.name() + " " + tableAlias[1];
      }

      // convert WHERE clause, plus ORDER BY and HAVING, if present
      final int havingIndex = ucsql.indexOf("HAVING", start + 1);
      final int orderIndex = ucsql.indexOf("ORDER", start + 1);
      final int whereEnd = havingIndex != -1 ? havingIndex : orderIndex != -1 ? orderIndex : sql.length();
      final String whereClause = sql.substring(start, whereEnd);
      final String whereSQL = EntityUtil.convertToSQL(whereClause, aliasToBinding);
      final String havingSQL = havingIndex == -1 ? "" : EntityUtil.convertToSQL(sql.substring(havingIndex, orderIndex != -1 ? orderIndex : sql.length()), aliasToBinding);
      final String orderSQL = orderIndex == -1 ? "" : EntityUtil.convertToSQL(sql.substring(orderIndex, sql.length()), aliasToBinding);
      return "SELECT " + selectSQL.substring(2) + " FROM " + fromSQL.substring(2) + " " + whereSQL + havingSQL + orderSQL;
    }
    catch (final ClassNotFoundException e) {
      throw new Error(e);
    }
  }

  private EntityUtil() {
  }
}