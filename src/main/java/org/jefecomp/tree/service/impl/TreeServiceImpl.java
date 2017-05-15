/**
 * 
 */
package org.jefecomp.tree.service.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jefecomp.tree.service.TreeService;
import org.jefecomp.tree.view.entities.TreeNodeView;

/**
 * @author jefecomp
 *
 */
@Path("/node")
@Produces(MediaType.APPLICATION_JSON)
public class TreeServiceImpl implements TreeService {
	
	private TreeServiceLocalImpl treeServiceLocal;
	
	
	public TreeServiceImpl() {
		
		this.treeServiceLocal = new TreeServiceLocalImpl();
	}
	
	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#add(org.jefecomp.tree.view.entities.TreeNodeView)
	 */
	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Long add(TreeNodeView newNode) {
		
		return this.treeServiceLocal.add(newNode);
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#update(org.jefecomp.tree.view.entities.TreeNodeView)
	 */
	@Override
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Long update(TreeNodeView node) {
		
		return this.treeServiceLocal.update(node);
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#getTree()
	 */
	@Override
	@GET
	public List<TreeNodeView> getTreeStructure() {
		
		return this.treeServiceLocal.getTreeStructure();
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#getChildren(java.lang.Long)
	 */
	@Override
	@GET
	@Path("/{parentId}")
	public List<TreeNodeView> getChildren(@PathParam("parentId") Long parentId) {
		
		return this.treeServiceLocal.getChildren(parentId);
	}

	/* (non-Javadoc)
	 * @see org.jefecomp.tree.service.TreeService#remove(java.lang.Long, boolean)
	 */
	@Override
	@GET
	@Path("/{id}/{removeChildren}")
	public Long remove(@PathParam("id") Long id, @PathParam("removeChildren") boolean removeChildren) {
		
		return this.treeServiceLocal.remove(id, removeChildren);
	}
}
