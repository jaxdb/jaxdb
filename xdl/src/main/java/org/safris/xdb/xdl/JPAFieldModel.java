package org.safris.xdb.xdl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;

public class JPAFieldModel {
  private final JPAEntityModel entityModel;
  private final List<Column> columns;
  private JPAForeignKeyModel foreignKeyModel;

  public JPAFieldModel(final JPAEntityModel entityModel, final List<Column> columns) {
    this.entityModel = entityModel;
    this.columns = columns;
  }

  public JPAFieldModel(final JPAEntityModel entityModel, final Column column) {
    this(entityModel, Collections.<Column>singletonList(column));
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

  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof JPAFieldModel))
      return false;

    final JPAFieldModel that = (JPAFieldModel)obj;
    return (columns != null ? columns.equals(that.columns) : that.columns == null) && (foreignKeyModel != null ? foreignKeyModel.equals(that.foreignKeyModel) : that.foreignKeyModel == null);
  }

  public int hashCode() {
    return columns.hashCode() + (foreignKeyModel != null ? foreignKeyModel.hashCode() : -139625);
  }

  public static class Column {
    private final $xdl_columnType<?> column;
    private final boolean isPrimary;

    public Column($xdl_columnType<?> column, boolean isPrimary) {
      this.column = column;
      this.isPrimary = isPrimary;
    }

    public $xdl_columnType<?> getColumn() {
      return column;
    }

    public boolean isPrimary() {
      return isPrimary;
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
      return column.get_name$().getText().hashCode() * (isPrimary ? 2 : -2);
    }
  }
}
