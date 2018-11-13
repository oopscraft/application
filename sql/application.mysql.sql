SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS app_code_item_info;
DROP TABLE IF EXISTS app_table_code_map;
DROP TABLE IF EXISTS app_code_info;
DROP TABLE IF EXISTS app_group_priv_map;
DROP TABLE IF EXISTS app_group_role_map;
DROP TABLE IF EXISTS app_user_group_map;
DROP TABLE IF EXISTS app_group_info;
DROP TABLE IF EXISTS app_menu_priv_map;
DROP TABLE IF EXISTS app_menu_info;
DROP TABLE IF EXISTS app_role_priv_map;
DROP TABLE IF EXISTS app_rsrc_priv_map;
DROP TABLE IF EXISTS app_user_priv_map;
DROP TABLE IF EXISTS app_priv_info;
DROP TABLE IF EXISTS app_user_role_map;
DROP TABLE IF EXISTS app_role_info;
DROP TABLE IF EXISTS app_rsrc_info;
DROP TABLE IF EXISTS app_user_prop_val;
DROP TABLE IF EXISTS APP_USER_INFO;
DROP TABLE IF EXISTS app_user_prop_info;




/* Create Tables */

-- Application Code
CREATE TABLE app_code_info
(
	code_id varchar(32) NOT NULL COMMENT 'Code ID',
	code_name varchar(256) NOT NULL COMMENT 'Code Name',
	code_desc text COMMENT 'Code Description',
	PRIMARY KEY (code_id)
) ENGINE = InnoDB COMMENT = 'Application Code' DEFAULT CHARACTER SET utf8;


-- Application Code Item
CREATE TABLE app_code_item_info
(
	code_id varchar(32) NOT NULL COMMENT 'Code ID',
	code_item_id varchar(32) NOT NULL COMMENT 'Item Code',
	code_item_name text COMMENT 'Item Name',
	code_item_desc text COMMENT 'Item Description',
	PRIMARY KEY (code_id, code_item_id)
) ENGINE = InnoDB COMMENT = 'Application Code Item' DEFAULT CHARACTER SET utf8;


-- Applition Group Information
CREATE TABLE app_group_info
(
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	upper_group_id varchar(32) COMMENT 'Upper Group ID',
	group_name varchar(256) COMMENT 'Group Name',
	group_desc text COMMENT 'Group Description',
	sort_seq int COMMENT 'Sort Sequence',
	PRIMARY KEY (group_id)
) ENGINE = InnoDB COMMENT = 'Applition Group Information' DEFAULT CHARACTER SET utf8;


-- Application Group Role Mapping
CREATE TABLE app_group_priv_map
(
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	PRIMARY KEY (group_id, priv_id)
) ENGINE = InnoDB COMMENT = 'Application Group Role Mapping' DEFAULT CHARACTER SET utf8;


-- Application Group Role Mapping
CREATE TABLE app_group_role_map
(
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	role_id varchar(32) NOT NULL COMMENT 'Privileges ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	PRIMARY KEY (group_id, role_id)
) ENGINE = InnoDB COMMENT = 'Application Group Role Mapping' DEFAULT CHARACTER SET utf8;


-- Application Menu
CREATE TABLE app_menu_info
(
	menu_id varchar(32) NOT NULL COMMENT '메뉴_아이디',
	upper_menu_id varchar(32) NOT NULL COMMENT '상위_메뉴_아이디',
	menu_name varchar(256) COMMENT 'Menu Name',
	menu_desc text COMMENT 'Menu Description',
	rsrc_uri varchar(256) NOT NULL COMMENT '메뉴_경로',
	rsrc_method varchar(16) NOT NULL COMMENT 'Resource Method',
	PRIMARY KEY (menu_id)
) ENGINE = InnoDB COMMENT = 'Application Menu' DEFAULT CHARACTER SET utf8;


-- 새 테이블
CREATE TABLE app_menu_priv_map
(
	menu_id varchar(32) NOT NULL COMMENT '메뉴_아이디',
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRIMARY KEY (menu_id, priv_id)
) ENGINE = InnoDB COMMENT = '새 테이블' DEFAULT CHARACTER SET utf8;


-- Application Privilege
CREATE TABLE app_priv_info
(
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	sys_delete_yn varchar(1) COMMENT 'System Delete Or Not',
	priv_name varchar(256) COMMENT 'Privilege Name',
	priv_desc text COMMENT 'Privilege Description',
	PRIMARY KEY (priv_id)
) ENGINE = InnoDB COMMENT = 'Application Privilege' DEFAULT CHARACTER SET utf8;


-- Application Role
CREATE TABLE app_role_info
(
	role_id varchar(32) NOT NULL COMMENT 'Privileges ID',
	role_name varchar(256) COMMENT 'Role Name',
	role_desc text COMMENT 'Role Description',
	PRIMARY KEY (role_id)
) ENGINE = InnoDB COMMENT = 'Application Role' DEFAULT CHARACTER SET utf8;


-- Application Role Privilege Mapping
CREATE TABLE app_role_priv_map
(
	role_id varchar(32) NOT NULL COMMENT 'Privileges ID',
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRIMARY KEY (role_id, priv_id)
) ENGINE = InnoDB COMMENT = 'Application Role Privilege Mapping' DEFAULT CHARACTER SET utf8;


