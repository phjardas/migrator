package de.jardas.migrator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.internal.MigrationExecution;
import de.jardas.migrator.internal.Preconditions;
import de.jardas.migrator.source.MigrationSource;

public class Migrator {
	private DatabaseAdapter databaseAdapter;
	private final List<MigrationSource> migrationResources = new ArrayList<MigrationSource>();
	private final List<MigrationListener> listeners = new LinkedList<MigrationListener>();

	public void execute() {
		Preconditions.notNull(databaseAdapter, "databaseAdapter");

		final MigrationExecution execution = new MigrationExecution(
				databaseAdapter, migrationResources, listeners);

		try {
			execution.execute();
		} catch (final SQLException e) {
			throw new MigrationException("Error during migration: " + e, e);
		}
	}

	public Migrator setDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		this.databaseAdapter = databaseAdapter;
		return this;
	}

	public Migrator addMigrationResources(
			final Collection<MigrationSource> migrationResources) {
		if (migrationResources != null) {
			this.migrationResources.addAll(migrationResources);
		}

		return this;
	}

	public Migrator addMigrationResource(final MigrationSource migrationResource) {
		Preconditions.notNull(migrationResource, "migrationResource");
		migrationResources.add(migrationResource);
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
