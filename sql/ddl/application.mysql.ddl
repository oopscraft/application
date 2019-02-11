/**********************************/
/* Table Name: Application Authority Info */
/**********************************/
CREATE TABLE APP_AUTH_INFO(
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Update Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		AUTH_NAME                     		VARCHAR(256)		 NULL COMMENT 'Authority Name' ,
		AUTH_DESC                     		VARCHAR(4000)		 NULL COMMENT 'Authority Description' 
);

ALTER TABLE APP_AUTH_INFO comment 'Application Authority Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_ID is 'Authority Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.SYS_UPDT_DTTM is 'System Update Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_NAME is 'Authority Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_DESC is 'Authority Description'; */


/**********************************/
/* Table Name: Application Code Info */
/**********************************/
CREATE TABLE APP_CD_INFO(
		CD_ID                         		VARCHAR(32)		 NOT NULL COMMENT 'Code Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Update Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		CD_NAME                       		VARCHAR(256)		 NULL COMMENT 'Code Name' ,
		CD_DESC                       		VARCHAR(4000)		 NULL COMMENT 'Code Description' 
);

ALTER TABLE APP_CD_INFO comment 'Application Code Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.CD_ID is 'Code Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.SYS_UPDT_DTTM is 'System Update Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.CD_NAME is 'Code Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_INFO.CD_DESC is 'Code Description'; */


/**********************************/
/* Table Name: Application Code Item Info */
/**********************************/
CREATE TABLE APP_CD_ITEM_INFO(
		CD_ID                         		VARCHAR(32)		 NOT NULL COMMENT 'Code Id',
		CD_ITEM_ID                    		VARCHAR(32)		 NOT NULL COMMENT 'Code Item Id',
		CD_ITEM_NAME                  		VARCHAR(256)		 NULL COMMENT 'Code Item Value' ,
		DISP_SEQ                      		DOUBLE		 NULL COMMENT 'Display Sequence' 
);

ALTER TABLE APP_CD_ITEM_INFO comment 'Application Code Item Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ID is 'Code Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ITEM_ID is 'Code Item Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ITEM_NAME is 'Code Item Value'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_CD_ITEM_INFO.DISP_SEQ is 'Display Sequence'; */


/**********************************/
/* Table Name: Application Group Info */
/**********************************/
CREATE TABLE APP_GROP_INFO(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Update Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		UPER_GROP_ID                  		VARCHAR(32)		 NULL COMMENT 'Upper Group ID' ,
		GROP_NAME                     		VARCHAR(256)		 NULL COMMENT 'Group Name' ,
		GROP_DESC                     		VARCHAR(4000)		 NULL COMMENT 'Group Description' ,
		DISP_SEQ                      		DOUBLE		 NULL COMMENT 'Display Sequence' 
);

ALTER TABLE APP_GROP_INFO comment 'Application Group Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.GROP_ID is 'Group Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.SYS_UPDT_DTTM is 'System Update Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.UPER_GROP_ID is 'Upper Group ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.GROP_NAME is 'Group Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.GROP_DESC is 'Group Description'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_INFO.DISP_SEQ is 'Display Sequence'; */


/**********************************/
/* Table Name: Application Group Authority Map */
/**********************************/
CREATE TABLE APP_GROP_AUTH_MAP(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
);

ALTER TABLE APP_GROP_AUTH_MAP comment 'Application Group Authority Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_AUTH_MAP.GROP_ID is 'Group Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_AUTH_MAP.AUTH_ID is 'Authority Id'; */


/**********************************/
/* Table Name: Application Role Info */
/**********************************/
CREATE TABLE APP_ROLE_INFO(
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Update Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		ROLE_NAME                     		VARCHAR(256)		 NULL COMMENT 'Role Name' ,
		ROLE_DESC                     		VARCHAR(4000)		 NULL COMMENT 'Role Description' 
);

ALTER TABLE APP_ROLE_INFO comment 'Application Role Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_ID is 'Role Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.SYS_UPDT_DTTM is 'System Update Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_NAME is 'Role Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_DESC is 'Role Description'; */


/**********************************/
/* Table Name: Application Group Role Map */
/**********************************/
CREATE TABLE APP_GROP_ROLE_MAP(
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group Id',
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id'
);

