/* Copyright (c) 2016 Seva Safris
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

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public abstract class GenerateOn<T> {
  public static GenerateOn<String> UUID = new GenerateOn<String>() {
    public String generate() {
      return java.util.UUID.randomUUID().toString().toUpperCase();
    }
  };

  public static GenerateOn<LocalDate> TIMESTAMP_DATE = new GenerateOn<LocalDate>() {
    public LocalDate generate() {
      return new LocalDate();
    }
  };

  public static GenerateOn<LocalTime> TIMESTAMP_TIME = new GenerateOn<LocalTime>() {
    public LocalTime generate() {
      return new LocalTime();
    }
  };

  public static GenerateOn<LocalDateTime> TIMESTAMP_DATETIME = new GenerateOn<LocalDateTime>() {
    public LocalDateTime generate() {
      return new LocalDateTime();
    }
  };

  public abstract T generate();
}