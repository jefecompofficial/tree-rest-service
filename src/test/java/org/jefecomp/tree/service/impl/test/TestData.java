/**
 * 
 */
package org.jefecomp.tree.service.impl.test;

import java.util.Arrays;
import java.util.List;

import org.jefecomp.tree.service.impl.TreeServiceLocalImpl;
import org.jefecomp.tree.view.entities.TreeNodeView;

/**
 * @author jefecomp
 *
 */
public class TestData {
	
	
	private TreeServiceLocalImpl treeServiceLocal;
	
	public TestData(TreeServiceLocalImpl treeServiceLocal) {

		this.treeServiceLocal = treeServiceLocal;
	}
	
	
	public List<Long> populateDatabase(){
		
		
		TreeNodeView node1 = new TreeNodeView();
		
		node1.setCode("node 1");
		node1.setDescription("Node1 description");
		node1.setDetail("Node1 detail");
		
		node1.setId(this.treeServiceLocal.add(node1));
		
		
		TreeNodeView node2 = new TreeNodeView();
		
		node2.setCode("node 2");
		node2.setDescription("Node 2 description");
		node2.setDetail("Node 2 detail");
		node2.setParentId(node1.getId());
		
		node2.setId(this.treeServiceLocal.add(node2));
		
		TreeNodeView node3 = new TreeNodeView();
		
		node3.setCode("node 3");
		node3.setDescription("Node 3 description");
		node3.setDetail("Node 3 detail");
		node3.setParentId(node1.getParentId());
		
		node3.setId(this.treeServiceLocal.add(node3));
		
		TreeNodeView node4 = new TreeNodeView();
		
		node4.setCode("node 4");
		node4.setDescription("Node 4 description");
		node4.setDetail("Node 4 detail");
		node4.setParentId(node3.getId());
		
		node4.setId(this.treeServiceLocal.add(node4));
		
		TreeNodeView node5 = new TreeNodeView();
		
		node5.setCode("node 5");
		node5.setDescription("Node 4 description");
		node5.setDetail("Node 5 detail");
		node5.setParentId(node3.getId());
		
		node5.setId(this.treeServiceLocal.add(node5));

		
		return Arrays.asList(node1.getId(), node2.getId(),node3.getId(),node4.getId(), node5.getId());
	}

}
