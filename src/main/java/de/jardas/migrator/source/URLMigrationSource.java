package de.jardas.migrator.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.jardas.migrator.internal.Preconditions;

public class URLMigrationSource implements MigrationSource {
	private final URL url;

	public URLMigrationSource(final URL url) {
		Preconditions.notNull(url, "url");
		this.url = url;
	}

	@Override
	public String getId() {
		return url.getPath();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}
}
