/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.schema;

import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.test.LoggableTest;
import org.safris.dbx.ddlx.SQLDataTypes;

public class SQLDataTypesTest extends LoggableTest {
  @Test
  public void testGetNumericByteCount() {
    final int[] byteCounts = {1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 8, 9};
    for (int i = 0; i < byteCounts.length; i++)
      Assert.assertEquals(byteCounts[i], SQLDataTypes.getNumericByteCount(i + 1, true, null, null));
  }
}