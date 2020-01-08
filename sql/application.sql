

/* Create Tables */

-- App Article File Info
CREATE TABLE APP_ATCL_FILE_INFO
(
	-- Article ID
	ATCL_ID varchar(32) NOT NULL,
	-- File ID
	FILE_ID varchar(32) NOT NULL,
	-- File Name
	FILE_NAME varchar(256),
	-- File Type
	FILE_TYPE varchar(128),
	-- File Size
	FILE_SIZE numeric,
	PRIMARY KEY (ATCL_ID, FILE_ID)
);


-- App Board Article Info
CREATE TABLE APP_ATCL_INFO
(
	-- Article ID
	ATCL_ID varchar(32) NOT NULL,
	-- Board ID
	BORD_ID varchar(32),
	-- Category ID
	CATE_ID varchar(32),
	-- Article Title
	ATCL_TITL varchar(1024),
	-- Article Contents
	ATCL_CNTS clob,
	-- Registeration Date
	RGST_DATE datetime,
	-- Modification Date
	MDFY_DATE datetime,
	-- User ID
	USER_ID varchar(32),
	-- User Nickname
	USER_NICK varchar(256) NOT NULL,
	-- Client IP
	CLNT_IP varchar(64),
	-- Reply Count
	RPLY_CNT numeric,
	-- Read Count
	READ_CNT numeric,
	-- Vote Positive Count
	VOTE_PGTV_CNT numeric,
	-- Vote Negative Count
	VOTE_NGTV_CNT numeric,
	PRIMARY KEY (ATCL_ID)
);


-- App Article Read Hist
CREATE TABLE APP_ATCL_READ_HIST
(
	-- Article ID
	ATCL_ID varchar(32) NOT NULL,
	-- Read ID
	READ_ID varchar(32) NOT NULL,
	-- Read Date
	READ_DATE datetime,
	-- User ID
	USER_ID varchar(32),
	-- Client IP
	CLNT_IP varchar(64),
	PRIMARY KEY (ATCL_ID, READ_ID)
);


-- App Article Reply Info
CREATE TABLE APP_ATCL_RPLY_INFO
(
	-- Article ID
	ATCL_ID varchar(32) NOT NULL,
	-- Reply ID
	RPLY_ID varchar(32) NOT NULL,
	-- Upper Reply ID
	UPER_RPLY_ID varchar(32),
	-- RPLY_SEQ
	RPLY_SEQ numeric,
	-- RPLY_LEVL
	RPLY_LEVL numeric,
	-- Reply Title
	RPLY_TITL varchar(1024),
	-- Reply Contents
	RPLY_CNTS varchar(4000),
	-- User ID
	USER_ID varchar(32),
	-- User Nickname
	USER_NICK varchar(256),
	PRIMARY KEY (ATCL_ID, RPLY_ID)
);


-- App Article Vote Hist
CREATE TABLE APP_ATCL_VOTE_HIST
(
	-- Article ID
	ATCL_ID varchar(32) NOT NULL,
	-- Vote ID
	VOTE_ID varchar(32) NOT NULL,
	-- Vote Type
	VOTE_TYPE varchar(64),
	-- Vote Date
	VOTE_DATE datetime,
	-- User ID
	USER_ID varchar(32),
	-- Client IP
	CLNT_IP varchar(64),
	PRIMARY KEY (ATCL_ID, VOTE_ID)
);


-- App Authority Info
CREATE TABLE APP_AUTH_INFO
(
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- System Insert Date
	SYS_INST_DATE datetime,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32),
	-- System Update Date
	SYS_UPDT_DATE datetime,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32),
	-- Authority Name
	AUTH_NAME varchar(256),
	-- Authority Description
	AUTH_DESC varchar(4000),
	PRIMARY KEY (AUTH_ID)
);


-- App Board Category Info
CREATE TABLE APP_BORD_CATE_INFO
(
	-- Board ID
	BORD_ID varchar(32) NOT NULL,
	-- Category ID
	CATE_ID varchar(32) NOT NULL,
	-- Category Name
	CATE_NAME varchar(256),
	PRIMARY KEY (BORD_ID, CATE_ID)
);


