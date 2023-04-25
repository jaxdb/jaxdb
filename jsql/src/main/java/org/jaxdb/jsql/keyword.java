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

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public final class keyword {
  abstract static class Provision extends Evaluable {
  }

  abstract static class Keyword extends Provision {
    @Override
    Serializable evaluate(final Set<Evaluable> visited) {
      throw new UnsupportedOperationException();
    }
  }

  public interface Insert {
    interface CONFLICT_ACTION extends CONFLICT_ACTION_EXECUTE, statement.Modification.Executable<CONFLICT_ACTION_EXECUTE> {
    }

    interface CONFLICT_ACTION_EXECUTE extends CONFLICT_ACTION_COMMIT, statement.Modification.Committable<CONFLICT_ACTION_COMMIT> {
    }

    interface CONFLICT_ACTION_COMMIT extends CONFLICT_ACTION_ROLLBACK, statement.Modification.Rollbackable<CONFLICT_ACTION_ROLLBACK> {
    }

    interface CONFLICT_ACTION_ROLLBACK extends statement.Modification.Insert, statement.NotifiableModification.Notifiable<CONFLICT_ACTION_NOTIFY>, statement.NotifiableModification.Notifiable.Static<CONFLICT_ACTION_NOTIFY> {
    }

    interface CONFLICT_ACTION_NOTIFY extends statement.NotifiableModification.Insert {
    }

    interface ON_CONFLICT {
      CONFLICT_ACTION DO_UPDATE();
      CONFLICT_ACTION DO_NOTHING();
    }

    interface INSERT<D extends data.Entity> extends CONFLICT_ACTION {
      ON_CONFLICT ON_CONFLICT();
    }

    interface _INSERT<D extends data.Table> extends INSERT<D> {
      INSERT<D> VALUES(Select.untyped.SELECT<?> select);
    }
  }

  public interface Update {
    interface UPDATE extends UPDATE_EXECUTE, statement.Modification.Executable<UPDATE_EXECUTE> {
    }

    interface UPDATE_EXECUTE extends UPDATE_COMMIT, statement.Modification.Committable<UPDATE_COMMIT> {
    }

    interface UPDATE_COMMIT extends UPDATE_ROLLBACK, statement.Modification.Rollbackable<UPDATE_ROLLBACK> {
    }

    interface UPDATE_ROLLBACK extends statement.Modification.Update, statement.NotifiableModification.Notifiable<UPDATE_NOTIFY>, statement.NotifiableModification.Notifiable.Static<UPDATE_NOTIFY> {
    }

    interface UPDATE_NOTIFY extends statement.NotifiableModification.Update {
    }

    interface _SET extends UPDATE {
      <T extends Serializable>SET SET(data.Column<? extends T> column, type.Column<? extends T> to);
      <T extends Serializable>SET SET(data.Column<T> column, T to);
    }

    interface SET extends _SET {
      UPDATE WHERE(Condition<?> condition);
    }
  }

  public interface Delete {
    interface DELETE extends DELETE_EXECUTE, statement.Modification.Executable<DELETE_EXECUTE> {
    }

    interface DELETE_EXECUTE extends DELETE_COMMIT, statement.Modification.Committable<DELETE_COMMIT> {
    }

    interface DELETE_COMMIT extends DELETE_ROLLBACK, statement.Modification.Rollbackable<DELETE_ROLLBACK> {
    }

    interface DELETE_ROLLBACK extends statement.Modification.Delete, statement.NotifiableModification.Notifiable<DELETE_NOTIFY>, statement.NotifiableModification.Notifiable.Static<DELETE_NOTIFY> {
    }

    interface DELETE_NOTIFY extends statement.NotifiableModification.Delete {
    }

    interface _DELETE extends DELETE {
      DELETE WHERE(Condition<?> condition);
    }
  }

  public interface Select {
    interface untyped {
      interface _SELECT<D extends type.Entity> extends SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends statement.Query<D>, _UNION<D> {
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> {
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends SELECT<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _JOIN<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> {
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        JOIN<D> LEFT_JOIN(data.Table table);
        JOIN<D> RIGHT_JOIN(data.Table table);
        JOIN<D> FULL_JOIN(data.Table table);
        JOIN<D> JOIN(data.Table table);

        ADV_JOIN<D> CROSS_JOIN(SELECT<?> select);
        ADV_JOIN<D> NATURAL_JOIN(SELECT<?> select);
        JOIN<D> LEFT_JOIN(SELECT<?> table);
        JOIN<D> RIGHT_JOIN(SELECT<?> select);
        JOIN<D> FULL_JOIN(SELECT<?> select);
        JOIN<D> JOIN(SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends _ON<D> {
      }

      interface _ON<D extends type.Entity> {
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends SELECT<D>, _WHERE<D>, _GROUP_BY<D>, _JOIN<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> {
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> {
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> {
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends statement.Query<D> {
      }

      interface _ORDER_BY<D extends type.Entity> {
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> {
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends SELECT<D>, _FOR<D> {
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends SELECT<D> {
      }

      interface _FOR<D extends type.Entity> {
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends SELECT<D> {
        NOWAIT<D> NOWAIT();
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends SELECT<D> {
      }
    }

    interface ARRAY {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.ARRAY<Object> {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.ARRAY<Object> {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.ARRAY<Object> {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.ARRAY<Object> {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.ARRAY<Object> {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.ARRAY<Object> {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.ARRAY<Object> {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.ARRAY<Object> {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.ARRAY<Object> {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface BIGINT {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.BIGINT {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.BIGINT {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.BIGINT {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.BIGINT {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.BIGINT {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.BIGINT {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.BIGINT {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.BIGINT {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.BIGINT {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface BINARY {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.BINARY {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.BINARY {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.BINARY {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.BINARY {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.BINARY {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.BINARY {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.BINARY {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.BINARY {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.BINARY {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface BLOB {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.BLOB {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.BLOB {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.BLOB {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.BLOB {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.BLOB {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.BLOB {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.BLOB {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.BLOB {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.BLOB {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface BOOLEAN {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.BOOLEAN {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.BOOLEAN {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.BOOLEAN {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.BOOLEAN {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.BOOLEAN {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.BOOLEAN {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.BOOLEAN {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.BOOLEAN {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.BOOLEAN {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface CHAR {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.CHAR {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.CHAR {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.CHAR {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.CHAR {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.CHAR {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.CHAR {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.CHAR {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.CHAR {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.CHAR {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface CLOB {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.CLOB {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.CLOB {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.CLOB {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.CLOB {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.CLOB {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.CLOB {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.CLOB {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.CLOB {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.CLOB {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface Column {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.Table {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.Table {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.Table {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.Table {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.Table {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.Table {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.Table {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.Table {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.Table {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface DATE {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.DATE {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.DATE {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.DATE {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.DATE {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.DATE {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.DATE {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.DATE {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.DATE {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.DATE {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface DATETIME {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.DATETIME {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.DATETIME {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.DATETIME {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.DATETIME {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.DATETIME {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.DATETIME {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.DATETIME {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.DATETIME {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.DATETIME {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface DECIMAL {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.DECIMAL {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.DECIMAL {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.DECIMAL {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.DECIMAL {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.DECIMAL {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.DECIMAL {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.DECIMAL {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.DECIMAL {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.DECIMAL {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface DOUBLE {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.DOUBLE {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.DOUBLE {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.DOUBLE {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.DOUBLE {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.DOUBLE {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.DOUBLE {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.DOUBLE {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.DOUBLE {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.DOUBLE {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface Entity {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.Table {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.Table {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.Table {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.Table {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.Table {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.Table {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.Table {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.Table {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.Table {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface ENUM<D extends EntityEnum> {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.ENUM {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.ENUM {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.ENUM {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.ENUM {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.ENUM {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.ENUM {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.ENUM {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.ENUM {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.ENUM {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface FLOAT {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.FLOAT {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.FLOAT {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.FLOAT {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.FLOAT {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.FLOAT {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.FLOAT {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.FLOAT {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.FLOAT {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.FLOAT {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface INT {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.INT {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.INT {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.INT {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.INT {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.INT {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.INT {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.INT {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.INT {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.INT {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface LargeObject {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.LargeObject {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.LargeObject {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.LargeObject {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.LargeObject {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.LargeObject {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.LargeObject {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.LargeObject {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.LargeObject {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.LargeObject {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface Numeric {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.Numeric<Number> {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.Numeric<Number> {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.Numeric<Number> {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.Numeric<Number> {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.Numeric<Number> {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.Numeric<Number> {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.Numeric<Number> {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.Numeric<Number> {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.Numeric<Number> {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface SMALLINT {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.SMALLINT {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.SMALLINT {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.SMALLINT {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.SMALLINT {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.SMALLINT {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.SMALLINT {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.SMALLINT {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.SMALLINT {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.SMALLINT {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface Temporal {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.Temporal {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.Temporal {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.Temporal {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.Temporal {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.Temporal {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.Temporal {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.Temporal {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.Temporal {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.Temporal {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface Textual<D> {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.Textual {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.Textual {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.Textual {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.Textual {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.Textual {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.Textual {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.Textual {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.Textual {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.Textual {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface TIME {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.TIME {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.TIME {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.TIME {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.TIME {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.TIME {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.TIME {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.TIME {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.TIME {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.TIME {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }

    interface TINYINT {
      interface _SELECT<D extends type.Entity> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
      }

      interface SELECT<D extends type.Entity> extends untyped.SELECT<D>, _UNION<D> {
        @Override
        D AS(D as);
      }

      interface _FROM<D extends type.Entity> extends untyped._FROM<D>, type.TINYINT {
        @Override
        FROM<D> FROM(data.Table ... tables);
      }

      interface FROM<D extends type.Entity> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _JOIN<D extends type.Entity> extends untyped._JOIN<D>, type.TINYINT {
        @Override
        ADV_JOIN<D> CROSS_JOIN(data.Table table);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(data.Table table);
        @Override
        JOIN<D> LEFT_JOIN(data.Table table);
        @Override
        JOIN<D> RIGHT_JOIN(data.Table table);
        @Override
        JOIN<D> FULL_JOIN(data.Table table);
        @Override
        JOIN<D> JOIN(data.Table table);

        @Override
        ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
        @Override
        ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
        @Override
        JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
        @Override
        JOIN<D> JOIN(untyped.SELECT<?> select);
      }

      interface ADV_JOIN<D extends type.Entity> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
      }

      interface JOIN<D extends type.Entity> extends untyped.JOIN<D>, _ON<D> {
      }

      interface _ON<D extends type.Entity> extends untyped._ON<D>, type.TINYINT {
        @Override
        ON<D> ON(Condition<?> condition);
      }

      interface ON<D extends type.Entity> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _WHERE<D extends type.Entity> extends untyped._WHERE<D>, type.TINYINT {
        @Override
        WHERE<D> WHERE(Condition<?> condition);
      }

      interface WHERE<D extends type.Entity> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _GROUP_BY<D extends type.Entity> extends untyped._GROUP_BY<D>, type.TINYINT {
        @Override
        GROUP_BY<D> GROUP_BY(data.Entity ... entities);
      }

      interface GROUP_BY<D extends type.Entity> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _HAVING<D extends type.Entity> extends untyped._HAVING<D>, type.TINYINT {
        @Override
        HAVING<D> HAVING(Condition<?> condition);
      }

      interface HAVING<D extends type.Entity> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _UNION<D extends type.Entity> extends type.TINYINT {
        UNION<D> UNION(SELECT<D> union);
        UNION<D> UNION_ALL(SELECT<D> union);
      }

      interface UNION<D extends type.Entity> extends untyped.UNION<D>, _UNION<D> {
      }

      interface _ORDER_BY<D extends type.Entity> extends untyped._ORDER_BY<D>, type.TINYINT {
        @Override
        ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
      }

      interface ORDER_BY<D extends type.Entity> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
      }

      interface _LIMIT<D extends type.Entity> extends untyped._LIMIT<D>, type.TINYINT {
        @Override
        LIMIT<D> LIMIT(int rows);
      }

      interface LIMIT<D extends type.Entity> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
        @Override
        OFFSET<D> OFFSET(int rows);
      }

      interface OFFSET<D extends type.Entity> extends untyped.OFFSET<D>, SELECT<D> {
      }

      interface _FOR<D extends type.Entity> extends untyped._FOR<D> {
        @Override
        FOR<D> FOR_SHARE(data.Entity ... subjects);
        @Override
        FOR<D> FOR_UPDATE(data.Entity ... subjects);
      }

      interface FOR<D extends type.Entity> extends untyped.FOR<D>, SELECT<D> {
        @Override
        NOWAIT<D> NOWAIT();
        @Override
        SKIP_LOCKED<D> SKIP_LOCKED();
      }

      interface NOWAIT<D extends type.Entity> extends untyped.NOWAIT<D>, SELECT<D> {
      }

      interface SKIP_LOCKED<D extends type.Entity> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
      }
    }
  }

  interface Case {
    interface CASE<T extends Serializable> {
    }

    interface simple {
      interface CASE<T extends Serializable> extends Case.CASE<T> {
        Case.simple.WHEN<T> WHEN(T condition);
      }

      interface WHEN<T extends Serializable> {
        Case.BOOLEAN.simple.THEN THEN(data.BOOLEAN bool);
        Case.FLOAT.simple.THEN<T> THEN(data.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
        Case.TINYINT.simple.THEN<T> THEN(data.TINYINT numeric);
        Case.SMALLINT.simple.THEN<T> THEN(data.SMALLINT numeric);
        Case.INT.simple.THEN<T> THEN(data.INT numeric);
        Case.BIGINT.simple.THEN<T> THEN(data.BIGINT numeric);

        Case.BOOLEAN.simple.THEN THEN(boolean bool);
        Case.FLOAT.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.TINYINT.simple.THEN<T> THEN(byte numeric);
        Case.SMALLINT.simple.THEN<T> THEN(short numeric);
        Case.INT.simple.THEN<T> THEN(int numeric);
        Case.BIGINT.simple.THEN<T> THEN(long numeric);

        Case.BINARY.simple.THEN<T> THEN(data.BINARY binary);
        Case.DATE.simple.THEN<T> THEN(data.DATE date);
        Case.TIME.simple.THEN<T> THEN(data.TIME time);
        Case.DATETIME.simple.THEN<T> THEN(data.DATETIME dateTime);
        Case.CHAR.simple.THEN<T> THEN(data.CHAR text);
        Case.ENUM.simple.THEN<T> THEN(data.ENUM<?> dateTime);
        Case.BINARY.simple.THEN<T> THEN(byte[] binary);
        Case.DATE.simple.THEN<T> THEN(LocalDate date);
        Case.TIME.simple.THEN<T> THEN(LocalTime time);
        Case.DATETIME.simple.THEN<T> THEN(LocalDateTime dateTime);
        Case.CHAR.simple.THEN<T> THEN(String text);
        Case.ENUM.simple.THEN<T> THEN(EntityEnum dateTime);
      }
    }

    interface search {
      interface WHEN<T extends Serializable> {
        Case.BOOLEAN.search.THEN<T> THEN(data.BOOLEAN bool);
        Case.FLOAT.search.THEN<T> THEN(data.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
        Case.TINYINT.search.THEN<T> THEN(data.TINYINT numeric);
        Case.SMALLINT.search.THEN<T> THEN(data.SMALLINT numeric);
        Case.INT.search.THEN<T> THEN(data.INT numeric);
        Case.BIGINT.search.THEN<T> THEN(data.BIGINT numeric);

        Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
        Case.FLOAT.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.TINYINT.search.THEN<T> THEN(byte numeric);
        Case.SMALLINT.search.THEN<T> THEN(short numeric);
        Case.INT.search.THEN<T> THEN(int numeric);
        Case.BIGINT.search.THEN<T> THEN(long numeric);

        Case.BINARY.search.THEN<T> THEN(data.BINARY binary);
        Case.DATE.search.THEN<T> THEN(data.DATE date);
        Case.TIME.search.THEN<T> THEN(data.TIME time);
        Case.DATETIME.search.THEN<T> THEN(data.DATETIME dateTime);
        Case.CHAR.search.THEN<T> THEN(data.CHAR text);
        Case.ENUM.search.THEN<T> THEN(data.ENUM<?> dateTime);
        Case.BINARY.search.THEN<T> THEN(byte[] binary);
        Case.DATE.search.THEN<T> THEN(LocalDate date);
        Case.TIME.search.THEN<T> THEN(LocalTime time);
        Case.DATETIME.search.THEN<T> THEN(LocalDateTime dateTime);
        Case.CHAR.search.THEN<T> THEN(String text);
        Case.ENUM.search.THEN<T> THEN(EntityEnum text);
      }
    }

    interface BOOLEAN {
      interface simple {
        interface WHEN {
          Case.BOOLEAN.simple.THEN THEN(data.BOOLEAN bool);
          Case.BOOLEAN.simple.THEN THEN(boolean bool);
        }

        interface THEN extends BOOLEAN.THEN {
          Case.BOOLEAN.simple.WHEN WHEN(boolean condition);
        }
      }

      interface search {
        interface CASE<T extends Serializable> {
          Case.BOOLEAN.search.THEN<T> THEN(data.BOOLEAN bool);
          Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
        }

        interface WHEN<T extends Serializable> {
          Case.BOOLEAN.search.THEN<T> THEN(data.BOOLEAN bool);
          Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
        }

        interface THEN<T extends Serializable> extends BOOLEAN.THEN {
          Case.BOOLEAN.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.BOOLEAN.ELSE ELSE(data.BOOLEAN bool);
        Case.BOOLEAN.ELSE ELSE(boolean bool);
      }

      interface ELSE {
        data.BOOLEAN END();
      }
    }

    interface FLOAT {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.FLOAT.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.INT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.FLOAT.simple.THEN<T> THEN(byte numeric);
          Case.DOUBLE.simple.THEN<T> THEN(short numeric);
          Case.DOUBLE.simple.THEN<T> THEN(int numeric);
          Case.DOUBLE.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends FLOAT.THEN {
          Case.FLOAT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.FLOAT.search.THEN<T> THEN(data.TINYINT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.INT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.FLOAT.search.THEN<T> THEN(byte numeric);
          Case.DOUBLE.search.THEN<T> THEN(short numeric);
          Case.DOUBLE.search.THEN<T> THEN(int numeric);
          Case.DOUBLE.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends FLOAT.THEN {
          Case.FLOAT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.FLOAT.ELSE ELSE(data.TINYINT numeric);
        Case.DOUBLE.ELSE ELSE(data.SMALLINT numeric);
        Case.DOUBLE.ELSE ELSE(data.INT numeric);
        Case.DOUBLE.ELSE ELSE(data.BIGINT numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.FLOAT.ELSE ELSE(byte numeric);
        Case.DOUBLE.ELSE ELSE(short numeric);
        Case.DOUBLE.ELSE ELSE(int numeric);
        Case.DOUBLE.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.FLOAT END();
      }
    }

    interface DOUBLE {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.INT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DOUBLE.simple.THEN<T> THEN(byte numeric);
          Case.DOUBLE.simple.THEN<T> THEN(short numeric);
          Case.DOUBLE.simple.THEN<T> THEN(int numeric);
          Case.DOUBLE.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends DOUBLE.THEN {
          Case.DOUBLE.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.TINYINT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.INT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DOUBLE.search.THEN<T> THEN(byte numeric);
          Case.DOUBLE.search.THEN<T> THEN(short numeric);
          Case.DOUBLE.search.THEN<T> THEN(int numeric);
          Case.DOUBLE.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends DOUBLE.THEN {
          Case.DOUBLE.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.DOUBLE.ELSE ELSE(data.TINYINT numeric);
        Case.DOUBLE.ELSE ELSE(data.SMALLINT numeric);
        Case.DOUBLE.ELSE ELSE(data.INT numeric);
        Case.DOUBLE.ELSE ELSE(data.BIGINT numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DOUBLE.ELSE ELSE(byte numeric);
        Case.DOUBLE.ELSE ELSE(short numeric);
        Case.DOUBLE.ELSE ELSE(int numeric);
        Case.DOUBLE.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.DOUBLE END();
      }
    }

    interface DECIMAL {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DECIMAL.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.INT numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.DECIMAL.simple.THEN<T> THEN(float numeric);
          Case.DECIMAL.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.simple.THEN<T> THEN(byte numeric);
          Case.DECIMAL.simple.THEN<T> THEN(short numeric);
          Case.DECIMAL.simple.THEN<T> THEN(int numeric);
          Case.DECIMAL.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends DECIMAL.THEN {
          Case.DECIMAL.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DECIMAL.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.TINYINT numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.INT numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.BIGINT numeric);

          Case.DECIMAL.search.THEN<T> THEN(float numeric);
          Case.DECIMAL.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.search.THEN<T> THEN(byte numeric);
          Case.DECIMAL.search.THEN<T> THEN(short numeric);
          Case.DECIMAL.search.THEN<T> THEN(int numeric);
          Case.DECIMAL.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends DECIMAL.THEN {
          Case.DECIMAL.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DECIMAL.ELSE ELSE(data.FLOAT numeric);
        Case.DECIMAL.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.DECIMAL.ELSE ELSE(data.TINYINT numeric);
        Case.DECIMAL.ELSE ELSE(data.SMALLINT numeric);
        Case.DECIMAL.ELSE ELSE(data.INT numeric);
        Case.DECIMAL.ELSE ELSE(data.BIGINT numeric);

        Case.DECIMAL.ELSE ELSE(float numeric);
        Case.DECIMAL.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.ELSE ELSE(byte numeric);
        Case.DECIMAL.ELSE ELSE(short numeric);
        Case.DECIMAL.ELSE ELSE(int numeric);
        Case.DECIMAL.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.DECIMAL END();
      }
    }

    interface TINYINT {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.TINYINT.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.SMALLINT.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.simple.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.TINYINT.simple.THEN<T> THEN(byte numeric);
          Case.SMALLINT.simple.THEN<T> THEN(short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends TINYINT.THEN {
          Case.TINYINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.TINYINT.search.THEN<T> THEN(data.TINYINT numeric);
          Case.SMALLINT.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.search.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.TINYINT.search.THEN<T> THEN(byte numeric);
          Case.SMALLINT.search.THEN<T> THEN(short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends TINYINT.THEN {
          Case.TINYINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.TINYINT.ELSE ELSE(data.TINYINT numeric);
        Case.SMALLINT.ELSE ELSE(data.SMALLINT numeric);
        Case.INT.ELSE ELSE(data.INT numeric);
        Case.BIGINT.ELSE ELSE(data.BIGINT numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.TINYINT.ELSE ELSE(byte numeric);
        Case.SMALLINT.ELSE ELSE(short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.TINYINT END();
      }
    }

    interface SMALLINT {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.SMALLINT.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.SMALLINT.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.simple.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.SMALLINT.simple.THEN<T> THEN(byte numeric);
          Case.SMALLINT.simple.THEN<T> THEN(short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends SMALLINT.THEN {
          Case.SMALLINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.FLOAT.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.SMALLINT.search.THEN<T> THEN(data.TINYINT numeric);
          Case.SMALLINT.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.search.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.BIGINT numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.SMALLINT.search.THEN<T> THEN(byte numeric);
          Case.SMALLINT.search.THEN<T> THEN(short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends SMALLINT.THEN {
          Case.SMALLINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.SMALLINT.ELSE ELSE(data.TINYINT numeric);
        Case.SMALLINT.ELSE ELSE(data.SMALLINT numeric);
        Case.INT.ELSE ELSE(data.INT numeric);
        Case.BIGINT.ELSE ELSE(data.BIGINT numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.SMALLINT.ELSE ELSE(byte numeric);
        Case.SMALLINT.ELSE ELSE(short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.SMALLINT END();
      }
    }

    interface INT {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.INT.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.INT.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.simple.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.INT.simple.THEN<T> THEN(byte numeric);
          Case.INT.simple.THEN<T> THEN(short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends INT.THEN {
          Case.INT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.INT.search.THEN<T> THEN(data.TINYINT numeric);
          Case.INT.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.INT.search.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.INT.search.THEN<T> THEN(byte numeric);
          Case.INT.search.THEN<T> THEN(short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends INT.THEN {
          Case.INT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.INT.ELSE ELSE(data.TINYINT numeric);
        Case.INT.ELSE ELSE(data.SMALLINT numeric);
        Case.INT.ELSE ELSE(data.INT numeric);
        Case.BIGINT.ELSE ELSE(data.BIGINT numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.INT.ELSE ELSE(byte numeric);
        Case.INT.ELSE ELSE(short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.INT END();
      }
    }

    interface BIGINT {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.simple.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.simple.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.simple.THEN<T> THEN(data.DECIMAL numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.TINYINT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.SMALLINT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.simple.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.BIGINT.simple.THEN<T> THEN(byte numeric);
          Case.BIGINT.simple.THEN<T> THEN(short numeric);
          Case.BIGINT.simple.THEN<T> THEN(int numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends BIGINT.THEN {
          Case.BIGINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DOUBLE.search.THEN<T> THEN(data.FLOAT numeric);
          Case.DOUBLE.search.THEN<T> THEN(data.DOUBLE numeric);
          Case.DECIMAL.search.THEN<T> THEN(data.DECIMAL numeric);
          Case.BIGINT.search.THEN<T> THEN(data.TINYINT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.SMALLINT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.INT numeric);
          Case.BIGINT.search.THEN<T> THEN(data.BIGINT numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.BIGINT.search.THEN<T> THEN(byte numeric);
          Case.BIGINT.search.THEN<T> THEN(short numeric);
          Case.BIGINT.search.THEN<T> THEN(int numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
        }

        interface THEN<T extends Serializable> extends BIGINT.THEN {
          Case.BIGINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(data.FLOAT numeric);
        Case.DOUBLE.ELSE ELSE(data.DOUBLE numeric);
        Case.DECIMAL.ELSE ELSE(data.DECIMAL numeric);
        Case.BIGINT.ELSE ELSE(data.TINYINT numeric);
        Case.BIGINT.ELSE ELSE(data.SMALLINT numeric);
        Case.BIGINT.ELSE ELSE(data.INT numeric);
        Case.BIGINT.ELSE ELSE(data.BIGINT numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.BIGINT.ELSE ELSE(byte numeric);
        Case.BIGINT.ELSE ELSE(short numeric);
        Case.BIGINT.ELSE ELSE(int numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
      }

      interface ELSE {
        data.BIGINT END();
      }
    }

    interface BINARY {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.BINARY.simple.THEN<T> THEN(data.BINARY binary);
          Case.BINARY.simple.THEN<T> THEN(byte[] binary);
        }

        interface THEN<T extends Serializable> extends BINARY.THEN {
          Case.BINARY.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.BINARY.search.THEN<T> THEN(data.BINARY binary);
          Case.BINARY.search.THEN<T> THEN(byte[] binary);
        }

        interface THEN<T extends Serializable> extends BINARY.THEN {
          Case.BINARY.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.BINARY.ELSE ELSE(data.BINARY binary);
        Case.BINARY.ELSE ELSE(byte[] binary);
      }

      interface ELSE {
        data.BINARY END();
      }
    }

    interface DATE {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DATE.simple.THEN<T> THEN(data.DATE date);
          Case.DATE.simple.THEN<T> THEN(LocalDate date);
        }

        interface THEN<T extends Serializable> extends DATE.THEN {
          Case.DATE.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DATE.search.THEN<T> THEN(data.DATE date);
          Case.DATE.search.THEN<T> THEN(LocalDate date);
        }

        interface THEN<T extends Serializable> extends DATE.THEN {
          Case.DATE.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DATE.ELSE ELSE(data.DATE date);
        Case.DATE.ELSE ELSE(LocalDate date);
      }

      interface ELSE {
        data.DATE END();
      }
    }

    interface TIME {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.TIME.simple.THEN<T> THEN(data.TIME time);
          Case.TIME.simple.THEN<T> THEN(LocalTime time);
        }

        interface THEN<T extends Serializable> extends TIME.THEN {
          Case.TIME.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.TIME.search.THEN<T> THEN(data.TIME time);
          Case.TIME.search.THEN<T> THEN(LocalTime time);
        }

        interface THEN<T extends Serializable> extends TIME.THEN {
          Case.TIME.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.TIME.ELSE ELSE(data.TIME time);
        Case.TIME.ELSE ELSE(LocalTime time);
      }

      interface ELSE {
        data.TIME END();
      }
    }

    interface DATETIME {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.DATETIME.simple.THEN<T> THEN(data.DATETIME dateTime);
          Case.DATETIME.simple.THEN<T> THEN(LocalDateTime dateTime);
        }

        interface THEN<T extends Serializable> extends DATETIME.THEN {
          Case.DATETIME.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.DATETIME.search.THEN<T> THEN(data.DATETIME dateTime);
          Case.DATETIME.search.THEN<T> THEN(LocalDateTime dateTime);
        }

        interface THEN<T extends Serializable> extends DATETIME.THEN {
          Case.DATETIME.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DATETIME.ELSE ELSE(data.DATETIME dateTime);
        Case.DATETIME.ELSE ELSE(LocalDateTime dateTime);
      }

      interface ELSE {
        data.DATETIME END();
      }
    }

    interface CHAR {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.CHAR.simple.THEN<T> THEN(data.ENUM<?> text);
          Case.CHAR.simple.THEN<T> THEN(data.CHAR text);
          Case.CHAR.simple.THEN<T> THEN(EntityEnum text);
          Case.CHAR.simple.THEN<T> THEN(String text);
        }

        interface THEN<T extends Serializable> extends CHAR.THEN {
          Case.CHAR.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.CHAR.search.THEN<T> THEN(data.ENUM<?> text);
          Case.CHAR.search.THEN<T> THEN(data.CHAR text);
          Case.CHAR.search.THEN<T> THEN(EntityEnum text);
          Case.CHAR.search.THEN<T> THEN(String text);
        }

        interface THEN<T extends Serializable> extends CHAR.THEN {
          Case.CHAR.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.CHAR.ELSE ELSE(data.ENUM<?> text);
        Case.CHAR.ELSE ELSE(data.CHAR text);
        Case.CHAR.ELSE ELSE(EntityEnum text);
        Case.CHAR.ELSE ELSE(String text);
      }

      interface ELSE {
        data.CHAR END();
      }
    }

    interface ENUM {
      interface simple {
        interface WHEN<T extends Serializable> {
          Case.ENUM.simple.THEN<T> THEN(data.ENUM<?> text);
          Case.CHAR.simple.THEN<T> THEN(data.CHAR text);
          Case.ENUM.simple.THEN<T> THEN(EntityEnum text);
          Case.CHAR.simple.THEN<T> THEN(String text);
        }

        interface THEN<T extends Serializable> extends ENUM.THEN {
          Case.ENUM.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T extends Serializable> {
          Case.ENUM.search.THEN<T> THEN(data.ENUM<?> text);
          Case.CHAR.search.THEN<T> THEN(data.CHAR text);
          Case.ENUM.search.THEN<T> THEN(EntityEnum text);
          Case.CHAR.search.THEN<T> THEN(String text);
        }

        interface THEN<T extends Serializable> extends ENUM.THEN {
          Case.ENUM.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.ENUM.ELSE ELSE(data.ENUM<?> text);
        Case.CHAR.ELSE ELSE(data.CHAR text);
        Case.ENUM.ELSE ELSE(EntityEnum text);
        Case.CHAR.ELSE ELSE(String text);
      }

      interface ELSE {
        data.ENUM<?> END();
      }
    }
  }

  public static final class Cast {
    static final class AS extends Provision {
      final type.Column<?> column;
      final data.Column<?> cast;
      final Integer length;
      final Integer scale;

      AS(final type.Column<?> column, final data.Column<?> castAs, final Integer length) {
        this.column = column;
        this.cast = castAs;
        this.length = length;
        this.scale = null;
      }

      AS(final type.Column<?> column, final data.Column<?> castAs) {
        this.column = column;
        this.cast = castAs;
        this.length = null;
        this.scale = null;
      }

      AS(final type.Column<?> column, final data.Column<?> castAs, final Integer length, final Integer scale) {
        this.column = column;
        this.cast = castAs;
        this.length = length;
        this.scale = scale;
      }

      @Override
      data.Table getTable() {
        return ((Subject)column).getTable();
      }

      @Override
      data.Column<?> getColumn() {
        return ((Subject)column).getColumn();
      }

      @Override
      boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        return compilation.compiler.compileCast(this, compilation);
      }

      @Override
      Serializable evaluate(final Set<Evaluable> visited) {
        return column instanceof Evaluable ? ((Evaluable)column).evaluate(visited) : null;
      }
    }

    public static final class BOOLEAN {
      public final class AS {
        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.CLOB CLOB(final Integer length) {
          final data.CLOB cast = new data.CLOB(length);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.BOOLEAN value;

      public BOOLEAN(final type.BOOLEAN value) {
        this.value = value;
      }
    }

    public static final class FLOAT {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public FLOAT(final type.FLOAT value) {
        this.value = value;
      }
    }

    public static final class DOUBLE {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public DOUBLE(final type.DOUBLE value) {
        this.value = value;
      }
    }

    public static final class DECIMAL {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public DECIMAL(final type.DECIMAL value) {
        this.value = value;
      }
    }

    public static final class TINYINT {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public TINYINT(final type.TINYINT value) {
        this.value = value;
      }
    }

    public static final class SMALLINT {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public SMALLINT(final type.SMALLINT value) {
        this.value = value;
      }
    }

    public static final class INT {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public INT(final type.INT value) {
        this.value = value;
      }
    }

    public static final class BIGINT {
      public final class AS {
        public data.FLOAT FLOAT() {
          final data.FLOAT cast = new data.FLOAT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DOUBLE DOUBLE() {
          final data.DOUBLE cast = new data.DOUBLE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.Numeric<?> value;

      public BIGINT(final type.BIGINT value) {
        this.value = value;
      }
    }

    public static final class CHAR {
      public final class AS {
        public data.DECIMAL DECIMAL() {
          final data.DECIMAL cast = new data.DECIMAL();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
          final data.DECIMAL cast = new data.DECIMAL(precision, scale);
          return cast.wrap(new Cast.AS(value, cast, precision, scale));
        }

        public data.TINYINT TINYINT() {
          final data.TINYINT cast = new data.TINYINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TINYINT TINYINT(final Integer precision) {
          final data.TINYINT cast = new data.TINYINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.SMALLINT SMALLINT() {
          final data.SMALLINT cast = new data.SMALLINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.SMALLINT SMALLINT(final Integer precision) {
          final data.SMALLINT cast = new data.SMALLINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.INT INT() {
          final data.INT cast = new data.INT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.INT INT(final Integer precision) {
          final data.INT cast = new data.INT(precision == null ? null : precision.shortValue());
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.BIGINT BIGINT() {
          final data.BIGINT cast = new data.BIGINT();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.BIGINT BIGINT(final Integer precision) {
          final data.BIGINT cast = new data.BIGINT(precision);
          return cast.wrap(new Cast.AS(value, cast, precision));
        }

        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.CLOB CLOB(final Integer length) {
          final data.CLOB cast = new data.CLOB(length);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.DATE DATE() {
          final data.DATE cast = new data.DATE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TIME TIME(final Integer precision) {
          final data.TIME cast = new data.TIME(precision);
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TIME TIME() {
          final data.TIME cast = new data.TIME();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DATETIME DATETIME(final Integer precision) {
          final data.DATETIME cast = new data.DATETIME(precision);
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DATETIME DATETIME() {
          final data.DATETIME cast = new data.DATETIME();
          return cast.wrap(new Cast.AS(value, cast));
        }
      }

      public final AS AS = new AS();

      private final type.Textual<?> value;

      public CHAR(final type.Textual<?> value) {
        this.value = value;
      }
    }

    public static final class DATE {
      public final class AS {
        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.DATE value;

      public DATE(final type.DATE value) {
        this.value = value;
      }
    }

    public static final class TIME {
      public final class AS {
        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.TIME TIME(final Integer precision) {
          final data.TIME cast = new data.TIME(precision);
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TIME TIME() {
          final data.TIME cast = new data.TIME();
          return cast.wrap(new Cast.AS(value, cast));
        }
      }

      public final AS AS = new AS();

      private final type.TIME value;

      public TIME(final type.TIME value) {
        this.value = value;
      }
    }

    public static final class DATETIME {
      public final class AS {
        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.DATE DATE() {
          final data.DATE cast = new data.DATE();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TIME TIME(final Integer precision) {
          final data.TIME cast = new data.TIME(precision);
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.TIME TIME() {
          final data.TIME cast = new data.TIME();
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DATETIME DATETIME(final Integer precision) {
          final data.DATETIME cast = new data.DATETIME(precision);
          return cast.wrap(new Cast.AS(value, cast));
        }

        public data.DATETIME DATETIME() {
          final data.DATETIME cast = new data.DATETIME();
          return cast.wrap(new Cast.AS(value, cast));
        }
      }

      public final AS AS = new AS();

      private final type.DATETIME value;

      public DATETIME(final type.DATETIME value) {
        this.value = value;
      }
    }

    public static final class CLOB {
      public final class AS {
        public data.CHAR CHAR(final Integer length) {
          final data.CHAR cast = new data.CHAR(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.CLOB CLOB(final Integer length) {
          final data.CLOB cast = new data.CLOB(length);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.CLOB value;

      public CLOB(final type.CLOB value) {
        this.value = value;
      }
    }

    public static final class BLOB {
      public final class AS {
        public data.BLOB BLOB(final Integer length) {
          final data.BLOB cast = new data.BLOB(length);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.BLOB value;

      public BLOB(final type.BLOB value) {
        this.value = value;
      }
    }

    public static final class BINARY {
      public final class AS {
        public data.BLOB BLOB(final Integer length) {
          final data.BLOB cast = new data.BLOB(length);
          return cast.wrap(new Cast.AS(value, cast, length));
        }

        public data.BINARY BINARY(final int length) {
          final data.BINARY cast = new data.BINARY(length, false);
          return cast.wrap(new Cast.AS(value, cast, length));
        }
      }

      public final AS AS = new AS();

      private final type.BINARY value;

      public BINARY(final type.BINARY value) {
        this.value = value;
      }
    }

    private Cast() {
    }
  }

  private keyword() {
  }
}