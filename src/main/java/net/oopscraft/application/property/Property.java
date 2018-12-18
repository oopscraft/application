package net.oopscraft.application.property;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_PROP_INFO")
@EntityListeners(SystemEntityListener.class)
public class Property extends SystemEntity {

	@Id
	@Column(name = "PROP_ID")
	String id;

	@Column(name = "PROP_NAME")
	String name;

	@Column(name = "PROP_VAL")
	String value;

	@Column(name = "PROP_DESC")
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
