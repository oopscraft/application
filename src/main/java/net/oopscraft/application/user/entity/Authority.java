package net.oopscraft.application.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_AUTH")
@EntityListeners(SystemEntityListener.class)
public class Authority extends SystemEntity {

	@Id
	@Column(name = "AUTH_ID", length=32)
	String id;

	@Column(name = "AUTH_NAME", length = 1024)
	String name;
	
	@Column(name = "AUTH_ICON", length = Integer.MAX_VALUE)
	@Lob
	String icon;

	@Column(name = "AUTH_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@Transient
	String holder = null;
	
	public Authority() {}
	
	public Authority(String id) {
		this.id = id;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

}
