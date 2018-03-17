SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS APP_CODE_ITEM_CD;
DROP TABLE IF EXISTS CORE_TAB_CODE_REL;
DROP TABLE IF EXISTS APP_CODE_INF;
DROP TABLE IF EXISTS APP_GRP_ROLE_REL;
DROP TABLE IF EXISTS app_user_group_map;
DROP TABLE IF EXISTS app_group_info;
DROP TABLE IF EXISTS APP_MENU_ROLE_REL;
DROP TABLE IF EXISTS APP_MENU_INF;
DROP TABLE IF EXISTS APP_ROLE_PRVL_REL;
DROP TABLE IF EXISTS APP_PRVL;
DROP TABLE IF EXISTS APP_USR_ROLE_REL;
DROP TABLE IF EXISTS APP_ROLE_INF;
DROP TABLE IF EXISTS app_user_prop_val;
DROP TABLE IF EXISTS app_user_info;
DROP TABLE IF EXISTS app_user_prop_cd;




/* Create Tables */

-- Application Code
CREATE TABLE APP_CODE_INF
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	CODE_NAME varchar(256) NOT NULL COMMENT 'Code Name',
	CODE_DESC text COMMENT 'Code Description',
	PRIMARY KEY (CODE_ID)
) ENGINE = InnoDB COMMENT = 'Application Code' DEFAULT CHARACTER SET utf8;


-- Application Code Item
CREATE TABLE APP_CODE_ITEM_CD
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	ITEM_CD text NOT NULL COMMENT 'Item Code',
	ITEM_NAME text COMMENT 'Item Name',
	ITEM_DESC text COMMENT 'Item Description',
	PRIMARY KEY (CODE_ID, ITEM_CD(null))
) ENGINE = InnoDB COMMENT = 'Application Code Item' DEFAULT CHARACTER SET utf8;


-- Applition Group Information
CREATE TABLE app_group_info
(
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	sys_delete_yn varchar(1) COMMENT 'System Delete or Not',
	upper_group_id varchar(32) COMMENT 'Upper Group ID',
	group_name varchar(256) COMMENT 'Group Name',
	group_desc text COMMENT 'Group Description',
	sort_seq int COMMENT 'Sort Sequence',
	PRIMARY KEY (group_id)
) ENGINE = InnoDB COMMENT = 'Applition Group Information' DEFAULT CHARACTER SET utf8;


-- Application Group Roles
CREATE TABLE APP_GRP_ROLE_REL
(
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (group_id, ROLE_ID)
) ENGINE = InnoDB COMMENT = 'Application Group Roles' DEFAULT CHARACTER SET utf8;


-- Application Menu
CREATE TABLE APP_MENU_INF
(
	menu_id varchar(0) NOT NULL COMMENT '메뉴_아이디',
	upper_menu_id varchar(0) NOT NULL COMMENT '상위_메뉴_아이디',
	menu_uri varchar(0) COMMENT '메뉴_경로',
	menu_nm varchar(0) COMMENT '메뉴_명',
	menu_description text COMMENT '메뉴_내용',
	role_need_yn text COMMENT '롤_필요_여부',
	PRIMARY KEY (menu_id)
) ENGINE = InnoDB COMMENT = 'Application Menu' DEFAULT CHARACTER SET utf8;


-- 공통_메뉴_롤_관계
CREATE TABLE APP_MENU_ROLE_REL
(
	menu_id varchar(0) NOT NULL COMMENT '메뉴_아이디',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (menu_id, ROLE_ID)
) ENGINE = InnoDB COMMENT = '공통_메뉴_롤_관계' DEFAULT CHARACTER SET utf8;


-- Application Privilege
CREATE TABLE APP_PRVL
(
	PRVL_ID varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRVL_NAME varchar(256) COMMENT 'Privilege Name',
	PRVL_DESC text COMMENT 'Privilege Description',
	PRIMARY KEY (PRVL_ID)
) ENGINE = InnoDB COMMENT = 'Application Privilege' DEFAULT CHARACTER SET utf8;


