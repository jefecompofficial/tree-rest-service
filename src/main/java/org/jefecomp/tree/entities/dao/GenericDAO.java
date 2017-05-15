/**
 * 
 */
package org.jefecomp.tree.entities.dao;

import java.util.List;
import java.util.Map;

/**
 * @author jefecomp
 * @param <K>
 *
 */
public interface GenericDAO<T> {
	
	
	public void openExternalSession();
	
	public void closeExternalSession();
	
	public boolean persist(T entity);
	
	public boolean update(T entity);
	
	public <K> boolean delete(Class<?> clazz, K primaryKey);
	
	public boolean deleteByNamedQuery(String namedQuery, Map<String, Object> params);
	
	public <K> T findByPrimaryKey(Class<T> clazz, K primaryKey);
	
	public List<T> findByNamedQuery(String namedQuery, Map<String, Object> params);
	
	public T findEntityByNamedQuery(String namedQuery, Map<String, Object> params);
}
