package de.jardas.migrator.internal;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class MigrationHelperTest {
	@Test
	public void getLines() {
		final String line1 = "select * from foo";
		final String line2 = "select * from bar";

		final Iterable<String> lines = MigrationHelper.getLines("  " + line1
				+ "\t;\n" + line2 + " ;\n  \n");
		final Iterator<String> it = lines.iterator();
		final String l1 = it.next();
		final String l2 = it.next();

		assertEquals("line 1", line1, l1);
		assertEquals("line 2", line2, l2);
	}
}
