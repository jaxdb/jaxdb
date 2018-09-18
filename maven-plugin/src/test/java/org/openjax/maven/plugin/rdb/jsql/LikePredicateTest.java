/* Copyright (c) 2017 OpenJAX
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

package org.openjax.maven.plugin.rdb.jsql;

import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.fastjax.test.MixedTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class LikePredicateTest {
  @Test
  public void testLike() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        OR(LIKE(p.name, "%Ford%"), LIKE(SELECT(p.name).FROM(p).LIMIT(1), "%Ford%")),
        SELECT(OR(LIKE(p.name, "%Ford%"), LIKE(SELECT(p.name).FROM(p).LIMIT(1), "%Ford%"))).
        FROM(p).
        WHERE(OR(LIKE(p.name, "%Ford%"), LIKE(SELECT(p.name).FROM(p).LIMIT(1), "%Ford%"))).
        LIMIT(1)).
      FROM(p).
      WHERE(OR(LIKE(p.name, "%Ford%"), LIKE(SELECT(p.name).FROM(p).LIMIT(1), "%Ford%"))).
      execute()) {
      for (int i = 0; i < 15; i++) {
        Assert.assertTrue(rows.nextRow());
        Assert.assertTrue(rows.nextEntity().get());
        Assert.assertTrue(rows.nextEntity().get());
      }
    }
  }
}