/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.entities.Case.ELSE;
import org.safris.xdb.entities.Case.Search;
import org.safris.xdb.entities.Case.Simple;
import org.safris.xdb.entities.Case.THEN;

final class CaseCommand extends Command {
  private final Simple.CASE<?,?> simpleCase;
  private final Search.WHEN<?> searchCase;
  private List<THEN<?,?>> then = new ArrayList<THEN<?,?>>();
  private ELSE<?> _else;

  protected CaseCommand(final Simple.CASE<?,?> simpleCase) {
    this.simpleCase = simpleCase;
    this.searchCase = null;
  }

  protected CaseCommand(final Search.WHEN<?> searchCase) {
    this.searchCase = searchCase;
    this.simpleCase = null;
  }

  protected final List<THEN<?,?>> then() {
    return then;
  }

  protected final void add(final THEN<?,?> then) {
    this.then.add(then);
  }

  protected final ELSE<?> else_() {
    return _else;
  }

  protected final void add(final ELSE<?> _else) {
    this._else = _else;
  }

  @Override
  @SuppressWarnings("rawtypes")
  protected final void serialize(final Serialization serialization) throws IOException {
    final Serializer serializer = Serializer.getSerializer(serialization.vendor);
    if (simpleCase != null)
      serializer.serialize(simpleCase, else_(), serialization);
    else if (searchCase != null)
      serializer.serialize(searchCase, serialization);
    else
      throw new UnsupportedOperationException("Both simple and search CASEs should not be null");

    for (int i = 0; i < then().size(); i++) {
      final THEN<?,?> then = then().get(i);
      serializer.serialize((Case.WHEN)then.parent(), then, else_(), serialization);
    }

    serializer.serialize(else_(), serialization);
  }
}