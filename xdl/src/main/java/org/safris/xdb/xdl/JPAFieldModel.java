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
import java.util.Collections;
import java.util.List;

import org.safris.xdb.xdr.GenerateOnInsert;
import org.safris.xdb.xdr.GenerateOnUpdate;

public final class JPAFieldModel implements Cloneable {
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

  public void setPrimary(final boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public boolean isPrimary() {
    return isPrimary;
  }

  public void setImmutable(final boolean isImmutable) {
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

  public static final class Column {
    private final $xdl_columnType column;
    private final boolean isPrimary;
    private final GenerateOnInsert.Strategy generationStrategy;
    private final GenerateOnUpdate.Strategy generateOnUpdate;
    private final List<String> checkOnUpdate;

    public Column(final $xdl_columnType column, final boolean isPrimary, final GenerateOnInsert.Strategy generateOnInsert, final GenerateOnUpdate.Strategy generateOnUpdate, final List<String> checkOnUpdate) {
      this.column = column;
      this.isPrimary = isPrimary;
      this.generationStrategy = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.checkOnUpdate = checkOnUpdate;
    }

    public $xdl_columnType getColumn() {
      return column;
    }

    public boolean isPrimary() {
      return isPrimary;
    }

    public GenerateOnInsert.Strategy getGenerateOnInsert() {
      return generationStrategy;
    }

    public GenerateOnUpdate.Strategy getGenerateOnUpdate() {
      return generateOnUpdate;
    }

    public List<String> getCheckOnUpdate() {
      return checkOnUpdate;
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
      return column._name$().text().hashCode() * (generationStrategy != null ? generationStrategy.hashCode() : -22378);
    }
  }
}