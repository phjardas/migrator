package de.jardas.migrator.internal;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.MigrationException;
import de.jardas.migrator.event.MigrationEvent;
import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.event.MigrationStartEvent;
import de.jardas.migrator.event.MigrationSuccessEvent;

public class MigrationExecution {
	private static final Logger LOG = LoggerFactory
			.getLogger(MigrationExecution.class);
	private final DataSource dataSource;
	private final List<Migration> migrations;
	private final List<MigrationListener> listeners;
	private final int totalMigrationCount;
	private int currentMigrationIndex;

	public MigrationExecution(final DataSource dataSource,
			final List<Migration> migrations,
			final List<MigrationListener> listeners) {
		this.dataSource = dataSource;
		this.migrations = migrations;
		this.listeners = listeners;
		totalMigrationCount = migrations.size();
	}

	public void execute() {
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
				executeMigration(migration);
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
