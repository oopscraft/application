package net.oopscraft.application.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_MESG_INFO")
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
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "id", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("language")
	List<MessageDetail> details = new ArrayList<MessageDetail>();
	
	/**
	 * getItem
	 * @param language
	 * @return
	 */
	public MessageDetail getDetail(String language) {
		for(MessageDetail detail : details) {
			if(language.contentEquals(detail.getLanguage())){
				return detail;
			}
		}
		return null;
	}
	
	/**
	 * getValue
	 * @param languageId
	 * @return
	 */
	public String getValue(String language) {
		MessageDetail detail = getDetail(language);
		if(detail != null) {
			return detail.getValue();
		}else {
			return null;
		}
	}
	
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

	public List<MessageDetail> getDetails() {
		return details;
	}

	public void setDetails(List<MessageDetail> details) {
		this.details = details;
	}

}
