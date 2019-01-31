/**********************************/
/* Table Name: Application Authority Info */
/**********************************/
CREATE TABLE APP_AUTH_INFO(
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		AUTH_NAME                     		VARCHAR2(256)		 NULL ,
		AUTH_DESC                     		VARCHAR2(4000)		 NULL 
);

COMMENT ON TABLE APP_AUTH_INFO is 'Application Authority Info';
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_ID is 'Authority Id';
COMMENT ON COLUMN APP_AUTH_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_AUTH_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_AUTH_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_AUTH_INFO.SYS_UPDT_DTTM is 'System Update Datetime';
COMMENT ON COLUMN APP_AUTH_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_NAME is 'Authority Name';
COMMENT ON COLUMN APP_AUTH_INFO.AUTH_DESC is 'Authority Description';


/**********************************/
/* Table Name: Application Code Info */
/**********************************/
CREATE TABLE APP_CD_INFO(
		CD_ID                         		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		CD_NAME                       		VARCHAR2(256)		 NULL ,
		CD_DESC                       		VARCHAR2(4000)		 NULL 
);

COMMENT ON TABLE APP_CD_INFO is 'Application Code Info';
COMMENT ON COLUMN APP_CD_INFO.CD_ID is 'Code Id';
COMMENT ON COLUMN APP_CD_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_CD_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_CD_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_CD_INFO.SYS_UPDT_DTTM is 'System Update Datetime';
COMMENT ON COLUMN APP_CD_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_CD_INFO.CD_NAME is 'Code Name';
COMMENT ON COLUMN APP_CD_INFO.CD_DESC is 'Code Description';


/**********************************/
/* Table Name: Application Code Item Info */
/**********************************/
CREATE TABLE APP_CD_ITEM_INFO(
		CD_ID                         		VARCHAR2(32)		 NOT NULL,
		CD_ITEM_ID                    		VARCHAR2(32)		 NOT NULL,
		CD_ITEM_NAME                  		VARCHAR2(256)		 NULL ,
		DISP_SEQ                      		NUMBER		 NULL 
);

COMMENT ON TABLE APP_CD_ITEM_INFO is 'Application Code Item Info';
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ID is 'Code Id';
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ITEM_ID is 'Code Item Id';
COMMENT ON COLUMN APP_CD_ITEM_INFO.CD_ITEM_NAME is 'Code Item Value';
COMMENT ON COLUMN APP_CD_ITEM_INFO.DISP_SEQ is 'Display Sequence';


/**********************************/
/* Table Name: Application Group Info */
/**********************************/
CREATE TABLE APP_GROP_INFO(
		GROP_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		UPER_GROP_ID                  		VARCHAR2(32)		 NULL ,
		GROP_NAME                     		VARCHAR2(256)		 NULL ,
		GROP_DESC                     		VARCHAR2(4000)		 NULL ,
		DISP_SEQ                      		NUMBER		 NULL 
);

COMMENT ON TABLE APP_GROP_INFO is 'Application Group Info';
COMMENT ON COLUMN APP_GROP_INFO.GROP_ID is 'Group Id';
COMMENT ON COLUMN APP_GROP_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_GROP_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_GROP_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_GROP_INFO.SYS_UPDT_DTTM is 'System Update Datetime';
COMMENT ON COLUMN APP_GROP_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_GROP_INFO.UPER_GROP_ID is 'Upper Group ID';
COMMENT ON COLUMN APP_GROP_INFO.GROP_NAME is 'Group Name';
COMMENT ON COLUMN APP_GROP_INFO.GROP_DESC is 'Group Description';
COMMENT ON COLUMN APP_GROP_INFO.DISP_SEQ is 'Display Sequence';


