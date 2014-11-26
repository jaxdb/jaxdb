package org.safris.xdb.xde.type;

public class Date extends java.sql.Date {
  private static final long serialVersionUID = -5190995792523088722L;
  private static final int MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

  public Date(final long date) {
    super((date / MILLISECONDS_IN_DAY) * MILLISECONDS_IN_DAY);
  }

  public void setTime(final long date) {
    super.setTime((date / MILLISECONDS_IN_DAY) * MILLISECONDS_IN_DAY);
  }
}