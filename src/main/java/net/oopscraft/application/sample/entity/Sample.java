package net.oopscraft.application.sample.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import net.oopscraft.application.core.jpa.BooleanStringConverter;
import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_SAMP_INFO")
@IdClass(Sample.Pk.class)
public class Sample extends SystemEntity {
	
	/**
	 * Sample.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 4668916039881626205L;
		private String key1;
		private String key2;
		public Pk() {}
		public Pk(String key1, String key2) {
			this.key1 = key1;
			this.key2 = key2;
		}
		public String getKey1() {
			return key1;
		}
		public void setKey1(String key1) {
			this.key1 = key1;
		}
		public String getKey2() {
			return key2;
		}
		public void setKey2(String key2) {
			this.key2 = key2;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
			result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
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
			if (key1 == null) {
				if (other.key1 != null)
					return false;
			} else if (!key1.equals(other.key1))
				return false;
			if (key2 == null) {
				if (other.key2 != null)
					return false;
			} else if (!key2.equals(other.key2))
				return false;
			return true;
		}
	}
	
	@Id
	@Column(name = "KEY_1", length = 32)
	String key1;
	
	@Id
	@Column(name = "KEY_2", length = 32)
	String key2;
	
	@Column(name = "VAL_CHAR", length = 4000)
	String valueChar;
	
	@Column(name = "VAL_CLOB", length = 4000)
	@Lob
	String valueClob;
	
	@Column(name = "VAL_INT")
	int valueInt;
	
	@Column(name = "VAL_LONG")
	long valueLong;
	
	@Column(name = "VAL_YN", length = 1)
	@Convert(converter=BooleanStringConverter.class)
	boolean valueYn;
	
	enum Code { CODE_1, CODE_2, CODE_3 }
	@Column(name = "VAL_ENUM")
	@Enumerated(EnumType.STRING)
	Code valueEnum;
	
	@Formula("(SELECT COUNT(*) FROM APP_SAMP_INFO)")
	long valueSubQuery = 0;

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getValueChar() {
		return valueChar;
	}

	public void setValueChar(String valueChar) {
		this.valueChar = valueChar;
	}

	public String getValueClob() {
		return valueClob;
	}

	public void setValueClob(String valueClob) {
		this.valueClob = valueClob;
	}

	public int getValueInt() {
		return valueInt;
	}

	public void setValueInt(int valueInt) {
		this.valueInt = valueInt;
	}

	public long getValueLong() {
		return valueLong;
	}

	public void setValueLong(long valueLong) {
		this.valueLong = valueLong;
	}

	public boolean isValueYn() {
		return valueYn;
	}

	public void setValueYn(boolean valueYn) {
		this.valueYn = valueYn;
	}

	public Code getValueEnum() {
		return valueEnum;
	}

	public void setValueEnum(Code valueEnum) {
		this.valueEnum = valueEnum;
	}

	public long getValueSubQuery() {
		return valueSubQuery;
	}

	public void setValueSubQuery(long valueSubQuery) {
		this.valueSubQuery = valueSubQuery;
	}

}
