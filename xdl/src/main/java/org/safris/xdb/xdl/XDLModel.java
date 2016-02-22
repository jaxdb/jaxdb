/* Copyright (c) 2011 Seva Safris
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.safris.xdb.xdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XDLModel {
  private final List<JPAEntityModel> entitiesList = new ArrayList<JPAEntityModel>();
  private final Map<String,JPAEntityModel> entitiesMap = new HashMap<String,JPAEntityModel>();
  private final Map<String,List<JPAForeignKeyModel.InverseField>> inverseFieldsPerTable = new HashMap<String,List<JPAForeignKeyModel.InverseField>>();

  public void addEntity(final JPAEntityModel entity) {
    entitiesMap.put(entity.getName(), entity);
    entitiesList.add(entity);
  }

  public List<JPAEntityModel> getEntities() {
    return entitiesList;
  }

  public JPAEntityModel getEntity(final String name) {
    return entitiesMap.get(name);
  }

  public void registerInverseField(final String tableName, final JPAForeignKeyModel.InverseField inverseField) {
    List<JPAForeignKeyModel.InverseField> list = inverseFieldsPerTable.get(tableName);
    if (list == null)
      inverseFieldsPerTable.put(tableName, list = new ArrayList<JPAForeignKeyModel.InverseField>());

    list.add(inverseField);
  }

  public List<JPAForeignKeyModel.InverseField> getInverseFields(final String tableName) {
    return inverseFieldsPerTable.get(tableName);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof XDLModel))
      return false;

    final XDLModel that = (XDLModel)obj;
    return (entitiesList != null ? entitiesList.equals(that.entitiesList) : that.entitiesList == null) && (inverseFieldsPerTable != null ? inverseFieldsPerTable.equals(that.inverseFieldsPerTable) : that.inverseFieldsPerTable == null);
  }

  @Override
  public int hashCode() {
    return entitiesList.hashCode() + inverseFieldsPerTable.hashCode();
  }
}