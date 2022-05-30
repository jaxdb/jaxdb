/* Copyright (c) 2015 JAX-DB
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

package org.jaxdb.ddlx;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints.PrimaryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PostgreSQLCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLCompiler.class);

  PostgreSQLCompiler() {
    super(DBVendor.POSTGRE_SQL);
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  String primaryKey(final $Table table, final int[] columns, final PrimaryKey.Using$ using) {
    return super.primaryKey(table, columns, null);
  }

  @Override
  LinkedHashSet<DropStatement> dropTypes(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final LinkedHashSet<DropStatement> statements = super.dropTypes(table, tableNameToEnumToOwner);
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Enum) {
          statements.add(new DropStatement("DROP TYPE IF EXISTS " + q(Dialect.getTypeName(($Enum)column, tableNameToEnumToOwner))));
        }
        else if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (Generator.isAuto(type))
            statements.add(new DropStatement("DROP SEQUENCE IF EXISTS " + q(getSequenceName(table, type))));
        }
      }
    }

    return statements;
  }

  @Override
  List<CreateStatement> types(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final List<CreateStatement> statements = new ArrayList<>();
    if (table.getColumn() != null) {
      StringBuilder sql = null;
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Enum) {
          final $Enum type = ($Enum)column;
          if (sql == null)
            sql = new StringBuilder();
          else
            sql.setLength(0);

          sql.append("CREATE TYPE ").append(q(Dialect.getTypeName(type, tableNameToEnumToOwner))).append(" AS ENUM (");
          if (type.getValues$() != null) {
            final List<String> enums = Dialect.parseEnum(type.getValues$().text());
            final Iterator<String> iterator = enums.iterator();
            for (int i = 0; iterator.hasNext(); ++i) {
              if (i > 0)
                sql.append(", ");

              sql.append('\'').append(iterator.next()).append('\'');
            }
          }

          statements.add(0, new CreateStatement(sql.append(')').toString()));
        }
        else if (column instanceof $Integer) {
          final $Integer integer = ($Integer)column;
          if (Generator.isAuto(integer)) {
            final StringBuilder builder = new StringBuilder("CREATE SEQUENCE " + q(getSequenceName(table, integer)));
            final String min = getAttr("min", integer);
            if (min != null)
              builder.append(" MINVALUE ").append(min);

            final String max = getAttr("max", integer);
            if (max != null)
              builder.append(" MAXVALUE ").append(max);

            final String _default = getAttr("default", integer);
            if (_default != null)
              builder.append(" START ").append(_default);

            builder.append(" CYCLE");
            statements.add(0, new CreateStatement(builder.toString()));
          }
        }
      }
    }

    statements.addAll(super.types(table, tableNameToEnumToOwner));
    return statements;
  }

  @Override
  String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    return Generator.isAuto(column) ? "DEFAULT nextval('" + getSequenceName(table, column) + "')" : "";
  }

  @Override
  String dropIndexOnClause(final $Table table) {
    return "";
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    final String uniqueClause;
    if ($Index.Type$.HASH.text().equals(type.text())) {
      if (columns.length > 1) {
        if (logger.isWarnEnabled())
          logger.warn("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition.");

        return null;
      }

      if (unique && logger.isWarnEnabled())
        logger.warn("UNIQUE HASH indexes are not supported by PostgreSQL. Creating non-UNIQUE index.");

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    return new CreateStatement("CREATE " + uniqueClause + "INDEX " + q(indexName) + " ON " + q(tableName) + " USING " + type.text() + " (" + SQLDataTypes.csvNames(getDialect(), columns) + ")");
  }
}