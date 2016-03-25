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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import javax.persistence.IdClass;
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
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.safris.commons.jci.CharSequenceJavaFileObject;
import org.safris.commons.jci.InMemoryCompiler;
import org.safris.commons.jci.MemoryJavaFileManager;
import org.safris.commons.lang.Resources;
import org.safris.commons.lang.Strings;
import org.safris.commons.util.Random;
import org.safris.commons.xml.NamespaceBinding;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.xdr.CheckOnUpdate;
import org.safris.xdb.xdr.GenerateOnInsert;
import org.safris.xdb.xdr.GenerateOnUpdate;
import org.w3.x2001.xmlschema.xs_schema;

public final class JPABeanTransform extends XDLTransformer {
  public static void main(final String[] args) throws Exception {
    createJPABeans(new File(args[0]), new File(args[1]));
  }

  public static void createJPABeans(final File xdlFile, final File outDir) throws IOException, XMLException {
    final xdl_database database = parseArguments(xdlFile.toURI().toURL(), null);

    final JPABeanTransform creator = new JPABeanTransform(database);
    final Map<String,String> files = creator.parse();

    final Collection<File> classpath = new ArrayList<File>(1);
    final File bindingLocationBase = Resources.getLocationBase(Entity.class);
    if (bindingLocationBase != null)
      classpath.add(bindingLocationBase);

    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final MemoryJavaFileManager fileManager = new MemoryJavaFileManager(compiler.getStandardFileManager(null, null, null));
    final File packageDir = outDir != null ? new File(outDir, creator.pkg.replace('.', '/')) : null;
    final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
    for (final Map.Entry<String,String> entry : files.entrySet()) {
      writeOutput(entry.getValue(), packageDir != null ? new File(packageDir, entry.getKey() + ".java") : null);
      javaFiles.add(new CharSequenceJavaFileObject(creator.pkg + "." + entry.getKey(), entry.getValue()));
    }

    final Collection<String> options = new ArrayList<String>();
    options.add("-classpath");
    options.add(Resources.getLocationBase(Table.class).getAbsolutePath() + File.pathSeparator + Resources.getLocationBase(org.safris.xdb.xdr.Entity.class).getAbsolutePath());
    options.add("-nowarn");

    InMemoryCompiler.compile(compiler, fileManager, options, javaFiles, outDir);
  }

  private static String getCascadeString(final List<String> cascadeList) {
    final Set<String> cascadeTypes = cascadeList != null ? new HashSet<String>(cascadeList) : null;
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
        for (final String cascadeType : cascadeTypes)
          cascadeString += ", " + CascadeType.class.getName() + "." + cascadeType;

        cascadeString = cascadeString.substring(2);
      }

