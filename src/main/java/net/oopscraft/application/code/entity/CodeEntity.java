package net.oopscraft.application.code.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_CODE_ENTY_INFO")
@IdClass(CodeEntity.Pk.class)
public class CodeEntity {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 7113981316550030275L;
		String codeId;
		String tableName;
		String columnName;
		public Pk() {}
		public Pk(String codeId, String tableName, String columnName) {
			this.tableName = tableName;
			this.columnName = columnName;
		}
		public String getCodeId() {
			return codeId;
		}
		public void setCodeId(String codeId) {
			this.codeId = codeId;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((codeId == null) ? 0 : codeId.hashCode());
			result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
			result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
			if (columnName == null) {
				if (other.columnName != null)
					return false;
			} else if (!columnName.equals(other.columnName))
				return false;
			if (tableName == null) {
				if (other.tableName != null)
					return false;
			} else if (!tableName.equals(other.tableName))
				return false;
			return true;
		}
	}
	
	@Id
	@Column(name = "CODE_ID", length = 32)
	String codeId;
	
	@Id
	@Column(name = "TABL_NAME", length = 255)
	String tableName;
	
	@Id
	@Column(name = "COLM_NAME", length = 255)
	String columnName;
	
	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
