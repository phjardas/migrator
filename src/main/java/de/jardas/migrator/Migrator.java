package de.jardas.migrator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.internal.MigrationExecution;
import de.jardas.migrator.internal.Preconditions;

public class Migrator {
	private static final Logger LOG = LoggerFactory.getLogger(Migrator.class);
	private DatabaseAdapter databaseAdapter;
	private final List<URL> migrationResources = new ArrayList<URL>();
	private final List<MigrationListener> listeners = new LinkedList<MigrationListener>();

	public void execute() {
		Preconditions.notNull(databaseAdapter, "databaseAdapter");

		final MigrationExecution execution = new MigrationExecution(
				databaseAdapter, migrationResources, listeners);

		try {
			execution.execute();
		} catch (final SQLException e) {
			throw new MigrationException("Error during migration: " + e, e);
		} finally {
			try {
				databaseAdapter.close();
			} catch (final IOException e) {
				LOG.warn("Error closing database adapter: " + e, e);
			}
		}
	}

	public Migrator setDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		this.databaseAdapter = databaseAdapter;
		return this;
	}

	public Migrator setMigrationResources(
			final Collection<URL> migrationResources) {
		if (migrationResources != null) {
			this.migrationResources.addAll(migrationResources);
		}

		return this;
	}

	public Migrator addListener(final MigrationListener listener) {
		listeners.add(listener);
		return this;
	}

	public Migrator removeListener(final MigrationListener listener) {
		listeners.remove(listener);
		return this;
	}
}
