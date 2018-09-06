package yyl.mvc.util.http.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Headers {
	// ==============================Fields========================================
	private final String[] namesAndValues;

	// ==============================Constructors===================================
	/** 构造函数 */
	protected Headers(Builder builder) {
		this.namesAndValues = builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
	}

	// ==============================Methods==========================================
	public Set<String> names() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		for (int i = 0, size = size(); i < size; i++) {
			result.add(name(i));
		}
		return Collections.unmodifiableSet(result);
	}

	public String get(String name) {
		for (int i = namesAndValues.length - 2; i >= 0; i -= 2) {
			if (name.equalsIgnoreCase(namesAndValues[i])) {
				return namesAndValues[i + 1];
			}
		}
		return null;
	}

	public List<String> values(String name) {
		List<String> result = null;
		for (int i = 0, size = size(); i < size; i++) {
			if (name.equalsIgnoreCase(name(i))) {
				if (result == null) {
					result = new ArrayList<String>(2);
				}
				result.add(value(i));
			}
		}
		return (result == null) ? Collections.<String> emptyList() : Collections.<String> unmodifiableList(result);
	}

	public Map<String, List<String>> toMultimap() {
		Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
		int i = 0;
		for (int size = size(); i < size; ++i) {
			String name = name(i).toLowerCase(Locale.US);
			List<String> values = result.get(name);
			if (values == null) {
				values = new ArrayList<String>(2);
				result.put(name, values);
			}
			values.add(value(i));
		}
		return result;
	}

	public int size() {
		return namesAndValues.length / 2;
	}

	private String name(int index) {
		return namesAndValues[index * 2];
	}

	private String value(int index) {
		return namesAndValues[index * 2 + 1];
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Headers && Arrays.equals(((Headers) other).namesAndValues, namesAndValues);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(namesAndValues);
	}

	public Builder newBuilder() {
		return new Builder(this);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0, size = size(); i < size; i++) {
			result.append(name(i)).append(": ").append(value(i)).append("\n");
		}
		return result.toString();
	}

	// =================================InnerClasses===========================================
	public static class Builder {
		private final List<String> namesAndValues;

		public Builder() {
			namesAndValues = new ArrayList<String>(20);
		}

		private Builder(Headers headers) {
			this();
			Collections.addAll(namesAndValues, headers.namesAndValues);
		}

		public Builder add(String name, String value) {
			this.namesAndValues.add(name);
			this.namesAndValues.add(value.trim());
			return this;
		}

		public Builder remove(String name) {
			for (int i = 0; i < namesAndValues.size(); i += 2) {
				if (name.equalsIgnoreCase(namesAndValues.get(i))) {
					namesAndValues.remove(i); // name
					namesAndValues.remove(i); // value
					i -= 2;
				}
			}
			return this;
		}

		public Builder set(String name, String value) {
			remove(name);
			add(name, value);
			return this;
		}

		public int size() {
			return this.namesAndValues.size() / 2;
		}

		public String get(String name) {
			for (int i = namesAndValues.size() - 2; i >= 0; i -= 2) {
				if (name.equalsIgnoreCase(namesAndValues.get(i))) {
					return namesAndValues.get(i + 1);
				}
			}
			return null;
		}

		public List<String> values(String name) {
			List<String> result = null;
			int i = 0;
			for (int size = size(); i < size; ++i) {
				if (name.equalsIgnoreCase(name(i))) {
					if (result == null) {
						result = new ArrayList<String>(2);
					}
					result.add(value(i));
				}
			}
			return (result != null) ? Collections.<String> unmodifiableList(result) : Collections.<String> emptyList();
		}

		public String name(int index) {
			return this.namesAndValues.get(index * 2);
		}

		public String value(int index) {
			return this.namesAndValues.get(index * 2 + 1);
		}

		public Headers build() {
			return new Headers(this);
		}
	}
}
