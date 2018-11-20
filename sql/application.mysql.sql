SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- Application Authority
CREATE TABLE APP_AUTH_INFO
(
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	AUTH_NAME varchar(256) COMMENT 'Authority Name',
	AUTH_DESC text COMMENT 'Authority Description',
	PRIMARY KEY (AUTH_ID)
) ENGINE = InnoDB COMMENT = 'Application Authority' DEFAULT CHARACTER SET utf8;


-- Application Code
CREATE TABLE APP_CODE_INFO
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	UPPER_CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	CODE_NAME varchar(256) NOT NULL COMMENT 'Code Name',
	CODE_DESC text COMMENT 'Code Description',
	PRIMARY KEY (CODE_ID)
) ENGINE = InnoDB COMMENT = 'Application Code' DEFAULT CHARACTER SET utf8;


-- Application Code Item
CREATE TABLE APP_CODE_ITEM_INFO
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	CODE_ITEM_ID varchar(32) NOT NULL COMMENT 'Item Code',
	CODE_ITEM_NAME text COMMENT 'Item Name',
	CODE_ITEM_DESC text COMMENT 'Item Description',
	PRIMARY KEY (CODE_ID, CODE_ITEM_ID)
) ENGINE = InnoDB COMMENT = 'Application Code Item' DEFAULT CHARACTER SET utf8;


-- Application Group Role Mapping
CREATE TABLE APP_GROUP_AUTH_MAP
(
	GROUP_ID varchar(32) NOT NULL COMMENT 'Group ID',
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	SYS_INST_USER_ID varchar(32) COMMENT 'Syste Insert User ID',
	SYS_INST_DTTM datetime COMMENT 'System Insert Datetime',
	PRIMARY KEY (GROUP_ID, AUTH_ID)
) ENGINE = InnoDB COMMENT = 'Application Group Role Mapping' DEFAULT CHARACTER SET utf8;


-- Applition Group Information
CREATE TABLE APP_GROUP_INFO
(
	GROUP_ID varchar(32) NOT NULL COMMENT 'Group ID',
	UPPER_GROUP_ID varchar(32) COMMENT 'Upper Group ID',
	GROUP_NAME varchar(256) COMMENT 'Group Name',
	GROUP_DESC text COMMENT 'Group Description',
	DISP_SEQ int COMMENT 'Display Sequence',
	PRIMARY KEY (GROUP_ID)
) ENGINE = InnoDB COMMENT = 'Applition Group Information' DEFAULT CHARACTER SET utf8;


-- Application Group Role Mapping
CREATE TABLE APP_GROUP_ROLE_MAP
(
	GROUP_ID varchar(32) NOT NULL COMMENT 'Group ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (GROUP_ID, ROLE_ID)
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
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	PRIMARY KEY (menu_id, AUTH_ID)
) ENGINE = InnoDB COMMENT = '새 테이블' DEFAULT CHARACTER SET utf8;


-- Application Role Privilege Mapping
CREATE TABLE APP_ROLE_AUTH_MAP
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	PRIMARY KEY (ROLE_ID, AUTH_ID)
) ENGINE = InnoDB COMMENT = 'Application Role Privilege Mapping' DEFAULT CHARACTER SET utf8;


-- Application Role
CREATE TABLE APP_ROLE_INFO
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	ROLE_NAME varchar(256) COMMENT 'Role Name',
	ROLE_DESC text COMMENT 'Role Description',
	PRIMARY KEY (ROLE_ID)
) ENGINE = InnoDB COMMENT = 'Application Role' DEFAULT CHARACTER SET utf8;


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
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	PRIMARY KEY (rsrc_uri, rsrc_method, AUTH_ID)
) ENGINE = InnoDB COMMENT = '새 테이블' DEFAULT CHARACTER SET utf8;


-- Core Table Code Relation
CREATE TABLE APP_TAB_COL_CODE_MAP
(
	TAB_ID varchar(32) NOT NULL COMMENT 'Table ID',
	COL_ID varchar(32) NOT NULL COMMENT 'Column ID',
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	PRIMARY KEY (TAB_ID, COL_ID, CODE_ID)
) ENGINE = InnoDB COMMENT = 'Core Table Code Relation' DEFAULT CHARACTER SET utf8;


-- Application User Role Mapping
CREATE TABLE APP_USER_AUTH_MAP
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	AUTH_ID varchar(32) NOT NULL COMMENT 'Authority ID',
	PRIMARY KEY (USER_ID, AUTH_ID)
) ENGINE = InnoDB COMMENT = 'Application User Role Mapping' DEFAULT CHARACTER SET utf8;


-- Application User Group Mapping
CREATE TABLE APP_USER_GROUP_MAP
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	GROUP_ID varchar(32) NOT NULL COMMENT 'Group ID',
	PRIMARY KEY (USER_ID, GROUP_ID)
) ENGINE = InnoDB COMMENT = 'Application User Group Mapping' DEFAULT CHARACTER SET utf8;


-- Application User Information
CREATE TABLE APP_USER_INFO
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	USER_PWD varchar(256) COMMENT 'User Password',
	USER_EMAIL varchar(256) COMMENT 'User Email',
	USER_PHONE varchar(14) COMMENT 'User Mobile',
	USER_NAME varchar(256) COMMENT 'User Name',
	USER_NICK varchar(256) COMMENT 'User Nickname',
	USER_PHOTO text COMMENT 'Profile Photo',
	USER_MSG text COMMENT 'Profile Message',
	USER_JOIN_DTTM datetime COMMENT 'User Join Datetime',
	PRIMARY KEY (USER_ID)
) ENGINE = InnoDB COMMENT = 'Application User Information' DEFAULT CHARACTER SET utf8;


-- Application User Property Code
CREATE TABLE APP_USER_PROP_INFO
(
	USER_PROP_ID varchar(32) NOT NULL COMMENT 'User Property ID',
	USER_PROP_NAME varchar(256) COMMENT 'Property Name',
	USER_PROP_DESC text COMMENT 'Property Description',
	MAND_YN varchar(1) DEFAULT 'N' NOT NULL COMMENT 'Mondatory True or False',
	PRIMARY KEY (USER_PROP_ID)
) ENGINE = InnoDB COMMENT = 'Application User Property Code' DEFAULT CHARACTER SET utf8;


-- Application User Property Value
CREATE TABLE APP_USER_PROP_VAL
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	USER_PROP_ID varchar(32) NOT NULL COMMENT 'User Property ID',
	USER_PROP_VAL text COMMENT 'User Property Value',
	PRIMARY KEY (USER_ID, USER_PROP_ID)
) ENGINE = InnoDB COMMENT = 'Application User Property Value' DEFAULT CHARACTER SET utf8;


-- Application User Role Mapping
CREATE TABLE APP_USER_ROLE_MAP
(
	USER_ID varchar(32) NOT NULL COMMENT 'User ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (USER_ID, ROLE_ID)
) ENGINE = InnoDB COMMENT = 'Application User Role Mapping' DEFAULT CHARACTER SET utf8;



