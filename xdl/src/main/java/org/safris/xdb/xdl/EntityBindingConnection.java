package org.safris.xdb.xdl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.safris.commons.lang.Pair;
import org.safris.xdb.xdl.EntityBridgeUtil.Assignment;
import org.safris.xml.generator.compiler.runtime.Binding;

public class EntityBindingConnection {
  private static final Map<Pair<EntityBridge,String>,EntityBindingConnection> instances = new HashMap<Pair<EntityBridge,String>,EntityBindingConnection>();

  protected static EntityBindingConnection getInstance(final EntityBridge entityBridge, final String url) {
    final Pair<EntityBridge,String> key = new Pair<EntityBridge,String>(entityBridge, url);
    EntityBindingConnection instance = instances.get(key);
    if (instance != null)
      return instance;

    synchronized (entityBridge) {
      instance = instances.get(key);
      if (instance != null)
        return instance;

      instances.put(key, instance = new EntityBindingConnection(entityBridge, url));
      return instance;
    }
  }

  private final EntityBridge entityBridge;
  private final String url;

  private EntityBindingConnection(final EntityBridge entityBridge, final String url) {
    this.entityBridge = entityBridge;
    this.url = url;
  }

  private Connection conneciton = null;

  private Connection getConnection() throws SQLException {
    if (conneciton != null)
      return conneciton;

    synchronized (this) {
      if (conneciton != null)
        return conneciton;

      return conneciton = DriverManager.getConnection(url);
    }
  }

  public List<Binding[]> query(final String sql, final Object ... parameter) throws SQLException {
    final Connection connection = getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);
    for (int i = 0; i < parameter.length; i++) {
      final Object par = parameter[i];
      
      // FIXME: NULL needs to know the kind of data type it is :/
      //if (par == null)
      //  statement.setNull(i + 1, );
      if (par instanceof String)
        statement.setString(i + 1, (String)par);
      else if (par instanceof Integer)
        statement.setInt(i + 1, (Integer)par);
      else if (par instanceof Double)
        statement.setDouble(i + 1, (Double)par);
      else if (par instanceof Float)
        statement.setFloat(i + 1, (Float)par);
      else if (par instanceof Boolean)
        statement.setBoolean(i + 1, (Boolean)par);
      else if (par instanceof Short)
        statement.setShort(i + 1, (Short)par);
      else if (par instanceof Long)
        statement.setLong(i + 1, (Long)par);
      else if (par instanceof BigDecimal)
        statement.setBigDecimal(i + 1, (BigDecimal)par);
      else if (par instanceof Byte)
        statement.setByte(i + 1, (Byte)par);
      else if (par instanceof java.sql.Time)
        statement.setTime(i + 1, (java.sql.Time)par);
//      else if (par instanceof org.safris.commons.xml.binding.Time)
//        statement.setTime(i + 1, new java.sql.Time(((org.safris.commons.xml.binding.Time)par).getTime()));
      else if (par instanceof java.util.Date)
        statement.setDate(i + 1, new java.sql.Date(((java.util.Date)par).getTime()));
//      else if (par instanceof org.safris.commons.xml.binding.Date)
//        statement.setDate(i + 1, new java.sql.Date(((org.safris.commons.xml.binding.Date)par).getTime()));
      else if (par instanceof byte[])
        statement.setBytes(i + 1, (byte[])par);
      else if (par instanceof Blob)
        statement.setBlob(i + 1, (Blob)par);
      else if (par instanceof Clob)
        statement.setClob(i + 1, (Clob)par);
      else
        throw new IllegalArgumentException("Unknown parameter type: " + par.getClass().getName());
    }
    
    final List<Assignment> assignments = entityBridge.parseAssignments(sql);
    
    final Iterator<Assignment> assignmentIterator = assignments.iterator();
    Iterator<Assignment.Entry> entryIterator = null;

    final ResultSet resultSet = statement.executeQuery();
    Assignment assignment = null;
    Assignment.Entry entry = null;
    Binding binding = null;
    final List<Binding[]> rows = new ArrayList<Binding[]>();
    Binding[] row;
    int bindingNumber;
    try {
      while (resultSet.next()) {
        rows.add(row = new Binding[assignments.size()]);
        bindingNumber = 0;
        int i = 1;
        while (true) {
          if((entryIterator == null || !entryIterator.hasNext()))
            if (assignmentIterator.hasNext())
              entryIterator = (assignment = assignmentIterator.next()).entries.values().iterator();
            else
              break;
 
          entry = entryIterator.next();
          if (assignment != null) {
            row[bindingNumber++] = binding = (Binding)assignment.binding.newInstance();
            assignment = null;
          }
          
          final Class<?> parameterType = entry.type;
          final Object data;
          if (parameterType == String.class)
            data = resultSet.getString(i);
          else if (parameterType == Integer.class)
            data = resultSet.getInt(i);
          else if (parameterType == Double.class)
            data = resultSet.getDouble(i);
          else if (parameterType == Float.class)
            data = resultSet.getFloat(i);
          else if (parameterType == Boolean.class)
            data = resultSet.getBoolean(i);
          else if (parameterType == Short.class)
            data = resultSet.getShort(i);
          else if (parameterType == Long.class)
            data = resultSet.getLong(i);
          else if (parameterType == BigDecimal.class)
            data = resultSet.getBigDecimal(i);
          else if (parameterType == Byte.class)
            data = resultSet.getByte(i);
          else if (parameterType.isAssignableFrom(org.safris.commons.xml.binding.Time.class) || parameterType.isAssignableFrom(java.sql.Time.class))
            data = new org.safris.commons.xml.binding.Time(resultSet.getTime(i).getTime());
          else if (parameterType.isAssignableFrom(org.safris.commons.xml.binding.Date.class) || parameterType.isAssignableFrom(java.util.Date.class))
            data = new org.safris.commons.xml.binding.Date(resultSet.getDate(i).getTime());
          else if (parameterType == byte[].class)
            data = resultSet.getBytes(i);
          else if (parameterType == Blob.class)
            data = resultSet.getBlob(i);
          else if (parameterType == Clob.class)
            data = resultSet.getClob(i);
          else
            throw new IllegalArgumentException("Unknown parameter type: " + parameterType.getName());
          
          ++i;
          entry.method.invoke(binding, data);
        }
      }
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
    
    return rows;
  }
}
