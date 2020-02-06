package net.oopscraft.application.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_MSGE_INFO")
@EntityListeners(SystemEntityListener.class)
public class Message extends SystemEntity {

	@Id
	@Column(name = "MSGE_ID", length = 32)
	String id;

	@Column(name = "MSGE_NAME", length = 1024)
	String name;

	@Column(name = "MSGE_VAL")
	@Lob
	String value;

	@Column(name = "MSGE_DESC")
	@Lob
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
