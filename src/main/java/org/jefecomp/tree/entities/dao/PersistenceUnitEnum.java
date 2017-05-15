/**
 * 
 */
package org.jefecomp.tree.entities.dao;

/**
 * @author jefecomp
 *
 */
public enum PersistenceUnitEnum {
	
	TREE_PU("TREE_PU");
	
	private String persistenceUnitName;
	
	private PersistenceUnitEnum(String persistenceUnitName) {
		
		this.persistenceUnitName = persistenceUnitName;
	}
	
	public String getPersistenceUnitName(){
		return this.persistenceUnitName;
	}
}