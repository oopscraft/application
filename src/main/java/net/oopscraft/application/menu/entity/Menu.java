package net.oopscraft.application.menu.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;
import net.oopscraft.application.user.entity.Authority;

@Entity
@Table(name = "APP_MENU_INFO")
@EntityListeners(SystemEntityListener.class)
public class Menu extends SystemEntity {
	
	public enum Type {
		NONE, LINK, INCLUDE_JSP, INCLUDE_URL
	}
	
	public enum Policy {
		ANONYMOUS, 	AUTHENTICATED,	AUTHORIZED
	}

	@Id
	@Column(name = "MENU_ID", length = 32)
	String id;

	@Column(name = "UPER_MENU_ID", length = 32)
	String upperId;
	
	@Column(name = "MENU_SEQ")
	int sequence;

	@Column(name = "MENU_NAME", length = 1024)
	String name;
	
	@Column(name = "MENU_ICON")
	@Lob
	String icon;

	@Column(name = "MENU_DESC")
	@Lob
	String description;
	
	@Column(name = "MENU_TYPE")
	@Enumerated(EnumType.STRING)
	Type type = Type.NONE;
	
	@Column(name = "MENU_VAL")
	String value;

	@Column(name = "PLCY_DISP")
	@Enumerated(EnumType.STRING)
	Policy displayPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_MENU_AUTH_DISP_MAP", 
		joinColumns = @JoinColumn(name = "MENU_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> displayAuthorities = new ArrayList<Authority>();

	@Column(name = "PLCY_ACES")
	@Enumerated(EnumType.STRING)
	Policy accessPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_MENU_AUTH_ACES_MAP", 
		joinColumns = @JoinColumn(name = "MENU_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> accessAuthorities =  new ArrayList<Authority>();

	public Menu() {}
	
	public Menu(String id) {
		this.id = id;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public Policy getDisplayPolicy() {
		return displayPolicy;
	}

	public void setDisplayPolicy(Policy displayPolicy) {
		this.displayPolicy = displayPolicy;
	}

	public List<Authority> getDisplayAuthorities() {
		return displayAuthorities;
	}

	public void setDisplayAuthorities(List<Authority> displayAuthorities) {
		this.displayAuthorities = displayAuthorities;
	}

	public Policy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(Policy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public List<Authority> getAccessAuthorities() {
		return accessAuthorities;
	}

	public void setAccessAuthorities(List<Authority> accessAuthorities) {
		this.accessAuthorities = accessAuthorities;
	}

}
