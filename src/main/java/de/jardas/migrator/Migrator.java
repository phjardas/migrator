package de.jardas.migrator;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.jardas.migrator.event.MigrationListener;
import de.jardas.migrator.internal.Migration;
import de.jardas.migrator.internal.MigrationExecution;
import de.jardas.migrator.internal.Preconditions;

public class Migrator {
	private static final Logger LOG = LoggerFactory.getLogger(Migrator.class);
	private final DataSource dataSource;
	private final List<URL> migrationResources = new ArrayList<URL>();
	private final List<MigrationListener> listeners = new LinkedList<MigrationListener>();

	public Migrator(final DataSource dataSource,
			final List<URL> migrationResources) {
		Preconditions.notNull(dataSource, "dataSource");
		this.dataSource = dataSource;

		if (migrationResources != null) {
			this.migrationResources.addAll(migrationResources);
		}
	}

	public void execute() {
		LOG.info("Migrating database...");
		final List<Migration> migrations = loadMigrations();
		final List<Migration> selectedMigrations = selectMigrations(migrations);

		final MigrationExecution execution = createExecution(selectedMigrations);
		execution.execute();
	}

	protected MigrationExecution createExecution(
			final List<Migration> selectedMigrations) {
		return new MigrationExecution(dataSource, selectedMigrations, listeners);
	}

	protected List<Migration> loadMigrations() {
		final List<Migration> migrations = new ArrayList<Migration>(
				migrationResources.size());
		// FIXME implement loadMigrations()
		return migrations;
	}

	protected List<Migration> selectMigrations(final List<Migration> migrations) {
		final String databaseVersion = getCurrentDatabaseVersion();

		if (databaseVersion == null) {
			return migrations;
		}

		return Lists.newArrayList(Collections2.filter(migrations,
				new Predicate<Migration>() {
					@Override
					public boolean apply(final Migration migration) {
						return isMigrationApplicable(migration, databaseVersion);
					}
				}));
	}

	/**
	 * Does the given migration need to be executed for the given database
	 * version?
	 */
	protected boolean isMigrationApplicable(final Migration migration,
			final String databaseVersion) {
		return migration.getId().compareToIgnoreCase(databaseVersion) > 0;
	}

	/**
	 * Get the current version of the database.
	 * 
	 * @return the current database version or <code>null</code> if the database
	 *         has no version information.
	 */
	protected String getCurrentDatabaseVersion() {
		// FIXME implement getCurrentDatabaseVersion()
		return null;
	}

	public void addListener(final MigrationListener listener) {
		listeners.add(listener);
	}

	public void removeListener(final MigrationListener listener) {
		listeners.remove(listener);
	}
}
