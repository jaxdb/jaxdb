/* Copyright (c) 2017 JAX-DB
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jaxdb.jsql.SelectImpl.untyped.FROM;
import org.jaxdb.jsql.SelectImpl.untyped.GROUP_BY;
import org.jaxdb.jsql.SelectImpl.untyped.HAVING;
import org.jaxdb.jsql.SelectImpl.untyped.JOIN;
import org.jaxdb.jsql.SelectImpl.untyped.LIMIT;
import org.jaxdb.jsql.SelectImpl.untyped.OFFSET;
import org.jaxdb.jsql.SelectImpl.untyped.ON;
import org.jaxdb.jsql.SelectImpl.untyped.ORDER_BY;
import org.jaxdb.jsql.SelectImpl.untyped.SELECT;
import org.jaxdb.jsql.SelectImpl.untyped.UNION;
import org.jaxdb.jsql.SelectImpl.untyped.WHERE;
import org.jaxdb.jsql.SelectImpl.untyped.FOR;
import org.jaxdb.jsql.SelectImpl.untyped.NOWAIT;
import org.jaxdb.jsql.SelectImpl.untyped.SKIP_LOCKED;

final class SelectCommand extends Command<SELECT<?>> {
  private FROM<?> from;
  private WHERE<?> where;
  private List<JOIN<?>> join;
  private List<ON<?>> on;
  private GROUP_BY<?> groupBy;
  private HAVING<?> having;
  private ORDER_BY<?> orderBy;
  private LIMIT<?> limit;
  private OFFSET<?> offset;
  private FOR<?> _for;
  private NOWAIT<?> nowait;
  private SKIP_LOCKED<?> skipLocked;
  private Collection<UNION<?>> union;
  private Map<Integer,type.ENUM<?>> translateTypes;

  SelectCommand(final SELECT<?> select) {
    super(select);
  }

  void add(final FROM<?> from) {
    this.from = from;
  }

  FROM<?> from() {
    return from;
  }

  void add(final WHERE<?> where) {
    this.where = where;
  }

  WHERE<?> where() {
    return where;
  }

  void add(final JOIN<?> join) {
    if (this.join == null)
      this.join = new ArrayList<>();

    this.join.add(join);
  }

  List<JOIN<?>> join() {
    return join;
  }

  void add(final ON<?> on) {
    if (this.on == null)
      this.on = new ArrayList<>();

    // Since ON is optional, for each JOIN without ON, add a null to this.on
    for (int i = 0; i < this.join.size() - this.on.size() - 1; i++)
      this.on.add(null);

    this.on.add(on);
  }

  List<ON<?>> on() {
    return on;
  }

  void add(final GROUP_BY<?> groupBy) {
    this.groupBy = groupBy;
  }

  GROUP_BY<?> groupBy() {
    return groupBy;
  }

  void add(final HAVING<?> having) {
    this.having = having;
  }

  HAVING<?> having() {
    return having;
  }

  void add(final ORDER_BY<?> orderBy) {
    this.orderBy = orderBy;
  }

  ORDER_BY<?> orderBy() {
    return orderBy;
  }

  void add(final LIMIT<?> limit) {
    this.limit = limit;
  }

  LIMIT<?> limit() {
    return limit;
  }

  void add(final OFFSET<?> offset) {
    this.offset = offset;
  }

  OFFSET<?> offset() {
    return offset;
  }

  void add(final FOR<?> _for) {
    this._for = _for;
  }

  FOR<?> _for() {
    return _for;
  }

  void add(final NOWAIT<?> nowait) {
    this.nowait = nowait;
  }

  NOWAIT<?> nowait() {
    return nowait;
  }

  void add(final SKIP_LOCKED<?> skipLocked) {
    this.skipLocked = skipLocked;
  }

  SKIP_LOCKED<?> skipLocked() {
    return skipLocked;
  }

  void add(final UNION<?> union) {
    if (this.union == null)
      this.union = new ArrayList<>();

    this.union.add(union);
  }

  Collection<UNION<?>> union() {
    return union;
  }

  public Map<Integer,type.ENUM<?>> getTranslateTypes() {
    return this.translateTypes;
  }

  public void setTranslateTypes(final Map<Integer,type.ENUM<?>> translateTypes) {
    this.translateTypes = translateTypes;
  }

  private Class<? extends Schema> schema;

  @Override
  Class<? extends Schema> getSchema() {
    return schema == null ? schema = (from != null ? from.tables.iterator().next().schema() : null) : schema;
  }

  private type.Entity getEntityWithoutFrom() {
    type.Entity table = null;
    for (final kind.Subject<?> entity : getKeyword().entities) {
      Evaluable original;
      if (entity instanceof type.Entity && (table == null || entity.getClass() == table.getClass()))
        table = (type.Entity)entity;
      else if (entity instanceof type.Subject && (original = ((type.Subject<?>)entity).original()) instanceof type.DataType && (table == null || ((type.DataType<?>)original).owner.getClass() == table.getClass()))
        table = ((type.DataType<?>)original).owner;
      else
        return null;
    }

    return table;
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException {
    final Compiler compiler = compilation.compiler;
    compiler.assignAliases(from(), join(), compilation);
    compiler.compile(this, getKeyword(), compilation);
    if (from() == null) {
      final type.Entity table = getEntityWithoutFrom();
      if (table != null)
        add(new SelectImpl.Entity.FROM(getKeyword().parent(), table));
    }

    compiler.compile(from(), compilation);

    if (join() != null)
      for (int i = 0; i < join().size(); i++)
        compiler.compile(join().get(i), on() != null && i < on().size() ? on().get(i) : null, compilation);

    compiler.compile(where(), compilation);
    compiler.compile(groupBy(), compilation);
    compiler.compile(having(), compilation);
    compiler.compile(union(), compilation);
    compiler.compile(orderBy(), compilation);
    compiler.compile(limit(), offset(), compilation);
    if (_for() != null)
      compiler.compile(_for().strength, _for().tables, compilation);

    compiler.compile(nowait(), compilation);
    compiler.compile(skipLocked(), compilation);
  }
}