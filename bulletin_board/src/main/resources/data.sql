--데이터입력
--관리자
INSERT INTO member(name, email, password)
VALUES('admin', 'hong@example.com', '1111');

INSERT INTO authority(authority, member_id)
VALUES('ROLE_ADMIN', 1);

INSERT INTO article(title, description, created, updated, member_id)
VALUES('첫 번째 게시글 제목', '첫 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

--사용자 1
INSERT INTO member(name, email, password)
VALUES('user1', 'hong@example.com', '1111');

INSERT INTO authority(authority, member_id)
VALUES('ROLE_USER', 2);

INSERT INTO article(title, description, created, updated, member_id)
VALUES('두 번째 게시글 제목', '두 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

--사용자 2
INSERT INTO member(name, email, password)
VALUES('user2', 'hong@example.com', '1111');

INSERT INTO authority(authority, member_id)
VALUES('ROLE_USER', 3);

INSERT INTO article(title, description, created, updated, member_id)
VALUES('두 번째 게시글 제목', '두 번째 게시글 본문',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);