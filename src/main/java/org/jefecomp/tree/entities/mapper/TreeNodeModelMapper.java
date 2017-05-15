/**
 * 
 */
package org.jefecomp.tree.entities.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * @author jefecomp
 *
 */
public class TreeNodeModelMapper {
	
	
	private ModelMapper modelMapper;
	
	public TreeNodeModelMapper() {
		
		this.modelMapper = new ModelMapper();
		
		this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public <T,M> M map(T sourceEntity, Class<M> destinationType){
		
		return this.modelMapper.map(sourceEntity, destinationType);
	}
}
