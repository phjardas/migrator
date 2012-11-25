package de.jardas.migrator.adapter.h2;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;
import org.junit.Test;

public class H2DatabaseAdapterTest {
	private H2DatabaseAdapter adapter;

	@Before
	public void setUp() {
		final JdbcDataSource dataSource = new JdbcDataSource();
		final String schema = UUID.randomUUID().toString();
		dataSource.setURL("jdbc:h2:mem:" + schema);
		dataSource.setUser("sa");
		dataSource.setPassword("");

		adapter = new H2DatabaseAdapter(dataSource, schema);
	}

	@Test
	public void migration() throws SQLException {
		final String a = "aaa";
		final String b = "bbb";
		assertMigrationApplied(a, false);
		assertMigrationApplied(b, false);

		adapter.registerExecutedMigration(a, new Date());
		assertMigrationApplied(a, true);
		assertMigrationApplied(b, false);

		adapter.registerExecutedMigration(b, new Date());
		assertMigrationApplied(a, true);
		assertMigrationApplied(b, true);
	}

	private void assertMigrationApplied(final String id, final boolean expected)
			throws SQLException {
		assertEquals("isMigrationApplied('" + id + "')", expected,
				adapter.isMigrationApplied(id));
	}
}