/**********************************/
/* Table Name: Application Group Authority Map */
/**********************************/
CREATE TABLE APP_GROP_AUTH_MAP(
		GROP_ID                       		VARCHAR2(32)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_GROP_AUTH_MAP is 'Application Group Authority Map';
COMMENT ON COLUMN APP_GROP_AUTH_MAP.GROP_ID is 'Group Id';
COMMENT ON COLUMN APP_GROP_AUTH_MAP.AUTH_ID is 'Authority Id';


/**********************************/
/* Table Name: Application Role Info */
/**********************************/
CREATE TABLE APP_ROLE_INFO(
		ROLE_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		ROLE_NAME                     		VARCHAR2(256)		 NULL ,
		ROLE_DESC                     		VARCHAR2(4000)		 NULL 
);

COMMENT ON TABLE APP_ROLE_INFO is 'Application Role Info';
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_ID is 'Role Id';
COMMENT ON COLUMN APP_ROLE_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_ROLE_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_ROLE_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_ROLE_INFO.SYS_UPDT_DTTM is 'System Update Datetime';
COMMENT ON COLUMN APP_ROLE_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_NAME is 'Role Name';
COMMENT ON COLUMN APP_ROLE_INFO.ROLE_DESC is 'Role Description';


/**********************************/
/* Table Name: Application Group Role Map */
/**********************************/
CREATE TABLE APP_GROP_ROLE_MAP(
		GROP_ID                       		VARCHAR2(32)		 NOT NULL,
		ROLE_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_GROP_ROLE_MAP is 'Application Group Role Map';
COMMENT ON COLUMN APP_GROP_ROLE_MAP.GROP_ID is 'Group Id';
COMMENT ON COLUMN APP_GROP_ROLE_MAP.ROLE_ID is 'Role Id';


/**********************************/
/* Table Name: Application Menu Information */
/**********************************/
CREATE TABLE APP_MENU_INFO(
		MENU_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		UPER_MENU_ID                  		VARCHAR2(32)		 NULL ,
		MENU_NAME                     		VARCHAR2(256)		 NULL ,
		MENU_ICON                     		CLOB		 NULL ,
		MENU_LINK                     		VARCHAR2(256)		 NULL ,
		MENU_DESC                     		VARCHAR2(4000)		 NULL ,
		DISP_PLCY                     		VARCHAR2(128)		 NULL ,
		DISP_SEQ                      		NUMBER		 NULL 
);

COMMENT ON TABLE APP_MENU_INFO is 'Application Menu Information';
COMMENT ON COLUMN APP_MENU_INFO.MENU_ID is 'Menu Id';
COMMENT ON COLUMN APP_MENU_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_MENU_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_MENU_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_MENU_INFO.SYS_UPDT_DTTM is 'System Updte Datetime';
COMMENT ON COLUMN APP_MENU_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_MENU_INFO.UPER_MENU_ID is 'Upper Menu Id';
COMMENT ON COLUMN APP_MENU_INFO.MENU_NAME is 'Menu Name';
COMMENT ON COLUMN APP_MENU_INFO.MENU_ICON is 'Menu Icon';
COMMENT ON COLUMN APP_MENU_INFO.MENU_LINK is 'Menu Link';
COMMENT ON COLUMN APP_MENU_INFO.MENU_DESC is 'Menu Description';
COMMENT ON COLUMN APP_MENU_INFO.DISP_PLCY is 'Display Policy';
COMMENT ON COLUMN APP_MENU_INFO.DISP_SEQ is 'DisplaySeq';


/**********************************/
/* Table Name: Application Menu Policy Authority Map */
/**********************************/
CREATE TABLE APP_MENU_PLCY_AUTH_MAP(
		MENU_ID                       		VARCHAR2(32)		 NOT NULL,
		PLCY_TYPE                     		VARCHAR2(128)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_MENU_PLCY_AUTH_MAP is 'Application Menu Policy Authority Map';
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.MENU_ID is 'Menu Id';
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.PLCY_TYPE is 'Policy Type';
COMMENT ON COLUMN APP_MENU_PLCY_AUTH_MAP.AUTH_ID is 'Authority ID';


/**********************************/
/* Table Name: Application Role Authority Map */
/**********************************/
CREATE TABLE APP_ROLE_AUTH_MAP(
		ROLE_ID                       		VARCHAR2(32)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_ROLE_AUTH_MAP is 'Application Role Authority Map';
COMMENT ON COLUMN APP_ROLE_AUTH_MAP.ROLE_ID is 'Role Id';
COMMENT ON COLUMN APP_ROLE_AUTH_MAP.AUTH_ID is 'Authority Id';


/**********************************/
/* Table Name: Application User Info */
/**********************************/
CREATE TABLE APP_USER_INFO(
		USER_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		USER_PWD                      		VARCHAR2(256)		 NULL ,
		USER_EMIL                     		VARCHAR2(256)		 NULL ,
		USER_LC                       		VARCHAR2(8)		 NULL ,
		USER_PHON                     		VARCHAR2(16)		 NULL ,
		USER_NAME                     		VARCHAR2(256)		 NULL ,
		USER_STAT                     		VARCHAR2(16)		 NULL ,
		USER_NICK                     		VARCHAR2(256)		 NULL ,
		USER_AVAT                     		CLOB		 NULL ,
		USER_SIGN                     		VARCHAR2(4000)		 NULL ,
		USER_JOIN_DTTM                		DATE		 NULL ,
		USER_CLOS_DTTM                		DATE		 NULL 
);

COMMENT ON TABLE APP_USER_INFO is 'Application User Info';
COMMENT ON COLUMN APP_USER_INFO.USER_ID is 'User Id';
COMMENT ON COLUMN APP_USER_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_USER_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_USER_INFO.SYS_INST_USER_ID is 'System Insert User id';
COMMENT ON COLUMN APP_USER_INFO.SYS_UPDT_DTTM is 'System Update Datetime';
COMMENT ON COLUMN APP_USER_INFO.SYS_UPDT_USER_ID is 'System Update User Id';
COMMENT ON COLUMN APP_USER_INFO.USER_PWD is 'User Password';
COMMENT ON COLUMN APP_USER_INFO.USER_EMIL is 'User Email';
COMMENT ON COLUMN APP_USER_INFO.USER_LC is 'User Locale';
COMMENT ON COLUMN APP_USER_INFO.USER_PHON is 'User Phone';
COMMENT ON COLUMN APP_USER_INFO.USER_NAME is 'User Name';
COMMENT ON COLUMN APP_USER_INFO.USER_STAT is 'User Status';
COMMENT ON COLUMN APP_USER_INFO.USER_NICK is 'User Nickname';
COMMENT ON COLUMN APP_USER_INFO.USER_AVAT is 'User Avatar';
COMMENT ON COLUMN APP_USER_INFO.USER_SIGN is 'User Signature';
COMMENT ON COLUMN APP_USER_INFO.USER_JOIN_DTTM is 'User Join Datetime';
COMMENT ON COLUMN APP_USER_INFO.USER_CLOS_DTTM is 'User Close Datetime';


/**********************************/
/* Table Name: Application User Authority Map */
/**********************************/
CREATE TABLE APP_USER_AUTH_MAP(
		USER_ID                       		VARCHAR2(32)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_USER_AUTH_MAP is 'Application User Authority Map';
COMMENT ON COLUMN APP_USER_AUTH_MAP.USER_ID is 'User Id';
COMMENT ON COLUMN APP_USER_AUTH_MAP.AUTH_ID is 'Authority Id';


/**********************************/
/* Table Name: Application User Group Map */
/**********************************/
CREATE TABLE APP_USER_GROP_MAP(
		USER_ID                       		VARCHAR2(32)		 NOT NULL,
		GROP_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_USER_GROP_MAP is 'Application User Group Map';
COMMENT ON COLUMN APP_USER_GROP_MAP.USER_ID is 'User ID';
COMMENT ON COLUMN APP_USER_GROP_MAP.GROP_ID is 'Group ID';


/**********************************/
/* Table Name: Application User Role Map */
/**********************************/
CREATE TABLE APP_USER_ROLE_MAP(
		USER_ID                       		VARCHAR2(32)		 NOT NULL,
		ROLE_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_USER_ROLE_MAP is 'Application User Role Map';
COMMENT ON COLUMN APP_USER_ROLE_MAP.USER_ID is 'User Id';
COMMENT ON COLUMN APP_USER_ROLE_MAP.ROLE_ID is 'Role Id';


/**********************************/
/* Table Name: Application Property Info */
/**********************************/
CREATE TABLE APP_PROP_INFO(
		PROP_ID                       		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		PROP_NAME                     		VARCHAR2(256)		 NULL ,
		PROP_VAL                      		CLOB		 NULL ,
		PROP_DESC                     		VARCHAR2(4000)		 NULL 
);

COMMENT ON TABLE APP_PROP_INFO is 'Application Property Info';
COMMENT ON COLUMN APP_PROP_INFO.PROP_ID is 'Property ID';
COMMENT ON COLUMN APP_PROP_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_PROP_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_PROP_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_PROP_INFO.SYS_UPDT_DTTM is 'System Updte Datetime';
COMMENT ON COLUMN APP_PROP_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_PROP_INFO.PROP_NAME is 'Property Name';
COMMENT ON COLUMN APP_PROP_INFO.PROP_VAL is 'Property Value';
COMMENT ON COLUMN APP_PROP_INFO.PROP_DESC is 'Property Description';


/**********************************/
/* Table Name: Application Message Info */
/**********************************/
CREATE TABLE APP_MSG_INFO(
		MSG_ID                        		VARCHAR2(32)		 NOT NULL,
		SYS_DATA_YN                   		VARCHAR2(1)		 NULL ,
		SYS_INST_DTTM                 		DATE		 NULL ,
		SYS_INST_USER_ID              		VARCHAR2(32)		 NULL ,
		SYS_UPDT_DTTM                 		DATE		 NULL ,
		SYS_UPDT_USER_ID              		VARCHAR2(32)		 NULL ,
		MSG_NAME                      		VARCHAR2(256)		 NULL ,
		MSG_VAL                       		CLOB		 NULL ,
		MSG_DESC                      		VARCHAR2(4000)		 NULL 
);

COMMENT ON TABLE APP_MSG_INFO is 'Application Message Info';
COMMENT ON COLUMN APP_MSG_INFO.MSG_ID is 'Message ID';
COMMENT ON COLUMN APP_MSG_INFO.SYS_DATA_YN is 'System Data Yn';
COMMENT ON COLUMN APP_MSG_INFO.SYS_INST_DTTM is 'System Insert Datetime';
COMMENT ON COLUMN APP_MSG_INFO.SYS_INST_USER_ID is 'System Insert User ID';
COMMENT ON COLUMN APP_MSG_INFO.SYS_UPDT_DTTM is 'System Updte Datetime';
COMMENT ON COLUMN APP_MSG_INFO.SYS_UPDT_USER_ID is 'System Update User ID';
COMMENT ON COLUMN APP_MSG_INFO.MSG_NAME is 'Message Name';
COMMENT ON COLUMN APP_MSG_INFO.MSG_VAL is 'Message Value';
COMMENT ON COLUMN APP_MSG_INFO.MSG_DESC is 'Message Description';


/**********************************/
/* Table Name: Application Board */
/**********************************/
CREATE TABLE APP_BORD_INFO(
		BORD_ID                       		VARCHAR2(32)		 NOT NULL,
		BORD_NAME                     		VARCHAR2(256)		 NULL ,
		BORD_ICON                     		CLOB		 NULL ,
		BORD_SKIN                     		VARCHAR2(128)		 NULL ,
		ACES_PLCY                     		VARCHAR2(128)		 NULL ,
		READ_PLCY                     		VARCHAR2(128)		 NULL ,
		WRIT_PLCY                     		VARCHAR2(128)		 NULL ,
		ROWS_PER_PAGE                 		NUMBER		 NULL ,
		CATE_USE_YN                   		VARCHAR2(1)		 NULL ,
		RPLY_USE_YN                   		VARCHAR2(1)		 NULL ,
		FILE_USE_YN                   		VARCHAR2(1)		 NULL 
);

COMMENT ON TABLE APP_BORD_INFO is 'Application Board';
COMMENT ON COLUMN APP_BORD_INFO.BORD_ID is 'Board ID';
COMMENT ON COLUMN APP_BORD_INFO.BORD_NAME is 'Board Name';
COMMENT ON COLUMN APP_BORD_INFO.BORD_ICON is 'Board Icon';
COMMENT ON COLUMN APP_BORD_INFO.BORD_SKIN is 'Board Skin';
COMMENT ON COLUMN APP_BORD_INFO.ACES_PLCY is 'Access Policy';
COMMENT ON COLUMN APP_BORD_INFO.READ_PLCY is 'Read Policy';
COMMENT ON COLUMN APP_BORD_INFO.WRIT_PLCY is 'Write Policy';
COMMENT ON COLUMN APP_BORD_INFO.ROWS_PER_PAGE is 'Rows Per Page';
COMMENT ON COLUMN APP_BORD_INFO.CATE_USE_YN is 'Category Use Yn';
COMMENT ON COLUMN APP_BORD_INFO.RPLY_USE_YN is 'Reply Use';
COMMENT ON COLUMN APP_BORD_INFO.FILE_USE_YN is 'File Use';


/**********************************/
/* Table Name: Application Board Category */
/**********************************/
CREATE TABLE APP_BORD_CATE_INFO(
		BORD_ID                       		VARCHAR2(32)		 NOT NULL,
		CATE_ID                       		VARCHAR2(32)		 NOT NULL,
		CATE_NAME                     		VARCHAR2(256)		 NULL ,
		DISP_SEQ                      		NUMBER		 NULL 
);

COMMENT ON TABLE APP_BORD_CATE_INFO is 'Application Board Category';
COMMENT ON COLUMN APP_BORD_CATE_INFO.BORD_ID is 'Board ID';
COMMENT ON COLUMN APP_BORD_CATE_INFO.CATE_ID is 'Cateogry ID';
COMMENT ON COLUMN APP_BORD_CATE_INFO.CATE_NAME is 'Category Name';
COMMENT ON COLUMN APP_BORD_CATE_INFO.DISP_SEQ is 'Display Sequence';


/**********************************/
/* Table Name: Application Board Article */
/**********************************/
CREATE TABLE APP_ATCL_INFO(
		ATCL_ID                       		VARCHAR2(32)		 NOT NULL,
		BORD_ID                       		VARCHAR2(32)		 NULL ,
		CATE_ID                       		VARCHAR2(32)		 NULL ,
		ATCL_TITL                     		VARCHAR2(256)		 NULL ,
		ATCL_CNTS                     		CLOB		 NULL ,
		ATCL_USER_ID                  		VARCHAR2(32)		 NULL ,
		ATCL_USER_NICK                		VARCHAR2(256)		 NULL ,
		ATCL_RGST_DTTM                		DATE		 NULL ,
		ATCL_MDFY_DTTM                		DATE		 NULL ,
		READ_CNT                      		NUMBER		 DEFAULT 0		 NULL 
);

COMMENT ON TABLE APP_ATCL_INFO is 'Application Board Article';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_ID is 'Article ID';
COMMENT ON COLUMN APP_ATCL_INFO.BORD_ID is 'Board ID';
COMMENT ON COLUMN APP_ATCL_INFO.CATE_ID is 'Cateogry ID';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_TITL is 'Article Title';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_CNTS is 'Article Contents';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_USER_ID is 'Article User ID';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_USER_NICK is 'Article User Nickname';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_RGST_DTTM is 'Article Registration Datetime';
COMMENT ON COLUMN APP_ATCL_INFO.ATCL_MDFY_DTTM is 'Article Modify Datetime';
COMMENT ON COLUMN APP_ATCL_INFO.READ_CNT is 'Read Count';


/**********************************/
/* Table Name: Application Board Article Reply Info */
/**********************************/
CREATE TABLE APP_ATCL_RPLY_INFO(
		ATCL_ID                       		VARCHAR2(32)		 NOT NULL,
		RPLY_ID                       		VARCHAR2(32)		 NOT NULL,
		UPER_RPLY_ID                  		VARCHAR2(32)		 NULL ,
		RPLY_SEQ                      		NUMBER		 NULL ,
		RPLY_LEVL                     		VARCHAR2(8)		 NULL ,
		RPLY_CNTS                     		CLOB		 NULL ,
		RPLY_USER_ID                  		VARCHAR2(32)		 NULL ,
		RPLY_USER_NICK                		VARCHAR2(256)		 NULL ,
		RPLY_RGST_DTTM                		DATE		 NULL ,
		RPLY_MDFY_DTTM                		DATE		 NULL 
);

COMMENT ON TABLE APP_ATCL_RPLY_INFO is 'Application Board Article Reply Info';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.ATCL_ID is 'Article ID';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_ID is 'Reply ID';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.UPER_RPLY_ID is 'Upper Reply ID';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_SEQ is 'Reply Sequence';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_LEVL is 'Reply Level';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_CNTS is 'Reply Contents';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_USER_ID is 'Reply User ID';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_USER_NICK is 'Reply User Nickname';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_RGST_DTTM is 'Reply Registration Datetime';
COMMENT ON COLUMN APP_ATCL_RPLY_INFO.RPLY_MDFY_DTTM is 'Reply Modify Datetime';


/**********************************/
/* Table Name: Application Article File Info */
/**********************************/
CREATE TABLE APP_ATCL_FILE_INFO(
		ATCL_ID                       		VARCHAR2(32)		 NOT NULL,
		FILE_ID                       		VARCHAR2(32)		 NOT NULL,
		FILE_NAME                     		VARCHAR2(256)		 NULL ,
		FILE_TYPE                     		VARCHAR2(128)		 NULL ,
		FILE_SIZE                     		NUMBER		 NULL 
);

COMMENT ON TABLE APP_ATCL_FILE_INFO is 'Application Article File Info';
COMMENT ON COLUMN APP_ATCL_FILE_INFO.ATCL_ID is 'Article ID';
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_ID is 'File ID';
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_NAME is 'File Name';
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_TYPE is 'File Type';
COMMENT ON COLUMN APP_ATCL_FILE_INFO.FILE_SIZE is 'File Size';


/**********************************/
/* Table Name: Application User Login History */
/**********************************/
CREATE TABLE APP_USER_LOGN_HIST(
		USER_ID                       		VARCHAR2(32)		 NOT NULL,
		LOGN_DTTM                     		DATE		 NOT NULL,
		LOGN_SUCS_YN                  		VARCHAR2(1)		 NULL ,
		LOGN_FAIL_RESN                		VARCHAR2(1024)		 NULL ,
		LOGN_IP                       		VARCHAR2(128)		 NULL ,
		LOGN_AGNT                     		VARCHAR2(1024)		 NULL ,
		LOGN_REFR                     		VARCHAR2(1024)		 NULL 
);

COMMENT ON TABLE APP_USER_LOGN_HIST is 'Application User Login History';
COMMENT ON COLUMN APP_USER_LOGN_HIST.USER_ID is 'User Id';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_DTTM is 'Login Datetime';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_SUCS_YN is 'Login Success YN';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_FAIL_RESN is 'Login Fail Reason';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_IP is 'Login IP';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_AGNT is 'Login Agent';
COMMENT ON COLUMN APP_USER_LOGN_HIST.LOGN_REFR is 'Login Referer';


/**********************************/
/* Table Name: Table25 */
/**********************************/
CREATE TABLE APP_BORD_PLCY_AUTH_MAP(
		BORD_ID                       		VARCHAR2(32)		 NOT NULL,
		PLCY_TYPE                     		VARCHAR2(128)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_BORD_PLCY_AUTH_MAP is 'Table25';
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.BORD_ID is 'Board ID';
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.PLCY_TYPE is 'Policy Type';
COMMENT ON COLUMN APP_BORD_PLCY_AUTH_MAP.AUTH_ID is 'Authority Id';


/**********************************/
/* Table Name: Applicaton Page Info */
/**********************************/
CREATE TABLE APP_PAGE_INFO(
		PAGE_ID                       		VARCHAR2(32)		 NOT NULL,
		PAGE_NAME                     		VARCHAR2(256)		 NULL ,
		PAGE_TYPE                     		VARCHAR2(16)		 NULL ,
		PAGE_VAL                      		VARCHAR2(4000)		 NULL ,
		ACES_PLCY                     		VARCHAR2(128)		 NULL 
);

COMMENT ON TABLE APP_PAGE_INFO is 'Applicaton Page Info';
COMMENT ON COLUMN APP_PAGE_INFO.PAGE_ID is 'Page ID';
COMMENT ON COLUMN APP_PAGE_INFO.PAGE_NAME is 'Page Name';
COMMENT ON COLUMN APP_PAGE_INFO.PAGE_TYPE is 'Page Type';
COMMENT ON COLUMN APP_PAGE_INFO.PAGE_VAL is 'Page Value';
COMMENT ON COLUMN APP_PAGE_INFO.ACES_PLCY is 'Access Policy';


/**********************************/
/* Table Name: Application Page Policy Authority Map */
/**********************************/
CREATE TABLE APP_PAGE_PLCY_AUTH_MAP(
		PAGE_ID                       		VARCHAR2(32)		 NOT NULL,
		PLCY_TYPE                     		VARCHAR2(128)		 NOT NULL,
		AUTH_ID                       		VARCHAR2(32)		 NOT NULL
);

COMMENT ON TABLE APP_PAGE_PLCY_AUTH_MAP is 'Application Page Policy Authority Map';
COMMENT ON COLUMN APP_PAGE_PLCY_AUTH_MAP.PAGE_ID is 'Page ID';
COMMENT ON COLUMN APP_PAGE_PLCY_AUTH_MAP.PLCY_TYPE is 'Policy Type';
COMMENT ON COLUMN APP_PAGE_PLCY_AUTH_MAP.AUTH_ID is 'Authority Id';



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

ALTER TABLE APP_PAGE_INFO ADD CONSTRAINT IDX_APP_PAGE_INFO_PK PRIMARY KEY (PAGE_ID);

ALTER TABLE APP_PAGE_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_PAGE_PLCY_AUTH_MAP_PK PRIMARY KEY (PAGE_ID, PLCY_TYPE, AUTH_ID);
ALTER TABLE APP_PAGE_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_PAGE_PLCY_AUTH_MAP_FK0 FOREIGN KEY (PAGE_ID) REFERENCES APP_PAGE_INFO (PAGE_ID);
ALTER TABLE APP_PAGE_PLCY_AUTH_MAP ADD CONSTRAINT IDX_APP_PAGE_PLCY_AUTH_MAP_FK1 FOREIGN KEY (AUTH_ID) REFERENCES APP_AUTH_INFO (AUTH_ID);


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
INSERT INTO APP_AUTH_INFO (AUTH_ID,SYS_DATA_YN,AUTH_NAME) VALUES ('ADMIN_PAGE','Y','Administrator Page Manage');
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
