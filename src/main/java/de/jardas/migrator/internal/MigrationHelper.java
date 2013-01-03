package de.jardas.migrator.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

public final class MigrationHelper {
	private MigrationHelper() {
		// utility class
	}

	public static Iterable<String> getLines(final InputStream in)
			throws IOException {
		final Reader reader = new InputStreamReader(in, Charsets.UTF_8);

		try {
			final String content = CharStreams.toString(reader);

			return getLines(content);
		} finally {
			Closeables.closeQuietly(reader);
		}
	}

	public static Iterable<String> getLines(final String content) {
		final String trimmed = content.replaceAll("\\s+", " ");
		return Splitter.on(';').trimResults().omitEmptyStrings().split(trimmed);
	}
}
