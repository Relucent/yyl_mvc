package yyl.mvc.plug.hibernate.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;

/**
 * 实体元数据信息对象
 */
public class MetadataInfo<T> {

	// ==============================Fields===========================================
	private ClassMetadata metadata;
	private Map<String, Class<?>> propertyTypeCache;
	private SessionFactoryImplementor sessionFactoryImplementor;

	// ==============================Constructors=====================================
	protected MetadataInfo(ClassMetadata metadata, SessionFactoryImplementor sessionFactoryImplementor) {
		this.metadata = metadata;
		this.sessionFactoryImplementor = sessionFactoryImplementor;
		this.propertyTypeCache = new ConcurrentHashMap<>();
	}

	// ==============================Methods==========================================
	public Class<?> getPropertyType(String propertyName) {
		Class<?> propertyType = propertyTypeCache.get(propertyName);
		if (propertyType != null) {
			try {
				propertyType = lookupActualPropertyType(propertyName, metadata, sessionFactoryImplementor);
			} catch (Exception e) {
				throw new NoSuchPropertyException("EntityProperty[" + metadata.getEntityName() + "#" + propertyName + "]", e);
			}
		}
		return propertyType;
	}

	protected Class<?> lookupActualPropertyType(String propertyName, ClassMetadata metadata, SessionFactoryImplementor sessionFactoryImplementor)
			throws NoSuchFieldException {

		PathTokenizer tokenizer = new PathTokenizer(propertyName);
		if (tokenizer.hasNext()) {
			Type type = metadata.getPropertyType(tokenizer.name);

			if (!type.isAssociationType()) {
				throw new NoSuchPropertyException("Not AssociationType [" + metadata.getEntityName() + "#" + propertyName + "]");
			}

			AssociationType associationType = (AssociationType) type;

			String associatedEntityName = associationType.getAssociatedEntityName(sessionFactoryImplementor);
			ClassMetadata associatedClassMetadata = sessionFactoryImplementor.getClassMetadata(associatedEntityName);

			return lookupActualPropertyType(tokenizer.children, associatedClassMetadata, sessionFactoryImplementor);
		} else {
			//SingleColumnType
			Type type = metadata.getPropertyType(propertyName);
			return type.getReturnedClass();
		}

	}

	private static class PathTokenizer {
		private String name;
		private String children;

		public PathTokenizer(String fullname) {
			int delim = fullname.indexOf('.');
			if (delim > -1) {
				this.name = fullname.substring(0, delim);
				this.children = fullname.substring(delim + 1);
			} else {
				this.name = fullname;
				this.children = null;
			}
		}

		public boolean hasNext() {
			return this.children != null;
		}
	}

	@SuppressWarnings("serial")
	public static class NoSuchPropertyException extends RuntimeException {

		protected NoSuchPropertyException(String message, Exception cause) {
			super(message, cause);
		}

		protected NoSuchPropertyException(String message) {
			super(message);
		}

		protected NoSuchPropertyException(Exception cause) {
			super(cause);
		}
	}

}
