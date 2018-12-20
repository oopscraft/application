package net.oopscraft.application.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_CD_ITEM_INFO")
@IdClass(CodeItem.CodeItemPk.class)
public class CodeItem {
	
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
	
	@Column(name = "DISP_SEQ")
	int displaySeq;
	
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

	public int getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(int displaySeq) {
		this.displaySeq = displaySeq;
	}
	
	
	
}
