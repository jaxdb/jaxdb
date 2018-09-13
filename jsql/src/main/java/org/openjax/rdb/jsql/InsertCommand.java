/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.jsql;

import java.io.IOException;

import org.openjax.rdb.jsql.InsertImpl.INSERT;
import org.openjax.rdb.jsql.InsertImpl.VALUES;

final class InsertCommand extends Command {
  private final INSERT<?> insert;
  private VALUES<?> values;

  public InsertCommand(final INSERT<?> insert) {
    this.insert = insert;
  }

  public INSERT<?> insert() {
    return insert;
  }

  public void add(final VALUES<?> values) {
    this.values = values;
  }

  public VALUES<?> values() {
    return values;
  }

  private Class<? extends Schema> schema;

  @Override
  protected Class<? extends Schema> getSchema() {
    if (schema != null)
      return schema;

    if (insert().entity != null)
      return schema = insert.entity.schema();

    if (insert().columns != null)
      return schema = insert.columns[0].owner.schema();

    throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
  }

  @Override
  protected void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = Compiler.getCompiler(compilation.vendor);
    if (values() != null)
      compiler.compile(insert(), values(), compilation);
    else
      compiler.compile(insert(), compilation);
  }
}