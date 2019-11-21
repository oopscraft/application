

/* Create Tables */

-- APP_AUTH_INFO
CREATE TABLE APP_AUTH_INFO
(
	-- AUTH_ID
	AUTH_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- SYS_INST_DTTM
	SYS_INST_DTTM datetime,
	-- SYS_INST_USER_ID
	SYS_INST_USER_ID varchar(32),
	-- SYS_UPDT_DTTM
	SYS_UPDT_DTTM datetime,
	-- SYS_UPDT_USER_ID
	SYS_UPDT_USER_ID varchar(32),
	-- AUTH_NAME
	AUTH_NAME varchar(256),
	-- AUTH_DESC
	AUTH_DESC varchar(4000),
	PRIMARY KEY (AUTH_ID)
);


-- APP_GROP_AUTH_MAP
CREATE TABLE APP_GROP_AUTH_MAP
(
	-- GROP_ID
	GROP_ID varchar(32) NOT NULL,
	-- AUTH_ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (GROP_ID, AUTH_ID)
);


-- APP_GROP_INFO
CREATE TABLE APP_GROP_INFO
(
	-- GROP_ID
	GROP_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- SYS_INST_DTTM
	SYS_INST_DTTM datetime,
	-- SYS_INST_USER_ID
	SYS_INST_USER_ID varchar(32),
	-- SYS_UPDT_DTTM
	SYS_UPDT_DTTM datetime,
	-- SYS_UPDT_USER_ID
	SYS_UPDT_USER_ID varchar(32),
	-- UPER_GROP_ID
	UPER_GROP_ID varchar(32),
	-- GROP_NAME
	GROP_NAME varchar(256),
	-- GROP_DESC
	GROP_DESC varchar(4000),
	-- DISP_SEQ
	DISP_SEQ int,
	PRIMARY KEY (GROP_ID)
);


-- APP_GROP_ROLE_MAP
CREATE TABLE APP_GROP_ROLE_MAP
(
	-- GROP_ID
	GROP_ID varchar(32) NOT NULL,
	-- ROLE_ID
	ROLE_ID varchar(32) NOT NULL,
	PRIMARY KEY (GROP_ID, ROLE_ID)
);


-- APP_ROLE_AUTH_MAP
CREATE TABLE APP_ROLE_AUTH_MAP
(
	-- ROLE_ID
	ROLE_ID varchar(32) NOT NULL,
	-- AUTH_ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (ROLE_ID, AUTH_ID)
);


-- APP_ROLE_INFO
CREATE TABLE APP_ROLE_INFO
(
	-- ROLE_ID
	ROLE_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- SYS_INST_DTTM
	SYS_INST_DTTM datetime,
	-- SYS_INST_USER_ID
	SYS_INST_USER_ID varchar(32),
	-- SYS_UPDT_DTTM
	SYS_UPDT_DTTM datetime,
	-- SYS_UPDT_USER_ID
	SYS_UPDT_USER_ID varchar(32),
	-- ROLE_NAME
	ROLE_NAME varchar(256),
	-- ROLE_DESC
	ROLE_DESC varchar(4000),
	PRIMARY KEY (ROLE_ID)
);


-- APP_USER_AUTH_MAP
CREATE TABLE APP_USER_AUTH_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- AUTH_ID
	AUTH_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, AUTH_ID)
);


-- APP_USER_GROP_MAP
CREATE TABLE APP_USER_GROP_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- GROP_ID
	GROP_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, GROP_ID)
);


-- APP_USER_INFO
CREATE TABLE APP_USER_INFO
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- System Data Yn
	SYS_DATA_YN varchar(1),
	-- SYS_INST_DTTM
	SYS_INST_DTTM datetime,
	-- SYS_INST_USER_ID
	SYS_INST_USER_ID varchar(32),
	-- SYS_UPDT_DTTM
	SYS_UPDT_DTTM datetime,
	-- SYS_UPDT_USER_ID
	SYS_UPDT_USER_ID varchar(32),
	-- USER_NAME
	USER_NAME varchar(256),
	-- USER_PASS
	USER_PASS varchar(256),
	-- USER_STAT
	USER_STAT varchar(16),
	-- USER_EMIL
	USER_EMIL varchar(256),
	-- USER_PHON
	USER_PHON varchar(16),
	-- USER_LOCL
	USER_LOCL varchar(8),
	-- USER_AVAT
	USER_AVAT varchar(4000),
	-- USER_SIGN
	USER_SIGN varchar(4000),
	-- USER_JOIN_DTTM
	USER_JOIN_DTTM datetime,
	-- USER_CLOS_DTTM
	USER_CLOS_DTTM datetime,
	PRIMARY KEY (USER_ID)
);


-- APP_USER_ROLE_MAP
CREATE TABLE APP_USER_ROLE_MAP
(
	-- User ID
	USER_ID varchar(32) NOT NULL,
	-- ROLE_ID
	ROLE_ID varchar(32) NOT NULL,
	PRIMARY KEY (USER_ID, ROLE_ID)
);



