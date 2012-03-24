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
