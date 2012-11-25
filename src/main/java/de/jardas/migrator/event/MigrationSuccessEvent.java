package de.jardas.migrator.event;

public class MigrationSuccessEvent extends MigrationEvent {
	public MigrationSuccessEvent(final String migrationId,
			final int currentMigrationIndex, final int totalMigrationCount) {
		super(migrationId, currentMigrationIndex, totalMigrationCount);
	}
}