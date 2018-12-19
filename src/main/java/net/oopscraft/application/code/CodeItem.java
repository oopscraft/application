package net.oopscraft.application.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_CD_ITEM_INFO")
@IdClass(CodeItem.CodeItemPk.class)
@EntityListeners(SystemEntityListener.class)
public class CodeItem extends SystemEntity {
	
	public static class CodeItemPk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public CodeItemPk() {}
		public CodeItemPk(String codeId, String id) {
			this.codeId = codeId;
			this.id = id;
		}
		String codeId;
		String id;
	}

	@Id
	@Column(name = "CD_ID")
	String codeId;
	
	@Id
	@Column(name = "CD_ITEM_ID")
	String id;
	
	@Column(name = "CD_ITEM_NAME")
	String name;
	
	@Column(name = "CD_ITEM_DESC")
	String description;

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
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
	
}