      cascadeString = ", cascade={" + cascadeString + "}";
    }

    return cascadeString;
  }

  private final String pkg;

  private JPABeanTransform(final xdl_database database) {
    super(database);
    pkg = NamespaceBinding.getPackageFromNamespace(getPackageName(database));
  }

  private static String[] parseType(final $xdl_column column, final StringBuffer columnsBuffer) {
    final String columnType;
    final String columnDef;
    if (column instanceof $xdl_enum) {
      final String enumName = Strings.toClassCase(column._name$().text());
      final StringBuffer enumBuffer = new StringBuffer();
      //enumBuffer.append("package ").append(pkg).append(";\n\n");
      enumBuffer.append("  public static enum ").append(enumName).append(" {\n");
      final StringBuffer fieldBuffer = new StringBuffer();
      for (final String value : (($xdl_enum)column)._values$().text())
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

      if (columnsBuffer != null) {
        columnsBuffer.append(enumBuffer).append("  }\n\n");
        columnsBuffer.append("  @").append(Enumerated.class.getName()).append("(").append(EnumType.class.getName()).append(".STRING)\n");
      }

      columnDef = (($xdl_enum)column)._default$().isNull() ? enumName + "." + (($xdl_enum)column)._default$().text().toUpperCase() : null;
    }
    else if (column instanceof $xdl_boolean) {
      columnType = Boolean.class.getName();
      columnDef = !(($xdl_boolean)column)._default$().isNull() ? String.valueOf((($xdl_boolean)column)._default$().text()) : null;
    }
    else if (column instanceof $xdl_char) {
      columnType = String.class.getName();
      columnDef = !(($xdl_char)column)._default$().isNull() ? "\"" + (($xdl_char)column)._default$().text() + "\"" : null;
    }
    else if (column instanceof $xdl_decimal) {
      columnType = Double.class.getName();
      columnDef = !(($xdl_decimal)column)._default$().isNull() ? (($xdl_decimal)column)._default$().text() + "D" : null;
    }
    else if (column instanceof $xdl_integer) {
      columnType = Short.class.getName();
      final $xdl_integer type = ($xdl_integer)column;
      final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
      if (noBytes == 1)
        columnDef = !type._default$().isNull() ? "(short)" + (($xdl_integer)column)._default$().text() : null;
      else if (noBytes > 4)
        columnDef = !type._default$().isNull() ? "new " + BigInteger.class.getName() + "(\"" + type._default$().text() + "\")" : null;
      else
        columnDef = !type._default$().isNull() ? "" + type._default$().text() : null;
    }
    else if (column instanceof $xdl_date) {
      columnType = Date.class.getName();
      columnDef = !(($xdl_date)column)._default$().isNull() ? String.valueOf((($xdl_date)column)._default$().text()) : null;
      if (columnsBuffer != null)
        columnsBuffer.append("\n  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".DATE)");
    }
    else if (column instanceof $xdl_time) {
      columnType = Date.class.getName();
      columnDef = !(($xdl_time)column)._default$().isNull() ? String.valueOf((($xdl_time)column)._default$().text()) : null;
      if (columnsBuffer != null)
        columnsBuffer.append("\n  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".TIMESTAMP)");
    }
    else if (column instanceof $xdl_dateTime) {
      columnType = Date.class.getName();
      columnDef = !(($xdl_dateTime)column)._default$().isNull() ? String.valueOf((($xdl_dateTime)column)._default$().text()) : null;
      if (columnsBuffer != null)
        columnsBuffer.append("\n  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".TIMESTAMP)");
    }
    else if (column instanceof $xdl_blob) {
      columnType = "byte[]";
      columnDef = null;
    }
    else {
      columnType = null;
      columnDef = null;
    }

    return new String[] {columnType, columnDef};
  }

  private Map<String,String> parse() {
    final xs_schema schema = new xs_schema();
    schema._targetNamespace$(new xs_schema._targetNamespace$(getPackageName(unmerged)));
    final XDLModel xdlModel = new XDLModel();
    // Phase 1: Determine # of primary keys per table, and instantiate all JPAEntityModel objects
    final Map<String,Map<String,$xdl_column>> tableNameToPrimatyColumnNames = new HashMap<String,Map<String,$xdl_column>>();
    for (final $xdl_table table : merged._table()) {
      if (table._skip$().text())
        continue;

      final Set<String> primaryColumnNames = new HashSet<String>();
      final Map<String,$xdl_column> primaryColumns = new HashMap<String,$xdl_column>();
      tableNameToPrimatyColumnNames.put(table._name$().text(), primaryColumns);
      if (table._constraints() != null && table._constraints(0)._primaryKey() != null) {
        for (final $xdl_named primaryColumn : table._constraints(0)._primaryKey(0)._column())
          primaryColumnNames.add(primaryColumn._name$().text());

        for (final $xdl_column column : table._column())
          if (primaryColumnNames.contains(column._name$().text()))
            primaryColumns.put(column._name$().text(), column);
      }

      xdlModel.addEntity(new JPAEntityModel(table._name$().text(), table._abstract$().text(), !table._extends$().isNull() ? table._extends$().text() : null, primaryColumnNames));
    }

    // Phase 2: Determine all foreign keys per table, and instantiate all FieldModel objects
    for (final $xdl_table table : unmerged._table()) {
      if (table._skip$().text())
        continue;

      final Map<String,$xdl_column> primaryColumns = tableNameToPrimatyColumnNames.get(table._name$().text());
      final JPAEntityModel entityModel = xdlModel.getEntity(table._name$().text());
      if (table._column() != null) {
        // Add inverse fields from <foreignKey> elements in <column> elements
        for (final $xdl_column column : table._column()) {
          if (column instanceof $xdl_inherited)
            continue;

          // Get generation strategy if it exists
          final String generateOnInsert;
          final String generateOnUpdate;
          if (column instanceof $xdl_char) {
            final $xdl_char varchar = ($xdl_char)column;
            generateOnInsert = !varchar._generateOnInsert$().isNull() ? varchar._generateOnInsert$().text() : null;
            generateOnUpdate = !varchar._generateOnUpdate$().isNull() ? varchar._generateOnUpdate$().text() : null;
          }
          else if (column instanceof $xdl_date) {
            final $xdl_date date = ($xdl_date)column;
            generateOnInsert = !date._generateOnInsert$().isNull() ? date._generateOnInsert$().text() : null;
            generateOnUpdate = !date._generateOnUpdate$().isNull() ? date._generateOnUpdate$().text() : null;
          }
          else if (column instanceof $xdl_time) {
            final $xdl_time time = ($xdl_time)column;
            generateOnInsert = !time._generateOnInsert$().isNull() ? time._generateOnInsert$().text() : null;
            generateOnUpdate = !time._generateOnUpdate$().isNull() ? time._generateOnUpdate$().text() : null;
          }
          else if (column instanceof $xdl_dateTime) {
            final $xdl_dateTime dateTime = ($xdl_dateTime)column;
            generateOnInsert = !dateTime._generateOnInsert$().isNull() ? dateTime._generateOnInsert$().text() : null;
            generateOnUpdate = !dateTime._generateOnUpdate$().isNull() ? dateTime._generateOnUpdate$().text() : null;
          }
          else if (column instanceof $xdl_integer) {
            final $xdl_integer date = ($xdl_integer)column;
            generateOnInsert = !date._generateOnInsert$().isNull() ? date._generateOnInsert$().text() : null;
            generateOnUpdate = !date._generateOnUpdate$().isNull() ? date._generateOnUpdate$().text() : null;
          }
          else {
            generateOnInsert = null;
            generateOnUpdate = null;
          }

          final List<String> checkOnUpdate = !column._checkOnUpdate$().isNull() ? column._checkOnUpdate$().text() : null;

          final JPAFieldModel.Column fieldColumn = new JPAFieldModel.Column(column, primaryColumns.containsKey(column._name$().text()), generateOnInsert != null && !"AUTO_INCREMENT".equals(generateOnInsert) ? GenerateOnInsert.Strategy.valueOf(generateOnInsert) : null, generateOnUpdate != null ? GenerateOnUpdate.Strategy.valueOf(generateOnUpdate) : null, checkOnUpdate);
          final JPAFieldModel fieldModel = new JPAFieldModel(entityModel, fieldColumn);
          if (column._foreignKey() != null) {
            final JPAFieldModel columnModel = fieldModel.clone();
            final List<String> cascade = column._foreignKey(0)._join() != null ? column._foreignKey(0)._join(0)._field(0)._cascade$().text() : null;
            if (cascade == null || (cascade.size() == 1 && $xdl_cascade.REFRESH.text().equals(cascade.get(0))))
              fieldModel.setImmutable(true);
            else
              columnModel.setImmutable(true);

            entityModel.addFieldModel(columnModel);
            fieldModel.setRealFieldModels(Collections.<JPAFieldModel>singletonList(columnModel));

            final $xdl_foreignKey foreignKey = column._foreignKey(0);
            final JPAForeignKeyModel foreignKeyModel = new JPAForeignKeyModel(fieldModel, !foreignKey._id$().isNull() ? foreignKey._id$().text() : null, foreignKey._references$().text(), foreignKey._column$().text());
            foreignKeyModel.setJoin(foreignKey._join(0));
            if (foreignKeyModel.getInverseField() != null)
              xdlModel.registerInverseField(foreignKey._references$().text(), foreignKeyModel.getInverseField());

            fieldModel.setForeignKeyModel(foreignKeyModel);
          }

          entityModel.addFieldModel(fieldModel);
        }

        // Add inverse fields from <foreignKey> elements in <constraints> element
        if (table._constraints() != null && table._constraints(0)._foreignKey() != null) {
          for (final $xdl_table._constraints._foreignKey foreignKey : table._constraints(0)._foreignKey()) {
            //final String referencedTableName = foreignKey._references$().text();
            final List<JPAFieldModel.Column> columns = new ArrayList<JPAFieldModel.Column>();
            final List<String> referencedColumnNames = new ArrayList<String>();
            final List<JPAFieldModel> realFieldModels = new ArrayList<JPAFieldModel>();
            for (final $xdl_table._constraints._foreignKey._column column : foreignKey._column()) {
              final JPAFieldModel fieldModel = entityModel.getFieldModel(column._name$().text());
              realFieldModels.add(fieldModel);
              fieldModel.setImmutable(true);
              if (fieldModel.getForeignKeyModel() != null)
                throw new Error("Column has 2 foreignKey definitions: " + column._name$().text());

              columns.add(fieldModel.getColumn(0));
              referencedColumnNames.add(column._column$().text());
            }

            final JPAFieldModel fieldModel = new JPAFieldModel(entityModel, columns);
            fieldModel.setRealFieldModels(realFieldModels);
            entityModel.addFieldModel(fieldModel);

            final JPAForeignKeyModel foreignKeyModel = new JPAForeignKeyModel(fieldModel, !foreignKey._id$().isNull() ? foreignKey._id$().text() : null, foreignKey._references$().text(), referencedColumnNames);
            foreignKeyModel.setJoin(foreignKey._join(0));
            if (foreignKeyModel.getInverseField() != null)
              xdlModel.registerInverseField(foreignKey._references$().text(), foreignKeyModel.getInverseField());

            fieldModel.setForeignKeyModel(foreignKeyModel);
          }
        }

        if (table._relation() != null) {
          final List<$xdl_relation> relations = table._relation();
          for (final $xdl_relation relation : relations) {
            final JPARelationModel.ForeignKey foreignKey = new JPARelationModel.ForeignKey(entityModel.getForeignKeyModel(relation._field(0)._id$().text()), relation._field(0)._name$().text(), relation._field(0)._cascade$().text());
            final JPARelationModel.ForeignKey inverseForeignKey = new JPARelationModel.ForeignKey(entityModel.getForeignKeyModel(relation._inverse(0)._id$().text()), relation._inverse(0)._name$().text(), relation._inverse(0)._cascade$().text());
            final JPARelationModel relationModel = new JPARelationModel(entityModel.getName(), $xdl_relation._association$.ManyToMany.text().equals(relation._association$().text()) ? ManyToMany.class : null, FetchType.valueOf(relation._field(0)._fetch$().text()), foreignKey, inverseForeignKey);
            final JPARelationModel inverseRelationModel = new JPARelationModel(entityModel.getName(), $xdl_relation._association$.ManyToMany.text().equals(relation._association$().text()) ? ManyToMany.class : null, FetchType.valueOf(relation._inverse(0)._fetch$().text()), foreignKey, inverseForeignKey);
            xdlModel.getEntity(foreignKey.getForeignKeyModel().getReferencedTableName()).addRelation(relationModel);
            xdlModel.getEntity(inverseForeignKey.getForeignKeyModel().getReferencedTableName()).addInverseRelation(inverseRelationModel);
          }
        }
      }
    }

    // Check primary keys
    for (final JPAEntityModel entityModel : xdlModel.getEntities()) {
      for (final JPAFieldModel fieldModel : entityModel.getFieldModels()) {
        // There are 2 modes of primary key associations here:
        // 1) Assign primary key to fields that are real columns in the DB (final currently used)
        // 2) Assign primary key to associated fields, but only if it is not a split composite id
        // NOTE: If (1), then the code below does not matter -- kept here if (2) is chosen
        boolean isPrimary = fieldModel.getForeignKeyModel() == null; // <-- uncomment for (final 1)
        //boolean isPrimary = !fieldModel.isImmutable(); // <-- uncomment for (final 2)
        boolean isSplitId = false;
        if (isPrimary) {
          for (int i = 0; i < fieldModel.getColumns().size(); i++) {
            final JPAFieldModel.Column column = fieldModel.getColumns().get(i);
            isPrimary = column.isPrimary();
            if (!isPrimary)
              break;

            for (final String primaryColumnName : entityModel.getPrimaryColumnNames()) {
              if (fieldModel.getForeignKeyModel() != null) {
                final JPAFieldModel primaryFieldModel = entityModel.getPrimaryFieldModel(primaryColumnName);
                isSplitId = isSplitId || primaryFieldModel.getForeignKeyModel() == null;
              }
            }
          }
        }

        // If there is a split primary key, then set the primary key onto the real columns
        if (isPrimary && isSplitId) {
          fieldModel.setPrimary(false);
          for (final JPAFieldModel realModel : fieldModel.getRealFieldModel())
            realModel.setPrimary(true);
        }
        else {
          fieldModel.setPrimary(isPrimary);
        }
      }
    }

    final Map<String,String> files = new HashMap<String,String>();
    for (final JPAEntityModel entityModel : xdlModel.getEntities()) {
      final Map<String,$xdl_column> primaryColumns = tableNameToPrimatyColumnNames.get(entityModel.getName());
      final String tableClassName = Strings.toClassCase(entityModel.getName());
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
        if (primaryColumns != null && primaryColumns.size() > 0)
          buffer.append("@").append(IdClass.class.getName()).append("(").append(tableClassName).append(".id.class)\n");

        buffer.append("@").append(Inheritance.class.getName()).append("(strategy=").append(InheritanceType.class.getName()).append(".TABLE_PER_CLASS)\n");
        buffer.append("public");
      }

      buffer.append(" final class ").append(tableClassName).append(" extends ");
      if (entityModel.getExtendsName() != null)
        buffer.append(Strings.toClassCase(entityModel.getExtendsName()));
      else
        buffer.append(org.safris.xdb.xdr.Entity.class.getName());

      if (!entityModel.isAbstract())
        buffer.append(" implements ").append(Serializable.class.getName());

      buffer.append(" {");
      if (!entityModel.isAbstract()) {
        if (primaryColumns != null && primaryColumns.size() > 0) {
          buffer.append("\n  public static final class id implements " + Serializable.class.getName() + " {");
          buffer.append("\n    private static final long serialVersionUID = ").append(Long.parseLong(Random.numeric(18))).append("L;\n\n");
          final List<String> primaryFieldNames = new ArrayList<String>();
          for (final $xdl_column column : primaryColumns.values()) {
            final String primaryFieldName = Strings.toCamelCase(column._name$().text());
            primaryFieldNames.add(primaryFieldName);
            buffer.append("  " + CodeGenUtil.createField(parseType(column, null)[0], primaryFieldName, null).replace("\n", "\n  ")).append("\n");
          }

          buffer.append("  " + CodeGenUtil.createEquals(false, null, tableClassName + ".id", primaryFieldNames).replace("\n", "\n  ")).append("\n");
          buffer.append("  " + CodeGenUtil.createHashCode(null, primaryFieldNames).replace("\n", "\n  "));

          buffer.append("}\n");
        }
        buffer.append("\n  private static final long serialVersionUID = ").append(Long.parseLong(Random.numeric(18))).append("L;\n");
        buffer.append("\n  static {\n");
        buffer.append("    init(").append(tableClassName).append(".class);\n");
        buffer.append("  }\n");
      }

      // CONSTRUCTORS
      if (entityModel.getFieldModels().size() > 0) {
        buffer.append("\n  public ").append(tableClassName).append("(final ").append(tableClassName).append(" copy) {\n");
        if (entityModel.getExtendsName() != null)
          buffer.append("    super(copy);\n");

        // Fields
        for (final JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          if (fieldModel.getForeignKeyModel() != null && fieldModel.getForeignKeyModel().getField() == null)
            continue;

          final JPAFieldModel.Column column = fieldModel.getColumn(0);
          final String instanceName = fieldModel.getForeignKeyModel() == null ? Strings.toInstanceCase(column.getColumn()._name$().text()) : Strings.toInstanceCase(fieldModel.getForeignKeyModel().getField().getName());
          buffer.append("    this.").append(instanceName).append(" = ");
          if (column.getColumn() instanceof $xdl_date || column.getColumn() instanceof $xdl_time || column.getColumn() instanceof $xdl_dateTime)
            buffer.append("copy.").append(instanceName).append(" != null ? new ").append(Date.class.getName()).append("(copy.").append(instanceName).append(".getTime()) : null;\n");
          else
            buffer.append("copy.").append(instanceName).append(";\n");
        }

        // Inverse Fields
        /*final List<JPAForeignKeyModel.InverseField> inverseFields = xdlModel.getInverseFields(entityModel.getName());
         if (inverseFields != null) {
         for (final JPAForeignKeyModel.InverseField inverseField : inverseFields) {
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
        for (final JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          final $xdl_column column = fieldModel.getColumn(0).getColumn();
          final String[] parsedType = parseType(column, columnsBuffer);
          final String columnType = parsedType[0];
          final String columnDef = parsedType[1];

          final String def;
          final String type;
          final Class<?> association;
          final FetchType fetch;
          final String fieldName;
          if (fieldModel.getForeignKeyModel() != null) {
            if (fieldModel.getForeignKeyModel().getField() == null)
              continue;

            type = Strings.toClassCase(fieldModel.getForeignKeyModel().getReferencedTableName());
            def = null;

            association = fieldModel.getForeignKeyModel().getAssociation();
            fetch = fieldModel.getForeignKeyModel().getFetchType();
            fieldName = fieldModel.getForeignKeyModel().getField().getName();
          }
          else {
            type = columnType;
            def = columnDef;
            association = null;
            fetch = null;
            fieldName = column._name$().text();
          }

          columnsBuffer.append("\n");
          //final boolean isPrimary = fieldModel.getColumns().size() == 1 && fieldModel.getColumn(0).isPrimary() && fieldModel.getForeignKeyModel() == null;
          //final boolean createImmutableIdField = false;
          /*if (fieldModel.getColumns().size() == 1) {
           isPrimary = fieldModel.getColumn(0).isPrimary();
           createImmutableIdField = isPrimary && fieldModel.getColumn(0).getColumn()._foreignKey() != null;
           }
           else {
           isPrimary = createImmutableIdField = false;
           }*/

          if (fieldModel.isPrimary())
            columnsBuffer.append("  @").append(Id.class.getName()).append("\n");

          if (fieldModel.getColumn(0).getGenerateOnInsert() != null)
            columnsBuffer.append("  @").append(GenerateOnInsert.class.getName()).append("(strategy=").append(GenerateOnInsert.class.getName()).append(".Strategy.").append(fieldModel.getColumn(0).getGenerateOnInsert()).append(")\n");

          if (fieldModel.getColumn(0).getGenerateOnUpdate() != null)
            columnsBuffer.append("  @").append(GenerateOnUpdate.class.getName()).append("(strategy=").append(GenerateOnUpdate.class.getName()).append(".Strategy.").append(fieldModel.getColumn(0).getGenerateOnUpdate()).append(")\n");

          if (fieldModel.getColumn(0).getCheckOnUpdate() != null)
            for (final String action : fieldModel.getColumn(0).getCheckOnUpdate())
              columnsBuffer.append("  @").append(CheckOnUpdate.class.getName()).append("(action=").append(CheckOnUpdate.class.getName()).append(".Action.").append(action).append(")\n");

          /*if (createImmutableIdField) {
           final String nullable = String.valueOf(fieldModel.getColumn(0).getColumn()._null$().text());
           columnsBuffer.append("  @").append(Column.class.getName()).append("(name=\"").append(column._name$().text()).append("\", nullable=").append(nullable).append(", insertable=false, updatable=false)\n");
           columnsBuffer.append(CodeGenUtil.createField(columnType, Strings.toClassCase(column._name$().text()), null));
           }*/

          if (fieldModel.getForeignKeyModel() != null && fieldModel.getForeignKeyModel().getField() != null) {
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
                final String columnName = fieldModelColumn.getColumn()._name$().text();
                final String referencedColumn = foreignKeyModel.getReferencedColumnNames().get(columnIndex);
                final String nullable = String.valueOf(fieldModelColumn.getColumn()._null$().text());
                joinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable;
                if (fieldModel.isImmutable())
                  joinColumns += ", insertable=false, updatable=false";

                joinColumns += ")";
              }

              columnsBuffer.append(joinColumns.substring(1)).append("\n    })\n");
            }
            else if (numPrimaryKeys == 1) {
              final JPAFieldModel.Column fieldModelColumn = fieldModel.getColumn(0);
              final String columnName = fieldModelColumn.getColumn()._name$().text();
              final String referencedColumn = foreignKeyModel.getReferencedColumnName(0);
              final String nullable = String.valueOf(fieldModelColumn.getColumn()._null$().text());
              columnsBuffer.append("  @").append(JoinColumn.class.getName()).append("(name=\"").append(columnName).append("\", referencedColumnName=\"").append(referencedColumn).append("\", nullable=").append(nullable);
              if (fieldModel.isImmutable())
                columnsBuffer.append(", insertable=false, updatable=false");

              columnsBuffer.append(")\n");
            }
            else {
              throw new Error("this should not happen");
            }
          }
          else {
            final JPAFieldModel.Column fieldModelColumn = fieldModel.getColumn(0);
            final String nullable = String.valueOf(fieldModelColumn.getColumn()._null$().text());
            columnsBuffer.append("  @").append(Column.class.getName()).append("(name=\"").append(fieldName).append("\", nullable=").append(nullable);
            if (fieldModel.isImmutable())
              columnsBuffer.append(", insertable=false, updatable=false");
            columnsBuffer.append(")\n");
          }

          //if (!fieldModel.isImmutable() && fieldModel.getColumns().size() == 1 && !fieldModel.getColumn(0).getColumn()._null$().text())
          //  columnsBuffer.append("  @").append(NotNull.class.getName()).append("\n");

          columnsBuffer.append(CodeGenUtil.createField(type, fieldName, def));
        }
      }

      // Inverse Fields
      final List<JPAForeignKeyModel.InverseField> inverseFields = xdlModel.getInverseFields(entityModel.getName());
      if (inverseFields != null) {
        for (final JPAForeignKeyModel.InverseField inverseField : inverseFields) {
          final String tableName = inverseField.getReferencedTableName();
          final String fieldName = Strings.toClassCase(inverseField.getName());
          final String cascadeString = getCascadeString(inverseField.getCascade());
          final Class<?> association = inverseField.getInverseAssociation();

          final String type = association == OneToMany.class ? Set.class.getName() + "<" + Strings.toClassCase(tableName) + ">" : Strings.toClassCase(tableName);
          columnsBuffer.append("\n  @").append(association.getName()).append("(targetEntity=").append(Strings.toClassCase(tableName)).append(".class, mappedBy=\"").append(Strings.toInstanceCase(inverseField.getMappedBy())).append("\"").append(cascadeString).append(")\n");
          columnsBuffer.append(CodeGenUtil.createField(type, fieldName, null));
        }
      }

      // Relations
      final List<JPARelationModel> relations = entityModel.getRelations();
      if (relations.size() > 0) {
        for (final JPARelationModel relation : relations) {
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
            final String columnName = fieldModelColumn.getColumn()._name$().text();
            final String referencedColumn = relation.getForeignKey().getForeignKeyModel().getReferencedColumnNames().get(columnIndex);
            final String nullable = String.valueOf(fieldModelColumn.getColumn()._null$().text());
            joinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable + ")";
          }
          columnsBuffer.append(joinColumns.substring(1)).append("\n    },\n");

          columnsBuffer.append("    inverseJoinColumns={");
          String inverseJoinColumns = "";
          for (int i = 0; i < relation.getInverseForeignKey().getForeignKeyModel().getReferencedColumnNames().size(); i++) {
            final JPAFieldModel.Column fieldModelColumn = relation.getInverseForeignKey().getForeignKeyModel().getFieldModel().getColumns().get(i);
            final String columnName = fieldModelColumn.getColumn()._name$().text();
            final String referencedColumn = relation.getInverseForeignKey().getForeignKeyModel().getReferencedColumnNames().get(i);
            final String nullable = String.valueOf(fieldModelColumn.getColumn()._null$().text());
            inverseJoinColumns += ",\n      @" + JoinColumn.class.getName() + "(name=\"" + columnName + "\", referencedColumnName=\"" + referencedColumn + "\", nullable=" + nullable + ")";
          }
          columnsBuffer.append(inverseJoinColumns.substring(1)).append("\n    })\n");

          final String type = relation.getAssociation() == ManyToMany.class ? Set.class.getName() + "<" + Strings.toClassCase(fieldName) + ">" : Strings.toClassCase(fieldName);
          columnsBuffer.append(CodeGenUtil.createField(type, Strings.toClassCase(relation.getForeignKey().getFieldName()), null));
        }
      }

      // Inverse Relations
      final List<JPARelationModel> inverseRelations = entityModel.getInverseRelations();
      if (inverseRelations.size() > 0) {
        for (final JPARelationModel inverseRelation : inverseRelations) {
          final String cascadeString = getCascadeString(inverseRelation.getInverseForeignKey().getCascade());
          final String fieldName = Strings.toClassCase(inverseRelation.getForeignKey().getForeignKeyModel().getReferencedTableName());
          columnsBuffer.append("\n  @").append(inverseRelation.getAssociation().getName()).append("(targetEntity=").append(fieldName).append(".class, mappedBy=\"").append(Strings.toInstanceCase(inverseRelation.getForeignKey().getFieldName())).append("\", fetch=").append(FetchType.class.getName()).append(".").append(inverseRelation.getFetchType()).append(cascadeString).append(")\n");
          final String type = inverseRelation.getAssociation() == ManyToMany.class ? Set.class.getName() + "<" + Strings.toClassCase(fieldName) + ">" : Strings.toClassCase(fieldName);
          columnsBuffer.append(CodeGenUtil.createField(type, Strings.toClassCase(inverseRelation.getInverseForeignKey().getFieldName()), null));
        }
      }

      // CLONE METHOD
      columnsBuffer.append("\n  public ").append(tableClassName).append(" clone() {\n");
      if (entityModel.isAbstract())
        columnsBuffer.append("    throw new ").append(RuntimeException.class.getName()).append("(\"Attempt to instantiate an abstract class.\");\n");
      else
        columnsBuffer.append("    return new ").append(tableClassName).append("(this);\n");
      columnsBuffer.append("  }\n\n");

      // FIELDS FOR EQUALS & HASHCODE
      final List<String> fields;
      if (entityModel.getFieldModels().size() > 0) {
        fields = new ArrayList<String>();
        // Fields
        for (final JPAFieldModel fieldModel : entityModel.getFieldModels()) {
          if (fieldModel.getForeignKeyModel() == null || fieldModel.getForeignKeyModel().getField() != null) {
            final JPAFieldModel.Column column = fieldModel.getColumn(0);
            final String instanceName = fieldModel.getForeignKeyModel() == null ? Strings.toInstanceCase(column.getColumn()._name$().text()) : Strings.toInstanceCase(Strings.toInstanceCase(fieldModel.getForeignKeyModel().getField().getName()));
            fields.add(instanceName);
          }
        }

        // Inverse Fields
        /*if (inverseFields != null) {
         for (final JPAForeignKeyModel.InverseField inverseField : inverseFields) {
         final String instanceName = inverseField.getName();
         columnsBuffer.append("    if (").append(instanceName).append(" != null ? !").append(instanceName).append(".equals(that.").append(instanceName).append(") : that.").append(instanceName).append(" != null)\n");
         columnsBuffer.append("      return false;\n\n");
         }
         }*/
      }
      else {
        fields = null;
      }

      // EQUALS METHOD
      columnsBuffer.append(CodeGenUtil.createEquals(entityModel.isAbstract(), entityModel.getExtendsName(), tableClassName, fields)).append("\n");

      // HASHCODE METHOD
      columnsBuffer.append(CodeGenUtil.createHashCode(entityModel.getExtendsName(), fields));

      buffer.append(columnsBuffer);

      buffer.append("}");

      files.put(tableClassName, buffer.toString());
    }

    return files;
  }
}