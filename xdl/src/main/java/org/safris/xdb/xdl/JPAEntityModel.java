package org.safris.xdb.xdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.safris.xdb.xdl.JPAFieldModel;
import org.safris.xdb.xdl.JPARelationModel;

public class JPAEntityModel {
  private final String name;
  private final boolean isAbstract;
  private final String extendsName;
  private final Set<String> primaryColumnNames;
  private final LinkedHashSet<JPAFieldModel> fieldModelsList = new LinkedHashSet<JPAFieldModel>();
  private final Map<String,JPAFieldModel> fieldModelsMap = new HashMap<String,JPAFieldModel>();
  private final Map<String,JPAForeignKeyModel> foreignKeyIds = new HashMap<String,JPAForeignKeyModel>();
  private final List<JPARelationModel> relations = new ArrayList<JPARelationModel>();
  private final List<JPARelationModel> inverseRelations = new ArrayList<JPARelationModel>();

  public JPAEntityModel(final String name, final boolean isAbstract, final String extendsName, final Set<String> primaryColumnNames) {
    this.name = name;
    this.isAbstract = isAbstract;
    this.extendsName = extendsName;
    this.primaryColumnNames = primaryColumnNames;
  }

  public String getName() {
    return name;
  }

  public boolean isAbstract() {
    return isAbstract;
  }

  public String getExtendsName() {
    return extendsName;
  }

  public Set<String> getPrimaryColumnNames() {
    return primaryColumnNames;
  }

  public JPAFieldModel getPrimaryFieldModel(final String columnName) {
    if (primaryColumnNames.contains(columnName))
      return getFieldModel(columnName);

    return null;
  }

  public void addFieldModel(final JPAFieldModel fieldModel) {
    fieldModelsList.add(fieldModel);
    for (JPAFieldModel.Column column : fieldModel.getColumns())
      fieldModelsMap.put(column.getColumn().get_name$().getText(), fieldModel);
  }

  public LinkedHashSet<JPAFieldModel> getFieldModels() {
    return fieldModelsList;
  }

  public JPAFieldModel getFieldModel(final String columnName) {
    return fieldModelsMap.get(columnName);
  }

  public JPAFieldModel removeFieldModel(final String columnName) {
    final JPAFieldModel fieldModel = fieldModelsMap.remove(columnName);
    if (fieldModel != null)
      fieldModelsList.remove(fieldModel);

    return fieldModel;
  }

  public JPAForeignKeyModel getForeignKeyModel(final String id) {
    return foreignKeyIds.get(id);
  }

  public void addForeignKeyModel(final JPAForeignKeyModel foreignKeyModel) {
    foreignKeyIds.put(foreignKeyModel.getId(), foreignKeyModel);
  }

  public List<JPARelationModel> getRelations() {
    return relations;
  }

  public void addRelation(final JPARelationModel relation) {
    relations.add(relation);
  }

  public List<JPARelationModel> getInverseRelations() {
    return inverseRelations;
  }

  public void addInverseRelation(final JPARelationModel relation) {
    inverseRelations.add(relation);
  }

  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof JPAEntityModel))
      return false;

    final JPAEntityModel that = (JPAEntityModel)obj;
    return name != null ? name.equals(that.name) : that.name == null;
  }

  public int hashCode() {
    return name != null ? name.hashCode() : -493269;
  }
}
