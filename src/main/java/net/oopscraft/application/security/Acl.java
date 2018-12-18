package net.oopscraft.application.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;
import net.oopscraft.application.security.Acl.AclId;

@Entity
@Table(name = "APP_ACL_INFO")
@IdClass(AclId.class)
@EntityListeners(SystemEntityListener.class)
public class Acl extends SystemEntity {
	
	public static class AclId implements Serializable {
		private static final long serialVersionUID = -6294635257950104787L;
		public AclId() {}
		public AclId(String uri, String method) {
			this.uri = uri;
			this.method = method;
		}
		String uri;
		String method;
	}

	@Id
	@Column(name = "ACL_URI")
	String uri;
	
	@Id
	@Column(name = "ACL_MTHD")
	String method;
	
	@Column(name = "ACL_NAME")
	String name;
	
	@Column(name = "ACL_DESC")
	String description;
	
	enum AccessPolicy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}
	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	AccessPolicy accessPolicy = AccessPolicy.ANONYMOUS;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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

	public AccessPolicy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(AccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	
}
