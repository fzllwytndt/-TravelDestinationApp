CREATE DATABASE IF NOT EXISTS login_register;

USE login_register;

-- ---------------------------------------------------------------------------
-- Tugas 9 - Role Admin & User
-- ---------------------------------------------------------------------------
-- Akun dipisah menjadi dua tabel. Nama tabelnya sekaligus menjadi role akun,
-- jadi tidak ada kolom role: akun yang berada di tabel `admin` berarti Admin,
-- dan akun yang berada di tabel `user` berarti User biasa.
--
-- Struktur kedua tabel dibuat sama persis supaya login.php dapat mencari
-- akun pada keduanya memakai perintah yang sama.

CREATE TABLE IF NOT EXISTS admin (
    id         INT(11) NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
    id         INT(11) NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ---------------------------------------------------------------------------
-- Memindahkan data dari struktur lama
-- ---------------------------------------------------------------------------
-- Sampai Tugas 8 seluruh akun berada pada satu tabel `users`. Bila tabel itu
-- masih ada, isinya dapat dipindahkan ke struktur baru dengan perintah di
-- bawah, lalu tabel lamanya dihapus.
--
-- INSERT INTO admin (username, password, created_at)
-- SELECT username, password, created_at FROM users WHERE role = 'admin';
--
-- INSERT INTO `user` (username, password, created_at)
-- SELECT username, password, created_at FROM users WHERE role = 'user';
--
-- DROP TABLE users;

-- ---------------------------------------------------------------------------
-- Membuat akun
-- ---------------------------------------------------------------------------
-- Cara termudah lewat aplikasi: buka halaman Daftar Akun, lalu pilih
-- "Daftar sebagai: Admin" atau "User". Aplikasi yang menentukan tabel tujuan.
--
-- Untuk memindahkan akun yang sudah ada dari User menjadi Admin, barisnya
-- disalin ke tabel admin lalu dihapus dari tabel user:
--
-- INSERT INTO admin (username, password, created_at)
-- SELECT username, password, created_at FROM `user` WHERE username = 'atik';
-- DELETE FROM `user` WHERE username = 'atik';
