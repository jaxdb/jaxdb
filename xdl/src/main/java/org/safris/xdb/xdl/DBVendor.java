package org.safris.xdb.xdl;

public final class DBVendor {
  public static DBVendor MY_SQL = new DBVendor("MySQL");
  public static DBVendor DERBY = new DBVendor("Derby");
  
  private final String name;

  private DBVendor(final String name) {
    this.name = name;
  }

  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (obj instanceof DBVendor)
      return false;

    final DBVendor that = (DBVendor)obj;
    return name != null ? name.equals(that.name) : that.name == null;
  }

  public int hashCode() {
    return name != null ? name.hashCode() * 3 : -323;
  }
}