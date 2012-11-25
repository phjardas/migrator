package de.jardas.migrator.adapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class InformationSchemaDatabaseAdapter extends
		DatabaseAdapterSupport {
	private static final Logger LOG = LoggerFactory
			.getLogger(InformationSchemaDatabaseAdapter.class);
	private final String schema;

	public InformationSchemaDatabaseAdapter(final DataSource dataSource,
			final String schema) {
		super(dataSource);
		this.schema = schema;
	}

	@Override
	protected boolean isTableExisting() throws SQLException {
		final String sql = getTableExistsQuery();
		final PreparedStatement stmt = connection().prepareStatement(sql);
		ResultSet rs = null;

		try {
			stmt.setString(1, schema);
			stmt.setString(2, getTableName());
			LOG.trace("SQL: {} ({})", sql, new Object[] { schema,
					getTableName(), });
			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

			return false;
		} finally {
			if (rs != null) {
				rs.close();
			}

			stmt.close();
		}
	}

	protected String getTableExistsQuery() {
		return "select 1 from information_schema.tables where table_schema = ? and table_name = ?";
	}
}
