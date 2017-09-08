/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.libx4j.rdb.jsql.SelectImpl.untyped.FROM;
import org.libx4j.rdb.jsql.SelectImpl.untyped.GROUP_BY;
import org.libx4j.rdb.jsql.SelectImpl.untyped.HAVING;
import org.libx4j.rdb.jsql.SelectImpl.untyped.JOIN;
import org.libx4j.rdb.jsql.SelectImpl.untyped.LIMIT;
import org.libx4j.rdb.jsql.SelectImpl.untyped.OFFSET;
import org.libx4j.rdb.jsql.SelectImpl.untyped.ON;
import org.libx4j.rdb.jsql.SelectImpl.untyped.ORDER_BY;
import org.libx4j.rdb.jsql.SelectImpl.untyped.SELECT;
import org.libx4j.rdb.jsql.SelectImpl.untyped.UNION;
import org.libx4j.rdb.jsql.SelectImpl.untyped.WHERE;

final class SelectCommand extends Command {
  private final SELECT<?> select;
  private FROM<?> from;
  private WHERE<?> where;
  private List<JOIN<?>> join;
  private List<ON<?>> on;
  private GROUP_BY<?> groupBy;
  private HAVING<?> having;
  private ORDER_BY<?> orderBy;
  private LIMIT<?> limit;
  private OFFSET<?> offset;
  private UNION<?> union;
  private Map<Integer,type.ENUM<?>> translateTypes;

  public SelectCommand(final SELECT<?> select) {
    this.select = select;
  }

  protected SELECT<?> select() {
    return select;
  }

  protected void add(final FROM<?> from) {
    this.from = from;
  }

  protected FROM<?> from() {
    return from;
  }

  protected void add(final WHERE<?> where) {
    this.where = where;
  }

  protected WHERE<?> where() {
    return where;
  }

  protected void add(final JOIN<?> join) {
    if (this.join == null)
      this.join = new ArrayList<JOIN<?>>();

    this.join.add(join);
  }

  protected List<JOIN<?>> join() {
    return join;
  }

  protected void add(final ON<?> on) {
    if (this.on == null)
      this.on = new ArrayList<ON<?>>();

    // Since ON is optional, for each JOIN without ON, add a null to this.on
    for (int i = 0; i < this.join.size() - this.on.size() - 1; i++)
      this.on.add(null);

    this.on.add(on);
  }

  protected List<ON<?>> on() {
    return on;
  }

  protected void add(final GROUP_BY<?> groupBy) {
    this.groupBy = groupBy;
  }

  protected GROUP_BY<?> groupBy() {
    return groupBy;
  }

  protected void add(final HAVING<?> having) {
    this.having = having;
  }

  protected HAVING<?> having() {
    return having;
  }

  protected void add(final ORDER_BY<?> orderBy) {
    this.orderBy = orderBy;
  }

  protected ORDER_BY<?> orderBy() {
    return orderBy;
  }

  protected void add(final LIMIT<?> limit) {
    this.limit = limit;
  }

  protected LIMIT<?> limit() {
    return limit;
  }

  protected void add(final OFFSET<?> offset) {
    this.offset = offset;
  }

  protected OFFSET<?> offset() {
    return offset;
  }

  protected void add(final UNION<?> union) {
    this.union = union;
  }

  protected UNION<?> union() {
    return union;
  }

  public Map<Integer,type.ENUM<?>> getTranslateTypes() {
    return this.translateTypes;
  }

  public void setTranslateTypes(final Map<Integer,type.ENUM<?>> translateTypes) {
    this.translateTypes = translateTypes;
  }

  @Override
  protected void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = Compiler.getCompiler(compilation.vendor);
    compiler.assignAliases(from(), compilation);
    compiler.compile(this, select(), compilation);
    compiler.compile(from(), compilation);
    if (join() != null)
      for (int i = 0; i < join().size(); i++)
        compiler.compile(join().get(i), on() != null && i < on().size() ? on().get(i) : null, compilation);

    compiler.compile(where(), compilation);
    compiler.compile(groupBy(), compilation);
    compiler.compile(having(), compilation);
    compiler.compile(orderBy(), compilation);
    compiler.compile(limit(), offset(), compilation);
    compiler.compile(union(), compilation);
  }
}