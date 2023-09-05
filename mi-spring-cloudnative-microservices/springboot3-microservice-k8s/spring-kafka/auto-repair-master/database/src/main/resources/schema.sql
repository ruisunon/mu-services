CREATE TABLE RISK (
    ID BIGINT PRIMARY KEY,
    RISK_NAME VARCHAR(255) NOT NULL,
    SCORE SMALLINT NOT NULL
);


CREATE TABLE ACCOUNT (
    ID BIGINT PRIMARY KEY,
    ACCOUNT_NAME VARCHAR(255) NOT NULL,
    RISK_ID BIGINT NOT NULL,
    FOREIGN KEY (`RISK_ID`) REFERENCES `RISK` (`ID`)
);



CREATE TABLE ERROR_CODE (
    CODE VARCHAR(255) PRIMARY KEY,
    ERROR_MSG VARCHAR(255) NOT NULL,
    ASSOCIATED_ENTITY VARCHAR(255)
);



CREATE TABLE FAILED_MESSAGE (
    ID BIGINT PRIMARY KEY,
    MSG VARCHAR(3000) NOT NULL,
    ERROR_CODE VARCHAR(255),
    STATUS VARCHAR(10) NOT NULL
);

CREATE SEQUENCE FAILED_MESSAGE_SEQ START WITH 1 INCREMENT BY 1;
