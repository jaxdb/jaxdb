package org.safris.xdb.xde;

public class XDERuntimeException extends RuntimeException {
  private static final long serialVersionUID = 4855500440768213663L;

  public XDERuntimeException() {
    super();
  }

  public XDERuntimeException(final String message) {
    super(message);
  }

  public XDERuntimeException(final Throwable cause) {
    super(cause);
  }

  public XDERuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }
}