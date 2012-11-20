/*  Copyright Safris Software 2011
 *
 *  This code is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.safris.xdb.xdl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateCheckFailedException extends Exception {
  public static class FailureDetail {
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
  private final Entity[] entities;

  public UpdateCheckFailedException(final Throwable cause, final FailureDetail ... failureDetail) {
    super(cause);
    this.failureDetails = failureDetail;
    if (failureDetail != null && failureDetail.length != 0) {
      final List<Entity> entities = new ArrayList<Entity>(failureDetail.length);
      for (final UpdateCheckFailedException.FailureDetail failure : failureDetail)
        if (failure != null)
          entities.add(failure.getEntity());

      this.entities = entities.toArray(new Entity[entities.size()]);;
    }
    else {
      this.entities = null;
    }
  }

  public UpdateCheckFailedException(final FailureDetail ... failureDetail) {
    this(null, failureDetail);
  }

  public UpdateCheckFailedException(final Throwable cause, final Collection<FailureDetail> failureDetail) {
    this(cause, failureDetail.toArray(new UpdateCheckFailedException.FailureDetail[failureDetail.size()]));
  }

  public UpdateCheckFailedException(final Collection<FailureDetail> failureDetail) {
    this(null, failureDetail.toArray(new UpdateCheckFailedException.FailureDetail[failureDetail.size()]));
  }

  public FailureDetail[] getFailureDetail() {
    return failureDetails;
  }

  public Entity[] getEntities() {
    return entities;
  }
}