-- Application Menu
CREATE TABLE app_rsrc_info
(
	rsrc_uri varchar(256) NOT NULL COMMENT '메뉴_경로',
	rsrc_method varchar(16) NOT NULL COMMENT 'Resource Method',
	rsrc_name varchar(0) COMMENT '메뉴_명',
	rsrc_desc text COMMENT '메뉴_내용',
	PRIMARY KEY (rsrc_uri, rsrc_method)
) ENGINE = InnoDB COMMENT = 'Application Menu' DEFAULT CHARACTER SET utf8;


-- 새 테이블
CREATE TABLE app_rsrc_priv_map
(
	rsrc_uri varchar(256) NOT NULL COMMENT '메뉴_경로',
	rsrc_method varchar(16) NOT NULL COMMENT 'Resource Method',
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRIMARY KEY (rsrc_uri, rsrc_method, priv_id)
) ENGINE = InnoDB COMMENT = '새 테이블' DEFAULT CHARACTER SET utf8;


-- Core Table Code Relation
CREATE TABLE app_table_code_map
(
	table_id varchar(32) NOT NULL COMMENT 'Table Name',
	column_id varchar(32) NOT NULL COMMENT 'Column Name',
	code_id varchar(32) NOT NULL COMMENT 'Code ID',
	PRIMARY KEY (table_id, column_id, code_id)
) ENGINE = InnoDB COMMENT = 'Core Table Code Relation' DEFAULT CHARACTER SET utf8;


-- Application User Group Mapping
CREATE TABLE app_user_group_map
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	group_id varchar(32) NOT NULL COMMENT 'Group ID',
	sys_insert_dtm datetime COMMENT 'System Inert Datetime',
	PRIMARY KEY (USER_ID, group_id)
) ENGINE = InnoDB COMMENT = 'Application User Group Mapping' DEFAULT CHARACTER SET utf8;


-- Application User Information
CREATE TABLE APP_USER_INFO
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	SYS_INST_DTM datetime COMMENT 'System Insert Datetime',
	SYS_INST_USER_ID varchar(32) COMMENT 'System Insert User ID',
	SYS_UPDT_DTM datetime COMMENT 'System Update Datetime',
	SYS_UPDT_USER_ID varchar(32) COMMENT 'System Update User ID',
	SYS_DELT_YN varchar(1) DEFAULT 'N' NOT NULL COMMENT 'System Delete or Not',
	USER_EMAL varchar(256) COMMENT 'User Email',
	USER_MOBL varchar(14) COMMENT 'User Mobile',
	USER_NAME varchar(256) COMMENT 'User Name',
	USER_NICK varchar(256) COMMENT 'User Nickname',
	USER_PWD varchar(256) COMMENT 'User Password',
	USER_IMG text COMMENT 'Profile Image',
	USER_MSG text COMMENT 'Profile Message',
	USER_DESC text COMMENT 'Profile Description',
	USER_STAT_CD varchar(16) COMMENT 'User Status Code',
	USER_JOIN_DTM datetime COMMENT 'User Join Datetime',
	PRIMARY KEY (USER_ID)
) ENGINE = InnoDB COMMENT = 'Application User Information' DEFAULT CHARACTER SET utf8;


-- Application User Role Mapping
CREATE TABLE app_user_priv_map
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	priv_id varchar(32) NOT NULL COMMENT 'Privilege ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	PRIMARY KEY (USER_ID, priv_id)
) ENGINE = InnoDB COMMENT = 'Application User Role Mapping' DEFAULT CHARACTER SET utf8;


-- Application User Property Code
CREATE TABLE app_user_prop_info
(
	user_prop_id varchar(16) NOT NULL COMMENT 'User Property ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Datetime',
	sys_delete_yn varchar(1) COMMENT 'System Delete or Not',
	user_prop_name text COMMENT 'Property Name',
	user_prop_desc text COMMENT 'Property Description',
	mand_yn varchar(1) DEFAULT 'N' NOT NULL COMMENT 'Mondatory True or False',
	PRIMARY KEY (user_prop_id)
) ENGINE = InnoDB COMMENT = 'Application User Property Code' DEFAULT CHARACTER SET utf8;


-- Application User Property Value
CREATE TABLE app_user_prop_val
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	user_prop_id varchar(16) NOT NULL COMMENT 'User Property ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	sys_update_dtm datetime COMMENT 'System Update Dttm',
	sys_delete_yn varchar(1) COMMENT 'System Delete or Not',
	user_prop_val text COMMENT 'User Property Value',
	PRIMARY KEY (USER_ID, user_prop_id)
) ENGINE = InnoDB COMMENT = 'Application User Property Value' DEFAULT CHARACTER SET utf8;


-- Application User Role Mapping
CREATE TABLE app_user_role_map
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	role_id varchar(32) NOT NULL COMMENT 'Privileges ID',
	sys_insert_dtm datetime COMMENT 'System Insert Datetime',
	PRIMARY KEY (USER_ID, role_id)
) ENGINE = InnoDB COMMENT = 'Application User Role Mapping' DEFAULT CHARACTER SET utf8;