ALTER TABLE APP_GROP_ROLE_MAP comment 'Application Group Role Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_ROLE_MAP.GROP_ID is 'Group Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_GROP_ROLE_MAP.ROLE_ID is 'Role Id'; */


/**********************************/
/* Table Name: Application Menu Information */
/**********************************/
CREATE TABLE APP_MENU_INFO(
		MENU_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Menu Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Updte Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		UPER_MENU_ID                  		VARCHAR(32)		 NULL COMMENT 'Upper Menu Id' ,
		MENU_NAME                     		VARCHAR(256)		 NULL COMMENT 'Menu Name' ,
		MENU_ICON                     		LONGTEXT		 NULL COMMENT 'Menu Icon' ,
		MENU_TYPE                     		VARCHAR(16)		 NULL COMMENT 'Menu Type' ,
		MENU_VAL                      		VARCHAR(4000)		 NULL COMMENT 'Menu Value' ,
		MENU_DESC                     		VARCHAR(4000)		 NULL COMMENT 'Menu Description' ,
		ACES_PLCY                     		VARCHAR(128)		 NULL COMMENT 'Access Policy' ,
		DISP_PLCY                     		VARCHAR(128)		 NULL COMMENT 'Display Policy' ,
		DISP_SEQ                      		DOUBLE		 NULL COMMENT 'DisplaySeq' 
);

ALTER TABLE APP_MENU_INFO comment 'Application Menu Information';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_ID is 'Menu Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.SYS_UPDT_DTTM is 'System Updte Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.UPER_MENU_ID is 'Upper Menu Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_NAME is 'Menu Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_ICON is 'Menu Icon'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_TYPE is 'Menu Type'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_VAL is 'Menu Value'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.MENU_DESC is 'Menu Description'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.ACES_PLCY is 'Access Policy'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.DISP_PLCY is 'Display Policy'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_INFO.DISP_SEQ is 'DisplaySeq'; */


/**********************************/
/* Table Name: Application Menu Policy Authority Map */
/**********************************/
CREATE TABLE APP_MENU_PLCY_AUTH_MAP(
		MENU_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Menu Id',
		PLCY_TYPE                     		VARCHAR(128)		 NOT NULL COMMENT 'Policy Type',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority ID'
);

ALTER TABLE APP_MENU_PLCY_AUTH_MAP comment 'Application Menu Policy Authority Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.MENU_ID is 'Menu Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.PLCY_TYPE is 'Policy Type'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.AUTH_ID is 'Authority ID'; */


/**********************************/
/* Table Name: Application Role Authority Map */
/**********************************/
CREATE TABLE APP_ROLE_AUTH_MAP(
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
);

ALTER TABLE APP_ROLE_AUTH_MAP comment 'Application Role Authority Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_AUTH_MAP.ROLE_ID is 'Role Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ROLE_AUTH_MAP.AUTH_ID is 'Authority Id'; */


/**********************************/
/* Table Name: Application User Info */
/**********************************/
CREATE TABLE APP_USER_INFO(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User id' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Update Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User Id' ,
		USER_PWD                      		VARCHAR(256)		 NULL COMMENT 'User Password' ,
		USER_EMIL                     		VARCHAR(256)		 NULL COMMENT 'User Email' ,
		USER_LC                       		VARCHAR(8)		 NULL COMMENT 'User Locale' ,
		USER_PHON                     		VARCHAR(16)		 NULL COMMENT 'User Phone' ,
		USER_NAME                     		VARCHAR(256)		 NULL COMMENT 'User Name' ,
		USER_STAT                     		VARCHAR(16)		 NULL COMMENT 'User Status' ,
		USER_NICK                     		VARCHAR(256)		 NULL COMMENT 'User Nickname' ,
		USER_AVAT                     		LONGTEXT		 NULL COMMENT 'User Avatar' ,
		USER_SIGN                     		VARCHAR(4000)		 NULL COMMENT 'User Signature' ,
		USER_JOIN_DTTM                		DATETIME		 NULL COMMENT 'User Join Datetime' ,
		USER_CLOS_DTTM                		DATETIME		 NULL COMMENT 'User Close Datetime' 
);

