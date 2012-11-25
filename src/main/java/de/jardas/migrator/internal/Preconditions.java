package de.jardas.migrator.internal;

public final class Preconditions {
	private static final String NOT_NULL = "%s must not be null";
	private static final String NOT_EMPTY = "%s must not be empty";

	private Preconditions() {
		// utility class
	}

	public static void notNull(final Object value, final String name) {
		if (value == null) {
			throw new IllegalArgumentException(String.format(NOT_NULL, name));
		}
	}

	public static void notEmpty(final String value, final String name) {
		if (value == null || value.trim().length() == 0) {
			throw new IllegalArgumentException(String.format(NOT_EMPTY, name));
		}
	}
}
