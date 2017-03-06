package yyl.mvc.core.plug.hibernate.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultHibernateSessionProviderImpl implements HibernateSessionProvider {
	// ===================================Fields==============================================
	@Autowired
	protected SessionFactory sessionFactory;

	// ===================================Methods=============================================
	public Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			throw new RuntimeException();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// ===================================Spring_Ioc==========================================
	public final void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
