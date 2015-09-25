package core.utils;

import javax.servlet.ServletException;

public class ServletRequestBindingException extends ServletException {
	private static final long serialVersionUID = 1L;

	public ServletRequestBindingException() {
		super();
	}

	public ServletRequestBindingException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public ServletRequestBindingException(String message) {
		super(message);
	}

	public ServletRequestBindingException(Throwable rootCause) {
		super(rootCause);
	}
}
