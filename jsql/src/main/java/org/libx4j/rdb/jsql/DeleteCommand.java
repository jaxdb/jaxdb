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

import org.libx4j.rdb.jsql.Delete.DELETE;
import org.libx4j.rdb.jsql.Delete.WHERE;

final class DeleteCommand extends Command {
  private final DELETE delete;
  private WHERE where;

  public DeleteCommand(final DELETE delete) {
    this.delete = delete;
  }

  public DELETE delete() {
    return delete;
  }

  public WHERE where() {
    return where;
  }

  public void add(final WHERE where) {
    this.where = where;
  }

  @Override
  protected void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = Compiler.getCompiler(compilation.vendor);
    if (where() != null)
      compiler.compile(delete(), where(), compilation);
    else
      compiler.compile(delete(), compilation);
  }
}