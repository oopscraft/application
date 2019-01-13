package net.oopscraft.application.page;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_PAGE_INFO")
public class Page {

	@Id
	@Column(name = "PAGE_ID")
	String id;
	
	@Column(name = "PAGE_NAME")
	String name;
	
	public enum Type { HTML, JSP, URL }
	
	@Column(name = "PAGE_TYPE")
	Type type;
	
	@Column(name = "PAGE_VAL")
	String value;
	
	@Column(name = "LAYT_ID")
	String layoutId;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	
}
