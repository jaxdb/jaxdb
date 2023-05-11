package org.jaxdb.jsql;

import java.util.function.IntConsumer;

import org.libj.util.function.Throwing;

@FunctionalInterface
public interface ThrowingIntConsumer<E extends Throwable> extends IntConsumer {
  @Override
  default void accept(final int v1) {
    try {
      acceptThrows(v1);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  void acceptThrows(int v1) throws E;
}