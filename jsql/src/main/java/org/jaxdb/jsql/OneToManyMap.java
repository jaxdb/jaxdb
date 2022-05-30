package org.jaxdb.jsql;

interface OneToManyMap<V> extends RelationMap<V> {
  @Override
  default boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }
}