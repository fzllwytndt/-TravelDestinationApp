CREATE DATABASE IF NOT EXISTS login_register;

USE login_register;

CREATE TABLE IF NOT EXISTS users (
    id         INT(11) NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