ALTER TABLE APP_USER_INFO comment 'Application User Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_ID is 'User Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.SYS_INST_USER_ID is 'System Insert User id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.SYS_UPDT_DTTM is 'System Update Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.SYS_UPDT_USER_ID is 'System Update User Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_PWD is 'User Password'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_EMIL is 'User Email'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_LC is 'User Locale'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_PHON is 'User Phone'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_NAME is 'User Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_STAT is 'User Status'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_NICK is 'User Nickname'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_AVAT is 'User Avatar'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_SIGN is 'User Signature'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_JOIN_DTTM is 'User Join Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_INFO.USER_CLOS_DTTM is 'User Close Datetime'; */


/**********************************/
/* Table Name: Application User Authority Map */
/**********************************/
CREATE TABLE APP_USER_AUTH_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
);

ALTER TABLE APP_USER_AUTH_MAP comment 'Application User Authority Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_AUTH_MAP.USER_ID is 'User Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_AUTH_MAP.AUTH_ID is 'Authority Id'; */


/**********************************/
/* Table Name: Application User Group Map */
/**********************************/
CREATE TABLE APP_USER_GROP_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User ID',
		GROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Group ID'
);

ALTER TABLE APP_USER_GROP_MAP comment 'Application User Group Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_GROP_MAP.USER_ID is 'User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_GROP_MAP.GROP_ID is 'Group ID'; */


/**********************************/
/* Table Name: Application User Role Map */
/**********************************/
CREATE TABLE APP_USER_ROLE_MAP(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		ROLE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Role Id'
);

ALTER TABLE APP_USER_ROLE_MAP comment 'Application User Role Map';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_ROLE_MAP.USER_ID is 'User Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_ROLE_MAP.ROLE_ID is 'Role Id'; */


/**********************************/
/* Table Name: Application Property Info */
/**********************************/
CREATE TABLE APP_PROP_INFO(
		PROP_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Property ID',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Updte Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		PROP_NAME                     		VARCHAR(256)		 NULL COMMENT 'Property Name' ,
		PROP_VAL                      		LONGTEXT		 NULL COMMENT 'Property Value' ,
		PROP_DESC                     		VARCHAR(4000)		 NULL COMMENT 'Property Description' 
);

ALTER TABLE APP_PROP_INFO comment 'Application Property Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.PROP_ID is 'Property ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.SYS_UPDT_DTTM is 'System Updte Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.PROP_NAME is 'Property Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.PROP_VAL is 'Property Value'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_PROP_INFO.PROP_DESC is 'Property Description'; */


/**********************************/
/* Table Name: Application Message Info */
/**********************************/
CREATE TABLE APP_MSG_INFO(
		MSG_ID                        		VARCHAR(32)		 NOT NULL COMMENT 'Message ID',
		SYS_DATA_YN                   		VARCHAR(1)		 NULL COMMENT 'System Data Yn' ,
		SYS_INST_DTTM                 		DATETIME		 NULL COMMENT 'System Insert Datetime' ,
		SYS_INST_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Insert User ID' ,
		SYS_UPDT_DTTM                 		DATETIME		 NULL COMMENT 'System Updte Datetime' ,
		SYS_UPDT_USER_ID              		VARCHAR(32)		 NULL COMMENT 'System Update User ID' ,
		MSG_NAME                      		VARCHAR(256)		 NULL COMMENT 'Message Name' ,
		MSG_VAL                       		LONGTEXT		 NULL COMMENT 'Message Value' ,
		MSG_DESC                      		VARCHAR(4000)		 NULL COMMENT 'Message Description' 
);

ALTER TABLE APP_MSG_INFO comment 'Application Message Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.MSG_ID is 'Message ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.SYS_DATA_YN is 'System Data Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.SYS_INST_DTTM is 'System Insert Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.SYS_INST_USER_ID is 'System Insert User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.SYS_UPDT_DTTM is 'System Updte Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.SYS_UPDT_USER_ID is 'System Update User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.MSG_NAME is 'Message Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.MSG_VAL is 'Message Value'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_MSG_INFO.MSG_DESC is 'Message Description'; */


