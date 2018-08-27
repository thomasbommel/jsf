package database.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class BaseDAO2<T> {
	private static final SessionFactory			sessionFactory				= getSessionFactory();
	private static final ThreadLocal<Session>	currentSessionThreadLocal	= new ThreadLocal<>();

	private final Class<T>						clazz;

	public BaseDAO2(Class<T> clazz) {
		this.clazz = clazz;
	}

	private static SessionFactory getSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	@SuppressWarnings("static-access")
	public void beginConversation() {
		closeCurrentSession();
	}

	public void beginConversationWithTransaction() {
		closeCurrentSession();
		beginTransaction();
	}

	@SuppressWarnings("static-access")
	public void endConversation() {
		closeCurrentSession();
	}

	public void endConversationWithTransaction() {
		commitTransaction();
		closeCurrentSession();
	}

	public void closeCurrentSession() {
		Session s = currentSessionThreadLocal.get();
		if (null != s) {
			s.clear();
			s.close();
			currentSessionThreadLocal.set(null);
		}
	}

	public Transaction beginTransaction() {
		return getSession().beginTransaction();
	}

	public void commitTransaction() {
		getSession().getTransaction().commit();
	}

	public Session getSession() {
		Session s = null;
		try {
			s = currentSessionThreadLocal.get();
			if (null == s || !s.isOpen()) {
				// No Session is assiciated with this thread so create one
				s = getSessionFactory().openSession();
				currentSessionThreadLocal.set(s);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return s;
	}

	public void persist(T entity) {
		getSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	public T findById(Integer id) {
		return getSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getSession().createQuery("from " + clazz.getSimpleName()).list();
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public void deleteAll() {
		List<T> entityList = findAll();
		for (T entity : entityList) {
			delete(entity);
		}
	}

}