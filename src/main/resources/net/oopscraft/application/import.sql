INSERT INTO APP_USER_INFO (USER_ID,USER_PASS,USER_NAME,USER_NICK,USER_STAT,USER_PHOT) VALUES ('admin','$2a$10$5BnAGC9KoVzfDprFZbzFyuS0SkgNvTiSIh3noRwCUFLQL78tuomZK','Administrator','Administrator','ACTIVE','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAHCAYAAAD03kbyAAAAU0lEQVR42mNgGChQX1//oqGh4T8yBok1NjY6UiIHws3NzcogjMyHW4pNAT7N6HLInkC3BKelA+pTXC4GYheCBmHRi+wYFHWkWEqMT2HmEHIYXQEAdZgPSWQ/jckAAAAASUVORK5CYII=');
INSERT INTO APP_ROLE_INFO (ROLE_ID,ROLE_NAME,ROLE_ICON) VALUES ('ADMIN','Administrator Role','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAHCAYAAAD03kbyAAAAU0lEQVR42mNgGChQX1//oqGh4T8yBok1NjY6UiIHws3NzcogjMyHW4pNAT7N6HLInkC3BKelA+pTXC4GYheCBmHRi+wYFHWkWEqMT2HmEHIYXQEAdZgPSWQ/jckAAAAASUVORK5CYII=');
INSERT INTO APP_AUTH_INFO (AUTH_ID,AUTH_NAME) VALUES ('ADMIN', 'Administrator Authority');


INSERT INTO APP_AUTH_INFO (AUTH_ID,AUTH_NAME) VALUES ('ADMIN_USER_READ', 'User Manage Authority');

INSERT INTO APP_AUTH_INFO (AUTH_ID,AUTH_NAME) VALUES ('ADMIN_GROUP', 'Group Manage Authority');


INSERT INTO APP_USER_ROLE_MAP (USER_ID,ROLE_ID) VALUES ('admin','ADMIN');
INSERT INTO APP_USER_AUTH_MAP (USER_ID,AUTH_ID) VALUES ('admin','ADMIN');
INSERT INTO APP_USER_AUTH_MAP (USER_ID,AUTH_ID) VALUES ('admin','ADMIN_USER');

-- APP_CODE_INFO
INSERT INTO APP_CODE_INFO (CODE_ID,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,CODE_DESC,CODE_NAME) VALUES ('TEST_CODE_1','N',{ts '2020-02-03 03:38:48.447000000'},null,null,null,'테스트 코드 설명','테스트 코드1');
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_1','ITEM_1',null,'코드 아이템 1',1);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_1','ITEM_2',null,'코드 아이템 2',2);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_1','ITEM_3',null,'코드 아이템 3',3);
INSERT INTO APP_CODE_INFO (CODE_ID,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,CODE_DESC,CODE_NAME) VALUES ('TEST_CODE_2','N',{ts '2020-02-03 03:38:48.447000000'},null,null,null,'테스트 코드 설명','테스트 코드2');
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_2','ITEM_1',null,'코드 아이템 1',1);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_2','ITEM_2',null,'코드 아이템 2',2);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_2','ITEM_3',null,'코드 아이템 3',3);
INSERT INTO APP_CODE_INFO (CODE_ID,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,CODE_DESC,CODE_NAME) VALUES ('TEST_CODE_3','N',{ts '2020-02-03 03:38:48.447000000'},null,null,null,'테스트 코드 설명','테스트 코드3');
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_3','ITEM_1',null,'코드 아이템 1',1);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_3','ITEM_2',null,'코드 아이템 2',2);
INSERT INTO APP_CODE_ITEM_INFO (CODE_ID,ITEM_ID,ITEM_DESC,ITEM_NAME,ITEM_SEQ) VALUES ('TEST_CODE_3','ITEM_3',null,'코드 아이템 3',3);




INSERT INTO APP_BORD_INFO (BORD_ID,ACES_PLCY,CATE_USE_YN,BORD_DESC,FILE_USE_YN,BORD_ICON,BORD_NAME,PLCY_READ,RPLY_USE_YN,ROWS_PER_PAGE,BORD_SKIN,PLCY_WRIT) VALUES ('TEST_BOARD','ANONYMOUS','Y','테스트 게시판','Y',null,'테스트 게시판','ANONYMOUS','Y',20,'default','ANONYMOUS');
INSERT INTO APP_BORD_CATE_INFO (BORD_ID,CATE_ID,CATE_NAME,CATE_SEQ) VALUES ('TEST_BOARD','1','카테고리1',1);
INSERT INTO APP_BORD_CATE_INFO (BORD_ID,CATE_ID,CATE_NAME,CATE_SEQ) VALUES ('TEST_BOARD','2','카테고리2',2);
INSERT INTO APP_BORD_CATE_INFO (BORD_ID,CATE_ID,CATE_NAME,CATE_SEQ) VALUES ('TEST_BOARD','3','카테고리3',3);





INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test1','Test user 1','Test1','test1@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test2','Test user 2','Test2','test2@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test3','Test user 3','Test3','test3@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test4','Test user 4','Test4','test4@oopscraft.net','CLOSED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test5','Test user 5','Test5','test5@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test6','Test user 6','Test6','test6@oopscraft.net','SUSPENDED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test7','Test user 7','Test7','test7@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test8','Test user 8','Test8','test8@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test9','Test user 9','Test9','test9@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test10','Test user 10','Test10','test10@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test11','Test user 1','Test1','test1@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test12','Test user 2','Test2','test2@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test13','Test user 3','Test3','test3@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test14','Test user 4','Test4','test4@oopscraft.net','CLOSED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test15','Test user 5','Test5','test5@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test16','Test user 6','Test6','test6@oopscraft.net','SUSPENDED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test17','Test user 7','Test7','test7@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test18','Test user 8','Test8','test8@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test19','Test user 9','Test9','test9@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test20','Test user 10','Test10','test10@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test21','Test user 1','Test1','test1@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test22','Test user 2','Test2','test2@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test23','Test user 3','Test3','test3@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test24','Test user 4','Test4','test4@oopscraft.net','CLOSED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test25','Test user 5','Test5','test5@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test26','Test user 6','Test6','test6@oopscraft.net','SUSPENDED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test27','Test user 7','Test7','test7@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test28','Test user 8','Test8','test8@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test29','Test user 9','Test9','test9@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test30','Test user 10','Test10','test10@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test31','Test user 4','Test4','test4@oopscraft.net','CLOSED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test32','Test user 5','Test5','test5@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test33','Test user 6','Test6','test6@oopscraft.net','SUSPENDED');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test34','Test user 7','Test7','test7@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test35','Test user 8','Test8','test8@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test36','Test user 9','Test9','test9@oopscraft.net','ACTIVE');
INSERT INTO APP_USER_INFO (USER_ID,USER_NAME,USER_NICK,USER_EMIL,USER_STAT) VALUES ('test37','Test user 10','Test10','test10@oopscraft.net','ACTIVE');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('ADMIN','Y',NULL,'Administrator Group','Administrator Group Description.','data:image/png;base64,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');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('IT','N',NULL,'IT Group','IT Group Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAGkElEQVRYha2V329UaRnHP+85c850fvbHUraiC7NSTJZEKbvojREqF/6IMWUvSCMJSm+82wD+A5iQaLxSkr0xIWSNbkhj3OXCpLrGgJuQAsYwgqFuoaXddhjaaaed6Znz633Peb1oZ2yBLi3Lkzw358fz/bzf5znnEWwxhs8eOZZOtQ3YllXwpCo0fNmxVHeLK67/DzdQ753/092prdZaH0JrvfGCEOtFO2zbOtPZnj/d1ZHvSLUlCaViYdlhfqnOfLXO/EKVfC6D46srleXG0Lt/vb+8HYDEZjeGzx7paM9nr+7d39eX63kdYkVQm6dWeshcdVV8rlJhZ2eOrx96CyMOj33w91vLwNB2AIzNbmTynR9+9bs/7vMzX+TB2B0+Kd6gPPMQU8TkbEWlusSuHe0c3N+LrX0aRo6EiE4N9RcK2wF4pgPvnz6MFxv9H11+lwMHv8Ke3TsROkllZpr5mRkip8HXegz2HXwLw87yn9v/pKqS+CKN1n4BmPpcAFJFPB6/y3feHuCNQ98AQMchrxZ6IayjZZ3y1AyPHi9iihTjD2eQdgciYaOiuB+49rkAQhVhGgZfPvhNSL8CgFAeGDYaDVrS89oXyGaX+OPwX4gjRSQDMp07iOLSVrWBTWbgp7+9QTKVmpKxjUi9ikj1IJJdYOUgkUYYSYSRoC2dItVmYghofjvxE1/VCwEAHNibKea6v4Sw2xHJdki0gWEhhAnCBCEQwsCyLWINMeCs1AGKLwWgO2/+23v8X7Sso8M6RAHEEq0jVuU0WsccenMv3/vWPtLJBEEQALyc/wAwpWqz6K5dIAQoF5QDykNHATqOiCNFLiWILZ+EEVNbrk0Nj85ee1kARW/uE7I9ewDQUbgKIFdAecRKonwP6bksLCwxPlMjUOJ32xGHz2jBvnc+LgbV0pT25tDeHPjzaL+KDuvEKkB6LoGzgl9f5u50Az8SxeHR2Z+/NAAA6a1cCxan0N5CSzwKPcKGg1dbIjbzSCmIo4hY8+3tij8XALhQL00S+Q7KcwgbK3i1JRrVChEZYtIslh+xp10zPDq7reHbEsC+dz4uNhYr11bmH+EszuNUHuPVHES6gJQmkzf+TH3F4763g4GBgf4XAfjMdQzwt9//oi8RNW7vzTUQUUgUBriL0yyWP8Wpe0RK8VF8lNKSj+/7V+I4HhoZGdmyG5sCXLx4scOyrHOWZZ2xbRtZn+H7r1cxtYOzOEf5wQOcwMLZ/QOuji1h2zbVapVSqbQspRwaGRm58sIAFy5c6LAs62pXV1dfd3c3URRx8+ZNfvb2a1jeNM78DOauo2Tf+CF/GP6Aer2O7/tEUUQcx0xOTtJoNH5z+fLls88DeOYMSCk/7Ozs7Ovt7SWdTmPbNkIIvFeOYO46QvvRX5Hv+xFjD6YJggDTNDEMAyklURRRKBTI5XJnBgcHf71tgPPnz/ebptm/f/9+UqkUtm1jmiZBEGCluzB2HcFo6yIMQ27fvt1yrRmu6+L7Pjt37iSZTJ45fvx4/7YApJQDuVyOOI5blmqtSafTWJbVeq5YLDb//U+F4zgAZDIZtNY/Wbss+P/S3BwgDMO+ZDKJ53k0Gg2CIEBKSSqVwjRNAObm5rh3717rHa31hozjmCAISCQSaK1PARmgDUgCNqsrwATEsxzosywL13VxXRfP86hUKmSzWQxj9fHr169vEGw6tT7DMMQ0TaIo4vDhw0fXxJ9M+6llJKXsCIIA3/dbp3Ndl0wmg2EYjI2NUa/XW61pAiiliKKolVrrlmNCiDeBfwEOIAHF6k6Pn+UAYRji+34rpZRks1mklBSLxQ3iSimklCilWhlFEZ7nEcdxs2Y3kAU6Ab0O4Ol1rJRCCLFhwKSU5PN5bt261SrcHFIp5YZsAjWdUEoRx/EeIA9Ea72fbNZ+yoEoiobu3LmD53mtoq7rIoRgfHx8Q7/DMCQIAsIwJAzDDRDNXGvR7rXTd6ydnk0BLl269F6tVhsaHR1dLpVKKKXwfZ+JiYmW9c1rvu8TBEELYj2M7/sotarl+34N6ALKwKfr9TbdBSdPniwA55LJ5Knu7m56enqwLAulVEukKdoc2mZ6nofv+ySTScrlsj8xMfHLhYWFi8CjJw/83G144sSJAnBMa306lUoVMpkMQggSiUSrDVJKgiDAdd0NMI7jTFQqlXOzs7PvPym8ZYD1MTg4WNBa92utCwBa6wNa6444jqeUUtPrZuDa+Ph4qVwu39+02Fr8DxXDUTKPMGJQAAAAAElFTkSuQmCC');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('OPERATOR','N','IT','Operator Group','Operator Group Description.','data:image/png;base64,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');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('DEVELOP','N','IT','Development Group','Development Group Description.','data:image/png;base64,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');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('TEST','N','IT','Test Group','Test Group Description.','data:image/png;base64,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');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('USER','N',NULL,'User Group','User Group Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('SALES','N',NULL,'Sales Department','Sales Department Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('SALES-1','N','SALES','Sales Department','Sales Department Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('QA','N',NULL,'QA Department','QA Department Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('HR','N',NULL,'Human Resource','Human Resource Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('HR2','N','HR','Human Resource','Human Resource Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_GROP_INFO (GROP_ID,SYS_EMBD_YN,UPER_GROP_ID,GROP_NAME,GROP_DESC,GROP_ICON) VALUES ('HR2','N','HR','Human Resource','Human Resource Description.','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAB6VBMVEUAAAD/ZjMrKyv/SST/YCD/VRwzMxpAKyvrYic3JCRbSTftWyRAMCBQQDA6IyNAKyA8KCI+KCJBJiE9KR9AJyJAJiLvXCTwWyI+KR9AKSA/KR9BKB/xWiPxWyXxWiTxWiM/JyDyWiRAKSHxWiPxWiXxWiU+KB8+Jx9AKCDomHFAKSHol24/KCDyWiRAKCE/KB9AKCDre03tekxAKCDxWSU/KCA/KSA/KCBFLSPxWyO2kHbxWiS0jXNGLSM+KCA/KCCBYU8/KCCHZlTxWiTxWiTxWiTxWiQ/KCA/KCC4kXe3j3Y/JyCObVrjtpXjt5VcQTReQzeObFk/KCDxWiTxWiTxWiQ/KB99XkyDY1HxWiQ/KCDnuZg/KCDmuZjouprou5rxWiQ/KCA/KSBAKCBAKSBCKiJDKyJGLiVHLiVHLiZJMihPNixTOi5cQjVdQjZfRDdjRzpnSz1rTT9vUkJxU0N1V0Z2V0d4WUmCYlCDYlCDY1GEZFGJaVaLalaWc1+XdV+demOfe2WwinGwi3K1jnS2kHa9lnu+lny/mHzFnIDGnYHIoILluJjmuZjou5nqu5rrvZzss4/stZLtbz7tcUHtv53vZTLvZjLvwZ/wwZ7wwZ/xWiTxWyXxWybxw6Dyw6Dzw6HzxKH0xaKTA5s/AAAAYXRSTlMABQYHCAkKDA0ODg4QEBYYJi0vMjQ8QENKUFFTWFpdXmFjZGxub3N7gIOEhIaIjJKYmZugoKGprbS0tra6u8DBz9DS2tvc3d7g7O3v9PT09fb29/f5+vv8/Pz9/f7+/v7+wo1VpgAAAV1JREFUOE91ylVDAlEQQOGxuxUDE2zF7u7ubh2xu7u7G0XHvL/UB0T2Lst5/Q6AMBd5bFywG5jKNa0NEREL/aQ9vAn1pVtLeLRe1YiYZ2PkCsQhwZIsdudqVOO4zsdwEHu8REMCIq5pTzZWFtcPNVuImM+7eTUOjV6xv24n1NjlzA0+OIxL7L9VHEEFN8TjDB4YhiOcxVRuyFhZmNQahs+p+eVcbqhk2ztM0N4mq+KGfrZ/LBxOd1k3N9Sxm1vhcHfNarkhixmVww32NRc8XzY6cQNETPPDXCTvYFd+JvTzCkfRAG71GoNrG9zFDuDb/qX3nw65sQN4FPTqvK/IU8oBIOb+9fvn7SHKBAMkvT8/Pr18JEqrQ1B2JxERUWdmoK0RW6qaSVCzyop3WQmJKvUWunJA7EQUavAAKacBf717t0o5UYtM5xZl0k5UbAYAACGmnCgIAADCUkymBPgFYd0U7tFqim4AAAAASUVORK5CYII=');
INSERT INTO APP_USER_GROP_MAP (USER_ID,GROP_ID) VALUES ('admin','ADMIN');

INSERT INTO APP_ROLE_INFO (ROLE_ID,ROLE_NAME,ROLE_ICON) VALUES ('TEST','Test Role','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACkklEQVR42pWSbUhTYRTH/3f37s2rrVaimdk00LSsDMOk+pAFBaZ9CAyLYGIYJAhh2Dc/FLRCSQmp7EMFVgst0IVoQaiVISrODNrEWZYvS4fu4jb3ovc+PbNA4hrYgcOB85z/73nOeQ6Df1hjY6OKhhKGYc5LkpQqimI4baOxQa1WPygoKAiFE8xqYrPZHEcIafXwsXvHRR6LhAGv1UCnYqAVJoC58QGWZfOKi4unmNVu9nq9PQuxaRlBZSQK9ydCzbFwekIYmQtgXlQgNG6Hb6TfKgjCARmgvr6+lOi31pHNyTBmhcVUsCThhxDExIIIv8RBJASjXRb4nV8vyQA1NTV9/L4TmdnJ8UiL4RESCbwhCZM+EdMBYD4oIU3P4d2QA86O5/0ygMlk8iTkXojM3BKFGF6JAJ2dK0Aw45coTIJaQbB7kxLNwwLsT256ZYDKykrPnrPlkfHrtdCwDHxLBH7qHK2MCg+RA7brODz+NIvPj67JARUVFX078ooy9XEGWszQfrEc9WoGGzUK6KkHafJhxyBsL+/JWygrKyvVJaTU7cw3IoIK6bwQrVUgOoJFHM8uv6r1mw9vG+5i1jEkH6LRaNRwHPfFcCg3Mf3wcaRs4CiABa+k7SxK6J4M4OPrFox2Nltp+d/fSKq56p+ajJRbjoNHXC7XtD5pV5Ih+xjWxWyDBAJhcgyOD22YGbYO0kXLtVgsK4sUFiOnttw/+Azvu+1vzGL+KbfbXUKPzlFP/VNmo/5UpVLdb2pqWlllUoUq5Ny54re3obe9DdMCes+8QhbWYAy5jhtSjumqaG9R9HT1wOleu/g3oErnXUov4ifaa6WBMXSebsbRtYqXAVOXcXvWh4vf52A5+QKF/yMO2y87gBXoaYuE6wAAAABJRU5ErkJggg==');

INSERT INTO APP_USER_ROLE_MAP (USER_ID,ROLE_ID) VALUES ('admin','TEST');
INSERT INTO APP_AUTH_INFO (AUTH_ID,AUTH_NAME) VALUES ('USER_READ', 'User Read Authority');
INSERT INTO APP_AUTH_INFO (AUTH_ID,AUTH_NAME) VALUES ('USER_MDFY', 'User Modification Authority');
INSERT INTO APP_USER_AUTH_MAP (USER_ID,AUTH_ID) VALUES ('admin','USER_READ');
INSERT INTO APP_USER_AUTH_MAP (USER_ID,AUTH_ID) VALUES ('admin','USER_MDFY');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu1',NULL,1,'Menu 1');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu2',NULL,2,'Menu 2');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu3',NULL,3,'Menu 3');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu4',NULL,4,'Menu 4');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu5',NULL,5,'Menu 5');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu6',NULL,6,'Menu 6');
INSERT INTO APP_MENU_INFO (MENU_ID,UPER_MENU_ID,MENU_SEQ,MENU_NAME) VALUES ('menu7',NULL,7,'Menu 7');

