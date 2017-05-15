/**
 * 
 */
package org.jefecomp.tree.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jefecomp.tree.entities.TreeNode;
import org.jefecomp.tree.entities.dao.GenericDAO;
import org.jefecomp.tree.entities.dao.impl.AbstractDaoFactory;
import org.jefecomp.tree.entities.mapper.TreeNodeModelMapper;
import org.jefecomp.tree.service.TreeService;
import org.jefecomp.tree.view.entities.TreeNodeView;

/**
 * @author jefecomp
 *
 */
public class TreeServiceLocalImpl implements TreeService {

	private GenericDAO<TreeNode> treeNodeDao;
	
	private TreeNodeModelMapper treeNodeMapper;
	
	private List<TreeNodeView> mapToTreeNodeViewChildren(List<TreeNode> treeNodeList){
		
		return treeNodeList.stream().map(treeNode -> {
			
			TreeNodeView treeNodeView = treeNodeMapper.map(treeNode, TreeNodeView.class);
			
			treeNodeView.setHasChildren(treeNode.getChildren() != null && !treeNode.getChildren().isEmpty());
			treeNodeView.setChildren(null);
			treeNodeView.setParentId(treeNode.getParent() != null ? treeNode.getParent().getId() : null);
			
			return treeNodeView;
		}).collect(Collectors.toList());
	}
	
	private List<TreeNodeView> mapToTreeNodeViewStructure(List<TreeNode> treeNodeList){
		
		return treeNodeList.stream().map(treeNode -> {
			
			TreeNodeView treeNodeView = treeNodeMapper.map(treeNode, TreeNodeView.class);
			
			if(treeNode.getChildren() != null && !treeNode.getChildren().isEmpty()){
				
				treeNodeView.setChildren(mapToTreeNodeViewStructure(treeNode.getChildren()));
				
			}
			
			treeNodeView.setParentId(treeNode.getParent() != null ? treeNode.getParent().getId() : null);
			
			return treeNodeView;
		}).collect(Collectors.toList());
	}
	
	public TreeServiceLocalImpl() {
		
		this.treeNodeDao = AbstractDaoFactory.getInstance(TreeNode.class, true);
		
		this.treeNodeMapper = new TreeNodeModelMapper();
	}
	
	/* (non-Javadoc
	 * @see org.jefecomp.tree.service.TreeService#add(org.jefecomp.tree.view.entities.TreeNodeView)
	 */
	@Override
	public Long add(TreeNodeView newNode) {
		
		TreeNode newTreeNode = this.treeNodeMapper.map(newNode, TreeNode.class);
		
		newTreeNode.setId(null);
		
		try{
			
			this.treeNodeDao.openExternalSession();
			
			if(newNode.getParentId() != null){
				
				TreeNode parent = this.treeNodeDao.findByPrimaryKey(TreeNode.class, newNode.getParentId());
				
				if(parent == null){
					
					return -1L;
				}
				
				newTreeNode.setParent(parent);
			}
				
				
			this.treeNodeDao.persist(newTreeNode);
			
			return newTreeNode.getId() != null ? newTreeNode.getId() : -2L;
		}
		finally{
			
			this.treeNodeDao.closeExternalSession();
		}
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#update(org.jefecomp.tree.view.entities.TreeNodeView)
	 */
	@Override
	public Long update(TreeNodeView node) {
		
		try{
			this.treeNodeDao.openExternalSession();
			
			TreeNode treeNode = this.treeNodeDao.findByPrimaryKey(TreeNode.class, node.getId());
			
			if(treeNode == null){
				
				return -1L;
			}
			
			if(treeNode.getParent() == null && 
			   node.getParentId() != null){
				
				TreeNode newRoot = this.treeNodeDao.findByPrimaryKey(TreeNode.class, node.getParentId());
				
				if(newRoot == null){
					
					return -2L;
				}
				
				newRoot.setParent(null);
				
				this.treeNodeDao.update(newRoot);
				
				treeNode.setParent(newRoot);
			}
			
			if(treeNode.getParent() != null && !treeNode.getParent().getId().equals(node.getParentId())){
				
				
				Optional<TreeNode> optionalChild = treeNode.getChildren().stream().filter(dbNode -> dbNode.getId().equals(node.getParentId())).findFirst();
				
				if(optionalChild.isPresent()){
					
					TreeNode childNode = optionalChild.get();
					
					childNode.setParent(treeNode.getParent());
					treeNode.setParent(childNode);
					
					this.treeNodeDao.update(childNode);
				}
				else if(node.getId() == null){
					
					treeNode.setParent(null);
				}
				else{
					
					TreeNode newParent = this.treeNodeDao.findByPrimaryKey(TreeNode.class, node.getParentId());
					
					if(newParent == null){
						
						return -3L;
					}
					
					treeNode.setParent(newParent);
				}
			}
			
			treeNode.setCode(node.getCode());
			treeNode.setDescription(node.getDescription());
			treeNode.setDetail(node.getDetail());
			
			this.treeNodeDao.update(treeNode);
			
			return treeNode.getId();
			
		}
		finally{
			this.treeNodeDao.closeExternalSession();
		}
	}

	/* (non-Javadoc)ode
	 * @see org.jefecomp.tree.service.TreeService#getTree()
	 */
	@Override
	public List<TreeNodeView> getTreeStructure() {
		
		try{
			
			this.treeNodeDao.openExternalSession();
			
			List<TreeNode> treeStructure = this.treeNodeDao.findByNamedQuery("TreeNode.getTreeStructure", null);
			
			if(treeStructure.isEmpty()){
				
				return Collections.emptyList();
			}
			
			return mapToTreeNodeViewStructure(treeStructure);
		}
		finally{
			
			this.treeNodeDao.closeExternalSession();
		}
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#getChildren(java.lang.Long)
	 */
	@Override
	public List<TreeNodeView> getChildren(Long parentId) {
		
		try{
			
			this.treeNodeDao.openExternalSession();
		
			TreeNode node = this.treeNodeDao.findByPrimaryKey(TreeNode.class, parentId);
			
			if(node == null || node.getChildren() == null || node.getChildren().isEmpty()){
				
				return Collections.emptyList();
			}
			
			return this.mapToTreeNodeViewChildren(node.getChildren());
		}
		finally{
			
			this.treeNodeDao.closeExternalSession();
		}
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#remove(java.lang.Long, boolean)
	 */
	@Override
	public Long remove(Long id, boolean removeChildren) {
		
		try{
			
			this.treeNodeDao.openExternalSession();
			
			TreeNode node = this.treeNodeDao.findByPrimaryKey(TreeNode.class, id);
			
			if(node == null){
				
				return -1L;
			}
			
			if(node.getChildren() != null && !node.getChildren().isEmpty() && !removeChildren){
				
				return -2L;
			}
					
			if(this.treeNodeDao.delete(TreeNode.class, id)){
				
				return id;
			}
			
			return -3L;
			
		}
		finally{
			
			this.treeNodeDao.closeExternalSession();
		}
	}
}
