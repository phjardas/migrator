package de.jardas.migrator.source;

import java.io.IOException;
import java.io.InputStream;

public interface MigrationSource {
	String getId();

	InputStream getInputStream() throws IOException;
}
