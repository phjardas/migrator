package de.jardas.migrator.adapter.h2;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public final class H2TestHelper {
	public static DataSource dataSource(final String schema) {
		final JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:" + schema);
		dataSource.setUser("sa");
		dataSource.setPassword("");

		return dataSource;
	}
}
