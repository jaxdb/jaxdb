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

package org.openjax.rdb.ddlx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class StatementBatch {
  private final LinkedHashSet<Statement> statements;

  public StatementBatch(final LinkedHashSet<Statement> statements) {
    this.statements = statements;
  }

  public void writeOutput(final File file) {
    final StringBuilder builder = new StringBuilder();
    final Iterator<Statement> iterator = statements.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0)
        builder.append("\n\n");

      builder.append(iterator.next()).append(';');
    }

    try {
      if (file.getParentFile().isFile())
        throw new IllegalArgumentException(file.getParent() + " is a file");

      if (!file.getParentFile().exists())
        if (!file.getParentFile().mkdirs())
          throw new IllegalArgumentException("Could not create path: " + file.getParent());

      Files.write(file.toPath(), builder.toString().getBytes());
    }
    catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public LinkedHashSet<Statement> getStatements() {
    return this.statements;
  }
}