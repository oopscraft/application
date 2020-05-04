package net.oopscraft.application.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "APP_MESG_LANG_INFO")
@IdClass(MessageLanguage.Pk.class)
public class MessageLanguage {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = -8409789656417268895L;
		String messageId;
		String id;
		public Pk() {}
		public Pk(String messageId, String id) {
			this.messageId = messageId;
			this.id = id;
		}
		public String getMessageId() {
			return messageId;
		}
		public void setMessageId(String messageId) {
			this.messageId = messageId;
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
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
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
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (messageId == null) {
				if (other.messageId != null)
					return false;
			} else if (!messageId.equals(other.messageId))
				return false;
			return true;
		}
	}

	@Id
	@Column(name = "MESG_ID", length = 32)
	String messageId;

	@Id
	@Column(name = "LANG_ID", length = 32)
	String id;
	
	@Column(name = "LANG_VAL", length = Integer.MAX_VALUE)
	@Lob
	String value;
	
	public MessageLanguage() {}
	
	public MessageLanguage(String messageId, String id) {
		this.messageId = messageId;
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
