package org.safris.xdb.xdl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JPAFieldModel implements Cloneable {
  private final JPAEntityModel entityModel;
  private final List<Column> columns;
  private JPAForeignKeyModel foreignKeyModel;
  private boolean isPrimary;
  private boolean isImmutable;
  private List<JPAFieldModel> realFieldModels;

  public JPAFieldModel(final JPAEntityModel entityModel, final List<Column> columns) {
    this.entityModel = entityModel;
    this.columns = columns;
  }

  public JPAFieldModel(final JPAEntityModel entityModel, final Column column) {
    this(entityModel, Collections.<Column>singletonList(column));
  }

  private JPAFieldModel(final JPAFieldModel copy) {
    this.entityModel = copy.entityModel;
    this.columns = copy.getColumns() != null ? new ArrayList<Column>(copy.getColumns()) : null;
    this.foreignKeyModel = copy.foreignKeyModel;
    this.isPrimary = copy.isPrimary;
    this.isImmutable = copy.isImmutable;
    this.realFieldModels = copy.realFieldModels;
  }

  public JPAEntityModel getEntityModel() {
    return entityModel;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public Column getColumn(final int index) {
    return columns != null && -1 < index && index < columns.size() ? columns.get(index) : null;
  }

  public void setForeignKeyModel(final JPAForeignKeyModel foreignKeyModel) {
    this.foreignKeyModel = foreignKeyModel;
  }

  public JPAForeignKeyModel getForeignKeyModel() {
    return foreignKeyModel;
  }

  public void setPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public boolean isPrimary() {
    return isPrimary;
  }

  public void setImmutable(boolean isImmutable) {
    this.isImmutable = isImmutable;
  }

  public boolean isImmutable() {
    return isImmutable;
  }

  public void setRealFieldModels(final List<JPAFieldModel> realFieldModels) {
    this.realFieldModels = realFieldModels;
  }

  public List<JPAFieldModel> getRealFieldModel() {
    return realFieldModels;
  }

  public JPAFieldModel clone() {
    return new JPAFieldModel(this);
  }

  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof JPAFieldModel))
      return false;

    final JPAFieldModel that = (JPAFieldModel)obj;
    return (columns != null ? columns.equals(that.columns) : that.columns == null) && (foreignKeyModel != null ? foreignKeyModel.equals(that.foreignKeyModel) : that.foreignKeyModel == null) && (isImmutable == that.isImmutable);
  }

  public int hashCode() {
    return columns.hashCode() + (foreignKeyModel != null ? foreignKeyModel.hashCode() : -139625) + (isImmutable ? 324 : -7483);
  }

  public static class Column {
    private final $xdl_columnType<?> column;
    private final boolean isPrimary;
    private final GeneratedValue.Strategy generationStrategy;

    public Column(final $xdl_columnType<?> column, final boolean isPrimary, final GeneratedValue.Strategy generationStrategy) {
      this.column = column;
      this.isPrimary = isPrimary;
      this.generationStrategy = generationStrategy;
    }

    public $xdl_columnType<?> getColumn() {
      return column;
    }

    public boolean isPrimary() {
      return isPrimary;
    }

    public GeneratedValue.Strategy getGenerationStrategy() {
      return generationStrategy;
    }

    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Column))
        return false;

      final Column that = (Column)obj;
      return column != null ? column.equals(that.column) : that.column == null;
    }

    public int hashCode() {
      return column.get_name$().getText().hashCode() * (generationStrategy != null ? generationStrategy.hashCode() : -22378);
    }
  }
}
