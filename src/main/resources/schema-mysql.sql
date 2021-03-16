<!--DROP TABLE IF EXISTS users CASCADE;-->

CREATE TABLE accounts (
  seq           bigint NOT NULL AUTO_INCREMENT, --사용자 PK
  email         varchar(50) NOT NULL,           --로그인 이메일
  nickname         varchar(50) NOT NULL,           --로그인 닉네임
  password        varchar(80) NOT NULL,           --로그인 비밀번호
  create_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (seq),
  CONSTRAINT unq_account_email UNIQUE (email)
);