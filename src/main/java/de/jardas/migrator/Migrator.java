package de.jardas.migrator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.internal.MigrationExecution;
import de.jardas.migrator.internal.Preconditions;

public class Migrator {
	private DataSource dataSource;
	private DatabaseAdapter databaseAdapter;
	private final List<URL> migrationResources = new ArrayList<URL>();
	private final List<MigrationListener> listeners = new LinkedList<MigrationListener>();

	public void execute() {
		Preconditions.notNull(dataSource, "dataSource");
		Preconditions.notNull(databaseAdapter, "databaseAdapter");

		final MigrationExecution execution = new MigrationExecution(dataSource,
				databaseAdapter, migrationResources, listeners);
		execution.execute();
	}

	public Migrator setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
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
