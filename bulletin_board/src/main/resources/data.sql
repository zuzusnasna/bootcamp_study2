--데이터입력
--관리자
INSERT INTO member(name, email, password)
VALUES('admin', 'admin@naver.com', '1111');

INSERT INTO authority(authority)
VALUES('ROLE_ADMIN');

INSERT INTO article(title, description, created, updated)
VALUES('첫 번째 게시글 제목', '첫 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

--사용자 1
INSERT INTO member(name, email, password)
VALUES('user1', 'user1@naver.com', '1111');

INSERT INTO authority(authority)
VALUES('ROLE_USER');

INSERT INTO article(title, description, created, updated)
VALUES('두 번째 게시글 제목', '두 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

--사용자 2
INSERT INTO member(name, email, password)
VALUES('user2', 'user2@naver.com', '1111');

INSERT INTO authority(authority)
VALUES('ROLE_USER');

INSERT INTO article(title, description, created, updated)
VALUES('두 번째 게시글 제목', '두 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);