/*  Copyright Safris Software 2012
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

public class UpdateCheckFailedException extends RuntimeException {
  public UpdateCheckFailedException() {
    super();
  }

  public UpdateCheckFailedException(final String message) {
    super(message);
  }

  public UpdateCheckFailedException(final Throwable cause) {
    super(cause);
  }

  public UpdateCheckFailedException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
