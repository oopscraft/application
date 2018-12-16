/**********************************/
/* Table Name: Application Authority Info */
/**********************************/
CREATE TABLE APP_AUTH_INFO(
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User ID',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Update Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User ID',
		AUTH_NAME                     		VARCHAR(256)		 NULL  COMMENT 'Authority Name',
		AUTH_DESC                     		MEDIUMTEXT		 NULL  COMMENT 'Authority Description'
) COMMENT='Application Authority Info';

/**********************************/
/* Table Name: Application Code Info */
/**********************************/
CREATE TABLE APP_CD_INFO(
		CD_ID                         		VARCHAR(32)		 NOT NULL COMMENT 'Code Id',
		UPER_CD_ID                    		VARCHAR(32)		 NULL  COMMENT 'Upper Code Id',
		CD_NAME                       		VARCHAR(256)		 NULL  COMMENT 'Code Name',
		CD_DESC                       		TEXT		 NULL  COMMENT 'Code Description',
		TAB                           		VARCHAR(32)		 NULL  COMMENT 'Table'
) COMMENT='Application Code Info';

/**********************************/
/* Table Name: Application Code Item Info */
/**********************************/
CREATE TABLE APP_CD_ITEM_INFO(
		CD_ID                         		VARCHAR(32)		 NOT NULL COMMENT 'Code Id',
		CD_ITEM_ID                    		VARCHAR(32)		 NOT NULL COMMENT 'Code Item Id',
		CD_ITEM_NAME                  		VARCHAR(256)		 NULL  COMMENT 'Code Item Name',
		CD_ITEM_DESC                  		MEDIUMTEXT		 NULL  COMMENT 'Code Item Description'
) COMMENT='Application Code Item Info';

/**********************************/
/* Table Name: Application Group Info */
/**********************************/
CREATE TABLE APP_GROP_INFO(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User ID',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Update Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User ID',
		UPER_GROP_ID                  		VARCHAR(32)		 NULL  COMMENT 'Upper Group ID',
		GROP_NAME                     		VARCHAR(256)		 NULL  COMMENT 'Group Name',
		GROP_DESC                     		TEXT		 NULL  COMMENT 'Group Description',
		DISP_SEQ                      		INT(10)		 NULL  COMMENT 'Display Sequence'
) COMMENT='Application Group Info';

/**********************************/
/* Table Name: Application Group Authority Map */
/**********************************/
CREATE TABLE APP_GROP_AUTH_MAP(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
) COMMENT='Application Group Authority Map';

/**********************************/
/* Table Name: Application Role Info */
/**********************************/
CREATE TABLE APP_ROLE_INFO(
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User ID',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Update Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User ID',
		ROLE_NAME                     		VARCHAR(256)		 NULL  COMMENT 'Role Name',
		ROLE_DESC                     		MEDIUMTEXT		 NULL  COMMENT 'Role Description'
) COMMENT='Application Role Info';

/**********************************/
/* Table Name: Application Group Role Map */
/**********************************/
CREATE TABLE APP_GROP_ROLE_MAP(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id'
) COMMENT='Application Group Role Map';

/**********************************/
/* Table Name: Application Menu Information */
/**********************************/
CREATE TABLE APP_MENU_INFO(
		MENU_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Menu Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User ID',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Updte Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User ID',
		UPER_MENU_ID                  		VARCHAR(32)		 NOT NULL COMMENT 'Upper Menu Id',
		MENU_NAME                     		VARCHAR(256)		 NULL  COMMENT 'Menu Name',
		MENU_ICON                     		MEDIUMTEXT		 NULL  COMMENT 'Menu Icon',
		MENU_LINK                     		VARCHAR(256)		 NOT NULL COMMENT 'Menu Link',
		MENU_DESC                     		MEDIUMTEXT		 NULL  COMMENT 'Menu Description',
		ACES_PLCY                     		VARCHAR(128)		 NULL  COMMENT 'Access Policy'
) COMMENT='Application Menu Information';

/**********************************/
/* Table Name: Application Menu Authority Map */
/**********************************/
CREATE TABLE APP_MENU_AUTH_MAP(
		MENU_ID                       		VARCHAR(32)		 NULL  COMMENT 'Menu Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority ID'
) COMMENT='Application Menu Authority Map';

/**********************************/
/* Table Name: Application Role Authority Map */
/**********************************/
CREATE TABLE APP_ROLE_AUTH_MAP(
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
) COMMENT='Application Role Authority Map';

/**********************************/
/* Table Name: Application Table Column Code Map */
/**********************************/
CREATE TABLE APP_TAB_COL_CODE_MAP(
		TAB                           		VARCHAR(32)		 NOT NULL COMMENT 'Table',
		COL                           		VARCHAR(32)		 NOT NULL COMMENT 'Column',
		CD_ID                         		VARCHAR(32)		 NULL  COMMENT 'Code Id'
) COMMENT='Application Table Column Code Map';

