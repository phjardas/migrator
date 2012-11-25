package de.jardas.migrator.adapter.h2;

import javax.sql.DataSource;

import de.jardas.migrator.adapter.InformationSchemaDatabaseAdapter;

public class H2DatabaseAdapter extends InformationSchemaDatabaseAdapter {
	public H2DatabaseAdapter(final DataSource dataSource, final String schema) {
		super(dataSource, schema);
	}

	@Override
	protected String getCreateTablePattern() {
		return "create table %s (%s varchar(255) not null primary key, %s timestamp not null)";
	}
}