-- App Board Info
CREATE TABLE APP_BORD_INFO
(
	-- Board ID
	BORD_ID varchar(32) NOT NULL,
	-- Board Name
	BORD_NAME varchar(256),
	-- Board Description
	BORD_DESC varchar(4000),
	-- Board Icon
	BORD_ICON varchar(4000),
	-- Board Skin
	BORD_SKIN varchar(256),
	-- Rows Per Page
	ROWS_PER_PAGE numeric,
	-- Display Permission
	DISP_PERM varchar(1),
	-- Access Permission
	ACES_PERM varchar(16),
	-- Read Permission
	READ_PERM varchar(16),
	-- Write Permission
	WRIT_PERM varchar(16),
	-- Category Use Yn
	CATE_USE_YN varchar(1),
	-- File Use Yn
	FILE_USE_YN varchar(1),
	-- File Permission
	FILE_PERM varchar(64),
	-- Reply Use Yn
	RPLY_USE_YN varchar(1),
	-- Reply Permission
	RPLY_PERM varchar(64),
	-- Vote Use Yn
	VOTE_USE_YN varchar(1),
	-- Vote Permission
	VOTE_PERM varchar(64),
	-- Article Count
	ATCL_CNT numeric,
	PRIMARY KEY (BORD_ID)
);


-- 새 테이블
CREATE TABLE APP_BORD_PERM_AUTH_MAP
(
	-- Board ID
	BORD_ID varchar(32) NOT NULL,
	-- Permission Type
	PERM_TYPE varchar(64) NOT NULL,
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (BORD_ID, PERM_TYPE, AUTH_ID)
);


-- App Code Info
CREATE TABLE APP_CODE_INFO
(
	-- Code ID
	CODE_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- System Insert Date
	SYS_INST_DATE datetime,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32),
	-- System Update Date
	SYS_UPDT_DATE datetime,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32),
	-- Code Name
	CODE_NAME varchar(256),
	-- Code Description
	CODE_DESC varchar(32),
	PRIMARY KEY (CODE_ID)
);


-- App Code Item Info
CREATE TABLE APP_CODE_ITEM_INFO
(
	-- Code ID
	CODE_ID varchar(32) NOT NULL,
	-- Item ID
	ITEM_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1) NOT NULL,
	-- System Insert Date
	SYS_INST_DATE datetime NOT NULL,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32) NOT NULL,
	-- System Update Date
	SYS_UPDT_DATE datetime NOT NULL,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32) NOT NULL,
	-- Item Name
	ITEM_NAME varchar(256),
	-- Item Description
	ITEM_DESC varchar(4000),
	-- Display Sequence
	DISP_SEQ numeric,
	PRIMARY KEY (CODE_ID, ITEM_ID)
);


-- App Group Authority Map
CREATE TABLE APP_GROP_AUTH_MAP
(
	-- Group ID
	GROP_ID varchar(32) NOT NULL,
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (GROP_ID, AUTH_ID)
);


-- App Group Info
CREATE TABLE APP_GROP_INFO
(
	-- Group ID
	GROP_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- System Insert Date
	SYS_INST_DATE datetime,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32),
	-- System Update Date
	SYS_UPDT_DATE datetime,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32),
	-- Upper Group ID
	UPER_GROP_ID varchar(32),
	-- Group Name
	GROP_NAME varchar(256),
	-- Group Description
	GROP_DESC varchar(4000),
	-- Display Sequence
	DISP_SEQ int,
	PRIMARY KEY (GROP_ID)
);


-- App Group Rule Map
CREATE TABLE APP_GROP_ROLE_MAP
(
	-- Group ID
	GROP_ID varchar(32) NOT NULL,
	-- Role ID
	ROLE_ID varchar(32) NOT NULL,
	PRIMARY KEY (GROP_ID, ROLE_ID)
);


-- App Menu Info
CREATE TABLE APP_MENU_INFO
(
	-- Menu ID
	MENU_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1) NOT NULL,
	-- System Insert Date
	SYS_INST_DATE datetime NOT NULL,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32) NOT NULL,
	-- System Update Date
	SYS_UPDT_DATE datetime NOT NULL,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32) NOT NULL,
	-- Upper Menu ID
	UPER_MENU_ID varchar(32),
	-- Menu Name
	MENU_NAME varchar(256),
	-- Menu Description
	MENU_DESC varchar(4000),
	-- Menu Icon
	MENU_ICON varchar(4000),
	-- Menu Type
	MENU_TYPE varchar(16),
	-- Menu Value
	MENU_VAL varchar(4000),
	-- Display Permission
	DISP_PERM varchar(16),
	-- Display Sequence
	DISP_SEQ numeric,
	-- Access Permission
	ACES_PERM varchar(16),
	PRIMARY KEY (MENU_ID)
);