/**********************************/
/* Table Name: Application User Info */
/**********************************/
CREATE TABLE APP_USER_INFO(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User id',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Update Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User Id',
		USER_PWD                      		VARCHAR(256)		 NULL  COMMENT 'User Password',
		USER_EMIL                     		VARCHAR(256)		 NULL  COMMENT 'User Email',
		USER_LC                       		VARCHAR(8)		 NULL  COMMENT 'User Locale',
		USER_PHON                     		VARCHAR(16)		 NULL  COMMENT 'User Phone',
		USER_NAME                     		VARCHAR(256)		 NULL  COMMENT 'User Name',
		USER_STAT                     		VARCHAR(16)		 NULL  COMMENT 'User Status',
		USER_NICK                     		VARCHAR(256)		 NULL  COMMENT 'User Nickname',
		USER_AVAT                     		MEDIUMTEXT		 NULL  COMMENT 'User Avatar',
		USER_SIGN                     		VARCHAR(2048)		 NULL  COMMENT 'User Signature',
		USER_JOIN_DTTM                		DATETIME		 NULL  COMMENT 'User Join Datetime',
		USER_CLOS_DTTM                		DATETIME		 NULL  COMMENT 'User Close Datetime'
) COMMENT='Application User Info';

/**********************************/
/* Table Name: Application User Authority Map */
/**********************************/
CREATE TABLE APP_USER_AUTH_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
) COMMENT='Application User Authority Map';

/**********************************/
/* Table Name: Application User Group Map */
/**********************************/
CREATE TABLE APP_USER_GROP_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User ID',
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group ID'
) COMMENT='Application User Group Map';

/**********************************/
/* Table Name: Application User Role Map */
/**********************************/
CREATE TABLE APP_USER_ROLE_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id'
) COMMENT='Application User Role Map';

/**********************************/
/* Table Name: Application Resource Information */
/**********************************/
CREATE TABLE APP_RSRC_INFO(
		ACL_URI                       		VARCHAR(256)		 NOT NULL COMMENT 'ACL URI',
		ACL_MTHD                      		VARCHAR(16)		 NOT NULL COMMENT 'ACL Method',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL  COMMENT 'System Data Yn',
		SYS_INST_DTTM                 		DATETIME		 NULL  COMMENT 'System Insert Datetime',
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Insert User ID',
		SYS_UPDT_DTTM                 		DATETIME		 NULL  COMMENT 'System Updte Datetime',
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL  COMMENT 'System Update User ID',
		ACL_NAME                      		VARCHAR(256)		 NULL  COMMENT 'ACL Name',
		ACL_DESC                      		MEDIUMTEXT		 NOT NULL COMMENT 'ACL Description',
		ACES_PLCY                     		VARCHAR(128)		 NULL  COMMENT 'Access Policy'
) COMMENT='Application Resource Information';

/**********************************/
/* Table Name: Application ACL(Access Control List) Authority Map */
/**********************************/
CREATE TABLE APP_ACL_AUTH_MAP(
		ACL_URI                       		VARCHAR(256)		 NOT NULL COMMENT 'ACL URI',
		ACL_MTHD                      		VARCHAR(16)		 NOT NULL COMMENT 'ACL Method',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
) COMMENT='Application ACL(Access Control List) Authority Map';


ALTER TABLE APP_AUTH_INFO ADD CONSTRAINT IDX_APP_AUTH_INFO_PK PRIMARY KEY (AUTH_ID);

ALTER TABLE APP_CD_INFO ADD CONSTRAINT IDX_APP_CD_INFO_PK PRIMARY KEY (CD_ID);

ALTER TABLE APP_CD_ITEM_INFO ADD CONSTRAINT IDX_APP_CD_ITEM_INFO_PK PRIMARY KEY (CD_ID, CD_ITEM_ID);
ALTER TABLE APP_CD_ITEM_INFO ADD CONSTRAINT IDX_APP_CD_ITEM_INFO_FK0 FOREIGN KEY (CD_ID) REFERENCES APP_CD_INFO (CD_ID);

ALTER TABLE APP_GROP_INFO ADD CONSTRAINT IDX_APP_GROP_INFO_PK PRIMARY KEY (GROP_ID);

ALTER TABLE APP_GROP_AUTH_MAP ADD CONSTRAINT IDX_APP_GROP_AUTH_MAP_PK PRIMARY KEY (GROP_ID, AUTH_ID);
ALTER TABLE APP_GROP_AUTH_MAP ADD CONSTRAINT IDX_APP_GROP_AUTH_MAP_FK0 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);
ALTER TABLE APP_GROP_AUTH_MAP ADD CONSTRAINT IDX_APP_GROP_AUTH_MAP_FK1 FOREIGN KEY (GROP_ID) REFERENCES APP_GROP_INFO (GROP_ID);

