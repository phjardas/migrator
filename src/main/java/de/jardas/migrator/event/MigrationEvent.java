package de.jardas.migrator.event;

public abstract class MigrationEvent {
	private final String migrationId;
	private final int currentMigrationIndex;
	private final int totalMigrationCount;

	public MigrationEvent(final String migrationId,
			final int currentMigrationIndex, final int totalMigrationCount) {
		this.migrationId = migrationId;
		this.currentMigrationIndex = currentMigrationIndex;
		this.totalMigrationCount = totalMigrationCount;
	}

	public String getMigrationId() {
		return migrationId;
	}

	public int getCurrentMigrationIndex() {
		return currentMigrationIndex;
	}

	public int getTotalMigrationCount() {
		return totalMigrationCount;
	}
}
