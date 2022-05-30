package org.jaxdb.jsql;

import java.util.Map;

public interface RelationMap<V> extends Map<data.Key,V> {
  V superGet(final data.Key key);
}