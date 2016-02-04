/* Copyright (c) 2011 Seva Safris
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

package org.safris.xdb.xdr;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class UpdateCheckFailedException extends Exception {
  private static final long serialVersionUID = -7183235643145536604L;

  public static final class FailureDetail {
    private final Entity entity;
    private final Field[] fields;

    public FailureDetail(final Entity entity, final Field[] fields) {
      this.entity = entity;
      this.fields = fields;
    }

    public Entity getEntity() {
      return entity;
    }

    public Field[] getFields() {
      return fields;
    }
  }

  private static String fieldsToString(final Field[] fields) {
    if (fields == null)
      return null;

    final StringBuffer buffer = new StringBuffer();
    for (final Field field : fields)
      buffer.append("\n").append(String.valueOf(field.getName()));

    return buffer.substring(1);
  }

  private final FailureDetail[] failureDetails;
  private final List<? extends Entity> entities;

  public UpdateCheckFailedException(final Throwable cause, final FailureDetail ... failureDetail) {
    super(cause);
    this.failureDetails = failureDetail;
    if (failureDetail != null && failureDetail.length != 0) {
      final List<Entity> entities = new ArrayList<Entity>(failureDetail.length);
      for (final UpdateCheckFailedException.FailureDetail failure : failureDetail)
        if (failure != null)
          entities.add(failure.getEntity());

      this.entities = entities;
    }
    else {
      this.entities = null;
    }
  }

  public UpdateCheckFailedException(final FailureDetail ... failureDetail) {
    this(null, failureDetail);
  }

  public UpdateCheckFailedException(final Throwable cause, final List<FailureDetail> failureDetail) {
    this(cause, failureDetail.toArray(new UpdateCheckFailedException.FailureDetail[failureDetail.size()]));
  }

  public UpdateCheckFailedException(final List<FailureDetail> failureDetail) {
    this(null, failureDetail.toArray(new UpdateCheckFailedException.FailureDetail[failureDetail.size()]));
  }

  public FailureDetail[] getFailureDetail() {
    return failureDetails;
  }

  public List<? extends Entity> getEntities() {
    return entities;
  }
}