INSERT INTO APP_PROP_INFO (PROP_ID,PROP_NAME,PROP_DESC,PROP_VAL) VALUES ('prop1','prop name1','prop desc1','prop value1');
INSERT INTO APP_PROP_INFO (PROP_ID,PROP_NAME,PROP_DESC,PROP_VAL) VALUES ('prop2','prop name2','prop desc2','prop value2');
INSERT INTO APP_PROP_INFO (PROP_ID,PROP_NAME,PROP_DESC,PROP_VAL) VALUES ('prop3','prop name3','prop desc3','prop value3');
INSERT INTO APP_PROP_INFO (PROP_ID,PROP_NAME,PROP_DESC,PROP_VAL) VALUES ('prop4','prop name4','prop desc4','prop value4');
INSERT INTO APP_PROP_INFO (PROP_ID,PROP_NAME,PROP_DESC,PROP_VAL) VALUES ('prop5','prop name5','prop desc5','prop value5');

INSERT INTO APP_MSGE_INFO (MSGE_ID,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,MSGE_DESC,MSGE_NAME,MSGE_VAL) VALUES ('INVALID_PARAM','N',{ts '2020-01-31 12:22:06.130000000'},null,{ts '2020-01-31 12:27:54.798000000'},null,'패라미터 오류에 대한 메시지.\nname - 패라미터명\nvalue - 패라미터 값','필수입력값 오류','{}는 필수 입력값입니다.');
INSERT INTO APP_MSGE_INFO (MSGE_ID,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,MSGE_DESC,MSGE_NAME,MSGE_VAL) VALUES ('INVALID_TIME','N',{ts '2020-01-31 12:25:13.196000000'},null,{ts '2020-01-31 12:27:37.760000000'},null,'업무시간 종료 대고객 메시지','업무시간 종료 대고객 메시지','처리가능한 시간이 아닙니다.\n업무시간(09:00~18:00)에만 접수가 가능합니다.\n');


-- APP_SAMP_INFO
INSERT INTO APP_SAMP_INFO (KEY_1,KEY_2,SYS_EMBD_YN,SYS_INST_DATE,SYS_INST_USER_ID,SYS_UPDT_DATE,SYS_UPDT_USER_ID,VAL_YN,VAL_CHAR,VAL_CLOB,VAL_ENUM,VAL_INT,VAL_LONG) VALUES ('1','1',NULL,NULL,NULL,NULL,NULL,'Y','VARCHAR','CLOB','CODE_1',10,100);


