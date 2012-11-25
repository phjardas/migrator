package de.jardas.migrator;

import java.io.File;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import de.jardas.migrator.adapter.h2.H2DatabaseAdapter;
import de.jardas.migrator.adapter.h2.H2TestHelper;
import de.jardas.migrator.source.MigrationDirectory;

public class MigratorTest {
	private DatabaseAdapter adapter;

	@Before
	public void setUp() {
		final String schema = UUID.randomUUID().toString();
		final DataSource dataSource = H2TestHelper.dataSource(schema);
		adapter = new H2DatabaseAdapter(dataSource, schema);
	}

	@Test
	public void migrate() throws Exception {
		final Migrator m = new Migrator();
		m.addListener(new LoggingMigrationListener());
		m.setDatabaseAdapter(adapter);

		final File migrationsDir = new File(getClass().getResource(
				"/test-migrations").toURI());
		m.addMigrationResources(MigrationDirectory.read(migrationsDir));

		m.execute();
	}
}