/**********************************/
/* Table Name: Application Board */
/**********************************/
CREATE TABLE APP_BORD_INFO(
		BORD_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Board ID',
		BORD_NAME                     		VARCHAR(256)		 NULL COMMENT 'Board Name' ,
		BORD_ICON                     		LONGTEXT		 NULL COMMENT 'Board Icon' ,
		BORD_SKIN                     		VARCHAR(128)		 NULL COMMENT 'Board Skin' ,
		ACES_PLCY                     		VARCHAR(128)		 NULL COMMENT 'Access Policy' ,
		READ_PLCY                     		VARCHAR(128)		 NULL COMMENT 'Read Policy' ,
		WRIT_PLCY                     		VARCHAR(128)		 NULL COMMENT 'Write Policy' ,
		ROWS_PER_PAGE                 		DOUBLE		 NULL COMMENT 'Rows Per Page' ,
		CATE_USE_YN                   		VARCHAR(1)		 NULL COMMENT 'Category Use Yn' ,
		RPLY_USE_YN                   		VARCHAR(1)		 NULL COMMENT 'Reply Use' ,
		FILE_USE_YN                   		VARCHAR(1)		 NULL COMMENT 'File Use' 
);

ALTER TABLE APP_BORD_INFO comment 'Application Board';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.BORD_ID is 'Board ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.BORD_NAME is 'Board Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.BORD_ICON is 'Board Icon'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.BORD_SKIN is 'Board Skin'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.ACES_PLCY is 'Access Policy'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.READ_PLCY is 'Read Policy'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.WRIT_PLCY is 'Write Policy'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.ROWS_PER_PAGE is 'Rows Per Page'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.CATE_USE_YN is 'Category Use Yn'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.RPLY_USE_YN is 'Reply Use'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_INFO.FILE_USE_YN is 'File Use'; */


/**********************************/
/* Table Name: Application Board Category */
/**********************************/
CREATE TABLE APP_BORD_CATE_INFO(
		BORD_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Board ID',
		CATE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Cateogry ID',
		CATE_NAME                     		VARCHAR(256)		 NULL COMMENT 'Category Name' ,
		DISP_SEQ                      		DOUBLE		 NULL COMMENT 'Display Sequence' 
);

ALTER TABLE APP_BORD_CATE_INFO comment 'Application Board Category';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_CATE_INFO.BORD_ID is 'Board ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_CATE_INFO.CATE_ID is 'Cateogry ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_CATE_INFO.CATE_NAME is 'Category Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_CATE_INFO.DISP_SEQ is 'Display Sequence'; */


/**********************************/
/* Table Name: Application Board Article */
/**********************************/
CREATE TABLE APP_ATCL_INFO(
		ATCL_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Article ID',
		BORD_ID                       		VARCHAR(32)		 NULL COMMENT 'Board ID' ,
		CATE_ID                       		VARCHAR(32)		 NULL COMMENT 'Cateogry ID' ,
		ATCL_TITL                     		VARCHAR(256)		 NULL COMMENT 'Article Title' ,
		ATCL_CNTS                     		LONGTEXT		 NULL COMMENT 'Article Contents' ,
		ATCL_USER_ID                  		VARCHAR(32)		 NULL COMMENT 'Article User ID' ,
		ATCL_USER_NICK                		VARCHAR(256)		 NULL COMMENT 'Article User Nickname' ,
		ATCL_RGST_DTTM                		DATETIME		 NULL COMMENT 'Article Registration Datetime' ,
		ATCL_MDFY_DTTM                		DATETIME		 NULL COMMENT 'Article Modify Datetime' ,
		READ_CNT                      		DOUBLE		 DEFAULT 0		 NULL COMMENT 'Read Count' 
);

ALTER TABLE APP_ATCL_INFO comment 'Application Board Article';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_ID is 'Article ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.BORD_ID is 'Board ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.CATE_ID is 'Cateogry ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_TITL is 'Article Title'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_CNTS is 'Article Contents'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_USER_ID is 'Article User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_USER_NICK is 'Article User Nickname'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_RGST_DTTM is 'Article Registration Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_MDFY_DTTM is 'Article Modify Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_INFO.READ_CNT is 'Read Count'; */


