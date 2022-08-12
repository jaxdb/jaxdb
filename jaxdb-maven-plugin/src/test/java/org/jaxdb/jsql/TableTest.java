/* Copyright (c) 2022 JAX-DB
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

package org.jaxdb.jsql;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableTest {
  @Test
  public void testAuto() {
    for (final data.Table<?> table : auto.getTables()) { // [A]
      assertEquals(table.getName(), auto.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getTable().getColumns()) { // [A]
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testClassicModels() {
    for (final data.Table<?> table : classicmodels.getTables()) { // [A]
      assertEquals(table.getName(), classicmodels.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) { // [A]
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testTypes() {
    for (final data.Table<?> table : types.getTables()) { // [A]
      assertEquals(table.getName(), types.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) { // [A]
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }

  @Test
  public void testWorld() {
    for (final data.Table<?> table : world.getTables()) { // [A]
      assertEquals(table.getName(), world.getTable(table.getName()).getName());
      for (final data.Column<?> column : table.getColumns()) { // [A]
        assertEquals(column.getName(), table.getColumn(column.getName()).getName());
      }
    }
  }
}