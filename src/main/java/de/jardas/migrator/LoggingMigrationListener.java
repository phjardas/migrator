package de.jardas.migrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jardas.migrator.event.MigrationEvent;
import de.jardas.migrator.event.MigrationListener;

public class LoggingMigrationListener implements MigrationListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(LoggingMigrationListener.class);

	@Override
	public void onMigrationEvent(final MigrationEvent event) {
		LOG.trace("Migration event: {}", event);
	}
}
