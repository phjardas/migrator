package de.jardas.migrator.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.jardas.migrator.internal.Preconditions;

public class FileMigrationSource implements MigrationSource {
	private final File file;

	public FileMigrationSource(final File file) {
		Preconditions.notNull(file, "file");
		this.file = file;
	}

	@Override
	public String getId() {
		return file.getName().replaceAll("\\.sql$", "");
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}
}
