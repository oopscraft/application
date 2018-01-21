SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- Application Code
CREATE TABLE APP_CODE_INF
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	CODE_NAME varchar(256) NOT NULL COMMENT 'Code Name',
	CODE_DESC text COMMENT 'Code Description',
	PRIMARY KEY (CODE_ID)
) COMMENT = 'Application Code';


-- Application Code Item
CREATE TABLE APP_CODE_ITEM_CD
(
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	ITEM_CD text NOT NULL COMMENT 'Item Code',
	ITEM_NAME text COMMENT 'Item Name',
	ITEM_DESC text COMMENT 'Item Description',
	PRIMARY KEY (CODE_ID, ITEM_CD(null))
) COMMENT = 'Application Code Item';


-- Appliation Group
CREATE TABLE APP_GRP_INF
(
	GRP_ID text NOT NULL COMMENT 'Group ID',
	UPPR_GRP_ID text COMMENT 'Upper Group ID',
	GRP_NM text COMMENT 'Group Name',
	GRP_DESC text COMMENT 'Group Description',
	PRIMARY KEY (GRP_ID(null))
) COMMENT = 'Appliation Group';


-- Application Group Roles
CREATE TABLE APP_GRP_ROLE_REL
(
	GRP_ID text NOT NULL COMMENT 'Group ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (GRP_ID(null), ROLE_ID)
) COMMENT = 'Application Group Roles';


-- Application Menu
CREATE TABLE APP_MENU_INF
(
	menu_id varchar(n) NOT NULL COMMENT '메뉴_아이디',
	upper_menu_id varchar(n) NOT NULL COMMENT '상위_메뉴_아이디',
	menu_uri varchar(n) COMMENT '메뉴_경로',
	menu_nm varchar(n) COMMENT '메뉴_명',
	menu_description text COMMENT '메뉴_내용',
	role_need_yn text COMMENT '롤_필요_여부',
	PRIMARY KEY (menu_id)
) COMMENT = 'Application Menu';


-- 공통_메뉴_롤_관계
CREATE TABLE APP_MENU_ROLE_REL
(
	menu_id varchar(n) NOT NULL COMMENT '메뉴_아이디',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (menu_id, ROLE_ID)
) COMMENT = '공통_메뉴_롤_관계';


-- Application Privilege
CREATE TABLE APP_PRVL
(
	PRVL_ID varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRVL_NAME text COMMENT 'Privilege Name',
	PRVL_DESC text COMMENT 'Privilege Description',
	PRIMARY KEY (PRVL_ID)
) COMMENT = 'Application Privilege';


-- Application Role
CREATE TABLE APP_ROLE_INF
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	ROLE_NM text COMMENT 'Role Name',
	ROLE_DESC text COMMENT 'Role Description',
	PRIMARY KEY (ROLE_ID)
) COMMENT = 'Application Role';


-- Application Role Privilege Relation
CREATE TABLE APP_ROLE_PRVL_REL
(
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRVL_ID varchar(32) NOT NULL COMMENT 'Privilege ID',
	PRIMARY KEY (ROLE_ID, PRVL_ID)
) COMMENT = 'Application Role Privilege Relation';


-- Application User Groups
CREATE TABLE APP_USR_GRP_REL
(
	USR_ID varchar(32) NOT NULL COMMENT 'User ID',
	GRP_ID text NOT NULL COMMENT 'Group ID',
	PRIMARY KEY (USR_ID, GRP_ID(null))
) COMMENT = 'Application User Groups';


-- Application User
CREATE TABLE APP_USR_INF
(
	USR_ID varchar(32) NOT NULL COMMENT 'User ID',
	USR_EMAIL varchar(256) COMMENT 'User Email',
	USR_MOB_NO varchar(14) COMMENT 'User Mobile Number',
	USR_NM varchar(256) COMMENT 'User Name',
	USR_NICK varchar(256) COMMENT 'User Nickname',
	USR_PWD varchar(256) COMMENT 'User Password',
	PRIMARY KEY (USR_ID)
) COMMENT = 'Application User';


-- Application User Property
CREATE TABLE APP_USR_PROP_CD
(
	PROP_CD text NOT NULL COMMENT 'Property ID',
	PROP_NM text COMMENT 'Property Name',
	PROP_DESC text COMMENT 'Property Description',
	PRIMARY KEY (PROP_CD(null))
) COMMENT = 'Application User Property';


-- Application User Property Value
CREATE TABLE APP_USR_PROP_VAL
(
	USR_ID varchar(32) NOT NULL COMMENT 'User ID',
	PROP_CD text NOT NULL COMMENT 'Property ID',
	PROP_VAL text COMMENT 'Property Value',
	PRIMARY KEY (USR_ID, PROP_CD(null))
) COMMENT = 'Application User Property Value';


-- Application User Roles
CREATE TABLE APP_USR_ROLE_REL
(
	USR_ID varchar(32) NOT NULL COMMENT 'User ID',
	ROLE_ID varchar(32) NOT NULL COMMENT 'Role ID',
	PRIMARY KEY (USR_ID, ROLE_ID)
) COMMENT = 'Application User Roles';


-- Core Table Code Relation
CREATE TABLE CORE_TAB_CODE_REL
(
	TAB_NAME varchar(64) NOT NULL COMMENT 'Table Name',
	COL_NAME varchar(64) NOT NULL COMMENT 'Column Name',
	CODE_ID varchar(32) NOT NULL COMMENT 'Code ID',
	PRIMARY KEY (TAB_NAME, COL_NAME, CODE_ID)
) COMMENT = 'Core Table Code Relation';



