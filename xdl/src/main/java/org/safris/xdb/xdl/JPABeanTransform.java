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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.eclipse.persistence.annotations.Cache;
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

  private final String pkg;

  private JPABeanTransform(final xdl_database database) throws Exception {
    super(database);
    pkg = NamespaceBinding.getPackageFromNamespace(database.get_targetNamespace$().getText());
  }

  private Map<String,String> parse() {
    final xs_schema schema = new xs_schema();
    schema.set_targetNamespace$(new xs_schema._targetNamespace$(unmerged.get_targetNamespace$().getText()));

    final Map<String,String> files = new HashMap<String,String>();
    for ($xdl_tableType<?> table : unmerged.get_table()) {
      if (table.get_skip$().getText())
        continue;

      final Set<String> primaryColumnNames = new HashSet<String>();
      if (table.get_constraints() != null && table.get_constraints().get(0).get_primaryKey() != null)
        for ($xdl_tableType._constraints._primaryKey._column primaryColumn : table.get_constraints().get(0).get_primaryKey().get(0).get_column())
          primaryColumnNames.add(primaryColumn.get_name$().getText());

      final StringBuffer buffer = new StringBuffer();
      buffer.append("package ").append(pkg).append(";\n\n");
      if (table.get_abstract$().getText()) {
        buffer.append("@").append(MappedSuperclass.class.getName()).append("\n");
        buffer.append("public abstract");
      }
      else {
        buffer.append("@").append(Entity.class.getName()).append("\n");
        buffer.append("@").append(Table.class.getName()).append("(name=\"").append(table.get_name$().getText()).append("\")\n");
        buffer.append("@").append(Cache.class.getName()).append("(expiry=0)\n");
        buffer.append("@").append(Inheritance.class.getName()).append("(strategy=").append(InheritanceType.class.getName()).append(".TABLE_PER_CLASS)\n");
        buffer.append("public");
      }

      final String tableClassName = Strings.toClassCase(table.get_name$().getText());
      buffer.append(" class ").append(tableClassName);
      if (table.get_extends$() != null)
        buffer.append(" extends ").append(Strings.toClassCase(table.get_extends$().getText()));

      if (!table.get_abstract$().getText())
        buffer.append(" implements ").append(Serializable.class.getName());

      buffer.append(" {");
      if (!table.get_abstract$().getText())
        buffer.append("\n  private static final long serialVersionUID = ").append(Long.parseLong(Random.numeric(18))).append("L;\n");

      // CONSTRUCTORS
      if (table.get_column() != null) {
        buffer.append("\n  public ").append(tableClassName).append("(final ").append(tableClassName).append(" copy) {\n");
        if (table.get_extends$() != null)
          buffer.append("    super(copy);\n");

        for ($xdl_columnType column : table.get_column()) {
          if (!(column instanceof $xdl_inherited)) {
            final String instanceName = getColumnInstanceName(column);
            buffer.append("    this.").append(instanceName).append(" = copy.").append(instanceName).append(";\n");
          }
        }
        buffer.append("  }\n");
      }

      if (table.get_extends$() != null) {
        buffer.append("\n  public ").append(tableClassName).append("(final ").append(Strings.toClassCase(table.get_extends$().getText())).append(" copy) {\n");
        buffer.append("    super(copy);\n");
        buffer.append("  }\n");

        if (table.get_column() == null) {
          final $xdl_tableType superTable = tableNameToTable.get(table.get_extends$().getText());
          if (superTable != null && superTable.get_extends$() != null) {
            buffer.append("\n  public ").append(tableClassName).append("(final ").append(Strings.toClassCase(superTable.get_extends$().getText())).append(" copy) {\n");
            buffer.append("    super(copy);\n");
            buffer.append("  }\n");
          }
        }
      }

      buffer.append("\n  public ").append(tableClassName).append("() {\n");
      buffer.append("  }\n");

      final StringBuffer columnsBuffer = new StringBuffer();
      if (table.get_column() != null) {
        for ($xdl_columnType column : table.get_column()) {
          if (column instanceof $xdl_inherited)
            continue;

          columnsBuffer.append("\n");
          if (primaryColumnNames.contains(column.get_name$().getText()))
            columnsBuffer.append("  @").append(Id.class.getName()).append("\n");

          final String type;
          final String def;
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

            type = enumName;

            columnsBuffer.append(enumBuffer).append("  }\n\n");
            columnsBuffer.append("  @").append(Enumerated.class.getName()).append("(").append(EnumType.class.getName()).append(".STRING)\n");
            def = (($xdl_enum)column).get_default$() != null ? "\"" + (($xdl_enum)column).get_default$().getText() + "\"" : null;
          }
          else if (column instanceof $xdl_boolean) {
            type = Boolean.class.getName();
            def = (($xdl_boolean)column).get_default$() != null ? String.valueOf((($xdl_boolean)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_varchar) {
            type = String.class.getName();
            def = (($xdl_varchar)column).get_default$() != null ? "\"" + (($xdl_varchar)column).get_default$().getText() + "\"" : null;
          }
          else if (column instanceof $xdl_smallint) {
            type = Short.class.getName();
            def = (($xdl_smallint)column).get_default$() != null ? String.valueOf((($xdl_smallint)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_int) {
            type = Integer.class.getName();
            def = (($xdl_int)column).get_default$() != null ? String.valueOf((($xdl_int)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_bigint) {
            type = Long.class.getName();
            def = (($xdl_bigint)column).get_default$() != null ? String.valueOf((($xdl_bigint)column).get_default$().getText()) : null;
          }
          else if (column instanceof $xdl_date) {
            type = Date.class.getName();
            def = (($xdl_date)column).get_default$() != null ? String.valueOf((($xdl_date)column).get_default$().getText()) : null;
            columnsBuffer.append("  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".DATE)\n");
          }
          else if (column instanceof $xdl_dateTime) {
            type = Date.class.getName();
            def = (($xdl_dateTime)column).get_default$() != null ? String.valueOf((($xdl_dateTime)column).get_default$().getText()) : null;
            columnsBuffer.append("  @").append(Temporal.class.getName()).append("(").append(TemporalType.class.getName()).append(".TIMESTAMP)\n");
          }
          else {
            type = null;
            def = null;
          }

          final String instanceName = getColumnInstanceName(column);

          columnsBuffer.append("  @").append(Column.class.getName()).append("(name=\"").append(column.get_name$().getText()).append("\")\n");
          if (!column.get_null$().getText())
            columnsBuffer.append("  @").append(NotNull.class.getName()).append("\n");

          columnsBuffer.append("  private ").append(type).append(" ").append(instanceName);
          if (def != null)
            columnsBuffer.append(" = ").append(def);
          columnsBuffer.append(";\n\n");

          columnsBuffer.append("  public void set").append(Strings.toClassCase(column.get_name$().getText())).append("(").append(type).append(" ").append(instanceName).append(") {\n");
          columnsBuffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
          columnsBuffer.append("  public ").append(type).append(" get").append(Strings.toClassCase(column.get_name$().getText())).append("() {\n");
          columnsBuffer.append("    return ").append(instanceName).append(";\n  }\n");
        }

      }

      // CLONE METHOD
      columnsBuffer.append("\n  public ").append(tableClassName).append(" clone() {\n");
      if (table.get_abstract$().getText())
        columnsBuffer.append("    throw new ").append(RuntimeException.class.getName()).append("(\"Attempt to instantiate an abstract class.\");\n");
      else
        columnsBuffer.append("    return new ").append(tableClassName).append("(this);\n");
      columnsBuffer.append("  }\n");

      // EQUALS METHOD
      columnsBuffer.append("\n  public boolean equals(final ").append(Object.class.getName()).append(" obj) {\n");
      if (!table.get_abstract$().getText()) {
        columnsBuffer.append("    if (obj == this)\n");
        columnsBuffer.append("      return true;\n\n");
      }

      if (table.get_extends$() != null) {
        columnsBuffer.append("    if (!super.equals(obj))\n");
        columnsBuffer.append("      return false;\n\n");
      }
      columnsBuffer.append("    if (!(this instanceof ").append(tableClassName).append("))\n");
      columnsBuffer.append("      return false;\n\n");

      if (table.get_column() != null) {
        columnsBuffer.append("    final ").append(tableClassName).append(" that = (").append(tableClassName).append(")obj;\n");
        for ($xdl_columnType column : table.get_column()) {
          if (!(column instanceof $xdl_inherited)) {
            final String instanceName = getColumnInstanceName(column);
            columnsBuffer.append("    if (").append(instanceName).append(" != null ? !").append(instanceName).append(".equals(that.").append(instanceName).append(") : that.").append(instanceName).append(" != null)\n");
            columnsBuffer.append("      return false;\n\n");
          }
        }
      }

      columnsBuffer.append("    return true;\n");
      columnsBuffer.append("  }\n");

      // HASHCODE METHOD
      columnsBuffer.append("\n  public int hashCode() {\n");
      if (table.get_extends$() != null)
        columnsBuffer.append("    int hashCode = super.hashCode();\n");
      else
        columnsBuffer.append("    int hashCode = 7;\n");

      if (table.get_column() != null) {
        for ($xdl_columnType column : table.get_column()) {
          if (!(column instanceof $xdl_inherited)) {
            final String instanceName = getColumnInstanceName(column);
            columnsBuffer.append("    hashCode += ").append(instanceName).append(" != null ? ").append(instanceName).append(".hashCode() : -1;\n");
          }
        }
      }

      columnsBuffer.append("    return hashCode;\n");
      columnsBuffer.append("  }\n");

      buffer.append(columnsBuffer);

      buffer.append("}");

      files.put(tableClassName, buffer.toString());
    }

    return files;
  }

  private String getColumnInstanceName(final $xdl_columnType column) {
    return Strings.toInstanceCase(column.get_name$().getText());
  }
}
