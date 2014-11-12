package org.safris.xdb.xde.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Float {
  public int precision();
  public int decimal();
  public boolean unsigned() default false;
  public boolean zerofill() default false;
  public int min();
  public int max();
}