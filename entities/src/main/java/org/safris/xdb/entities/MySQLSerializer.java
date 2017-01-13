/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import org.safris.xdb.entities.Select.FROM;
import org.safris.xdb.entities.Select.GROUP_BY;
import org.safris.xdb.entities.Select.SELECT;
import org.safris.xdb.schema.DBVendor;

class MySQLSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected <T extends Subject<?>>void serialize(final Select.HAVING<T> serializable, final Serialization serialization) {
    final SELECT<?> select = (SELECT<?>)Keyword.getParentRoot(serializable.parent());
    if (serializable.parent() instanceof FROM) {
      final GROUP_BY<T> groupBy = new GROUP_BY<T>(null, select.getEntitiesWithOwners());
      serializable.parent().serialize(serialization);
      groupBy.serialize(serialization);
    }
    else if (serializable.parent() instanceof GROUP_BY) {
      final GROUP_BY groupBy = (GROUP_BY)serializable.parent();
      groupBy.subjects.addAll(select.getEntitiesWithOwners());
      serializable.parent().serialize(serialization);
    }
    else {
      throw new UnsupportedOperationException("Unexpected parent to HAVING clause: " + serializable.parent().getClass());
    }

    serialization.append(" HAVING ");
    serializable.condition.serialize(serialization);
  }

  @Override
  protected String tableName(final Entity entity, final Serialization serialization) {
    return entity.getClass().getEnclosingClass().getSimpleName() + "." + entity.name();
  }

  @Override
  protected <T extends Subject<?>>void serialize(final Select.LIMIT<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" FETCH FIRST " + serializable.limit + " ROWS ONLY");
  }
}