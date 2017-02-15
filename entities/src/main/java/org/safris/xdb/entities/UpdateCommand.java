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

import org.safris.xdb.entities.Update.SET;
import org.safris.xdb.entities.Update.UPDATE;
import org.safris.xdb.entities.Update.WHERE;

final class UpdateCommand extends Command {
  private UPDATE update;
  private SET set;
  private WHERE where;

  public UPDATE update() {
    return update;
  }

  public void add(final UPDATE update) {
    this.update = update;
  }

  public SET set() {
    return set;
  }

  public void add(final SET set) {
    this.set = set;
  }

  public WHERE where() {
    return where;
  }

  public void add(final WHERE where) {
    this.where = where;
  }

  @Override
  protected void serialize(final Serialization serialization) {
  }
}