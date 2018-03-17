package net.oopscraft.application.core.user;

public class Group {
	
	String id;
	String upperId;
	String name;
	String description;
	int sortSeq;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUpperId() {
		return upperId;
	}
	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}
	public int getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(int sortSeq) {
		this.sortSeq = sortSeq;
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
