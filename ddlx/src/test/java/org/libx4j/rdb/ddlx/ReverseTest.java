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

package org.libx4j.rdb.ddlx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.ddlx.runner.Derby;
import org.libx4j.rdb.ddlx.runner.MySQL;
import org.libx4j.rdb.ddlx.runner.Oracle;
import org.libx4j.rdb.ddlx.runner.PostgreSQL;
import org.libx4j.rdb.ddlx.runner.SQLite;
import org.libx4j.rdb.ddlx.runner.VendorRunner;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, SQLite.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class ReverseTest extends DDLxTest {
  private static final Logger logger = LoggerFactory.getLogger(ReverseTest.class);

  private static void assertEqual(final ddlx_schema expected, final ddlx_schema actual) {
//    expected._na
  }

  @Test
  public void testRecreateSchema(final Connection connection) throws GeneratorExecutionException, IOException, MarshalException, ParseException, SQLException, ValidationException {
    final ddlx_schema original = recreateSchema(connection, "reverse");
    final ddlx_schema reverse = Generator.createDDL(connection);
    final Map<String,String> schemaLocations = new HashMap<String,String>();
    schemaLocations.put("http://rdb.libx4j.org/ddlx.xsd", "http://rdb.libx4j.org/ddlx.xsd");
    logger.info(DOMs.domToString(reverse.marshal(), schemaLocations, DOMStyle.INDENT));
  }
}