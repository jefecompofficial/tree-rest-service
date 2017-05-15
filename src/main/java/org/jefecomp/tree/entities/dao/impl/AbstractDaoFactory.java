/**
 * 
 */
package org.jefecomp.tree.entities.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.jefecomp.tree.entities.dao.GenericDAO;
import org.jefecomp.tree.entities.dao.PersistenceUnitEnum;

/**
 * @author jefecomp
 *
 */
public abstract class AbstractDaoFactory {
	
	@SuppressWarnings("rawtypes")
	private static Map<Object,GenericDAO> daoMap = new HashMap<>();
	
	
	private static <T> GenericDAO<T> getInstance(Class<T> clazz){
		
		@SuppressWarnings("unchecked")
		GenericDAO<T> genericDao = daoMap.get(clazz);
		
		if(genericDao == null){
			
			genericDao = new GenericDAOImpl<T>(PersistenceUnitEnum.TREE_PU);
			
			daoMap.put(clazz, genericDao);
		}
		
		return genericDao;
	}
	 
	public static <T> GenericDAO<T> getInstance(Class<T> clazz, boolean isConcurrent){
		
		GenericDAO<T> genericDao = null;
		
		if(isConcurrent){
			
			synchronized(AbstractDaoFactory.class){
				
				genericDao = getInstance(clazz);
				
			}
		}
		else{
			
			genericDao = getInstance(clazz);
		}
		
		return genericDao;
	}		
}