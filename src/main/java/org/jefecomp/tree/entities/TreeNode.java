/**
 * 
 */
package org.jefecomp.tree.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author jefecomp
 *
 */
@Entity
@NamedQueries({
	
	@NamedQuery(name="TreeNode.getTreeStructure", query="Select node from TreeNode node where node.parent is NULL"),
	@NamedQuery(name="TreeNode.DeleteALL", query="Delete from TreeNode")
	
})
public class TreeNode {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String code;
	
	private String description;
	
	@ManyToOne(cascade=CascadeType.MERGE, optional=true)
	private TreeNode parent;
	
	private String detail;
	
	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy="parent")
	private List<TreeNode> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
}