package main.java.rf;

import java.io.Serializable;
import java.util.List;

/**
 * The interface that all DAO interfaces must extends.
 * 
 * @param <T>
 *            the specific entity type
 * @param <K>
 *            the specific entity primary key type
 */

public interface BaseDAO<T, K extends Serializable> {

	/**
	 * Insert a new object.
	 *
	 * @param t
	 *            the object to insert
	 * @return the inserted object
	 */
	public T insert(T t);

	/**
	 * Delete an object.
	 *
	 * @param id
	 *            object id to delete
	 */
	public void delete(K id);

	/**
	 * Update an object.
	 *
	 * @param t
	 *            the object to update
	 * @return the updated object
	 */
	public T update(T t);

	/**
	 * Find an object by id.
	 *
	 * @param id
	 *            the object id to search
	 * @return the found object if exists
	 */
	public T findById(K id);

	/**
	 * Find all objects.
	 *
	 * @return the object list without filters
	 */
	public List<T> findAll();

	/**
	 * Invokes the entity manager flush method
	 */
	void flush();

}