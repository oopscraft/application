package net.oopscraft.application.layout;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_LAYT_INFO")
public class Layout {

	@Id
	@Column(name = "LAYT_ID")
	String id;
	
	@Column(name = "LAYT_NAME")
	String name;
	
	@Column(name = "LAYT_DESC")
	String description;
	
	@Column(name = "HEAD_PAGE")
	String headerPage;
	
	@Column(name = "FOOT_PAGE")
	String footerPage;
	
	@Column(name = "DFLT_YN")
	String defaultYn;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHeaderPage() {
		return headerPage;
	}

	public void setHeaderPage(String headerPage) {
		this.headerPage = headerPage;
	}

	public String getFooterPage() {
		return footerPage;
	}

	public void setFooterPage(String footerPage) {
		this.footerPage = footerPage;
	}

	public String getDefaultYn() {
		return defaultYn;
	}

	public void setDefaultYn(String defaultYn) {
		this.defaultYn = defaultYn;
	}
	
}
