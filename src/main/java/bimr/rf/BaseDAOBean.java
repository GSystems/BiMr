package bimr.rf;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class BaseDAOBean<T, K extends Serializable> implements BaseDAO<T, K> {

	@PersistenceContext
	private EntityManager entityManager;
	private Class<T> type;

	@SuppressWarnings("unchecked")
	public BaseDAOBean() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public T insert(T t) {
		entityManager.persist(t);
		return t;
	}

	@Override
	public void delete(K id) {
		// TODO implement this
	}

	@Override
	public T update(T t) {
		return null;
	}

	@Override
	public T findById(K id) {
		return null;
	}

	@Override
	public List<T> findAll() {
		TypedQuery<T> query = entityManager.createQuery("from " + type.getName(), type);
		return query.getResultList();
	}

	@Override
	public void flush() {
		// TODO implement this
	}

}
