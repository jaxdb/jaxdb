package org.jaxdb.jsql;

interface OneToOneMap<V extends data.Table<?>> extends RelationMap<V> {
  @Override
  default V remove(final Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  default boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  default V removeOld(final type.Key key) {
    return remove(key);
  }

  @Override
  default V superGet(final data.Key key) {
    return get(key);
  }
}