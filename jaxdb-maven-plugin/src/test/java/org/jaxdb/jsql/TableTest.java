/* Copyright (c) 2022 JAX-DB
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableTest {
  @Test
  public void testAuto() {
    for (final data.Table<?> table : auto.getTables()) {
      assertEquals(table.getName(), auto.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getTable().getColumns()) {
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testClassicModels() {
    for (final data.Table<?> table : classicmodels.getTables()) {
      assertEquals(table.getName(), classicmodels.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) {
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testTypes() {
    for (final data.Table<?> table : types.getTables()) {
      assertEquals(table.getName(), types.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) {
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testWorld() {
    for (final data.Table<?> table : world.getTables()) {
      assertEquals(table.getName(), world.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) {
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }
}