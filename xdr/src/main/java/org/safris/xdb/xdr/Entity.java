/*  Copyright Safris Software 2011
 *
 *  This code is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.safris.xdb.xdr;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
  private static final Map<Class,Map<Method[],GenerateOnInsert.Strategy>> classToGenerateOnInsert = new HashMap<Class,Map<Method[],GenerateOnInsert.Strategy>>();
  private static final Map<Class,Map<Method[],GenerateOnUpdate.Strategy>> classToGenerateOnUpdate = new HashMap<Class,Map<Method[],GenerateOnUpdate.Strategy>>();
  private static final Map<Class,List<Field>> classToCheckValuesOnUpdate = new HashMap<Class,List<Field>>();

  protected static void init(final Class entityClass) {
    final Map<String,Method[]> idFieldMethods = new HashMap<String,Method[]>();
    classToIdFieldMethods.put(entityClass, idFieldMethods);

    final String idClassName = entityClass.getName() + "$id";
    final Class<?>[] innerClasses = entityClass.getDeclaredClasses();
    for (final Class<?> innerClass : innerClasses) {
      if (idClassName.equals(innerClass.getName())) {
        idClasses.put(entityClass, innerClass);
        final Method[] idClassMethods = innerClass.getMethods();
        for (final Method idClassMethod : idClassMethods)
          if (idClassMethod.getName().startsWith("set"))
            idFieldMethods.put(idClassMethod.getName().substring(3), new Method[]{null, idClassMethod});

        break;
      }
    }

    final Map<String,Method> setMethods = new HashMap<String,Method>();
    final Map<String,Method> getMethods = new HashMap<String,Method>();
    for (final Method method : entityClass.getMethods()) {
      if (method.getName().startsWith("get")) {
        final String name = method.getName().substring(3);
        getMethods.put(name.toLowerCase(), method);
        final Method[] idFieldMethod = idFieldMethods.get(name);
        if (idFieldMethod != null)
          idFieldMethod[0] = method;
      }
      else if (method.getName().startsWith("set")) {
        setMethods.put(method.getName().substring(3).toLowerCase(), method);
      }
    }

    final List<Field> primaryKeyFields = new ArrayList<Field>();
    final Map<Method[],GenerateOnInsert.Strategy> fieldToGenerateOnInsert = new HashMap<Method[],GenerateOnInsert.Strategy>();
    final Map<Method[],GenerateOnUpdate.Strategy> fieldToGenerateOnUpdate = new HashMap<Method[],GenerateOnUpdate.Strategy>();
    final List<Field> checkFieldsOnUpdate = new ArrayList<Field>();
    Class cls = entityClass;
    do {
      for (final Field field : cls.getDeclaredFields()) {
        if (field.getAnnotation(Id.class) != null)
          primaryKeyFields.add(field);

        final GenerateOnInsert generateOnInsert = field.getAnnotation(GenerateOnInsert.class);
        if (generateOnInsert != null) {
          final String name = field.getName().toLowerCase();
          final Method getMethod = getMethods.get(name);
          final Method setMethod = setMethods.get(name);
          fieldToGenerateOnInsert.put(new Method[]{getMethod, setMethod}, generateOnInsert.strategy());
        }

        final GenerateOnUpdate generateOnUpdate = field.getAnnotation(GenerateOnUpdate.class);
        if (generateOnUpdate != null) {
          final String name = field.getName().toLowerCase();
          final Method getMethod = getMethods.get(name);
          final Method setMethod = setMethods.get(name);
          fieldToGenerateOnUpdate.put(new Method[]{getMethod, setMethod}, generateOnUpdate.strategy());
        }

        final CheckOnUpdate checkOnUpdate = field.getAnnotation(CheckOnUpdate.class);
        if (checkOnUpdate != null) {
          if (checkOnUpdate.action() == CheckOnUpdate.Action.EQUALS)
            checkFieldsOnUpdate.add(field);
        }
      }
    }
    while((cls = cls.getSuperclass()) != null);

    if (primaryKeyFields.size() > 0)
      classToPrimaryKeyFields.put(entityClass, primaryKeyFields);

    if (fieldToGenerateOnInsert.size() > 0)
      classToGenerateOnInsert.put(entityClass, fieldToGenerateOnInsert);

    if (fieldToGenerateOnUpdate.size() > 0)
      classToGenerateOnUpdate.put(entityClass, fieldToGenerateOnUpdate);

    if (checkFieldsOnUpdate.size() > 0)
      classToCheckValuesOnUpdate.put(entityClass, checkFieldsOnUpdate);
  }

  /**
   * Informs whether all generated values are present.
   *
   * @return true, if and only if all generated values are present or no generated values exist.
   */
  public boolean hasGeneratedValuesOnInsert() {
    final Map<Method[],GenerateOnInsert.Strategy> fieldToGeneratedValueOnInsert = classToGenerateOnInsert.get(getClass());
    if (fieldToGeneratedValueOnInsert == null)
      return true;

    try {
      for (final Method[] methods : fieldToGeneratedValueOnInsert.keySet()) {
        if (methods[0].invoke(this) == null)
          return false;
      }
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }

    return true;
  }

  //@PrePersist // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  private void generateOnInsert() {
    final Map<Method[],GenerateOnInsert.Strategy> fieldToGeneratedValueOnInsert = classToGenerateOnInsert.get(getClass());
    if (fieldToGeneratedValueOnInsert == null)
      return;

    try {
      for (final Method[] methods : fieldToGeneratedValueOnInsert.keySet()) {
        if (methods[0].invoke(this) != null)
          continue;

        final GenerateOnInsert.Strategy strategy = fieldToGeneratedValueOnInsert.get(methods);
        if (strategy == GenerateOnInsert.Strategy.UUID) {
          if (methods[0].getReturnType() == String.class)
            methods[1].invoke(this, UUID.randomUUID().toString().toUpperCase());
          else
            throw new UnsupportedOperationException("Unexpected GenerateOnUpdate.Strategy." + strategy + " for " + methods[0].getReturnType().getSimpleName() + " parameter type.");
        }
        else if (strategy == GenerateOnInsert.Strategy.TIMESTAMP) {
          if (methods[0].getReturnType() == Date.class)
            methods[1].invoke(this, new Date());
          else if (methods[0].getReturnType() == Long.class)
            methods[1].invoke(this, System.currentTimeMillis());
          else
            throw new UnsupportedOperationException("Unexpected GenerateOnUpdate.Strategy." + strategy + " for " + methods[0].getReturnType().getSimpleName() + " parameter type.");
        }
        else {
          throw new UnsupportedOperationException("Unknown GenerateOnInsert.Strategy " + strategy);
        }
      }
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }
  }

  //@PrePersist // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  //@PreUpdate // NOTE: Bug in EclipseLink does not allow @PreUpdate to change fields that have already been marked as no diff
  private void generateOnUpdate(final EntityManager entityManager) {
    final Map<Method[],GenerateOnUpdate.Strategy> fieldToGeneratedValueOnUpdate = classToGenerateOnUpdate.get(getClass());
    if (fieldToGeneratedValueOnUpdate == null)
      return;

    Entity currentEntity = null;
    try {
      for (final Method[] methods : fieldToGeneratedValueOnUpdate.keySet()) {
        final GenerateOnUpdate.Strategy strategy = fieldToGeneratedValueOnUpdate.get(methods);
        if (strategy == GenerateOnUpdate.Strategy.TIMESTAMP) {
          if (methods[0].getReturnType() == Date.class)
            methods[1].invoke(this, new Date());
          else if (methods[0].getReturnType() == Long.class)
            methods[1].invoke(this, System.currentTimeMillis());
          else
            throw new UnsupportedOperationException("Unexpected GenerateOnUpdate.Strategy." + strategy + " for " + methods[0].getReturnType().getSimpleName() + " parameter type.");
        }
        else if (strategy == GenerateOnUpdate.Strategy.INCREMENT) {
          if (entityManager != null) {
            if (methods[0].getReturnType() == Short.class) {
              if (currentEntity == null)
                currentEntity = (Entity)entityManager.find(getClass(), createId());

              methods[1].invoke(this, (Short)methods[0].invoke(currentEntity) + (short)1);
            }
            else if (methods[0].getReturnType() == Integer.class) {
              if (currentEntity == null)
                currentEntity = (Entity)entityManager.find(getClass(), createId());

              methods[1].invoke(this, (Integer)methods[0].invoke(currentEntity) + 1);
            }
            else if (methods[0].getReturnType() == Long.class) {
              if (currentEntity == null)
                currentEntity = (Entity)entityManager.find(getClass(), createId());

              methods[1].invoke(this, (Long)methods[0].invoke(currentEntity) + 1L);
            }
            else if (methods[0].getReturnType() == BigInteger.class) {
              if (currentEntity == null)
                currentEntity = (Entity)entityManager.find(getClass(), createId());

              methods[1].invoke(this, ((BigInteger)methods[0].invoke(currentEntity)).add(BigInteger.ONE));
            }
            else {
              throw new UnsupportedOperationException("Unexpected GenerateOnUpdate.Strategy." + strategy + " for " + methods[0].getReturnType().getSimpleName() + " parameter type.");
            }
          }
        }
        else {
          throw new UnsupportedOperationException("Unknown GenerateOnUpdate.Strategy " + strategy);
        }
      }
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }
  }

  private void checkValuesOnUpdate(final EntityManager entityManager) throws UpdateCheckFailedException {
    final List<Field> checkValuesOnUpdate = classToCheckValuesOnUpdate.get(getClass());
    if (checkValuesOnUpdate == null)
      return;

    final Entity currentEntity = (Entity)entityManager.find(getClass(), createId());
    final List<Field> failedFields = new ArrayList<Field>();
    try {
      for (final Field field : checkValuesOnUpdate) {
        field.setAccessible(true);
        final Object currentValue = field.get(currentEntity);
        final Object newValue = field.get(this);
        if (currentValue != null ? !currentValue.equals(newValue) : newValue != null)
          failedFields.add(field);
      }
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException("Implementation issue", e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Implementation issue", e);
    }

    if (failedFields.size() != 0)
      throw new UpdateCheckFailedException(new UpdateCheckFailedException.FailureDetail(currentEntity, failedFields.toArray(new Field[failedFields.size()])));
  }

  private Object createId() {
    Object id = null;
    try {
      id = idClasses.get(getClass()).newInstance();
      final Map<String,Method[]> idFieldMethods = classToIdFieldMethods.get(getClass());
      for (final Method[] idMethods : idFieldMethods.values())
        idMethods[1].invoke(id, idMethods[0].invoke(this));
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

  public void insert(final EntityManager entityManager) {
    generateOnInsert();
    generateOnUpdate(null);
    entityManager.persist(this);
  }

  public <T>T update(final EntityManager entityManager) throws UpdateCheckFailedException {
    checkValuesOnUpdate(entityManager);
    generateOnUpdate(entityManager);
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

  public void attach(final EntityManager entityManager) throws UpdateCheckFailedException {
    if (entityManager.contains(this))
      return;

    final Entity clone = clone();
    final Entity attached = clone.update(entityManager);
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
