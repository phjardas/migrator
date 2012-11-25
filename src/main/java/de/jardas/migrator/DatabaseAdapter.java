package de.jardas.migrator;

import javax.sql.DataSource;

public interface DatabaseAdapter {
	/**
	 * Get the current version of the database.
	 * 
	 * @return the current database version or <code>null</code> if the database
	 *         has no version information.
	 */
	String getDatabaseVersion(DataSource dataSource);

	void setDatabaseVersion(DataSource dataSource, String version);
}