/**********************************/
/* Table Name: Application Board Article Reply Info */
/**********************************/
CREATE TABLE APP_ATCL_RPLY_INFO(
		ATCL_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Article ID',
		RPLY_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Reply ID',
		UPER_RPLY_ID                  		VARCHAR(32)		 NULL COMMENT 'Upper Reply ID' ,
		RPLY_SEQ                      		DOUBLE		 NULL COMMENT 'Reply Sequence' ,
		RPLY_LEVL                     		VARCHAR(8)		 NULL COMMENT 'Reply Level' ,
		RPLY_CNTS                     		LONGTEXT		 NULL COMMENT 'Reply Contents' ,
		RPLY_USER_ID                  		VARCHAR(32)		 NULL COMMENT 'Reply User ID' ,
		RPLY_USER_NICK                		VARCHAR(256)		 NULL COMMENT 'Reply User Nickname' ,
		RPLY_RGST_DTTM                		DATETIME		 NULL COMMENT 'Reply Registration Datetime' ,
		RPLY_MDFY_DTTM                		DATETIME		 NULL COMMENT 'Reply Modify Datetime' 
);

ALTER TABLE APP_ATCL_RPLY_INFO comment 'Application Board Article Reply Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.ATCL_ID is 'Article ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_ID is 'Reply ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.UPER_RPLY_ID is 'Upper Reply ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_SEQ is 'Reply Sequence'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_LEVL is 'Reply Level'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_CNTS is 'Reply Contents'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_USER_ID is 'Reply User ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_USER_NICK is 'Reply User Nickname'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_RGST_DTTM is 'Reply Registration Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_MDFY_DTTM is 'Reply Modify Datetime'; */


/**********************************/
/* Table Name: Application Article File Info */
/**********************************/
CREATE TABLE APP_ATCL_FILE_INFO(
		ATCL_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Article ID',
		FILE_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'File ID',
		FILE_NAME                     		VARCHAR(256)		 NULL COMMENT 'File Name' ,
		FILE_TYPE                     		VARCHAR(128)		 NULL COMMENT 'File Type' ,
		FILE_SIZE                     		DOUBLE		 NULL COMMENT 'File Size' 
);

ALTER TABLE APP_ATCL_FILE_INFO comment 'Application Article File Info';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_FILE_INFO.ATCL_ID is 'Article ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_ID is 'File ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_NAME is 'File Name'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_TYPE is 'File Type'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_SIZE is 'File Size'; */


/**********************************/
/* Table Name: Application User Login History */
/**********************************/
CREATE TABLE APP_USER_LOGN_HIST(
		USER_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'User Id',
		LOGN_DTTM                     		DATETIME		 NOT NULL COMMENT 'Login Datetime',
		LOGN_SUCS_YN                  		VARCHAR(1)		 NULL COMMENT 'Login Success YN' ,
		LOGN_FAIL_RESN                		VARCHAR(1024)		 NULL COMMENT 'Login Fail Reason' ,
		LOGN_IP                       		VARCHAR(128)		 NULL COMMENT 'Login IP' ,
		LOGN_AGNT                     		VARCHAR(1024)		 NULL COMMENT 'Login Agent' ,
		LOGN_REFR                     		VARCHAR(1024)		 NULL COMMENT 'Login Referer' 
);

ALTER TABLE APP_USER_LOGN_HIST comment 'Application User Login History';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.USER_ID is 'User Id'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_DTTM is 'Login Datetime'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_SUCS_YN is 'Login Success YN'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_FAIL_RESN is 'Login Fail Reason'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_IP is 'Login IP'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_AGNT is 'Login Agent'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_REFR is 'Login Referer'; */


/**********************************/
/* Table Name: Table25 */
/**********************************/
CREATE TABLE APP_BORD_PLCY_AUTH_MAP(
		BORD_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Board ID',
		PLCY_TYPE                     		VARCHAR(128)		 NOT NULL COMMENT 'Policy Type',
		AUTH_ID                       		VARCHAR(32)		 NOT NULL COMMENT 'Authority Id'
);

ALTER TABLE APP_BORD_PLCY_AUTH_MAP comment 'Table25';
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.BORD_ID is 'Board ID'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.PLCY_TYPE is 'Policy Type'; */
/* Moved to CREATE TABLE
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.AUTH_ID is 'Authority Id'; */



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

