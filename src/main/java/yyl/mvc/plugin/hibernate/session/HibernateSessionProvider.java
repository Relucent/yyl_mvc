package yyl.mvc.plugin.hibernate.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface HibernateSessionProvider {

	Session getCurrentSession();

	SessionFactory getSessionFactory();
}
