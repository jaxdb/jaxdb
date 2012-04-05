package org.safris.xdb.xdl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;

@MappedSuperclass
public abstract class Entity {
  private static final Map<Class,List<Field>> classToPrimaryKeyFields = new HashMap<Class,List<Field>>();
  private static final Map<Class,Map<Field,GeneratedValue.Strategy>> classToGeneratedValues = new HashMap<Class,Map<Field,GeneratedValue.Strategy>>();

  protected static void init(final Class entityClass) {
    final List<Field> primaryKeyFields = new ArrayList<Field>();
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValue = new HashMap<Field,GeneratedValue.Strategy>();
    Class cls = entityClass;
    do {
      for (Field field : cls.getDeclaredFields()) {
        if (field.getAnnotation(Id.class) != null)
          primaryKeyFields.add(field);

        final GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        if (generatedValue != null)
          fieldToGeneratedValue.put(field, generatedValue.strategy());
      }
    }
    while((cls = cls.getSuperclass()) != null);

    if (primaryKeyFields.size() > 0)
      classToPrimaryKeyFields.put(entityClass, primaryKeyFields);

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

  public boolean delete(final EntityManager entityManager) {
    final List<Field> primaryKeyFields = classToPrimaryKeyFields.get(getClass());
    String pql = "";
    for (int i = 0; i < primaryKeyFields.size(); i++) {
      final Field field = primaryKeyFields.get(i);
      pql += " AND a." + field.getName() + " = ?" + (i + 1);
    }

    pql = "DELETE FROM " + getClass().getSimpleName() + " a WHERE " + pql.substring(5);
    final Query query = entityManager.createQuery(pql);
    try {
      for (int i = 0; i < primaryKeyFields.size(); i++) {
        final Field field = primaryKeyFields.get(i);
        field.setAccessible(true);
        query.setParameter(i + 1, field.get(this));
      }
    }
    catch (IllegalAccessException e) {
    }

    final boolean deleted = query.executeUpdate() == 1;
    if (deleted) {
      final PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
      final Object id = util.getIdentifier(this);
      entityManager.getEntityManagerFactory().getCache().evict(getClass(), id);
      entityManager.detach(this);
    }

    return deleted;
  }
}
