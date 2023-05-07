/* Copyright (c) 2023 JAX-DB
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

import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;

import org.libj.util.Interval;
import org.openjax.binarytree.IntervalTreeSet;

public abstract class NavigableRelationMap<V> extends RelationMap<V> implements NavigableMap<data.Key,V> {
  static Notifier<?>[] getNotifiers(final Iterator<Connector> iterator, final int depth) throws SQLException, IOException {
    if (!iterator.hasNext())
      return depth == 0 ? null : new Notifier<?>[depth];

    final Notifier<?> notifier = iterator.next().getNotifier();
    if (notifier == null)
      return getNotifiers(iterator, depth);

    final Notifier<?>[] notifiers = getNotifiers(iterator, depth + 1);
    notifiers[depth] = notifier;
    return notifiers;
  }

  private static data.BOOLEAN and(final type.Column<?> c, final Object min, final Object max) {
    return AND(new ComparisonPredicate.Gte<>(c, min), new ComparisonPredicate.Lt<>(c, max));
  }

  private static data.BOOLEAN and(final Interval<type.Key> i) {
    data.BOOLEAN and = null;
    final type.Key min = i.getMin();
    final type.Key max = i.getMax();
    and = and(min.column(0), min.value(0), max.value(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, and(min.column(j), min.value(j), max.value(j)));

    return and;
  }

  private static data.BOOLEAN where(final Interval<type.Key>[] missings) {
    data.BOOLEAN or = and(missings[0]);
    for (int i = 1, i$ = missings.length; i < i$; ++i)
      or = OR(or, and(missings[i]));

    return or;
  }

  private final IntervalTreeSet<type.Key> mask = new IntervalTreeSet<>();

  NavigableRelationMap(final data.Table table) {
    super(table);
  }

  @Override
  public final boolean containsKey(final data.Key key) {
    return mask.contains(key);
  }

  final void addKey(final Interval<type.Key>[] intervals) {
    mask.addAll(intervals);
  }

  final V put(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    return put(key, value);
  }

  public Interval<type.Key>[] diffKeys(final data.Key fromKey, final data.Key toKey) {
    return mask.difference(new Interval<>(fromKey, toKey));
  }

  public final SortedMap<data.Key,V> select(final data.Key fromKey, final data.Key toKey) throws IOException, SQLException {
    final Interval<type.Key>[] diff = diffKeys(fromKey, toKey);
    if (diff.length > 0) {
      final ConcurrentHashMap<String,Connector> dataSourceIdToConnectors = Database.getConnectors(table.getSchema().getClass());
      final Notifier<?>[] notifiers = getNotifiers(dataSourceIdToConnectors.values().iterator(), 0);
      if (notifiers == null)
        return null;

      try (final RowIterator<? extends data.Table> rows =
        SELECT(table).
        FROM(table).
        WHERE(where(diff))
          .execute(dataSourceIdToConnectors.get(null))) {

        while (rows.nextRow()) {
          final data.Table row = rows.nextEntity();
          for (final Notifier<?> notifier : notifiers) // [A]
            notifier.onSelect(row, false);
        }

        for (final Notifier<?> notifier : notifiers) // [A]
          notifier.onSelectRange(table, diff);
      }
    }

    return subMap(fromKey, toKey);

  }
}