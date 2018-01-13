package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	String id = null;
	String upperId = null;
	String name = null;
	String description = null;
	List<Group> childGroupList = new ArrayList<Group>();
	
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
	
	public void addChildGroup(Group group) {
		this.childGroupList.add(group);
	}
	
	public List<Group> getChildGroupList() {
		return this.childGroupList;
	}

}
