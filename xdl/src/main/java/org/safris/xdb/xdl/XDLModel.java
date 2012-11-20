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

package org.safris.xdb.xdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XDLModel {
  private List<JPAEntityModel> entitiesList = new ArrayList<JPAEntityModel>();
  private Map<String,JPAEntityModel> entitiesMap = new HashMap<String,JPAEntityModel>();
  private Map<String,List<JPAForeignKeyModel.InverseField>> inverseFieldsPerTable = new HashMap<String,List<JPAForeignKeyModel.InverseField>>();

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

  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof XDLModel))
      return false;

    final XDLModel that = (XDLModel)obj;
    return (entitiesList != null ? entitiesList.equals(that.entitiesList) : that.entitiesList == null) &&
      (inverseFieldsPerTable != null ? inverseFieldsPerTable.equals(that.inverseFieldsPerTable) : that.inverseFieldsPerTable == null);
  }

  public int hashCode() {
    return entitiesList.hashCode() + inverseFieldsPerTable.hashCode();
  }
}
