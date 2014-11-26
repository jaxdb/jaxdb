package org.safris.xdb.xde;

import static org.safris.xdb.xde.cql.BooleanCondition.AND;
import static org.safris.xdb.xde.cql.BooleanCondition.OR;
import static org.safris.xdb.xde.cql.DML.ALL;
import static org.safris.xdb.xde.cql.DML.SELECT;
import static org.safris.xdb.xde.cql.LogicalCondition.EQ;
import static org.safris.xdb.xde.cql.ORDER_BY.DESC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.safris.commons.lang.Strings;
import org.safris.xdb.xde.cql.SELECT;

import xdb.xde.library;
import xdb.xde.library.Book;
import xdb.xde.library.Card;
import xdb.xde.library.Library;

public class CQLTest {
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
  public void test() throws Exception {
    library.createDDL();
    final Library l = new Library();
    final Card c = new Card();
    final Book b = new Book();
    final SELECT select = SELECT(ALL, l, c)
    .FROM(l, c)
    .LEFT_OUTER_JOIN(b).ON(
      OR(
        EQ(b.id, "134"),
        EQ(b.id, "134")
      )
    )
    .WHERE(AND(
      EQ(l.openWeekDays, 8),
      OR(
          EQ(c.email, "x"),
          EQ(c.email, "email")
        )
      )
    )//.GROUP_BY(l.city)
    //.HAVING(EQ(l.name, "bla"))
    .ORDER_BY(c.gender, DESC);

    System.err.println(select);

    final List<Entity[]> entities = select.execute();
  }
}