package de.jardas.migrator.internal;

import java.util.Comparator;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;

public class Migration {
	public static final Comparator<Migration> ID_COMPARATOR = new Comparator<Migration>() {
		@Override
		public int compare(final Migration o1, final Migration o2) {
			return o1.getId().compareToIgnoreCase(o2.getId());
		}
	};

	private final String id;
	private final String content;

	public Migration(final String id, final String content) {
		Preconditions.notEmpty(id, "id");
		Preconditions.notEmpty(content, "content");
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Iterable<String> getLines() {
		return Splitter.on(';').trimResults().split(content);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).toString();
	}
}
