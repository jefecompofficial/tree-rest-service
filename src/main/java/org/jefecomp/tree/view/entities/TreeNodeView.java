/**
 * 
 */
package org.jefecomp.tree.view.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author jefecomp
 *
 */
public class TreeNodeView {
	
	private Long id;
	
	private String code;
	
	private String description;
	
	private Long parentId;
	
	private String detail;
	
	private Boolean hasChildren;
	
	private List<TreeNodeView> children;

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@JsonInclude(Include.NON_NULL)
	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@JsonInclude(Include.NON_NULL)
	public List<TreeNodeView> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNodeView> children) {
		this.children = children;
	}
}