ALTER TABLE APP_ROLE_INFO ADD CONSTRAINT IDX_APP_ROLE_INFO_PK PRIMARY KEY (ROLE_ID);

ALTER TABLE APP_GROP_ROLE_MAP ADD CONSTRAINT IDX_APP_GROP_ROLE_MAP_PK PRIMARY KEY (GROP_ID, ROLE_ID);
ALTER TABLE APP_GROP_ROLE_MAP ADD CONSTRAINT IDX_APP_GROP_ROLE_MAP_FK0 FOREIGN KEY (GROP_ID) REFERENCES APP_GROP_INFO (GROP_ID);
ALTER TABLE APP_GROP_ROLE_MAP ADD CONSTRAINT IDX_APP_GROP_ROLE_MAP_FK1 FOREIGN KEY (ROLE_ID) REFERENCES APP_ROLE_INFO (ROLE_ID);

ALTER TABLE APP_MENU_INFO ADD CONSTRAINT IDX_APP_MENU_INFO_PK PRIMARY KEY (MENU_ID);

ALTER TABLE APP_MENU_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_AUTH_MAP_PK PRIMARY KEY (MENU_ID, AUTH_ID);
ALTER TABLE APP_MENU_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_AUTH_MAP_FK0 FOREIGN KEY (MENU_ID) REFERENCES APP_MENU_INFO (MENU_ID);
ALTER TABLE APP_MENU_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_PK PRIMARY KEY (ROLE_ID, AUTH_ID);
ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_FK0 FOREIGN KEY (ROLE_ID) REFERENCES APP_ROLE_INFO (ROLE_ID);
ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_TAB_COL_CODE_MAP ADD CONSTRAINT IDX_APP_TAB_COL_CODE_MAP_PK PRIMARY KEY (TAB, COL);
ALTER TABLE APP_TAB_COL_CODE_MAP ADD CONSTRAINT IDX_APP_TAB_COL_CODE_MAP_FK0 FOREIGN KEY (CD_ID) REFERENCES APP_CD_INFO (CD_ID);

ALTER TABLE APP_USER_INFO ADD CONSTRAINT IDX_APP_USER_INFO_PK PRIMARY KEY (USER_ID);
CREATE INDEX APP_USER_INFO_IX_1 ON APP_USER_INFO (SYS_EMBD_YN, USER_JOIN_DTTM);
CREATE INDEX APP_USER_INFO_IX_2 ON APP_USER_INFO (USER_NAME);

ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_PK PRIMARY KEY (USER_ID, AUTH_ID);
ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);
ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_USER_GROP_MAP ADD CONSTRAINT IDX_APP_USER_GROP_MAP_PK PRIMARY KEY (USER_ID, GROP_ID);
ALTER TABLE APP_USER_GROP_MAP ADD CONSTRAINT IDX_APP_USER_GROP_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);

ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_PK PRIMARY KEY (USER_ID, ROLE_ID);
ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);
ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_FK1 FOREIGN KEY (ROLE_ID) REFERENCES APP_ROLE_INFO (ROLE_ID);

ALTER TABLE APP_RSRC_INFO ADD CONSTRAINT IDX_APP_RSRC_INFO_PK PRIMARY KEY (ACL_URI, ACL_MTHD);

ALTER TABLE APP_ACL_AUTH_MAP ADD CONSTRAINT IDX_APP_ACL_AUTH_MAP_PK PRIMARY KEY (ACL_URI, ACL_MTHD, AUTH_ID);
ALTER TABLE APP_ACL_AUTH_MAP ADD CONSTRAINT IDX_APP_ACL_AUTH_MAP_FK0 FOREIGN KEY (ACL_URI,ACL_MTHD) REFERENCES APP_RSRC_INFO (ACL_URI,ACL_MTHD);
ALTER TABLE APP_ACL_AUTH_MAP ADD CONSTRAINT IDX_APP_ACL_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);


INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN','Y','Administrator');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_AUTHORITY','Y','Administrator Authority Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_GROUP','Y','Administrator Group Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_MONITOR','Y','Administrator Monitor');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_ROLE','Y','Administrator Role Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_USER','Y','Administrator User Manage');

INSERT INTO APP_USER_INFO (USER_ID,SYS_DATA_YN,USER_NAME,USER_PWD) VALUES ('admin','Y','Administrator','$2a$10$ELs7fvpZii3P.KFYaQEJfOeN3iFjQefnY.SXPnLh6mtENV1Rvxc/C');

INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_AUTHORITY');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_GROUP');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_MONITOR');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_ROLE');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_USER');
