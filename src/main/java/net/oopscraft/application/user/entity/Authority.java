package net.oopscraft.application.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_AUTH_INFO")
@Inheritance(
    strategy = InheritanceType.JOINED
)
@EntityListeners(SystemEntityListener.class)
public class Authority extends SystemEntity {

	@Id
	@Column(name = "AUTH_ID", length=32)
	String id;

	@Column(name = "AUTH_NAME")
	String name;
	
	@Column(name = "AUTH_ICON", length=4000)
	String icon;

	@Column(name = "AUTH_DESC", length=4000)
	String description;

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

}
