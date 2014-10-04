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

import java.util.List;

import javax.persistence.FetchType;

public final class JPARelationModel {
  private final String joinTableName;
  private final Class<?> association;
  private final FetchType fetchType;
  private final ForeignKey foreignKey;
  private final ForeignKey inverseForeignKey;

  public JPARelationModel(final String joinTableName, final Class<?> association, final FetchType fetchType, final ForeignKey foreignKey, final ForeignKey inverseForeignKey) {
    this.joinTableName = joinTableName;
    this.association = association;
    this.fetchType = fetchType;
    this.foreignKey = foreignKey;
    this.inverseForeignKey = inverseForeignKey;
  }

  public String getJoinTableName() {
    return joinTableName;
  }

  public final Class<?> getAssociation() {
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

  public static final class ForeignKey {
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