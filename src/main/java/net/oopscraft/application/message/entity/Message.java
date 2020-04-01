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
@Table(name = "APP_MESG_INFO")
@EntityListeners(SystemEntityListener.class)
public class Message extends SystemEntity {

	@Id
	@Column(name = "MESG_ID", length = 32)
	String id;

	@Column(name = "MESG_NAME", length = 1024)
	String name;

	@Column(name = "MESG_VAL", length = Integer.MAX_VALUE)
	@Lob
	String value;

	@Column(name = "MESG_DESC", length = Integer.MAX_VALUE)
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
