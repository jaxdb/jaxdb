package org.safris.xdb.xdl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface OnUpdate {
  public Action action();

  public static enum Action {
    CHECK_EQUALS;
  }
}
