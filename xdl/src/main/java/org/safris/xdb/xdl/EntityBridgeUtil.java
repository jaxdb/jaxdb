package org.safris.xdb.xdl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.safris.commons.lang.Strings;
import org.safris.commons.xml.NamespaceBinding;

final class EntityBridgeUtil {
  protected static final class Assignment {
    protected static final class Interator implements java.util.Iterator<Assignment.Entry> {
      private final java.util.Iterator<Assignment> assignmentIterator;
      private java.util.Iterator<Assignment.Entry> entryIterator;

      public Interator(final List<Assignment> assignments) {
        this.assignmentIterator = assignments.iterator();
      }

      public boolean hasNext() {
        if (entryIterator != null && entryIterator.hasNext())
          return true;
        
        if (!assignmentIterator.hasNext())
          return false;
        
        entryIterator = assignmentIterator.next().entries.values().iterator();
        return entryIterator.hasNext();
      }

      public Assignment.Entry next() {
        return hasNext() ? entryIterator.next() : null;
      }

      public void remove() {
        if (hasNext())
          entryIterator.remove();
      }
    }
    
    protected static final class Entry {
      public final Method method;
      public final Class<?> type;

      public Entry(final Method method, final Class<?> type) {
        this.method = method;
        this.type = type;
      }
    }
    
    public final Class<?> binding;
    public final LinkedHashMap<String,Entry> entries = new LinkedHashMap<String,Entry>();
    
    public Assignment(final Class<?> binding) {
      this.binding = binding;
    }
  }
  
  private static Class<?> lookupBinding(final xdl_database database, final String tableName) {
    final String packageName = NamespaceBinding.getPackageFromNamespace(database._targetNamespace$().text());
    final String prefix = database._name$().text();
    final String element = Strings.toJavaCase(tableName);
    try {
      return Class.forName(packageName + "." + prefix + "_" + element);
    }
    catch (final ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  protected static Map<Class<?>,Assignment> prototypeAssignments(final xdl_database database) {
    final Map<Class<?>,Assignment> bindingToAssignment = new HashMap<Class<?>,Assignment>();
    for (final $xdl_tableType table : database._table()) {
      if (table._abstract$() != null && table._abstract$().text())
        continue;
      
      final Class<?> binding = lookupBinding(database, table._name$().text());
      final Map<String,Method> methodNameToMethod = new HashMap<String,Method>();
      final Method[] methods = binding.getMethods();
      for (final Method method : methods)
        if (method.getParameterTypes().length == 1)
          methodNameToMethod.put(method.getName(), method);
      
      final Assignment assignment = new Assignment(binding);
      for (final $xdl_columnType column : table._column()) {
        final Method method = methodNameToMethod.get("_" + column._name$().text() + "$");
        final Class<?> type = method.getParameterTypes()[0];
        assignment.entries.put(column._name$().text(), new Assignment.Entry(method, type));
      }
      
      bindingToAssignment.put(binding, assignment);
    }
    
    return bindingToAssignment;
  }
  
  protected static Map<String,Class<?>> parseBindings(final xdl_database database, final String sql) {
    final String from = sql.replaceAll("((^.*\\sFROM\\s+)|(\\s+WHERE\\s.*$))", "");
    final String[] parts = from.split(",");
    final Map<String,Class<?>> map = new HashMap<String,Class<?>>();
    for (final String part : parts) {
      final String[] alias = part.trim().split("\\s+");
      if (alias.length == 2)
        map.put(alias[1], lookupBinding(database, alias[0]));
      else
        map.put(alias[0], lookupBinding(database, alias[0]));
    }
    
    return map;
  }

  protected static String[] parseParts(final String[] parts, final Map<String,Class<?>> aliasToBinding) {
    if (parts.length == 2)
      return parts;
    
    if (aliasToBinding.size() == 1)
      return new String[] {aliasToBinding.keySet().iterator().next(), parts[0]};

    throw new IllegalArgumentException("Ambiguous reference: " + Arrays.toString(parts));
  }

  private EntityBridgeUtil() {
  }
}