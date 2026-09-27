CREATE DATABASE IF NOT EXISTS login_register;

USE login_register;

CREATE TABLE IF NOT EXISTS users (
    id         INT(11) NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('admin', 'user') NOT NULL DEFAULT 'user',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ---------------------------------------------------------------------------
-- Tugas 9 - Role Admin & User
-- ---------------------------------------------------------------------------
-- Perintah di bawah hanya dijalankan bila tabel users sudah terlanjur dibuat
-- sebelum Tugas 9, jadi kolom role belum ada. Bila database dibuat dari awal
-- memakai perintah CREATE TABLE di atas, baris ini tidak perlu dijalankan.
--
-- ALTER TABLE users ADD role ENUM('admin', 'user') NOT NULL DEFAULT 'user' AFTER password;

-- Akun yang sudah terdaftar sebelumnya otomatis bernilai 'user'.
-- Untuk menjadikan salah satu akun sebagai Admin, jalankan:
--
-- UPDATE users SET role = 'admin' WHERE username = 'admin';
