package org.safris.xdb.xdl;

import org.junit.Assert;
import org.junit.Test;

public class SQLDataTypesTest {
  @Test
  public void test() {
    final int[] byteCounts = {1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 8, 9};
    for (int i = 0; i < byteCounts.length; i++) {
      System.out.println((i + 1) + " digits needs: " + byteCounts[i] + " bytes");
      Assert.assertEquals(byteCounts[i], SQLDataTypes.getNumericByteCount(i + 1, true, null, null));
    }
  }
}