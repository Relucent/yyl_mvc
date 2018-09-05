package yyl.mvc.plug.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import yyl.mvc.plug.hibernate.session.HibernateSessionProvider;

/**
 * 基于Hibernate的数据访问层支持基类
 */
public class HibernateDaoSupport {
	// ===================================Fields==============================================
	@Autowired
	protected HibernateSessionProvider hibernateSessionProvider;

	// ===================================Methods=============================================
	protected final Session getSession() {
		return hibernateSessionProvider.getCurrentSession();
	}

	protected SessionFactory getSessionFactory() {
		return hibernateSessionProvider.getSessionFactory();
	}

	// ===================================Spring_Ioc==========================================
	public void setHibernateSessionProvider(HibernateSessionProvider hibernateSessionProvider) {
		this.hibernateSessionProvider = hibernateSessionProvider;
	}
}
