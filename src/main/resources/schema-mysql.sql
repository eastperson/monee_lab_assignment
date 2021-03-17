/*DROP TABLE IF EXISTS accounts CASCADE;*/

/**

모델 데이터 테이블 생성

**/

CREATE TABLE accounts (
  seq           bigint NOT NULL AUTO_INCREMENT,
  email         varchar(50) NOT NULL,
  nickname         varchar(50) NOT NULL,
  password        varchar(80) NOT NULL,
  create_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (seq),
  CONSTRAINT unq_account_email UNIQUE (email)
);

CREATE TABLE posts (
  seq          bigint NOT NULL AUTO_INCREMENT,
  title        varchar(200) NOT NULL,
  content      varchar(5000) NOT NULL,
  author_seq    bigint NOT NULL,
  revw_cnt     bigint NOT NULL DEFAULT 0,
  create_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  update_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (seq)
);

CREATE TABLE replys
(
    seq        bigint   NOT NULL AUTO_INCREMENT,
    Post_seq   bigint   NOT NULL,
    content    varchar(500)  NOT NULL,
    author_seq bigint   NOT NULL,
	 create_at  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  	 update_at  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (seq)
);



CREATE TABLE Roles
(
    `role`  VARCHAR(50)    NOT NULL    COMMENT '권한',
    PRIMARY KEY (role)
);

/**

 관계 테이블 생성

 **/


CREATE TABLE Posts_replys
(
    `Post_seq`   BIGINT    NOT NULL    COMMENT '게시글',
    `Reply_seq`  BIGINT    NOT NULL    COMMENT '댓글'
);

CREATE TABLE Accounts_roles
(
    `Account_seq`  BIGINT         NOT NULL    COMMENT '계정',
    `role`         VARCHAR(50)    NOT NULL    COMMENT '권한'
);



/**

제약 조건

**/

ALTER TABLE Accounts
    ADD CONSTRAINT UC_email UNIQUE (email);

ALTER TABLE Roles
    ADD CONSTRAINT UC_role UNIQUE (role);

ALTER TABLE Replys
    ADD CONSTRAINT FK_Replys_author_seq_Accounts_seq FOREIGN KEY (author_seq)
        REFERENCES Accounts (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Replys
    ADD CONSTRAINT FK_Replys_Post_seq_Posts_seq FOREIGN KEY (Post_seq)
        REFERENCES Posts (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Posts
    ADD CONSTRAINT FK_Posts_author_seq_Accounts_seq FOREIGN KEY (author_seq)
        REFERENCES Accounts (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;


ALTER TABLE Accounts_roles
    ADD CONSTRAINT FK_Accounts_roles_Account_seq_Accounts_seq FOREIGN KEY (Account_seq)
        REFERENCES Accounts (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Accounts_roles
    ADD CONSTRAINT FK_Accounts_roles_role_Roles_role FOREIGN KEY (role)
        REFERENCES Roles (role) ON DELETE RESTRICT ON UPDATE RESTRICT;


ALTER TABLE Posts_replys
    ADD CONSTRAINT FK_Posts_replys_Post_seq_Posts_seq FOREIGN KEY (Post_seq)
        REFERENCES Posts (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Posts_replys
    ADD CONSTRAINT FK_Posts_replys_Reply_seq_Replys_seq FOREIGN KEY (Reply_seq)
        REFERENCES Replys (seq) ON DELETE RESTRICT ON UPDATE RESTRICT;



