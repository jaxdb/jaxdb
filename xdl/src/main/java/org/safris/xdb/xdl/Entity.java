package org.safris.xdb.xdl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

@MappedSuperclass
public abstract class Entity implements Cloneable {
  private static final Map<Class,Class<?>> idClasses = new HashMap<Class,Class<?>>();
  private static final Map<Class,Map<String,Method[]>> classToIdFieldMethods = new HashMap<Class,Map<String,Method[]>>();
  private static final Map<Class,List<Field>> classToPrimaryKeyFields = new HashMap<Class,List<Field>>();
  private static final Map<Class,Map<Field,GeneratedValue.Strategy>> classToGeneratedValuesOnInsert = new HashMap<Class,Map<Field,GeneratedValue.Strategy>>();
  private static final Map<Class,Map<Field,GeneratedValue.Strategy>> classToGeneratedValuesOnUpdate = new HashMap<Class,Map<Field,GeneratedValue.Strategy>>();
  private static final Map<Class,List<Field>> classToCheckValuesOnUpdate = new HashMap<Class,List<Field>>();

  protected static void init(final Class entityClass) {
    final Map<String,Method[]> idFieldMethods = new HashMap<String,Method[]>();
    classToIdFieldMethods.put(entityClass, idFieldMethods);

    final String idClassName = entityClass.getName() + "$id";
    final Class<?>[] innerClasses = entityClass.getDeclaredClasses();
    for (final Class<?> innerClass : innerClasses) {
      if (idClassName.equals(innerClass.getName())) {
        idClasses.put(entityClass, innerClass);
        final Method[] methods = innerClass.getDeclaredMethods();
        for (final Method setMethod : methods)
          if (setMethod.getName().startsWith("set"))
            idFieldMethods.put(setMethod.getName().substring(3), new Method[]{setMethod, null});

        break;
      }
    }

    for (final Method getMethod : entityClass.getDeclaredMethods()) {
      if (getMethod.getName().startsWith("get")) {
        final Method[] method = idFieldMethods.get(getMethod.getName().substring(3));
        if (method != null) {
          method[1] = getMethod;
        }
      }
    }

    final List<Field> primaryKeyFields = new ArrayList<Field>();
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValueOnInsert = new HashMap<Field,GeneratedValue.Strategy>();
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValueOnUpdate = new HashMap<Field,GeneratedValue.Strategy>();
    final List<Field> checkFieldsOnUpdate = new ArrayList<Field>();
    Class cls = entityClass;
    do {
      for (Field field : cls.getDeclaredFields()) {
        if (field.getAnnotation(Id.class) != null)
          primaryKeyFields.add(field);

        final GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        if (generatedValue != null) {
          if (generatedValue.strategy() == GeneratedValue.Strategy.UPDATE)
            fieldToGeneratedValueOnUpdate.put(field, generatedValue.strategy());
          else
            fieldToGeneratedValueOnInsert.put(field, generatedValue.strategy());
        }

        final OnUpdate onUpdate = field.getAnnotation(OnUpdate.class);
        if (onUpdate != null) {
          if (onUpdate.action() == OnUpdate.Action.CHECK_EQUALS)
            checkFieldsOnUpdate.add(field);
        }
      }
    }
    while((cls = cls.getSuperclass()) != null);

    if (primaryKeyFields.size() > 0)
      classToPrimaryKeyFields.put(entityClass, primaryKeyFields);

    if (fieldToGeneratedValueOnInsert.size() > 0)
      classToGeneratedValuesOnInsert.put(entityClass, fieldToGeneratedValueOnInsert);

    if (fieldToGeneratedValueOnUpdate.size() > 0)
      classToGeneratedValuesOnUpdate.put(entityClass, fieldToGeneratedValueOnUpdate);

    if (checkFieldsOnUpdate.size() > 0)
      classToCheckValuesOnUpdate.put(entityClass, checkFieldsOnUpdate);
  }

  /**
   * Informs whether all generated values are present.
   *
   * @return true, if and only if all generated values are present or no generated values exist.
   */
  public boolean hasGeneratedValuesOnInsert() {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValueOnInsert = classToGeneratedValuesOnInsert.get(getClass());
    if (fieldToGeneratedValueOnInsert == null)
      return true;

    try {
      for (Field field : fieldToGeneratedValueOnInsert.keySet()) {
        field.setAccessible(true);
        if (field.get(this) == null)
          return false;
      }
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }

    return true;
  }

  //@PrePersist // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  private void generateValuesOnInsert() {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValueOnInsert = classToGeneratedValuesOnInsert.get(getClass());
    if (fieldToGeneratedValueOnInsert == null)
      return;