-- App Menu Permission Authority Map
CREATE TABLE APP_MENU_PERM_AUTH_MAP
(
	-- Menu ID
	MENU_ID varchar(32) NOT NULL,
	-- Permission Type
	PERM_TYPE varchar(64) NOT NULL,
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (MENU_ID, PERM_TYPE, AUTH_ID)
);


-- App Message Info
CREATE TABLE APP_MESG_INFO
(
	-- MESG_ID
	MESG_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1) NOT NULL,
	-- System Insert Date
	SYS_INST_DATE datetime NOT NULL,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32) NOT NULL,
	-- System Update Date
	SYS_UPDT_DATE datetime NOT NULL,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32) NOT NULL,
	-- Message Name
	MESG_NAME varchar(256),
	-- Message Description
	MESG_DESC varchar(4000),
	-- Message Value
	MESG_VAL varchar(4000),
	PRIMARY KEY (MESG_ID)
);


-- App Property Info
CREATE TABLE APP_PROP_INFO
(
	-- PROP_ID
	PROP_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1) NOT NULL,
	-- System Insert Date
	SYS_INST_DATE datetime NOT NULL,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32) NOT NULL,
	-- System Update Date
	SYS_UPDT_DATE datetime NOT NULL,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32) NOT NULL,
	-- Property Name
	PROP_NAME varchar(256),
	-- Property Description
	PROP_DESC varchar(4000),
	-- Property Value
	PROP_VAL varchar(4000),
	PRIMARY KEY (PROP_ID)
);


-- App Role Authority Info
CREATE TABLE APP_ROLE_AUTH_MAP
(
	-- Role ID
	ROLE_ID varchar(32) NOT NULL,
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (ROLE_ID, AUTH_ID)
);


-- App Role Info
CREATE TABLE APP_ROLE_INFO
(
	-- Role ID
	ROLE_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- System Insert Date
	SYS_INST_DATE datetime,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32),
	-- System Update Date
	SYS_UPDT_DATE datetime,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32),
	-- Role Name
	ROLE_NAME varchar(256),
	-- Role Description
	ROLE_DESC varchar(4000),
	PRIMARY KEY (ROLE_ID)
);


-- App User Authority Map
CREATE TABLE APP_USER_AUTH_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- Authority ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, AUTH_ID)
);


-- App User Group Map
CREATE TABLE APP_USER_GROP_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- Group ID
	GROP_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, GROP_ID)
);


-- App User Info
CREATE TABLE APP_USER_INFO
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- System Insert Date
	SYS_INST_DATE datetime,
	-- System Insert User ID
	SYS_INST_USER_ID varchar(32),
	-- System Update Date
	SYS_UPDT_DATE datetime,
	-- System Update User ID
	SYS_UPDT_USER_ID varchar(32),
	-- User Email
	USER_EMIL varchar(256),
	-- User Phone
	USER_PHON varchar(16),
	-- User Password
	USER_PASS varchar(256),
	-- User Name
	USER_NAME varchar(256),
	-- User Nickname
	USER_NICK varchar(256),
	-- User Status
	USER_STAT varchar(16),
	-- User Avatar
	USER_AVAT varchar(4000),
	-- User Signature
	USER_SIGN varchar(4000),
	-- User Locale
	USER_LOCL varchar(8),
	-- Join Date
	JOIN_DATE datetime,
	-- Close Date
	CLOS_DATE datetime,
	PRIMARY KEY (USER_ID)
);


-- App User Login Hist
CREATE TABLE APP_USER_LOGN_HIST
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- Login Date
	LOGN_DATE datetime,
	-- Login Success Yn
	LOGN_SUCS_YN varchar(1),
	-- Login Fail Reason
	LOGN_FAIL_RESN varchar(4000),
	-- Client IP
	CLNT_IP varchar(64),
	-- Client Agent
	CLNT_AGNT varchar(256),
	-- Client Referer
	CLNT_REFR varchar(1024)
);


-- App User Role Map
CREATE TABLE APP_USER_ROLE_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- Role ID
	ROLE_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, ROLE_ID)
);


-- App User Sns Info
CREATE TABLE APP_USER_SNS_INFO
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- Sns Type
	SNS_TYPE varchar(16),
	-- Sns Token
	SNS_TOKN varchar(4000)
);



