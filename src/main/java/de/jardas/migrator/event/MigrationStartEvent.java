package de.jardas.migrator.event;

public class MigrationStartEvent extends MigrationEvent {
	public MigrationStartEvent(final String migrationId,
			final int currentMigrationIndex, final int totalMigrationCount) {
		super(migrationId, currentMigrationIndex, totalMigrationCount);
	}
}