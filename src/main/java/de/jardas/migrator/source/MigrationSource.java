package de.jardas.migrator.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

public interface MigrationSource {
	Comparator<MigrationSource> ID_COMPARATOR = new Comparator<MigrationSource>() {
		@Override
		public int compare(final MigrationSource o1, final MigrationSource o2) {
			return o1.getId().compareToIgnoreCase(o2.getId());
		}
	};

	String getId();

	InputStream getInputStream() throws IOException;
}
