package net.oopscraft.application.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ATCL_CNTS_INFO")
public class ArticleContents {
	
	@Id
	@Column(name = "ATCL_NO")
	long no;
	
	@Column(name = "ATCL_CNTS")
	String contents;

}
