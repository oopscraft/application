package net.oopscraft.application.code.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_CODE_INFO")
public class Code extends SystemEntity {

	@Id
	@Column(name = "CODE_ID", length = 32)
	String id;
	
	@Column(name = "CODE_NAME", length = 1024)
	String name;

	@Column(name = "CODE_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "codeId", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("sequence")
	List<CodeItem> items = new ArrayList<CodeItem>();
	
	public Code() {}
	
	public Code(String id) {
		this.id = id;
	}
	
	/**
	 * Gets Code Item by id
	 * @param id
	 * @return
	 */
	public CodeItem getItem(String id) {
		for(CodeItem item : this.items) {
			if(item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CodeItem> getItems() {
		return items;
	}

	public void setItems(List<CodeItem> items) {
		this.items = items;
	}

}
