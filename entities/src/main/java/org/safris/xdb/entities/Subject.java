/* Copyright (c) 2015 Seva Safris
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

import java.util.ArrayList;
import java.util.List;

import org.safris.commons.lang.Strings;

public abstract class Subject<T> extends Serializable {
  // This is implemented as a ThreadLocal variable, because of one main reason:
  // The CQL query construct crosses into static space when applying Condition(s),
  // such as AND, OR, EQ, etc. A ThreadLocal reference to the aliases list works,
  // because the CQL query is rendered immediately upon construct. As it is
  // guaranteed to run linearly in a single thread, a ThreadLocal variable fits.
  private static final ThreadLocal<List<Subject<?>>> aliases = new ThreadLocal<List<Subject<?>>>() {
    @Override
    protected List<Subject<?>> initialValue() {
      return new ArrayList<Subject<?>>();
    }
  };

  protected static String subjectAlias(final Subject<?> subject, final boolean register) {
    final List<Subject<?>> list = aliases.get();
    int i;
    for (i = 0; i < list.size(); i++)
      if (list.get(i) == subject)
        return Strings.getAlpha(i);

    if (!register)
      return null;

    list.add(subject);
    return Strings.getAlpha(i);
  }

  protected static <B>Object columnRef(final Variable<B> variable) {
    return subjectAlias(variable.owner(), false) == null ? variable.get() : variable;
  }

  protected static void clearAliases() {
    aliases.get().clear();
  }
}