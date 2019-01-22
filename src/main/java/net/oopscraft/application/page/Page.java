package net.oopscraft.application.page;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.WhereJoinTable;

import net.oopscraft.application.menu.Menu.Policy;
import net.oopscraft.application.user.Authority;

@Entity
@Table(name = "APP_PAGE_INFO")
public class Page {

	@Id
	@Column(name = "PAGE_ID")
	String id;
	
	@Column(name = "PAGE_NAME")
	String name;
	
	public enum Type { JSP, URL }

	@Column(name = "PAGE_TYPE")
	@Enumerated(EnumType.STRING)
	Type type;
	
	@Column(name = "PAGE_VAL")
	String value;
	
	public enum Policy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}

	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	Policy accessPolicy = Policy.ANONYMOUS;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_PAGE_PLCY_AUTH_MAP", joinColumns = @JoinColumn(name = "PAGE_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "PLCY_TYPE ='ACES_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_PAGE_PLCY_AUTH_MAP (PAGE_ID, PLCY_TYPE, AUTH_ID) VALUES (?, 'ACES_PLCY', ?)") 
	List<Authority> accessAuthorities;

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
