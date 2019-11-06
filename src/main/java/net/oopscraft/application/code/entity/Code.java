package net.oopscraft.application.code.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_CD_INFO")
@EntityListeners(SystemEntityListener.class)
public class Code extends SystemEntity {

	@Id
	@Column(name = "CD_ID")
	String id;
	
	@Column(name = "CD_NAME")
	String name;

	@Column(name = "CD_DESC")
	String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "codeId", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("displaySeq")
	List<CodeItem> items = new ArrayList<CodeItem>();
	
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