ALTER TABLE APP_MENU_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_PLCY_AUTH_MAP_PK PRIMARY KEY (MENU_ID, PLCY_TYPE, AUTH_ID);
ALTER TABLE APP_MENU_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_PLCY_AUTH_MAP_FK0 FOREIGN KEY (MENU_ID) REFERENCES APP_MENU_INFO (MENU_ID);
ALTER TABLE APP_MENU_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_MENU_PLCY_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_PK PRIMARY KEY (ROLE_ID, AUTH_ID);
ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_FK0 FOREIGN KEY (ROLE_ID) REFERENCES APP_ROLE_INFO (ROLE_ID);
ALTER TABLE APP_ROLE_AUTH_MAP ADD CONSTRAINT IDX_APP_ROLE_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_USER_INFO ADD CONSTRAINT IDX_APP_USER_INFO_PK PRIMARY KEY (USER_ID);

ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_PK PRIMARY KEY (USER_ID, AUTH_ID);
ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);
ALTER TABLE APP_USER_AUTH_MAP ADD CONSTRAINT IDX_APP_USER_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);

ALTER TABLE APP_USER_GROP_MAP ADD CONSTRAINT IDX_APP_USER_GROP_MAP_PK PRIMARY KEY (USER_ID, GROP_ID);
ALTER TABLE APP_USER_GROP_MAP ADD CONSTRAINT IDX_APP_USER_GROP_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);
ALTER TABLE APP_USER_GROP_MAP ADD CONSTRAINT IDX_APP_USER_GROP_MAP_FK1 FOREIGN KEY (GROP_ID) REFERENCES APP_GROP_INFO (GROP_ID);

ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_PK PRIMARY KEY (USER_ID, ROLE_ID);
ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);
ALTER TABLE APP_USER_ROLE_MAP ADD CONSTRAINT IDX_APP_USER_ROLE_MAP_FK1 FOREIGN KEY (ROLE_ID) REFERENCES APP_ROLE_INFO (ROLE_ID);

ALTER TABLE APP_PROP_INFO ADD CONSTRAINT IDX_APP_PROP_INFO_PK PRIMARY KEY (PROP_ID);

ALTER TABLE APP_MSG_INFO ADD CONSTRAINT IDX_APP_MSG_INFO_PK PRIMARY KEY (MSG_ID);

ALTER TABLE APP_BORD_INFO ADD CONSTRAINT IDX_APP_BORD_INFO_PK PRIMARY KEY (BORD_ID);

ALTER TABLE APP_BORD_CATE_INFO ADD CONSTRAINT IDX_APP_BORD_CATE_INFO_PK PRIMARY KEY (BORD_ID, CATE_ID);
ALTER TABLE APP_BORD_CATE_INFO ADD CONSTRAINT IDX_APP_BORD_CATE_INFO_FK0 FOREIGN KEY (BORD_ID) REFERENCES APP_BORD_INFO (BORD_ID);

ALTER TABLE APP_ATCL_INFO ADD CONSTRAINT IDX_APP_ATCL_INFO_PK PRIMARY KEY (ATCL_ID);
ALTER TABLE APP_ATCL_INFO ADD CONSTRAINT IDX_APP_ATCL_INFO_FK0 FOREIGN KEY (BORD_ID) REFERENCES APP_BORD_INFO (BORD_ID);
ALTER TABLE APP_ATCL_INFO ADD CONSTRAINT IDX_APP_ATCL_INFO_FK1 FOREIGN KEY (BORD_ID,CATE_ID) REFERENCES APP_BORD_CATE_INFO (BORD_ID,CATE_ID);
CREATE INDEX IDX_APP_ATCL_INFO_1 ON APP_ATCL_INFO (BORD_ID, ATCL_RGST_DTTM, CATE_ID);

ALTER TABLE APP_ATCL_RPLY_INFO ADD CONSTRAINT IDX_APP_ATCL_RPLY_INFO_PK PRIMARY KEY (ATCL_ID, RPLY_ID);
ALTER TABLE APP_ATCL_RPLY_INFO ADD CONSTRAINT IDX_APP_ATCL_RPLY_INFO_FK0 FOREIGN KEY (ATCL_ID) REFERENCES APP_ATCL_INFO (ATCL_ID);

