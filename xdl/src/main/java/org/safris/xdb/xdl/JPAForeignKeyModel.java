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

import java.util.Collections;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public final class JPAForeignKeyModel {
  private final JPAFieldModel fieldModel;
  private final String id;
  private final String referencedTableName;
  private final List<String> referencedColumnNames;
  private Class<?> association;
  private FetchType fetchType;
  private Field field;
  private InverseField inverseField;

  public JPAForeignKeyModel(final JPAFieldModel fieldModel, final String id, final String referencedTableName, final List<String> referencedColumnNames) {
    this.fieldModel = fieldModel;
    this.id = id;
    this.referencedTableName = referencedTableName;
    this.referencedColumnNames = referencedColumnNames;
    if (id != null)
      fieldModel.getEntityModel().addForeignKeyModel(this);
  }

  public JPAForeignKeyModel(final JPAFieldModel fieldModel, final String id, final String referencedTableName, final String referencedColumnName) {
    this(fieldModel, id, referencedTableName, Collections.singletonList(referencedColumnName));
  }

  public JPAFieldModel getFieldModel() {
    return fieldModel;
  }

  public String getId() {
    return id;
  }

  public String getReferencedTableName() {
    return referencedTableName;
  }

  public List<String> getReferencedColumnNames() {
    return referencedColumnNames;
  }

  public String getReferencedColumnName(final int index) {
    return referencedColumnNames != null && -1 < index && index < referencedColumnNames.size() ? referencedColumnNames.get(index) : null;
  }

  public final Class<?> getAssociation() {
    return association;
  }

  public final Class<?> getInverseAssociation() {
    return association == ManyToMany.class ? ManyToMany.class : (association == ManyToOne.class ? OneToMany.class : (association == OneToMany.class ? ManyToOne.class : OneToOne.class));
  }

  public FetchType getFetchType() {
    return fetchType;
  }

  public Field getField() {
    return field;
  }

  public InverseField getInverseField() {
    return inverseField;
  }

  public void setJoin(final $xdl_join join) {
    if (join == null)
      return;

    fetchType = FetchType.valueOf(join._fetch$().text());
    association = $xdl_column._foreignKey._join._association$.ManyToOne.text().equals(join._association$().text()) ? ManyToOne.class : ($xdl_column._foreignKey._join._association$.OneToMany.text().equals(join._association$().text()) ? OneToMany.class : OneToOne.class);
    field = new Field(join._field(0)._name$().text(), join._field(0)._cascade$().text());
    if (join._inverse() != null)
      inverseField = new InverseField(join._inverse(0)._name$().text(), join._inverse(0)._cascade$().text(), fieldModel.getEntityModel().getName(), join._field(0)._name$().text());
  }

  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof JPAForeignKeyModel))
      return false;

    final JPAForeignKeyModel that = (JPAForeignKeyModel)obj;
    return (id != null ? id.equals(that.id) : that.id == null) &&
      (referencedTableName != null ? referencedTableName.equals(that.referencedTableName) : that.referencedTableName == null) &&
      (association == that.association) &&
      (fetchType == that.fetchType) &&
      (field != null ? field.equals(that.field) : that.field == null) &&
      (inverseField != null ? inverseField.equals(that.inverseField) : that.inverseField == null) &&
      (referencedColumnNames != null ? referencedColumnNames.equals(that.referencedColumnNames) : that.referencedColumnNames == null);
  }

  public int hashCode() {
    return (id != null ? id.hashCode() : -32) + (referencedTableName != null ? referencedTableName.hashCode() : -81364) + (association != null ? association.hashCode() : 32128) + (fetchType != null ? fetchType.hashCode() : -291864) + (field != null ? field.hashCode() : 3864) + (inverseField != null ? inverseField.hashCode() : -82796) + (referencedColumnNames != null ? referencedColumnNames.hashCode() : 3792);
  }

  public class Field {
    private final String name;
    private final List<String> cascade;

    public Field(final String name, final List<String> cascade) {
      this.name = name;
      this.cascade = cascade;
    }

    public String getName() {
      return name;
    }

    public List<String> getCascade() {
      return cascade;
    }

    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Field))
        return false;

      final Field that = (Field)obj;
      return (name != null ? name.equals(that.name) : that.name == null) && (cascade != null ? cascade.equals(that.cascade) : that.cascade == null);
    }

    public int hashCode() {
      return (name != null ? name.hashCode() : -382) + (cascade != null ? cascade.hashCode() : -32382);
    }
  }

  public final class InverseField extends Field {
    private final String referencedTableName;
    private final String mappedBy;
    private final Class<?> inverseAssociation;

    public InverseField(final String name, final List<String> cascade, final String referencedTableName, final String mappedBy) {
      super(name, cascade);
      this.referencedTableName = referencedTableName;
      this.mappedBy = mappedBy;
      this.inverseAssociation = JPAForeignKeyModel.this.association == ManyToMany.class ? ManyToMany.class : (JPAForeignKeyModel.this.association == ManyToOne.class ? OneToMany.class : (JPAForeignKeyModel.this.association == OneToMany.class ? ManyToOne.class : OneToOne.class));
    }

    public String getReferencedTableName() {
      return referencedTableName;
    }

    public String getMappedBy() {
      return mappedBy;
    }

    public final Class<?> getInverseAssociation() {
      return inverseAssociation;
    }

    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof InverseField) || !super.equals(obj))
        return false;

      final InverseField that = (InverseField)obj;
      return (referencedTableName != null ? referencedTableName.equals(that.referencedTableName) : that.referencedTableName == null) && (mappedBy != null ? mappedBy.equals(that.mappedBy) : that.mappedBy == null) && (inverseAssociation == that.inverseAssociation);
    }

    public int hashCode() {
      return super.hashCode() + referencedTableName.hashCode() + mappedBy.hashCode() + inverseAssociation.hashCode();
    }
  }
}