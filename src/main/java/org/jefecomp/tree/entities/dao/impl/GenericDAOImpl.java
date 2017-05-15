/**
 * 
 */
package org.jefecomp.tree.entities.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jefecomp.tree.entities.dao.GenericDAO;
import org.jefecomp.tree.entities.dao.PersistenceUnitEnum;

/**
 * @author jefecomp
 *
 */
public class GenericDAOImpl<T> implements GenericDAO<T> {

	private ThreadLocal<EntityManager> entityManagerThreadLocal;
	
	private ThreadLocal<Boolean> externalTransactionThreadLocal;
	
	private EntityManagerFactory emFactory;
	
	
	private void openSession(){
		
		if(this.entityManagerThreadLocal.get() == null){
			
			this.entityManagerThreadLocal.set(this.emFactory.createEntityManager());
			this.entityManagerThreadLocal.get().getTransaction().begin();
		}
	}
	
	private void closeSession(){
		
		if(this.externalTransactionThreadLocal.get() == null){
			
			this.entityManagerThreadLocal.get().getTransaction().commit();
			this.entityManagerThreadLocal.get().close();
			this.entityManagerThreadLocal.remove();
		}
	}
	
	public GenericDAOImpl(PersistenceUnitEnum persistenceUnit) {
		
		this.emFactory = Persistence.createEntityManagerFactory(persistenceUnit.getPersistenceUnitName());
		
		this.entityManagerThreadLocal = new ThreadLocal<>();
		
		this.externalTransactionThreadLocal = new ThreadLocal<>();
	}
	
	@Override
	public void openExternalSession() {
	
		this.externalTransactionThreadLocal.set(true);
		
		this.openSession();
	}

	@Override
	public void closeExternalSession() {
		
		this.externalTransactionThreadLocal.remove();
		
		this.closeSession();
	}

	@Override
	public boolean persist(T entity) {
		
		this.openSession();
		
		this.entityManagerThreadLocal.get().persist(entity);
		
		this.closeSession();
		
		return true;
	}

	@Override
	public boolean update(T entity) {
		 
		this.openSession();
		
		this.entityManagerThreadLocal.get().merge(entity);
		
		this.closeSession();
		
		return true;
		
	}

	@Override
	public <K> boolean delete(Class<?> clazz, K primaryKey) {
		
		this.openSession();
		
		@SuppressWarnings("unchecked")
		T atachedEntity = (T) this.entityManagerThreadLocal.get().find(clazz, primaryKey);
		
		boolean isRemoved = false;
		
		if(atachedEntity != null){
		
			this.entityManagerThreadLocal.get().remove(atachedEntity);
			
			isRemoved = true;
		}
		
		this.closeSession();
		
		return isRemoved;
	}

	@Override
	public boolean deleteByNamedQuery(String namedQuery, Map<String, Object> params) {
		
		this.openSession();
		
		Query query = this.entityManagerThreadLocal.get().createNamedQuery(namedQuery);
		
		if(params != null && !params.isEmpty()){
			
			params.entrySet().stream().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
			
		}
		
		Integer deletedEntities = query.executeUpdate();
		
		this.closeSession();
		
		return deletedEntities > 0;
	}

	@Override
	public <K> T findByPrimaryKey(Class<T> clazz, K primaryKey) {
		
		this.openSession();
		T result = this.entityManagerThreadLocal.get().find(clazz, primaryKey);
		this.closeSession();
		
		return result;
	}
	
	@Override
	public List<T> findByNamedQuery(String namedQuery, Map<String,Object> params) {
		
		this.openSession();
		
		Query query = this.entityManagerThreadLocal.get().createNamedQuery(namedQuery);
		
		if(params != null && !params.isEmpty()){
			
			params.entrySet().stream().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
			
		}
		
		
		@SuppressWarnings("unchecked")
		List<T> resultList = query.getResultList();
		
		this.closeSession();
		
		
		return resultList;
	}

	@Override
	public T findEntityByNamedQuery(String namedQuery, Map<String, Object> params) {
		
		List<T> resultList = this.findByNamedQuery(namedQuery, params);
		
		return resultList.isEmpty() ? null : resultList.get(0);
	}
}