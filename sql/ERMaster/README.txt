(2019.09.06)
원 제작자가 sourceforge(https://sourceforge.net/projects/ermaster)를 통해 한글 지원 버전을 배포하고 있습니다.
그런데, 국산DBMS(Tibero, CUBRID) 지원 부분은 하다만듯 합니다. class 파일을 제외한 프로퍼티 류의 소스만 일부 반영되어 있어 전체소스를 github에 공개합니다.
Tibero, CUBRID 지원과 한글 테이블정의서(xls) 포함되었습니다.
http://justinkwony.github.io
https://github.com/justinkwony/ermaster-nhit
(2014.04.15)
Open Source ERD도구인 ERMaster를 한글화 및 국산DBMS(Tibero, CUBRID) 지원 기능 추가

* 변경사항
Table 편집시 인덱스 컬럼 중복 생성됨 수정
Table Edit Dialog에서 Index Tab의 구분자를 일련번호에서 인덱스명으로 변경
Outline window에서 Index Node의 인덱스 수정시 Table Node의 Table/Index 반영 안됨 수정

* Known Bugs/Issues
Tibero
- 발견하시면 Report해주세요.
CUBRID
- Schema 개념 없음
- Context Menu/가져오기/데이터베이스 : Auto Increment Column의 Seed, Increment 값을 가져오지 못함
- Context Menu/가져오기/데이터베이스 : Trigger Source(SQL) 가져오지 못함(소스 보는 방법 아시는 분 알려주세요)

The purpose of this site is to distribute ERMaster, includes Korean and Tibero, CUBRID Database support.

ERMaster is ER-Diagram editor(Eclipse Plug-in). ER-Win alternative.

Korean version
Eclipse Plug-in Update site :	http://justinkwony.github.io/ermaster-nhit/update-site/
Download zip :	org.insightech.er_1.0.0.v20150619-0219-nhit.zip
 
Original site
URL :	http://ermaster.sourceforge.net , http://sourceforge.net/project/ermaster
Eclipse Plug-in Update site :	http://ermaster.sourceforge.net/update-site -> http://sourceforge.net/project/ermaster/files/ermaster
Install
Eclipse Menu -> Help -> Install New Software... -> Work with : [type Update_site_url or select zip_file]
or extract zip_file to eclipse directory.