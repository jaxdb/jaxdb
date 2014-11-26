/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import static org.safris.xdb.xde.cql.DML.SELECT;
import static org.safris.xdb.xde.cql.LogicalCondition.EQ;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.lang.Strings;
import org.safris.xdb.xde.cql.SELECT;

import xdb.xde.library;
import xdb.xde.library.Address.Country;

public class EntitiesTest {
  static {
    EntityDataSources.register(library.class, new EntityDataSource() {
      private Connection connection;

      public Connection getConnection() {
        try {
          return connection == null ? connection = DriverManager.getConnection("jdbc:derby:memory:" + Strings.getRandomAlphaString(6) + ";create=true") : connection;
        }
        catch (final SQLException e) {
          throw new Error(e);
        }
      }
    });
  }

  @Test
  public void testCreateTable() throws SQLException {
    library.createDDL();
    final library.Library original = new library.Library();
    original.id.set(UUID.randomUUID().toString().toUpperCase());
    original.name.set("Main Library");
    original.address1.set("100 Larkin St.");
    original.city.set("San Francisco");
    original.locality.set("California");
    original.postalCode.set("94102");
    original.country.set(Country.US);
    original.openTime.set(new Time(9, 0, 0));
    original.closeTime.set(new Time(20, 0, 0));
    original.openWeekDays.set((short)127);
    original.phone.set(new BigInteger("14155574400"));
    original.email.set("info@sfpl.org");
    original.website.set("sfpl.org");
    original.createdOn.set(new Timestamp(System.currentTimeMillis()));
    original.modifiedOn.set(new Timestamp(System.currentTimeMillis()));
    original.version.set(1L);
    original.insert();

    original.openTime.set(new Time(10, 0, 0));
    original.update();

    final library.Library copy = new library.Library();
    copy.id.set(original.id.get());
    copy.select();
    //System.out.println("  " + ToStrings.toString(mainLibrary));
    //System.out.println(ToStrings.toString(copy));
    Assert.assertEquals(original, copy);

    final library.Library a = new library.Library();
    final SELECT select = SELECT(a).
      FROM(a).
      WHERE(EQ(a.id, original.id));

    final List<Entity[]> results = select.execute();
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(1, results.get(0).length);
    Assert.assertEquals(original, results.get(0)[0]);
  }
}