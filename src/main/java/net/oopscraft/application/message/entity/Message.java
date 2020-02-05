package net.oopscraft.application.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_MASG_INFO")
@EntityListeners(SystemEntityListener.class)
public class Message extends SystemEntity {

	@Id
	@Column(name = "MASG_ID", length = 32)
	String id;

	@Column(name = "MASG_NAME", length = 1024)
	String name;

	@Column(name = "MASG_VAL", length = Integer.MAX_VALUE)
	String value;

	@Column(name = "MASG_DESC", length = Integer.MAX_VALUE)
	String description;
	
	public Message() {}
	
	public Message(String id) {
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
