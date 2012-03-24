/*  Copyright Safris Software 2012
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.safris.commons.lang.Strings;
import org.safris.commons.util.Random;
import org.safris.commons.xml.NamespaceBinding;
import org.w3.x2001.xmlschema.xs_schema;

public class JPABeanTransform extends XDLTransformer {
  public static void main(final String[] args) throws Exception {
    createJPABeans(new File(args[0]), new File(args[1]));
  }

  public static void createJPABeans(final File xdlFile, final File outDir) {
    final xdl_database database = parseArguments(xdlFile, null);
    try {
      final JPABeanTransform creator = new JPABeanTransform(database);
      final Map<String,String> files = creator.parse();

      final File packageDir = outDir != null ? new File(outDir, creator.pkg.replace('.', '/')) : null;
      for (Map.Entry<String,String> entry : files.entrySet())
        writeOutput(entry.getValue(), packageDir != null ? new File(packageDir, entry.getKey() + ".java") : null);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String getCascadeString(final List<String> cascadeList) {
    final Set<String> cascadeTypes = cascadeList != null ? new HashSet(cascadeList) : null;
    String cascadeString;
    if (cascadeTypes == null || cascadeTypes.size() == 0) {
      cascadeString = "";
    }
    else {
      if (cascadeTypes.size() == 4) {
        cascadeString = CascadeType.class.getName() + ".ALL";
      }
      else {
        cascadeString = "";
        for (String cascadeType : cascadeTypes)
          cascadeString += ", " + CascadeType.class.getName() + "." + cascadeType;

        cascadeString = cascadeString.substring(2);
      }

      cascadeString = ", cascade={" + cascadeString + "}";
    }

    return cascadeString;
  }

  private final String pkg;

  private JPABeanTransform(final xdl_database database) throws Exception {
    super(database);
    pkg = NamespaceBinding.getPackageFromNamespace(database.get_targetNamespace$().getText());
  }

  private Map<String,String> parse() {
    final xs_schema schema = new xs_schema();
    schema.set_targetNamespace$(new xs_schema._targetNamespace$(unmerged.get_targetNamespace$().getText()));
    final XDLModel xdlModel = new XDLModel();
    // Phase 1: Determine # of primary keys per table, and instantiate all JPAEntityModel objects
    final Map<String,Set<String>> tableNameToPrimatyColumnNames = new HashMap<String,Set<String>>();
    for ($xdl_tableType<?> table : merged.get_table()) {
      if (table.get_skip$().getText())
        continue;

      final Set<String> primaryColumnNames = new HashSet<String>();
      tableNameToPrimatyColumnNames.put(table.get_name$().getText(), primaryColumnNames);
      if (table.get_constraints() != null && table.get_constraints(0).get_primaryKey() != null)
        for ($xdl_tableType._constraints._primaryKey._column primaryColumn : table.get_constraints(0).get_primaryKey(0).get_column())
          primaryColumnNames.add(primaryColumn.get_name$().getText());

      xdlModel.addEntity(new JPAEntityModel(table.get_name$().getText(), table.get_abstract$().getText(), table.get_extends$() != null ? table.get_extends$().getText() : null, primaryColumnNames));
    }

    // Phase 2: Determine all foreign keys per table, and instantiate all FieldModel objects
    for ($xdl_tableType<?> table : unmerged.get_table()) {
      if (table.get_skip$().getText())
        continue;

      final Set<String> primaryColumnNames = tableNameToPrimatyColumnNames.get(table.get_name$().getText());
      final JPAEntityModel entityModel = xdlModel.getEntity(table.get_name$().getText());
      if (table.get_column() != null) {
        // Add inverse fields from <foreignKey> elements in <column> elements
        for ($xdl_columnType<?> column : table.get_column()) {
          if (column instanceof $xdl_inherited)
            continue;

          final JPAFieldModel.Column jpaFieldColumn = new JPAFieldModel.Column(column, primaryColumnNames.contains(column.get_name$().getText()));
          final JPAFieldModel fieldModel = new JPAFieldModel(entityModel, jpaFieldColumn);
          entityModel.addFieldModel(fieldModel);
          if (column.get_foreignKey() != null) {
            final $xdl_columnType._foreignKey foreignKey = column.get_foreignKey(0);
            final JPAForeignKeyModel foreignKeyModel = new JPAForeignKeyModel(fieldModel, foreignKey.get_id$() != null ? foreignKey.get_id$().getText() : null, foreignKey.get_references$().getText(), foreignKey.get_column$().getText());
            foreignKeyModel.setMultiplicity(foreignKey.get_multiplicity(0));
            if (foreignKeyModel.getInverseField() != null)
              xdlModel.registerInverseField(foreignKey.get_references$().getText(), foreignKeyModel.getInverseField());

            fieldModel.setForeignKeyModel(foreignKeyModel);
          }
        }

        // Add inverse fields from <foreignKey> elements in <constraints> element
        if (table.get_constraints() != null && table.get_constraints(0).get_foreignKey() != null) {
          final $xdl_tableType._constraints._foreignKey foreignKey = table.get_constraints(0).get_foreignKey(0);
          final String referencedTableName = foreignKey.get_references$().getText();
          final List<JPAFieldModel.Column> columns = new ArrayList<JPAFieldModel.Column>();
          final List<String> referencedColumnNames = new ArrayList<String>();
          for ($xdl_tableType._constraints._foreignKey._column column : foreignKey.get_column()) {
            final JPAFieldModel fieldModel = entityModel.removeFieldModel(column.get_name$().getText());
            if (fieldModel.getForeignKeyModel() != null)
              throw new Error("Column has 2 foreignKey definitions: " + column.get_name$().getText());

            columns.add(fieldModel.getColumn(0));
            referencedColumnNames.add(column.get_column$().getText());
          }

          final JPAFieldModel fieldModel = new JPAFieldModel(entityModel, columns);
          entityModel.addFieldModel(fieldModel);

          final JPAForeignKeyModel foreignKeyModel = new JPAForeignKeyModel(fieldModel, foreignKey.get_id$() != null ? foreignKey.get_id$().getText() : null, foreignKey.get_references$().getText(), referencedColumnNames);
          foreignKeyModel.setMultiplicity(foreignKey.get_multiplicity(0));
          if (foreignKeyModel.getInverseField() != null)
            xdlModel.registerInverseField(foreignKey.get_references$().getText(), foreignKeyModel.getInverseField());

          fieldModel.setForeignKeyModel(foreignKeyModel);
        }

        if (table.get_relation() != null) {
          final List<$xdl_relationType<?>> relations = table.get_relation();
          for ($xdl_relationType<?> relation : relations) {
            final JPARelationModel.ForeignKey foreignKey = new JPARelationModel.ForeignKey(entityModel.getForeignKeyModel(relation.get_field(0).get_id$().getText()), relation.get_field(0).get_name$().getText(), relation.get_field(0).get_cascade$().getText());
            final JPARelationModel.ForeignKey inverseForeignKey = new JPARelationModel.ForeignKey(entityModel.getForeignKeyModel(relation.get_inverse(0).get_id$().getText()), relation.get_inverse(0).get_name$().getText(), relation.get_inverse(0).get_cascade$().getText());
            final JPARelationModel relationModel = new JPARelationModel(entityModel.getName(), $xdl_relationType._association$.MANYTOMANY.getText().equals(relation.get_association$().getText()) ? ManyToMany.class : null, FetchType.valueOf(relation.get_fetch$().getText()), foreignKey, inverseForeignKey);
            xdlModel.getEntity(foreignKey.getForeignKeyModel().getReferencedTableName()).addRelation(relationModel);
            xdlModel.getEntity(inverseForeignKey.getForeignKeyModel().getReferencedTableName()).addInverseRelation(relationModel);
          }
        }
      }
    }

    final Map<String,String> files = new HashMap<String,String>();
    for (JPAEntityModel entityModel : xdlModel.getEntities()) {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("package ").append(pkg).append(";\n\n");
      if (entityModel.isAbstract()) {
        buffer.append("@").append(MappedSuperclass.class.getName()).append("\n");
        buffer.append("public abstract");
      }
      else {
        buffer.append("@").append(Entity.class.getName()).append("\n");
        buffer.append("@").append(Table.class.getName()).append("(name=\"").append(entityModel.getName()).append("\")\n");
        //buffer.append("@").append(Cache.class.getName()).append("(expiry=0)\n");
        buffer.append("@").append(Inheritance.class.getName()).append("(strategy=").append(InheritanceType.class.getName()).append(".TABLE_PER_CLASS)\n");
        buffer.append("public");
      }

      final String tableClassName = Strings.toClassCase(entityModel.getName());
      buffer.append(" class ").append(tableClassName);
      if (entityModel.getExtendsName() != null)
        buffer.append(" extends ").append(Strings.toClassCase(entityModel.getExtendsName()));

      if (!entityModel.isAbstract())
        buffer.append(" implements ").append(Serializable.class.getName());

      buffer.append(" {");
      if (!entityModel.isAbstract())
        buffer.append("\n  private static final long serialVersionUID = ").append(Long.parseLong(Random.numeric(18))).append("L;\n");

      // CONSTRUCTORS
      if (entityModel.getFieldModels().size() > 0) {
        buffer.append("\n  public ").append(tableClassName).append("(final ").append(tableClassName).append(" copy) {\n");
        if (entityModel.getExtendsName() != null)
          buffer.append("    super(copy);\n");

        // Fields
        for (JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          final JPAFieldModel.Column column = fieldModel.getColumn(0);
          final String instanceName = fieldModel.getForeignKeyModel() == null ? Strings.toInstanceCase(column.getColumn().get_name$().getText()) : Strings.toInstanceCase(fieldModel.getForeignKeyModel().getField().getName());
          buffer.append("    this.").append(instanceName).append(" = copy.").append(instanceName).append(";\n");
        }

        // Inverse Fields
        /*final List<JPAForeignKeyModel.InverseField> inverseFields = xdlModel.getInverseFields(entityModel.getName());
        if (inverseFields != null) {
          for (JPAForeignKeyModel.InverseField inverseField : inverseFields) {
            final String instanceName = inverseField.getName();
            buffer.append("    this.").append(instanceName).append(" = copy.").append(instanceName).append(";\n");
          }
        }*/

        buffer.append("  }\n");
      }

      if (entityModel.getExtendsName() != null) {
        buffer.append("\n  public ").append(tableClassName).append("(final ").append(Strings.toClassCase(entityModel.getExtendsName())).append(" copy) {\n");
        buffer.append("    super(copy);\n");
        buffer.append("  }\n");

        if (entityModel.getFieldModels().size() > 0) {
          final JPAEntityModel superEntityModel = xdlModel.getEntity(entityModel.getExtendsName());
          if (superEntityModel != null && superEntityModel.getExtendsName() != null) {
            buffer.append("\n  public ").append(tableClassName).append("(final ").append(Strings.toClassCase(superEntityModel.getExtendsName())).append(" copy) {\n");
            buffer.append("    super(copy);\n");
            buffer.append("  }\n");
          }
        }
      }

      buffer.append("\n  public ").append(tableClassName).append("() {\n");
      buffer.append("  }\n");

      final StringBuffer columnsBuffer = new StringBuffer();
      if (entityModel.getFieldModels().size() > 0) {
        for (JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          columnsBuffer.append("\n");
          boolean isPrimary = true;
          boolean createImmutableIdField = false;
          for (int i = 0; i < fieldModel.getColumns().size(); i++) {
            final JPAFieldModel.Column column = fieldModel.getColumns().get(i);
            isPrimary = column.isPrimary();
            if (!isPrimary)
              break;

            for (String primaryColumnName : entityModel.getPrimaryColumnNames()) {
              if (fieldModel.getForeignKeyModel() != null) {
                final JPAFieldModel primaryFieldModel = entityModel.getPrimaryFieldModel(primaryColumnName);
                createImmutableIdField = createImmutableIdField || primaryFieldModel.getForeignKeyModel() == null;
              }
            }
          }

          if (isPrimary)
            columnsBuffer.append("  @").append(Id.class.getName()).append("\n");


          final String columnDef;
          final String columnType;
          final $xdl_columnType<?> column = fieldModel.getColumn(0).getColumn();
          if (column instanceof $xdl_enum) {
            final String enumName = Strings.toClassCase(column.get_name$().getText());
            final StringBuffer enumBuffer = new StringBuffer();
            //enumBuffer.append("package ").append(pkg).append(";\n\n");
            enumBuffer.append("  public static enum ").append(enumName).append(" {\n");
            final StringBuffer fieldBuffer = new StringBuffer();
            for (String value : (($xdl_enum)column).get_values$().getText())
              fieldBuffer.append(",\n    ").append(value.toUpperCase());//.append("(\"").append(value).append("\")");

            enumBuffer.append(fieldBuffer.substring(2)).append(";\n");
            /*enumBuffer.append("\n    private final ").append(String.class.getName()).append(" value;\n\n");
             enumBuffer.append("    private ").append(enumName).append("(").append(String.class.getName()).append(" value) {\n");
             enumBuffer.append("      this.value = value;\n    }\n");*/
            //files.put(enumName, enumBuffer.toString());

            // This was an attempt to have upper/lower-case enums convert propely between java and the db
            /*enumBuffer.append("\n    public static ").append(enumName).append(" parseType(final ").append(String.class.getName()).append(" ").append(instanceName).append(") {\n");
             enumBuffer.append("      return ").append(enumName).append(".valueOf(").append(instanceName).append(".toUpperCase());\n    }\n");*/

            columnType = enumName;

            columnsBuffer.append(enumBuffer).append("  }\n\n");
            columnsBuffer.append("  @").append(Enumerated.class.getName()).append("(").append(EnumType.class.getName()).append(".STRING)\n");
            columnDef = (($xdl_enum)column).get_default$() != null ? "\"" + (($xdl_enum)column).get_default$().getText() + "\"" : null;
          }
          else if (column instanceof $xdl_boolean) {
            columnType = Boolean.class.getName();
            columnDef = (($xdl_boolean)column).get_default$() != null ? String.valueOf((($xdl_boolean)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_varchar) {
            columnType = String.class.getName();
            columnDef = (($xdl_varchar)column).get_default$() != null ? "\"" + (($xdl_varchar)column).get_default$().getText() + "\"" : null;
          }
          else if (column instanceof $xdl_smallint) {
            columnType = Short.class.getName();
            columnDef = (($xdl_smallint)column).get_default$() != null ? String.valueOf((($xdl_smallint)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_int) {
            columnType = Integer.class.getName();
            columnDef = (($xdl_int)column).get_default$() != null ? String.valueOf((($xdl_int)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_bigint) {
            columnType = Long.class.getName();
            columnDef = (($xdl_bigint)column).get_default$() != null ? String.valueOf((($xdl_bigint)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_date) {
            columnType = Date.class.getName();
            columnDef = (($xdl_date)column).get_default$() != null ? String.valueOf((($xdl_date)column).get_default$().getText()) : null;
            columnsBuffer.append("  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".DATE)\n");
          }
          else if (column instanceof $xdl_dateTime) {
            columnType = Date.class.getName();
            columnDef = (($xdl_dateTime)column).get_default$() != null ? String.valueOf((($xdl_dateTime)column).get_default$().getText()) : null;
            columnsBuffer.append("  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".TIMESTAMP)\n");
          }
          else {
            columnType = null;
            columnDef = null;
          }

          final String def;
          final String type;
          final Class association;
          final FetchType fetch;
          final String fieldName;
          final List<String> fieldCascade;
          if (fieldModel.getForeignKeyModel() != null) {
            type = Strings.toClassCase(fieldModel.getForeignKeyModel().getReferencedTableName());
            def = null;

            association = fieldModel.getForeignKeyModel().getAssociation();
            fetch = fieldModel.getForeignKeyModel().getFetchType();
            fieldName = fieldModel.getForeignKeyModel().getField().getName();
            fieldCascade = fieldModel.getForeignKeyModel().getField().getCascade();
          }
          else {
            type = columnType;
            def = columnDef;
            association = null;
            fetch = null;
            fieldName = column.get_name$().getText();
          }

          if (createImmutableIdField) {
            final String instanceName = Strings.toInstanceCase(column.get_name$().getText());
            final String nullable = String.valueOf(fieldModel.getColumn(0).getColumn().get_null$().getText());
            columnsBuffer.append("  @").append(Column.class.getName()).append("(name=\"").append(column.get_name$().getText()).append("\", nullable=").append(nullable).append(", insertable=false, updatable=false)\n");
            columnsBuffer.append("  private ").append(columnType).append(" ").append(instanceName).append(";\n\n");
          }

          final String instanceName = Strings.toInstanceCase(fieldName);
          if (fieldModel.getForeignKeyModel() != null) {
            final JPAForeignKeyModel foreignKeyModel = fieldModel.getForeignKeyModel();
            final String cascadeString = getCascadeString(foreignKeyModel.getField().getCascade());
            columnsBuffer.append("  @").append(association.getName()).append("(targetEntity=").append(type).append(".class, fetch=").append(FetchType.class.getName()).append(".").append(fetch).append(cascadeString).append(")\n");

            final String referencedTable = foreignKeyModel.getReferencedTableName();
            final JPAEntityModel referencedEntityModel = xdlModel.getEntity(referencedTable);
            final int numPrimaryKeys = referencedEntityModel != null ? referencedEntityModel.getPrimaryColumnNames().size() : 1; // FIXME: This is here due to "skip='true'" rule
            if (foreignKeyModel.getReferencedColumnNames().size() > numPrimaryKeys)
              throw new Error("Should never have more foreign keys than primary keys on referenced table");

            // FIXME: This is a hack to get EclipseLink 2.3 to work properly for JoinColumn over a single column of a composite key of an entity
            if (numPrimaryKeys > 1) {
              columnsBuffer.append("  @").append(JoinColumns.class.getName()).append("({");
              String joinColumns = "";
              for (int i = 0; i < numPrimaryKeys; i++) {
                final int columnIndex = Math.min(i, foreignKeyModel.getReferencedColumnNames().size() - 1);
                final JPAFieldModel.Column fieldModelColumn = fieldModel.getColumns().get(columnIndex);
                final String columnName = fieldModelColumn.getColumn().get_name$().getText();
                final String referencedColumn = foreignKeyModel.getReferencedColumnNames().get(columnIndex);
                final String nullable = String.valueOf(fieldModelColumn.getColumn().get_null$().getText());
                joinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable + ")";
              }

              columnsBuffer.append(joinColumns.substring(1)).append("\n    })\n");
            }
            else if (numPrimaryKeys == 1) {
              final JPAFieldModel.Column fieldModelColumn = fieldModel.getColumn(0);
              final String columnName = fieldModelColumn.getColumn().get_name$().getText();
              final String referencedColumn = foreignKeyModel.getReferencedColumnName(0);
              final String nullable = String.valueOf(fieldModelColumn.getColumn().get_null$().getText());
              columnsBuffer.append("  @").append(JoinColumn.class.getName()).append("(name=\"").append(columnName).append("\", referencedColumnName=\"").append(referencedColumn).append("\", nullable=").append(nullable).append(")\n");
            }
            else {
              throw new Error("this should not happen");
            }
          }
          else {
            final JPAFieldModel.Column fieldModelColumn = fieldModel.getColumn(0);
            final String nullable = String.valueOf(fieldModelColumn.getColumn().get_null$().getText());
            columnsBuffer.append("  @").append(Column.class.getName()).append("(name=\"").append(fieldName).append("\", nullable=").append(nullable).append(")\n");
          }

          if (fieldModel.getColumns().size() == 1 && !fieldModel.getColumn(0).getColumn().get_null$().getText())
            columnsBuffer.append("  @").append(NotNull.class.getName()).append("\n");

          columnsBuffer.append("  private ").append(type).append(" ").append(instanceName);
          if (def != null)
            columnsBuffer.append(" = ").append(def);
          columnsBuffer.append(";\n\n");

          columnsBuffer.append("  public void set").append(Strings.toClassCase(fieldName)).append("(final ").append(type).append(" ").append(instanceName).append(") {\n");
          columnsBuffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
          columnsBuffer.append("  public ").append(type).append(" get").append(Strings.toClassCase(fieldName)).append("() {\n");
          columnsBuffer.append("    return ").append(instanceName).append(";\n  }\n");
        }
      }

      // Inverse Fields
      final List<JPAForeignKeyModel.InverseField> inverseFields = xdlModel.getInverseFields(entityModel.getName());
      if (inverseFields != null) {
        for (JPAForeignKeyModel.InverseField inverseField : inverseFields) {
          final String tableName = inverseField.getReferencedTableName();
          final String fieldName = Strings.toClassCase(inverseField.getName());
          final String instanceName = inverseField.getName();
          final String cascadeString = getCascadeString(inverseField.getCascade());
          final Class association = inverseField.getInverseAssociation();

          final String type = association == OneToMany.class ? List.class.getName() + "<" + Strings.toClassCase(tableName) + ">" : Strings.toClassCase(tableName);
          columnsBuffer.append("\n  @").append(association.getName()).append("(targetEntity=").append(Strings.toClassCase(tableName)).append(".class, mappedBy=\"").append(inverseField.getMappedBy()).append("\"").append(cascadeString).append(")\n");
          columnsBuffer.append("  private ").append(type).append(" ").append(instanceName).append(";\n\n");
          columnsBuffer.append("  public void set").append(fieldName).append("(final ").append(type).append(" ").append(instanceName).append(") {\n");
          columnsBuffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
          columnsBuffer.append("  public ").append(type).append(" get").append(fieldName).append("() {\n");
          columnsBuffer.append("    return ").append(instanceName).append(";\n  }\n");
        }
      }

      // Relations
      final List<JPARelationModel> relations = entityModel.getRelations();
      if (relations.size() > 0) {
        for (JPARelationModel relation : relations) {
          final String cascadeString = getCascadeString(relation.getForeignKey().getCascade());
          final String fieldName = Strings.toClassCase(relation.getInverseForeignKey().getForeignKeyModel().getReferencedTableName());
          columnsBuffer.append("\n  @").append(relation.getAssociation().getName()).append("(targetEntity=").append(fieldName).append(".class, fetch=").append(FetchType.class.getName()).append(".").append(relation.getFetchType()).append(cascadeString).append(")\n");
          final JPAEntityModel referencedEntityModel = xdlModel.getEntity(relation.getForeignKey().getForeignKeyModel().getReferencedTableName());
          final int numPrimaryKeys = referencedEntityModel != null ? referencedEntityModel.getPrimaryColumnNames().size() : 1; // FIXME: This is here due to "skip='true'" rule

          columnsBuffer.append("  @").append(JoinTable.class.getName()).append("(");
          columnsBuffer.append("name=\"").append(relation.getJoinTableName()).append("\",\n    joinColumns={");
          String joinColumns = "";
          for (int i = 0; i < numPrimaryKeys; i++) {
            final int columnIndex = Math.min(i, relation.getForeignKey().getForeignKeyModel().getReferencedColumnNames().size() - 1);
            final JPAFieldModel.Column fieldModelColumn = relation.getForeignKey().getForeignKeyModel().getFieldModel().getColumns().get(columnIndex);
            final String columnName = fieldModelColumn.getColumn().get_name$().getText();
            final String referencedColumn = relation.getForeignKey().getForeignKeyModel().getReferencedColumnNames().get(columnIndex);
            final String nullable = String.valueOf(fieldModelColumn.getColumn().get_null$().getText());
            joinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable + ")";
          }
          columnsBuffer.append(joinColumns.substring(1)).append("\n    },\n");

          columnsBuffer.append("    inverseJoinColumns={");
          String inverseJoinColumns = "";
          for (int i = 0; i < numPrimaryKeys; i++) {
            final int columnIndex = Math.min(i, relation.getInverseForeignKey().getForeignKeyModel().getReferencedColumnNames().size() - 1);
            final JPAFieldModel.Column fieldModelColumn = relation.getInverseForeignKey().getForeignKeyModel().getFieldModel().getColumns().get(columnIndex);
            final String columnName = fieldModelColumn.getColumn().get_name$().getText();
            final String referencedColumn = relation.getInverseForeignKey().getForeignKeyModel().getReferencedColumnNames().get(columnIndex);
            final String nullable = String.valueOf(fieldModelColumn.getColumn().get_null$().getText());
            inverseJoinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable + ")";
          }
          columnsBuffer.append(inverseJoinColumns.substring(1)).append("\n    })\n");

          final String instanceName = Strings.toInstanceCase(relation.getForeignKey().getFieldName());
          final String type = relation.getAssociation() == ManyToMany.class ? List.class.getName() + "<" + Strings.toClassCase(fieldName) + ">" : Strings.toClassCase(fieldName);
          columnsBuffer.append("  private ").append(type).append(" ").append(instanceName).append(";\n");

          columnsBuffer.append("\n  public void set").append(Strings.toClassCase(relation.getForeignKey().getFieldName())).append("(final ").append(type).append(" ").append(instanceName).append(") {\n");
          columnsBuffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
          columnsBuffer.append("  public ").append(type).append(" get").append(Strings.toClassCase(relation.getForeignKey().getFieldName())).append("() {\n");
          columnsBuffer.append("    return ").append(instanceName).append(";\n  }\n");
        }
      }

      // Inverse Relations
      final List<JPARelationModel> inverseRelations = entityModel.getInverseRelations();
      if (inverseRelations.size() > 0) {
        for (JPARelationModel inverseRelation : inverseRelations) {
          final String cascadeString = getCascadeString(inverseRelation.getInverseForeignKey().getCascade());
          final String fieldName = Strings.toClassCase(inverseRelation.getForeignKey().getForeignKeyModel().getReferencedTableName());
          columnsBuffer.append("\n  @").append(inverseRelation.getAssociation().getName()).append("(targetEntity=").append(fieldName).append(".class, mappedBy=\"").append(Strings.toInstanceCase(inverseRelation.getForeignKey().getFieldName())).append("\"").append(cascadeString).append(")\n");
          final String instanceName = Strings.toInstanceCase(inverseRelation.getInverseForeignKey().getFieldName());
          final String type = inverseRelation.getAssociation() == ManyToMany.class ? List.class.getName() + "<" + Strings.toClassCase(fieldName) + ">" : Strings.toClassCase(fieldName);
          columnsBuffer.append("  private ").append(type).append(" ").append(instanceName).append(";\n");

          columnsBuffer.append("\n  public void set").append(Strings.toClassCase(inverseRelation.getInverseForeignKey().getFieldName())).append("(final ").append(type).append(" ").append(instanceName).append(") {\n");
          columnsBuffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
          columnsBuffer.append("  public ").append(type).append(" get").append(Strings.toClassCase(inverseRelation.getInverseForeignKey().getFieldName())).append("() {\n");
          columnsBuffer.append("    return ").append(instanceName).append(";\n  }\n");
        }
      }

      // CLONE METHOD
      columnsBuffer.append("\n  public ").append(tableClassName).append(" clone() {\n");
      if (entityModel.isAbstract())
        columnsBuffer.append("    throw new ").append(RuntimeException.class.getName()).append("(\"Attempt to instantiate an abstract class.\");\n");
      else
        columnsBuffer.append("    return new ").append(tableClassName).append("(this);\n");
      columnsBuffer.append("  }\n");

      // EQUALS METHOD
      columnsBuffer.append("\n  public boolean equals(final ").append(Object.class.getName()).append(" obj) {\n");
      if (!entityModel.isAbstract()) {
        columnsBuffer.append("    if (obj == this)\n");
        columnsBuffer.append("      return true;\n\n");
      }

      if (entityModel.getExtendsName() != null) {
        columnsBuffer.append("    if (!super.equals(obj))\n");
        columnsBuffer.append("      return false;\n\n");
      }
      columnsBuffer.append("    if (!(obj instanceof ").append(tableClassName).append("))\n");
      columnsBuffer.append("      return false;\n\n");

      if (entityModel.getFieldModels().size() > 0) {
        columnsBuffer.append("    final ").append(tableClassName).append(" that = (").append(tableClassName).append(")obj;\n");
        // Fields
        for (JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          final JPAFieldModel.Column column = fieldModel.getColumn(0);
            final String instanceName = fieldModel.getForeignKeyModel() == null ? Strings.toInstanceCase(column.getColumn().get_name$().getText()) : Strings.toInstanceCase(Strings.toInstanceCase(fieldModel.getForeignKeyModel().getField().getName()));
            columnsBuffer.append("    if (").append(instanceName).append(" != null ? !").append(instanceName).append(".equals(that.").append(instanceName).append(") : that.").append(instanceName).append(" != null)\n");
            columnsBuffer.append("      return false;\n\n");
        }

        // Inverse Fields
        /*if (inverseFields != null) {
          for (JPAForeignKeyModel.InverseField inverseField : inverseFields) {
            final String instanceName = inverseField.getName();
            columnsBuffer.append("    if (").append(instanceName).append(" != null ? !").append(instanceName).append(".equals(that.").append(instanceName).append(") : that.").append(instanceName).append(" != null)\n");
            columnsBuffer.append("      return false;\n\n");
          }
        }*/
      }

      columnsBuffer.append("    return true;\n");
      columnsBuffer.append("  }\n");

      // HASHCODE METHOD
      columnsBuffer.append("\n  public int hashCode() {\n");
      if (entityModel.getExtendsName() != null)
        columnsBuffer.append("    int hashCode = super.hashCode();\n");
      else
        columnsBuffer.append("    int hashCode = 7;\n");

      // Fields
      if (entityModel.getFieldModels().size() > 0) {
        for (JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          final JPAFieldModel.Column column = fieldModel.getColumn(0);
          final String instanceName = fieldModel.getForeignKeyModel() == null ? Strings.toInstanceCase(column.getColumn().get_name$().getText()) : Strings.toInstanceCase(Strings.toInstanceCase(fieldModel.getForeignKeyModel().getField().getName()));
          columnsBuffer.append("    hashCode += ").append(instanceName).append(" != null ? ").append(instanceName).append(".hashCode() : -1;\n");
        }
      }

      // Inverse Fields
      /*if (inverseFields != null) {
        for (JPAForeignKeyModel.InverseField inverseField : inverseFields) {
          final String instanceName = inverseField.getName();
          columnsBuffer.append("    hashCode += ").append(instanceName).append(" != null ? ").append(instanceName).append(".hashCode() : -1;\n");
        }
      }*/

      columnsBuffer.append("    return hashCode;\n");
      columnsBuffer.append("  }\n");

      buffer.append(columnsBuffer);

      buffer.append("}");

      files.put(tableClassName, buffer.toString());
    }

    return files;
  }
}
