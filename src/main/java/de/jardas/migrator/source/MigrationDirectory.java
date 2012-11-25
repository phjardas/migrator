package de.jardas.migrator.source;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.internal.Preconditions;

public class MigrationDirectory {
	private static final Logger LOG = LoggerFactory
			.getLogger(MigrationDirectory.class);

	public static List<MigrationSource> read(final File directory) {
		Preconditions.notNull(directory, "directory");

		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(String.format(
					"Not a directory: %s", directory));
		}

		final File[] files = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.isFile() && file.getName().endsWith(".sql");
			}
		});

		LOG.debug("Loading {} migrations from directory {}", files.length,
				directory.getAbsolutePath());

		if (files == null || files.length == 0) {
			return Collections.emptyList();
		}

		final List<MigrationSource> sources = new ArrayList<MigrationSource>(
				files.length);

		for (final File file : files) {
			sources.add(new FileMigrationSource(file));
		}

		return sources;
	}
}
