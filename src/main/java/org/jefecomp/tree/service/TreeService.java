/**
 * 
 */
package org.jefecomp.tree.service;

import java.util.List;

import org.jefecomp.tree.view.entities.TreeNodeView;

/**
 * @author jefecomp
 *
 */
public interface TreeService {
	
	
	Long add(TreeNodeView newNode);
	
	Long update(TreeNodeView node);
	
	List<TreeNodeView> getTreeStructure();
	
	List<TreeNodeView> getChildren(Long parentId);
	
	Long remove(Long id, boolean removeChildren);
}
