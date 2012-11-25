package de.jardas.migrator;

public class MigrationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MigrationException() {
		super();
	}

	public MigrationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MigrationException(final String message) {
		super(message);
	}

	public MigrationException(final Throwable cause) {
		super(cause);
	}
}
