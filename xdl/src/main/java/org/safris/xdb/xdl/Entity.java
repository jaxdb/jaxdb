package org.safris.xdb.xdl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class Entity {
  private static final Map<Class,Map<Field,GeneratedValue.Strategy>> classToGeneratedValues = new HashMap<Class,Map<Field,GeneratedValue.Strategy>>();

  protected static void init(final Class entityClass) {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValue = new HashMap<Field,GeneratedValue.Strategy>();
    Class cls = entityClass;
    do {
      for (Field field : cls.getDeclaredFields()) {
        final GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        if (generatedValue != null)
          fieldToGeneratedValue.put(field, generatedValue.strategy());
      }
    }
    while((cls = cls.getSuperclass()) != null);

    if (fieldToGeneratedValue.size() > 0)
      classToGeneratedValues.put(entityClass, fieldToGeneratedValue);
  }

  /**
   * Informs whether all generated values are present.
   *
   * @return true, if and only if all generated values are present or no generated values exist.
   */
  public boolean hasGeneratedValues() {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValue = classToGeneratedValues.get(getClass());
    if (fieldToGeneratedValue == null)
      return true;

    try {
      for (Field field : fieldToGeneratedValue.keySet())
        if (field.get(this) == null)
          return false;
    }
    catch (IllegalAccessException e) {
      return false;
    }

    return true;
  }

  @PrePersist
  @PreUpdate
  private void generateValues() {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValue = classToGeneratedValues.get(getClass());
    if (fieldToGeneratedValue == null)
      return;

    try {
      for (Field field : fieldToGeneratedValue.keySet()) {
        field.setAccessible(true);
        if (field.get(this) != null)
          continue;

        final Map<Field,GeneratedValue.Strategy> fieldToGeneratedKeys = classToGeneratedValues.get(getClass());
        if (fieldToGeneratedKeys == null)
          continue;

        final GeneratedValue.Strategy strategy = fieldToGeneratedKeys.get(field);
        if (strategy == GeneratedValue.Strategy.UUID)
          field.set(this, UUID.randomUUID().toString());
        else if (strategy == GeneratedValue.Strategy.CREATION)
          field.set(this, new Date());
        else
          throw new UnsupportedOperationException("Unknown GeneratedValue.Strategy " + strategy);
      }
    }
    catch (IllegalAccessException e) {
    }
  }
}
