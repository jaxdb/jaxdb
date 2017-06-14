/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.lib4j.lang.IntArrayList;
import org.libx4j.rdb.jsql.type.DataType;
import org.libx4j.rdb.vendor.DBVendor;

final class Serialization {
  private static final class Batch {
    protected final String sql;
    protected final List<type.DataType<?>> parameters;

    public Batch(final String sql, final List<DataType<?>> parameters) {
      this.sql = sql;
      this.parameters = parameters;
    }
  }

  private List<Batch> batches;

  private final StringBuilder builder = new StringBuilder();
  private final List<type.DataType<?>> parameters = new ArrayList<type.DataType<?>>();
  private final boolean prepared;
  private final boolean batching;

  protected final Command command;
  protected final DBVendor vendor;
  protected final Serializer serializer;

  private boolean skipFirstColumn = false;

  protected Serialization(final Command command, final DBVendor vendor, final boolean prepared, final boolean batching) {
    this.command = command;
    this.vendor = vendor;
    this.prepared = prepared;
    this.batching = batching;
    this.serializer = Serializer.getSerializer(vendor);
  }

  protected boolean skipFirstColumn() {
    return skipFirstColumn;
  }

  protected void skipFirstColumn(final boolean skipFirstColumn) {
    this.skipFirstColumn = skipFirstColumn;
  }

  private final Map<Subject<?>,Alias> aliases = new IdentityHashMap<Subject<?>,Alias>();

  protected Alias registerAlias(final Subject<?> subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(aliases.size()));

    return alias;
  }

  protected Alias getAlias(final Subject<?> subject) {
    return aliases.get(subject);
  }

  protected StringBuilder append(final CharSequence seq) {
    return builder.append(seq);
  }

  protected void addCondition(final type.DataType<?> dataType) throws IOException {
    append(dataType.name);
    if (dataType.get() == null) {
      append(" IS NULL");
    }
    else {
      append(" = ");
      addParameter(dataType);
    }
  }

  protected void addParameter(final type.DataType<?> dataType) throws IOException {
    if (prepared) {
      builder.append(Serializer.getSerializer(vendor).getPreparedStatementMark(dataType));
      parameters.add(dataType);
    }
    else {
      builder.append(dataType.serialize(vendor));
    }
  }

  protected void addBatch() {
    if (batches == null)
      batches = new ArrayList<Batch>();

    batches.add(new Batch(builder.toString(), new ArrayList<type.DataType<?>>(parameters)));
    builder.setLength(0);
    parameters.clear();
  }

  protected ResultSet executeQuery(final Connection connection) throws IOException, SQLException {
    if (prepared) {
      final PreparedStatement statement = connection.prepareStatement(builder.toString());
      for (int i = 0; i < parameters.size(); i++)
        parameters.get(i).get(statement, i + 1);

      return statement.executeQuery();
    }

    final Statement statement = connection.createStatement();
    return statement.executeQuery(builder.toString());
  }

  protected int[] execute(final Connection connection) throws IOException, SQLException {
    if (builder.length() > 0)
      addBatch();

    if (prepared) {
      if (batching) {
        final IntArrayList results = new IntArrayList(batches.size());
        PreparedStatement statement = null;
        String last = null;
        for (int i = 0; i < batches.size(); i++) {
          final Batch batch = batches.get(i);
          if (!batch.sql.equals(last)) {
            if (statement != null)
              results.addAll(statement.executeBatch());

            statement = connection.prepareStatement(batch.sql);
            last = batch.sql;
          }

          for (int j = 0; j < batch.parameters.size(); j++)
            batch.parameters.get(j).get(statement, j + 1);

          statement.addBatch();
        }

        if (statement != null)
          results.addAll(statement.executeBatch());

        return results.toArray();
      }

      final int[] results = new int[batches.size()];
      for (int i = 0; i < batches.size(); i++) {
        final Batch batch = batches.get(i);
        final PreparedStatement statement = connection.prepareStatement(batch.sql);
        for (int j = 0; j < batch.parameters.size(); j++)
          batch.parameters.get(j).get(statement, j + 1);

        results[i] = statement.executeUpdate();
      }

      return results;
    }

    if (batching) {
      final Statement statement = connection.createStatement();
      for (int i = 0; i < batches.size(); i++) {
        final Batch batch = batches.get(i);
        statement.addBatch(batch.sql.toString());
      }

      return statement.executeBatch();
    }

    final int[] results = new int[batches.size()];
    for (int i = 0; i < batches.size(); i++) {
      final Batch batch = batches.get(i);
      final Statement statement = connection.createStatement();
      results[i] = statement.executeUpdate(batch.sql.toString());
    }

    return results;
  }
}