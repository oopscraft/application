package net.oopscraft.application.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_CD_ITEM_INFO")
@IdClass(Item.Pk.class)
public class Item {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String codeId, String id) {
			this.codeId = codeId;
			this.id = id;
		}
		String codeId;
		String id;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk codeItemPk = (Pk)obj;
				if(this.getCodeId().equals(codeItemPk.getCodeId())
				&& this.getId().equals(codeItemPk.getId())
				) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return (codeId + id).hashCode();
		}
		
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