-- Application Role
CREATE TABLE APP_ROLE_INF
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	ROLE_NM varchar(256) COMMENT 'Role Name',
	ROLE_DESC text COMMENT 'Role Description',
	PRIMARY KEY (ROLE_ID)
) ENGINE = InnoDB COMMENT = 'Application Role' DEFAULT CHARACTER SET utf8;


-- Application Role Privilege Relation
CREATE TABLE APP_ROLE_PRVL_REL
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRVL_ID varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRIMARY KEY (ROLE_ID, PRVL_ID)
) ENGINE = InnoDB COMMENT = 'Application Role Privilege Relation' DEFAULT CHARACTER SET utf8;


-- Application User Group Mapping
CREATE TABLE app_user_group_map
(
	user_id varchar(32) NOT NULL COMMENT 'User ID',
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	PRIMARY KEY (user_id, group_id)
) ENGINE = InnoDB COMMENT = 'Application User Group Mapping' DEFAULT CHARACTER SET utf8;


-- Application User Information
CREATE TABLE app_user_info
(
	user_id varchar(32) NOT NULL COMMENT 'User ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	sys_delete_yn varchar(1) DEFAULT 'N' NOT NULL COMMENT 'System Delete or Not',
	user_email varchar(256) COMMENT 'User Email',
	user_mobile varchar(14) COMMENT 'User Mobile',
	user_name varchar(256) COMMENT 'User Name',
	user_nick varchar(256) COMMENT 'User Nickname',
	user_pwd varchar(256) COMMENT 'User Password',
	user_img text COMMENT 'Profile Image',
	user_msg text COMMENT 'Profile Message',
	user_desc text COMMENT 'Profile Description',
	user_stat_cd varchar(16) NOT NULL COMMENT 'User Status Code',
	user_join_dtm datetime COMMENT 'User Join Datetime',
	PRIMARY KEY (user_id)
) ENGINE = InnoDB COMMENT = 'Application User Information' DEFAULT CHARACTER SET utf8;


-- Application User Property Code
CREATE TABLE app_user_prop_cd
(
	user_prop_cd varchar(16) NOT NULL COMMENT 'User Property Code',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	sys_delete_yn varchar(1) COMMENT 'System Delete or Not',
	user_prop_name text COMMENT 'Property Name',
	user_prop_desc text COMMENT 'Property Description',
	mand_yn varchar(1) DEFAULT 'N' NOT NULL COMMENT 'Mondatory True or False',
	PRIMARY KEY (user_prop_cd)
) ENGINE = InnoDB COMMENT = 'Application User Property Code' DEFAULT CHARACTER SET utf8;


-- Application User Property Value
CREATE TABLE app_user_prop_val
(
	user_id varchar(32) NOT NULL COMMENT 'User ID',
	user_prop_cd varchar(16) NOT NULL COMMENT 'User Property Code',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Dttm',
	sys_delete_yn varchar(1) COMMENT 'System Delete or Not',
	user_prop_val text COMMENT 'User Property Value',
	PRIMARY KEY (user_id, user_prop_cd)
) ENGINE = InnoDB COMMENT = 'Application User Property Value' DEFAULT CHARACTER SET utf8;


-- Application User Roles
CREATE TABLE APP_USR_ROLE_REL
(
	user_id varchar(32) NOT NULL COMMENT 'User ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (user_id, ROLE_ID)
) ENGINE = InnoDB COMMENT = 'Application User Roles' DEFAULT CHARACTER SET utf8;


-- Core Table Code Relation
CREATE TABLE CORE_TAB_CODE_REL
(
	TAB_NAME varchar(64) NOT NULL COMMENT 'Table Name',
	COL_NAME varchar(64) NOT NULL COMMENT 'Column Name',
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	PRIMARY KEY (TAB_NAME, COL_NAME, CODE_ID)
) ENGINE = InnoDB COMMENT = 'Core Table Code Relation' DEFAULT CHARACTER SET utf8;



