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

package org.libx4j.rdb.ddlx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;

public class StatementBatch {
  private final LinkedHashSet<Statement> statements;

  public StatementBatch(final LinkedHashSet<Statement> statements) {
    this.statements = statements;
  }

  public void writeOutput(final File file) {
    final StringBuilder builder = new StringBuilder();
    for (final Statement statement : statements)
      builder.append("\n\n").append(statement).append(';');

    try {
      if (file.getParentFile().isFile())
        throw new IllegalArgumentException(file.getParent() + " is a file.");

      if (!file.getParentFile().exists())
        if (!file.getParentFile().mkdirs())
          throw new IllegalArgumentException("Could not create path: " + file.getParent());

      try (final FileOutputStream out = new FileOutputStream(file)) {
        out.write(builder.substring(2).getBytes());
      }
    }
    catch (final IOException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  public LinkedHashSet<Statement> getStatements() {
    return this.statements;
  }
}