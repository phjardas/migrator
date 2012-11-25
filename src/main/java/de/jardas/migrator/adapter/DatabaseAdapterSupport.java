package de.jardas.migrator.adapter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.DatabaseAdapter;
import de.jardas.migrator.internal.Preconditions;

public abstract class DatabaseAdapterSupport implements DatabaseAdapter {
	private static final Logger LOG = LoggerFactory
			.getLogger(DatabaseAdapterSupport.class);
	private String tableName = "schema_migrations";
	private String idColumnName = "migration_id";
	private String executedAtColumnName = "executed_at";
	private final DataSource dataSource;
	private boolean tableChecked;
	private Connection connection;

	public DatabaseAdapterSupport(final DataSource dataSource) {
		Preconditions.notNull(dataSource, "dataSource");
		this.dataSource = dataSource;
	}

	protected String getTableName() {
		return tableName;
	}

	protected void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	protected String getIdColumnName() {
		return idColumnName;
	}

	protected void setIdColumnName(final String idColumnName) {
		this.idColumnName = idColumnName;
	}

	protected String getExecutedAtColumnName() {
		return executedAtColumnName;
	}

	protected void setExecutedAtColumnName(final String executedAtColumnName) {
		this.executedAtColumnName = executedAtColumnName;
	}

	@Override
	public boolean isMigrationApplied(final String migrationId)
			throws SQLException {
		checkTableExists();

		final String sql = String.format(getIsMigrationAppliedPattern(),
				tableName, idColumnName);
		final PreparedStatement stmt = connection().prepareStatement(sql);
		ResultSet rs = null;

		try {
			stmt.setString(1, migrationId);
			LOG.trace("SQL: {} ({})", sql, new Object[] { migrationId, });
			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

			return false;
		} finally {
			if (rs != null) {
				rs.close();
			}

			stmt.close();
		}
	}

	protected String getIsMigrationAppliedPattern() {
		return "select 1 from %s where %s = ?";
	}

	@Override
	public void registerExecutedMigration(final String migrationId,
			final Date executedAt) throws SQLException {
		checkTableExists();

		final String sql = String.format(getRegisterExecutedMigrationPattern(),
				tableName, idColumnName, executedAtColumnName);
		final PreparedStatement stmt = connection().prepareStatement(sql);

		try {
			stmt.setString(1, migrationId);
			stmt.setObject(2, executedAt);
			LOG.trace("SQL: {} ({})", sql, new Object[] { migrationId,
					executedAt, });
			stmt.executeUpdate();
		} finally {
			stmt.close();
		}
	}

	protected String getRegisterExecutedMigrationPattern() {
		return "insert into %s (%s, %s) values (?, ?)";
	}

	@Override
	public void executeStatement(final String sql) throws SQLException {
		execute(sql);
	}

	private void checkTableExists() throws SQLException {
		if (!tableChecked) {
			LOG.debug("Checking whether table exists...");

			if (!isTableExisting()) {
				LOG.info("Table does not exist yet, creating it.");
				createTable();
			} else {
				LOG.trace("Table already exists.");
			}

			tableChecked = true;
		}
	}

	protected abstract boolean isTableExisting() throws SQLException;

	protected void createTable() throws SQLException {
		final String sql = String.format(getCreateTablePattern(), tableName,
				idColumnName, executedAtColumnName);
		execute(sql);
	}

	protected void execute(final String sql) throws SQLException {
		final PreparedStatement stmt = connection().prepareStatement(sql);

		try {
			LOG.trace("SQL: {}", sql);
			stmt.execute();
		} finally {
			stmt.close();
		}
	}

	protected abstract String getCreateTablePattern();

	protected Connection connection() throws SQLException {
		if (connection == null) {
			connection = dataSource.getConnection();
		}

		return connection;
	}

	@Override
	public void close() throws IOException {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (final SQLException e) {
				throw new IOException("Error closing SQL connection: " + e, e);
			}
		}
	}
}
