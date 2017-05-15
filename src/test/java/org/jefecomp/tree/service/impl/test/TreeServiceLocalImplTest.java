/**
 * 
 */
package org.jefecomp.tree.service.impl.test;


import java.util.List;

import org.jefecomp.tree.entities.TreeNode;
import org.jefecomp.tree.entities.dao.GenericDAO;
import org.jefecomp.tree.entities.dao.impl.AbstractDaoFactory;
import org.jefecomp.tree.service.impl.TreeServiceLocalImpl;
import org.jefecomp.tree.view.entities.TreeNodeView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jefecomp
 *
 */
public class TreeServiceLocalImplTest {

	private TreeServiceLocalImpl treeServiceLocal;

	private List<Long> testDataIds;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		this.treeServiceLocal = new TreeServiceLocalImpl();
		
		TestData testData = new TestData(this.treeServiceLocal);
		
		this.testDataIds = testData.populateDatabase();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
		this.treeServiceLocal = null;
		
		GenericDAO<TreeNode> treeNodeDao = AbstractDaoFactory.getInstance(TreeNode.class, true);
		
		treeNodeDao.deleteByNamedQuery("TreeNode.DeleteALL", null);
		
		this.testDataIds = null;
	}

	/**
	 * Test method for {@link org.jefecomp.tree.service.impl.TreeServiceLocalImpl#add(org.jefecomp.tree.view.entities.TreeNodeView)}.
	 */
	@Test
	public void testAdd() {
		
		/*
		 * 1. Add a root node (i.e. parentId == null) in the tree).
		 * 
		 */
		
		TreeNodeView rootNode = new TreeNodeView();
		
		rootNode.setCode("root");
		rootNode.setDescription("Root node description");
		rootNode.setDetail("Root node detail");
		
		rootNode.setId(this.treeServiceLocal.add(rootNode));
		
		Assert.assertTrue(rootNode.getId() > 0);
		
		/*
		 * 2. Add another root node
		 */
		
		TreeNodeView rootNode1 = new TreeNodeView();
		
		rootNode1.setCode("root 1");
		rootNode1.setDescription("RootNode 1 description");
		rootNode1.setDetail("RootNode 1 detail");
		
		rootNode1.setId(this.treeServiceLocal.add(rootNode1));
		
		Assert.assertTrue(rootNode1.getId() > 0);
		
		
		/*
		 *3. Add a new node as children of the rootNode
		 */
		
		TreeNodeView node1 = new TreeNodeView();
		
		node1.setCode("node 1");
		node1.setDescription("Node 1 description");
		node1.setDetail("Node 1 detail");
		node1.setParentId(rootNode.getId());
		
		node1.setId(this.treeServiceLocal.add(node1));
		
		Assert.assertTrue(node1.getId() > 0);
		Assert.assertTrue(node1.getId() != rootNode.getId());
		
		/*
		 * 4. Add a new node as children of node 1
		 */
		
		TreeNodeView node2 = new TreeNodeView();
		
		node2.setCode("node 2");
		node2.setDescription("Node 2 description");
		node2.setDetail("Node 2 detail");
		node2.setParentId(node1.getId());
		
		node2.setId(this.treeServiceLocal.add(node2));
		
		Assert.assertTrue(node2.getId() > 0);
		Assert.assertTrue(node2.getId() != node1.getId());
		Assert.assertTrue(node2.getId() != rootNode.getId());
		
		
		/*
		 * 5.Add a node with the id property set 
		 */
		
		TreeNodeView node3 = new TreeNodeView();
		
		node3.setCode("node 3");
		node3.setDescription("Node 3 description");
		node3.setDetail("Node 3 detail");
		node3.setParentId(node2.getParentId());
		node3.setId(3L);
		
		node3.setId(this.treeServiceLocal.add(node3));
		
		Assert.assertNotEquals(new Long(3), node3.getId());
		
		/*
		 * 6. Add a new node with a parentId that does not exist into the database
		 */
		
		TreeNodeView node4 = new TreeNodeView();
		
		node4.setCode("node 4");
		node4.setDescription("Node 4 description");
		node4.setDetail("Node 4 detail");
		node4.setParentId(-1L);
		
		
		Assert.assertEquals(new Long(-1), this.treeServiceLocal.add(node4));
	}

	/**
	 * Test method for {@link org.jefecomp.tree.service.impl.TreeServiceLocalImpl#update(org.jefecomp.tree.view.entities.TreeNodeView)}.
	 */
	@Test
	public void testUpdate() {
		
		/*
		 * 1. Update node information
		 */
		
		TreeNodeView node1 = new TreeNodeView();
		
		node1.setId(this.testDataIds.get(0));
		node1.setCode("node 1");
		node1.setDescription("Updated Node1 description");
		node1.setDetail("Updated Node1 detail");
		
		Assert.assertEquals(node1.getId(), this.treeServiceLocal.update(node1));
		
		/*
		 * 2. Update a node that is not present into the database
		 */
		
		TreeNodeView nonExistent = new TreeNodeView();
		
		nonExistent.setId(-5L);
		nonExistent.setCode("node ghost");
		nonExistent.setDescription("Updated Ghost description");
		nonExistent.setDetail("Updated Ghost detail");
		
		Assert.assertEquals(new Long(-1), this.treeServiceLocal.update(nonExistent));
		
		/*
		 * 3. Update a root node (i.e. node which parentId=NULL) with a non-existent parent
		 */
		
		node1.setParentId(-5L);
		
		Assert.assertEquals(new Long(-2), this.treeServiceLocal.update(node1));
		
		/*
		 * 4. Update a node with a non-existent parent
		 */
		
		TreeNodeView node2 = new TreeNodeView();
		
		node2.setCode("node 2");
		node2.setDescription("Node 2 description");
		node2.setDetail("Node 2 detail");
		node2.setParentId(-5L);
		
		node2.setId(this.testDataIds.get(1));
		
		Assert.assertEquals(new Long(-3), this.treeServiceLocal.update(node2));
		
		/*
		 * 5. Update parent of node 2 to another node
		 */
		
		
		node2.setParentId(this.testDataIds.get(3));
		
		Assert.assertEquals(node2.getId(), this.treeServiceLocal.update(node2));
		
		
		/*
		 * 6. Change the parentId of a given node to the id of one of its children.
		 */
		
		TreeNodeView node3 = new TreeNodeView();
		
		node3.setCode("node 3");
		node3.setDescription("Node 3 description");
		node3.setDetail("Node 3 detail");
		node3.setParentId(this.testDataIds.get(3));
		
		node3.setId(this.testDataIds.get(2));
		
		Assert.assertEquals(node3.getId(), this.treeServiceLocal.update(node3));
	}

	/**
	 * Test method for {@link org.jefecomp.tree.service.impl.TreeServiceLocalImpl#remove(java.lang.Long, boolean)}.
	 */
	@Test
	public void testRemove() {
		
		/*
		 * 1. Try to Remove node with children
		 */
		
		Assert.assertEquals(new Long(-2), this.treeServiceLocal.remove(this.testDataIds.get(2), false));
		
		
		/*
		 * 2. Remove Leaf node
		 */
		
		Assert.assertEquals(this.testDataIds.get(4), this.treeServiceLocal.remove(this.testDataIds.get(4), false));
		
		/*
		 * 3. Remove node with children
		 */
		
		Assert.assertEquals(this.testDataIds.get(2), this.treeServiceLocal.remove(this.testDataIds.get(2), true));
		
		/*
		 * 4. Remove a non-existent node within the database
		 */
		
		Assert.assertEquals(new Long(-1), this.treeServiceLocal.remove(-5L, false));
		Assert.assertEquals(new Long(-1), this.treeServiceLocal.remove(-5L, true));
		
	}
}
