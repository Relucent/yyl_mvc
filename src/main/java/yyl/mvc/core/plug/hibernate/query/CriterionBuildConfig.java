package yyl.mvc.core.plug.hibernate.query;

public class CriterionBuildConfig {

	public static class PathExcluder {

		private final String[] excludeds;

		public PathExcluder(String... excludeds) {
			this.excludeds = excludeds;
		}

		public boolean exclude(String path) {
			for (String excluded : excludeds) {
				if (path.equals(excluded)) {
					return true;
				}
			}
			return false;
		}
	}

	public static class PathIncluder {

		private final String[] includeds;

		public PathIncluder(String... includeds) {
			this.includeds = includeds;
		}

		public boolean include(String path) {
			for (String excluded : includeds) {
				if (path.equals(excluded)) {
					return true;
				}
			}
			return false;
		}
	}
}
