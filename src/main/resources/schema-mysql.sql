DROP TABLE IF EXISTS accounts CASCADE;

CREATE TABLE accounts (
  seq           bigint NOT NULL AUTO_INCREMENT,
  email         varchar(50) NOT NULL,
  nickname         varchar(50) NOT NULL,
  password        varchar(80) NOT NULL,
  create_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (seq),
  CONSTRAINT unq_account_email UNIQUE (email)
);

INERT INTO accounts (email,nickname,password)
VALUES('kjuioq@email.com','eastperson','123123');