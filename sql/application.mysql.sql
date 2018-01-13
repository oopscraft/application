SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 공통_코드_정보
CREATE TABLE app_cd_info
(
	cd_id varchar(32) NOT NULL COMMENT '코드_아이디',
	cd_nm varchar(100) NOT NULL COMMENT '코드_명',
	cd_desc varchar(4000) COMMENT '코드_설명',
	PRIMARY KEY (cd_id)
) COMMENT = '공통_코드_정보';


-- 공통_코드_아이템
CREATE TABLE app_cd_item
(
	cd_id varchar(32) NOT NULL COMMENT '코드_아이디',
	item_id varchar(32) NOT NULL COMMENT '아이템_아이디',
	item_nm varchar(100) COMMENT '아이템_명',
	item_desc varchar(4000) COMMENT '아이템_설명',
	PRIMARY KEY (cd_id, item_id)
) COMMENT = '공통_코드_아이템';


-- 공통_그룹_정보
CREATE TABLE core_group_info
(
	group_id varchar(32) NOT NULL COMMENT '그룹_아이디',
	upper_group_id varchar(32) COMMENT '상위_그룹_아이디',
	group_nm varchar(100) COMMENT '그룹_명',
	group_desc varchar(4000) COMMENT '그룹_설명',
	PRIMARY KEY (group_id)
) COMMENT = '공통_그룹_정보';


-- 공통_그룹_롤_관계
CREATE TABLE core_group_role_rel
(
	group_id varchar(32) NOT NULL COMMENT '그룹_아이디',
	role_id varchar(32) NOT NULL COMMENT '롤_아이디',
	PRIMARY KEY (group_id, role_id)
) COMMENT = '공통_그룹_롤_관계';


-- 공통_메뉴정보
CREATE TABLE core_menu_info
(
	menu_id varchar(32) NOT NULL COMMENT '메뉴_아이디',
	upper_menu_id varchar(32) NOT NULL COMMENT '상위_메뉴_아이디',
	menu_uri varchar(256) COMMENT '메뉴_경로',
	role_need_yn varchar(1) COMMENT '롤_필요_여부',
	menu_nm varchar(100) COMMENT '메뉴_명',
	menu_desc varchar(4000) COMMENT '메뉴_설명',
	PRIMARY KEY (menu_id)
) COMMENT = '공통_메뉴정보';


-- 공통_메뉴_롤_관계
CREATE TABLE core_menu_role_rel
(
	menu_id varchar(32) NOT NULL COMMENT '메뉴_아이디',
	role_id varchar(32) NOT NULL COMMENT '롤_아이디',
	PRIMARY KEY (menu_id, role_id)
) COMMENT = '공통_메뉴_롤_관계';


-- 공통_권한_정보
CREATE TABLE core_priv_info
(
	priv_id varchar(32) NOT NULL COMMENT '권한_아이디',
	priv_nm varchar(100) COMMENT '권한_명',
	priv_desc varchar(4000) COMMENT '권한_설명',
	PRIMARY KEY (priv_id)
) COMMENT = '공통_권한_정보';


-- 공통_롤_정보
CREATE TABLE core_role_info
(
	role_id varchar(32) NOT NULL COMMENT '롤_아이디',
	role_nm varchar(100) COMMENT ' 롤_명',
	role_desc varchar(4000) COMMENT '롤_설명',
	PRIMARY KEY (role_id)
) COMMENT = '공통_롤_정보';


-- 공통_롤_권한_관계
CREATE TABLE core_role_priv_rel
(
	role_id varchar(32) NOT NULL COMMENT '롤_아이디',
	priv_id varchar(32) NOT NULL COMMENT '권한_아이디',
	PRIMARY KEY (role_id, priv_id)
) COMMENT = '공통_롤_권한_관계';


-- 공통_사용자_그룹_관계
CREATE TABLE core_user_group_rel
(
	user_id varchar(32) NOT NULL COMMENT '사용자_아이디',
	group_id varchar(32) NOT NULL COMMENT '그룹_아이디',
	PRIMARY KEY (user_id, group_id)
) COMMENT = '공통_사용자_그룹_관계';


-- 공통_사용자_정보
CREATE TABLE core_user_info
(
	user_id varchar(32) NOT NULL COMMENT '사용자_아이디',
	user_email varchar(100) COMMENT '사용자_이메일',
	user_mblno varchar(32) COMMENT '사용자_이동전화번호',
	user_nm varchar(100) COMMENT '사용자_명',
	user_pwd varchar(256) COMMENT '사용자_암호',
	PRIMARY KEY (user_id)
) COMMENT = '공통_사용자_정보';


-- 공통_사용자_속성
CREATE TABLE core_user_prop
(
	prop_id varchar(32) NOT NULL COMMENT '속성_아이디',
	prop_nm varchar(100) COMMENT '속성_명',
	prop_desc varchar(4000) COMMENT '속성_설명',
	PRIMARY KEY (prop_id)
) COMMENT = '공통_사용자_속성';


-- 공통_사용자_롤_관계
CREATE TABLE core_user_role_rel
(
	user_id varchar(32) NOT NULL COMMENT '사용자_아이디',
	role_id varchar(32) NOT NULL COMMENT '롤_아이디',
	PRIMARY KEY (user_id, role_id)
) COMMENT = '공통_사용자_롤_관계';


-- 사용자_속성_값
CREATE TABLE user_props_val
(
	user_id varchar(32) NOT NULL COMMENT '사용자_아이디',
	prop_id varchar(32) NOT NULL COMMENT '속성_아이디',
	prop_val varchar(100) COMMENT '속성_값',
	PRIMARY KEY (user_id, prop_id)
) COMMENT = '사용자_속성_값';



