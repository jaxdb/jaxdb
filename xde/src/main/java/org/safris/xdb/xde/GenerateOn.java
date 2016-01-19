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