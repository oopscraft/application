package net.oopscraft.application.property;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_PROP_INFO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Property extends SystemEntity {

	@Id
	@Column(name = "PROP_ID", length = 32)
	@NotNull
	String id;
	
	@Column(name = "PROP_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "PROP_VAL", length = Integer.MAX_VALUE)
	@Lob
	String value;

	@Column(name = "PROP_DESC", length = Integer.MAX_VALUE)
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
