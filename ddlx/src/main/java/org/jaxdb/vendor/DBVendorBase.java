/* Copyright (c) 2020 JAX-DB
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

package org.jaxdb.vendor;

import java.util.function.Supplier;
import java.util.zip.CRC32;

import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Table;

public abstract class DBVendorBase {
  protected static String hash(final String str) {
    final CRC32 crc = new CRC32();
    final byte[] bytes = str.getBytes();
    crc.update(bytes, 0, bytes.length);
    return Long.toString(crc.getValue(), 16);
  }

  protected final static StringBuilder getConstraintName(final $Table table, final int columnIndex) {
    return new StringBuilder(table.getName$().text()).append('_').append(columnIndex);
  }

  private final DBVendor vendor;
  private final Supplier<Dialect> dialectSupplier;
  private Dialect dialect;

  protected DBVendorBase(final DBVendor vendor) {
    this.vendor = vendor;
    this.dialectSupplier = vendor::getDialect;
  }

  protected final Dialect getDialect() {
    return dialect == null ? dialect = dialectSupplier.get() : dialect;
  }

  protected final DBVendor getVendor() {
    return vendor;
  }

  protected final StringBuilder getConstraintName(final String prefix, final StringBuilder constraintName) {
    constraintName.insert(0, prefix + "_");
    final short constraintNameMaxLength = getDialect().constraintNameMaxLength();
    if (constraintName.length() > constraintNameMaxLength) {
      final String hash = hash(constraintName.toString());
      constraintName.delete(constraintNameMaxLength - 8, constraintName.length());
      constraintName.append(hash);
    }

    return constraintName;
  }

  protected final StringBuilder getConstraintName(final String prefix, final $Table table, final $ForeignKey.References$ references, final int[] columnIndexes) {
    final StringBuilder builder = new StringBuilder(table.getName$().text());
    if (references != null)
      builder.append('_').append(references.text());

    for (int i = 0; i < columnIndexes.length; ++i)
      builder.append('_').append(columnIndexes[i]);

    return getConstraintName(prefix, builder);
  }

  protected final String getSequenceName(final $Table table, final $Integer column) {
    return getSequenceName(table.getName$().text(), column.getName$().text());
  }

  protected final String getSequenceName(final String tableName, final String columnName) {
    return getConstraintName("sq", new StringBuilder(tableName + "_" + columnName)).toString();
  }

  protected final StringBuilder getTriggerName(final $Table table, final $Integer column) {
    return getConstraintName("tr", new StringBuilder(table.getName$().text() + "_" + column.getName$().text()));
  }

  protected final StringBuilder getTriggerName(final String tableName, final $Table.Triggers.Trigger trigger, final String action) {
    return getConstraintName("tr", new StringBuilder(tableName).append('_').append(trigger.getTime$().text().toLowerCase()).append('_').append(action.toLowerCase()));
  }

  protected final String getIndexName(final $Table table, final $Index index, final int ... columnIndexes) {
    if (index == null || columnIndexes.length == 0)
      return null;

    final StringBuilder builder = new StringBuilder(index.getType$().text().substring(0, 2).toLowerCase());
    builder.append('_').append(table.getName$().text());
    for (int i = 0; i < columnIndexes.length; ++i)
      builder.append('_').append(columnIndexes[i]);

    return getConstraintName("id", builder).toString();
  }

  /**
   * Quote a named identifier.
   *
   * @param identifier The identifier.
   * @return The quoted identifier.
   */
  protected final String q(final CharSequence identifier) {
    return getDialect().quoteIdentifier(identifier);
  }
}