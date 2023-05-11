package org.jaxdb.jsql;

import org.libj.util.function.IntBooleanConsumer;
import org.libj.util.function.Throwing;

@FunctionalInterface
public interface ThrowingIntBooleanConsumer<E extends Throwable> extends IntBooleanConsumer {
  @Override
  default void accept(final int v1, final boolean v2) {
    try {
      acceptThrows(v1, v2);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  void acceptThrows(int v1, boolean v2) throws E;
}