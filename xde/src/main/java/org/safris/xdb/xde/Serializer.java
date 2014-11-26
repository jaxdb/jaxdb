package org.safris.xdb.xde;

public final class Serializer {
  public static String serialize(final Object[] object) {
    if (object == null)
      return "null";

    String out = "";
    for (final Object item : object)
      out += ", " + serialize(item);

    return "new " + object.getClass().getComponentType().getName() + "[] {" + out.substring(2) + "}";
  }

  public static String serialize(final Object object) {
    if (object == null)
      return "null";

    if (object instanceof String)
      return "\"" + ((String)object).replace("\"", "\\\"").replace("\n", "\\n") + "\"";

    if (object instanceof Long)
      return String.valueOf(object + "L");

    if (object instanceof Short)
      return String.valueOf("(short)" + object);

    return String.valueOf(object);
  }

  private Serializer() {
  }
}