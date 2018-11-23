package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "APP_GROUP_INFO")
public class Group {

	@Id
	@Column(name = "GROUP_ID")
	String id;

	@Column(name = "UPPER_GROUP_ID")
	String upperId;

	@Column(name = "GROUP_NAME")
	String name;

	@Column(name = "GROUP_DESC")
	String description;

	@Column(name = "DISP_SEQ")
	Integer displaySeq;

	@Transient
	List<Group> childGroups = new ArrayList<Group>();

	public void setChildGroups(List<Group> childGroups) {
		this.childGroups = childGroups;
	}

	public void addChildGroup(Group group) {
		childGroups.add(group);
	}

	public List<Group> getChildGroups() {
		return childGroups;
	}

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

	public Integer getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
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
