package de.jardas.migrator;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.Date;

public interface DatabaseAdapter extends Closeable {
	boolean isMigrationApplied(String migrationId) throws SQLException;

	void registerExecutedMigration(String migrationId, Date executedAt)
			throws SQLException;

	void executeStatement(String sql) throws SQLException;
}
