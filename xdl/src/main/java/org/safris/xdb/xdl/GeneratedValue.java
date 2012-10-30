package org.safris.xdb.xdl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface GeneratedValue {
  public Strategy strategy() default Strategy.UUID;

  public static enum Strategy {
    UUID,
    INSERT,
    UPDATE;
  }
}
