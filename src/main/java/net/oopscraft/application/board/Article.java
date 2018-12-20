package net.oopscraft.application.board;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ATCL_INFO")
public class Article {
	
	@Id
	@Column(name = "ATCL_NO")
	long no;
	
	@Column(name = "ATCL_TITL")
	String title;
	
	@Column(name = "ATCL_USER_ID")
	String userId;
	
	@Column(name = "ATCL_RGST_DTTM")
	Date registDate;
	
	@Column(name = "ATCL_MDFY_DTTM")
	Date modifyDate;

}
