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

import java.util.List;
import javax.persistence.FetchType;

public class JPARelationModel {
  private final String joinTableName;
  private final Class association;
  private final FetchType fetchType;
  private final ForeignKey foreignKey;
  private final ForeignKey inverseForeignKey;

  public JPARelationModel(final String joinTableName, final Class association, final FetchType fetchType, final ForeignKey foreignKey, final ForeignKey inverseForeignKey) {
    this.joinTableName = joinTableName;
    this.association = association;
    this.fetchType = fetchType;
    this.foreignKey = foreignKey;
    this.inverseForeignKey = inverseForeignKey;
  }

  public String getJoinTableName() {
    return joinTableName;
  }

  public Class getAssociation() {
    return association;
  }

  public FetchType getFetchType() {
    return fetchType;
  }

  public ForeignKey getForeignKey() {
    return foreignKey;
  }

  public ForeignKey getInverseForeignKey() {
    return inverseForeignKey;
  }

  public static class ForeignKey {
    private final JPAForeignKeyModel foreignKeyModel;
    private final String fieldName;
    private final List<String> cascade;

    public ForeignKey(final JPAForeignKeyModel foreignKeyModel, final String fieldName, final List<String> cascade) {
      this.foreignKeyModel = foreignKeyModel;
      this.fieldName = fieldName;
      this.cascade = cascade;
    }

    public JPAForeignKeyModel getForeignKeyModel() {
      return foreignKeyModel;
    }

    public String getFieldName() {
      return fieldName;
    }

    public List<String> getCascade() {
      return cascade;
    }
  }
}
