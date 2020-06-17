package net.oopscraft.application.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(
	name = "APP_MESG_INFO",
	indexes = {
		@Index(columnList="SYS_INST_DATE"),
		@Index(columnList="MESG_NAME")
	}
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message extends SystemEntity {
	
	@Id
	@Column(name = "MESG_ID", length = 32)
	@NotNull
	String id;

	@Column(name = "MESG_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "MESG_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@Column(name = "MESG_VAL", length = Integer.MAX_VALUE)
	@Lob
	String value;
	
	@JsonIgnore
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "id", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("language")
	List<MessageI18n> i18ns = new ArrayList<MessageI18n>();
	
	/**
	 * Message
	 */
	public Message() {}
	
	/**
	 * Message
	 * @param id
	 */
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * getValue
	 * @param language
	 * @return
	 */
	public String getValue(String language) {
		for(MessageI18n i18n : i18ns) {
			if(i18n.getLanguage().contentEquals(language)) {
				return i18n.getValue();
			}
		}
		return this.getValue();
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public List<MessageI18n> getI18ns() {
		return i18ns;
	}

	public void setI18ns(List<MessageI18n> i18ns) {
		this.i18ns = i18ns;
	}

}