ALTER TABLE APP_ATCL_FILE_INFO ADD CONSTRAINT IDX_APP_ATCL_FILE_INFO_PK PRIMARY KEY (ATCL_ID, FILE_ID);
ALTER TABLE APP_ATCL_FILE_INFO ADD CONSTRAINT IDX_APP_ATCL_FILE_INFO_FK0 FOREIGN KEY (ATCL_ID) REFERENCES APP_ATCL_INFO (ATCL_ID);

ALTER TABLE APP_USER_LOGN_HIST ADD CONSTRAINT IDX_APP_USER_LOGN_HIST_PK PRIMARY KEY (USER_ID, LOGN_DTTM);
ALTER TABLE APP_USER_LOGN_HIST ADD CONSTRAINT IDX_APP_USER_LOGN_HIST_FK0 FOREIGN KEY (USER_ID) REFERENCES APP_USER_INFO (USER_ID);

ALTER TABLE APP_BORD_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_BORD_PLCY_AUTH_MAP_PK PRIMARY KEY (BORD_ID, PLCY_TYPE, AUTH_ID);
ALTER TABLE APP_BORD_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_BORD_PLCY_AUTH_MAP_FK0 FOREIGN KEY (BORD_ID) REFERENCES APP_BORD_INFO (BORD_ID);
ALTER TABLE APP_BORD_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_BORD_PLCY_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);


INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN','Y','Administrator');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_AUTHORITY','Y','Administrator Authority Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_GROUP','Y','Administrator Group Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_MONITOR','Y','Administrator Monitor');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_ROLE','Y','Administrator Role Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_USER','Y','Administrator User Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_MENU','Y','Administrator Menu Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_PROPERTY','Y','Administrator Property Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_CODE','Y','Administrator Code Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_MESSAGE','Y','Administrator Message Manage');
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_BOARD','Y','Administrator Board Manage');


INSERT INTO APP_CD_INFO (CD_ID,CD_NAME,CD_DESC) VALUES ('SAMPLE_CD','Sample Code Name','Sample Code Description');

INSERT INTO APP_CD_ITEM_INFO (CD_ID, CD_ITEM_ID, CD_ITEM_NAME, DISP_SEQ) VALUES ('SAMPLE_CD', 'ITEM_1','Sample Code Item Name', 1);
INSERT INTO APP_CD_ITEM_INFO (CD_ID, CD_ITEM_ID, CD_ITEM_NAME, DISP_SEQ) VALUES ('SAMPLE_CD', 'ITEM_2','Sample Code Item Name', 2);
INSERT INTO APP_CD_ITEM_INFO (CD_ID, CD_ITEM_ID, CD_ITEM_NAME, DISP_SEQ) VALUES ('SAMPLE_CD', 'ITEM_3','Sample Code Item Name', 3);
INSERT INTO APP_CD_ITEM_INFO (CD_ID, CD_ITEM_ID, CD_ITEM_NAME, DISP_SEQ) VALUES ('SAMPLE_CD', 'ITEM_4','Sample Code Item Name', 4);
INSERT INTO APP_CD_ITEM_INFO (CD_ID, CD_ITEM_ID, CD_ITEM_NAME, DISP_SEQ) VALUES ('SAMPLE_CD', 'ITEM_5','Sample Code Item Name', 5);

INSERT INTO APP_USER_INFO (USER_ID,SYS_DATA_YN,USER_NAME,USER_PWD,USER_EMIL,USER_PHON,USER_LC,USER_STAT,USER_NICK) VALUES ('admin','Y','Administrator','$2a$10$ELs7fvpZii3P.KFYaQEJfOeN3iFjQefnY.SXPnLh6mtENV1Rvxc/C','admin@oopscraft.net','010-1234-1234', 'en_US','ACTIVE','Admin');


INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_MONITOR');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_USER');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_GROUP');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_ROLE');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_AUTHORITY');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_PROPERTY');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_MESSAGE');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_CODE');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_MENU');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_PAGE');
INSERT INTO APP_USER_AUTH_MAP (USER_ID, AUTH_ID) VALUES ('admin', 'ADMIN_BOARD');
