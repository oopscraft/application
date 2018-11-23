package net.oopscraft.application.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_AUTH_INFO")
public class Authority {

	@Id
	@Column(name = "AUTH_ID")
	String id;

	@Column(name = "AUTH_NAME")
	String name;

	@Column(name = "AUTH_DESC")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
