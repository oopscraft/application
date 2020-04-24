package net.oopscraft.application.code.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "APP_CODE_ITEM_INFO")
@IdClass(CodeItem.Pk.class)
public class CodeItem {
	
	/**
	 * ClassItem.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String codeId, String id) {
			this.codeId = codeId;
			this.id = id;
		}
		String codeId;
		String id;
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
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((codeId == null) ? 0 : codeId.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pk other = (Pk) obj;
			if (codeId == null) {
				if (other.codeId != null)
					return false;
			} else if (!codeId.equals(other.codeId))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		
	}

	@Id
	@Column(name = "CODE_ID", length = 32)
	String codeId;
	
	@Id
	@Column(name = "ITEM_ID", length = 32)
	String id;
	
	@Column(name = "ITEM_SEQ")
	int sequence;
	
	@Column(name = "ITEM_NAME", length = 1024)
	String name;
	
	@Column(name = "ITEM_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	public CodeItem() {}
	
	public CodeItem(Pk pk) {
		this.codeId = pk.getCodeId();
		this.id = pk.getId();
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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
