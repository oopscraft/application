package net.oopscraft.application.property.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_PROP")
public class Property extends SystemEntity {

	@Id
	@Column(name = "PROP_ID", length = 32)
	String id;

	@Column(name = "PROP_NAME", length = 1024)
	String name;

	@Column(name = "PROP_VAL")
	@Lob
	String value;

	@Column(name = "PROP_DESC")
	@Lob
	String description;
	
	public Property() {}
	
	public Property(String id) {
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
