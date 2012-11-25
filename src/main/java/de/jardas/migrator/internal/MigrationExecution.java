package de.jardas.migrator.internal;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.DatabaseAdapter;
import de.jardas.migrator.MigrationException;
import de.jardas.migrator.event.MigrationEvent;
import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.event.MigrationStartEvent;
import de.jardas.migrator.event.MigrationSuccessEvent;

public class MigrationExecution {
	private static final Logger LOG = LoggerFactory
			.getLogger(MigrationExecution.class);
	private final DatabaseAdapter databaseAdapter;
	private final List<URL> migrationResources;
	private final List<MigrationListener> listeners;
	private final int totalMigrationCount;
	private List<Migration> migrations;
	private int currentMigrationIndex;

	public MigrationExecution(final DatabaseAdapter databaseAdapter,
			final List<URL> migrationResources,
			final List<MigrationListener> listeners) {
		this.databaseAdapter = databaseAdapter;
		this.migrationResources = migrationResources;
		this.listeners = listeners;
		totalMigrationCount = migrations.size();
	}

	public void execute() throws SQLException {
		migrations = loadMigrations();
		migrations = selectMigrations(migrations);

		if (migrations.isEmpty()) {
			LOG.info("Database is up to date, no migration required.");
			return;
		}

		LOG.info("Going to execute {} migrations.", migrations.size());

		for (final Migration migration : migrations) {
			fireEvent(new MigrationStartEvent(migration.getId(),
					currentMigrationIndex, totalMigrationCount));

			try {
				LOG.info("Executing migration '{}' ({}/{})", new Object[] {
						migration.getId(), currentMigrationIndex,
						totalMigrationCount, });
				final Date executedAt = new Date();
				executeMigration(migration);
				databaseAdapter.registerExecutedMigration(migration.getId(),
						executedAt);
			} catch (final RuntimeException e) {
				final String error = String.format(
						"Migration '%s' (%d/%d) failed: %s", migration.getId(),
						currentMigrationIndex, totalMigrationCount, e);
				LOG.error(error, e);
				throw new MigrationException(error, e);
			}

			fireEvent(new MigrationSuccessEvent(migration.getId(),
					currentMigrationIndex, totalMigrationCount));
			currentMigrationIndex++;
		}
	}

	private void executeMigration(final Migration migration) {
		// FIXME executeMigration()
	}

	private List<Migration> loadMigrations() {
		final List<Migration> migrations = new ArrayList<Migration>(
				migrationResources.size());
		// FIXME implement loadMigrations()

		Collections.sort(migrations, Migration.ID_COMPARATOR);

		return migrations;
	}

	private List<Migration> selectMigrations(final List<Migration> migrations)
			throws SQLException {
		final List<Migration> selected = new ArrayList<Migration>();

		for (final Migration migration : migrations) {
			if (!databaseAdapter.isMigrationApplied(migration.getId())) {
				selected.add(migration);
			}
		}

		return selected;
	}

	private void fireEvent(final MigrationEvent event) {
		for (final MigrationListener listener : listeners) {
			try {
				listener.onMigrationEvent(event);
			} catch (final RuntimeException e) {
				LOG.warn("Error notifying listener " + listener + " about "
						+ event + ": " + e, e);
			}
		}
	}
}