    try {
      for (final Field field : fieldToGeneratedValueOnInsert.keySet()) {
        field.setAccessible(true);
        if (field.get(this) != null)
          continue;

        final GeneratedValue.Strategy strategy = fieldToGeneratedValueOnInsert.get(field);
        if (strategy == GeneratedValue.Strategy.UUID) {
          if (field.getType() == String.class)
            field.set(this, UUID.randomUUID().toString().toUpperCase());
        }
        else if (strategy == GeneratedValue.Strategy.INSERT) {
          if (field.getType() == Date.class)
            field.set(this, new Date());
        }
        else {
          throw new UnsupportedOperationException("Unknown GeneratedValue.Strategy " + strategy);
        }
      }
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }
  }

  //@PrePersist // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  //@PreUpdate // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  private void generateValuesOnUpdate() {
    final Map<Field,GeneratedValue.Strategy> fieldToGeneratedValueOnUpdate = classToGeneratedValuesOnUpdate.get(getClass());
    if (fieldToGeneratedValueOnUpdate == null)
      return;

    try {
      for (final Field field : fieldToGeneratedValueOnUpdate.keySet()) {
        field.setAccessible(true);
        final GeneratedValue.Strategy strategy = fieldToGeneratedValueOnUpdate.get(field);
        if (strategy == GeneratedValue.Strategy.UPDATE) {
          if (field.getType() == Date.class)
            field.set(this, new Date());
        }
        else {
          throw new UnsupportedOperationException("Unknown GeneratedValue.Strategy " + strategy);
        }
      }
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }
  }

  private void checkValuesOnUpdate(final EntityManager entityManager) {
    final List<Field> checkValuesOnUpdate = classToCheckValuesOnUpdate.get(getClass());
    if (checkValuesOnUpdate == null)
      return;

    final Object id = createId();
    for (final Field field : checkValuesOnUpdate) {
      field.setAccessible(true);
      final Entity currentEntity = (Entity)entityManager.find(getClass(), id);
      try {
        final Object currentValue = field.get(currentEntity);
        final Object newValue = field.get(this);
        if (currentValue != null ? !currentValue.equals(newValue) : newValue != null)
          throw new CheckNotSatisfiedException(field.getName() + ": " + currentValue + " != " + newValue);
      }
      catch (IllegalArgumentException e) {
        throw new RuntimeException("Implementation issue", e);
      }
      catch (IllegalAccessException e) {
        throw new RuntimeException("Implementation issue", e);
      }
    }
  }

  private Object createId() {
    Object id = null;
    try {
      id = idClasses.get(getClass()).newInstance();
      final Map<String,Method[]> idFieldMethods = classToIdFieldMethods.get(getClass());
      for (final Method[] idMethods : idFieldMethods.values())
        idMethods[0].invoke(id, idMethods[1].invoke(this));
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (InstantiationException e) {
      throw new RuntimeException("Implementation issue", e);
    }

    return id;
  }

  public void persist(final EntityManager entityManager) {
    generateValuesOnInsert();
    generateValuesOnUpdate();
    entityManager.persist(this);
  }

  public <T>T merge(final EntityManager entityManager) {
    checkValuesOnUpdate(entityManager);
    generateValuesOnUpdate();
    return entityManager.<T>merge((T)this);
  }

  public void refresh(final EntityManager entityManager) {
    entityManager.refresh(this);
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
      throw new RuntimeException("Implementation issue", e);
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

  public void detach() {
    final Field[] fields = getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getName().startsWith("_persistence")) {
        field.setAccessible(true);
        if (!field.getType().isPrimitive()) {
          try {
            field.set(this, null);
          }
          catch (IllegalAccessException e) {
            throw new RuntimeException("Implementation issue", e);
          }
        }
      }
      else if (Collection.class.isAssignableFrom(field.getType())) {
        field.setAccessible(true);
        try {
          final Collection collection = (Collection)field.get(this);
          if (collection == null || !collection.getClass().getName().startsWith("org.eclipse.persistence.indirection"))
            continue;

          if (collection instanceof List) {
            final Field elementDataField = collection.getClass().getDeclaredField("elementData");
            elementDataField.setAccessible(true);
            final Object[] elementData = (Object[])elementDataField.get(collection);
            field.set(this, elementData != null && elementData.length > 0 ? Arrays.asList(elementData) : null);
          }
          else if (collection instanceof Set) {
            final Field delegateField = collection.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            final Set delegate = (Set)delegateField.get(collection);
            field.set(this, delegate != null && delegate.size() > 0 ? new HashSet(delegate) : null);
          }
        }
        catch (Exception e) {
          throw new RuntimeException("Implementation issue", e);
        }
      }
    }
  }

  public void attach(final EntityManager entityManager) {
    if (entityManager.contains(this))
      return;

    final Entity clone = clone();
    final Entity attached = clone.merge(entityManager);
    entityManager.refresh(attached);

    final Field[] fields = getClass().getDeclaredFields();
    for (Field field : fields) {
      if (Collection.class.isAssignableFrom(field.getType())) {
        field.setAccessible(true);
        try {
          final Collection collection = (Collection)field.get(attached);
          if (collection != null && collection.getClass().getName().startsWith("org.eclipse.persistence.indirection") && field.get(this) == null)
            field.set(this, collection);
        }
        catch (Exception e) {
          throw new RuntimeException("Implementation issue", e);
        }
      }
    }
  }

  public abstract Entity clone();
}
