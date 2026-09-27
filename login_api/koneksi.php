<?php

$host     = "localhost";
$username = "root";
$password = "";
$database = "login_register";

$conn = mysqli_connect($host, $username, $password, $database);

if (!$conn) {
    die("Koneksi database gagal: " . mysqli_connect_error());
}

/**
 * Akun disimpan pada dua tabel terpisah: `admin` dan `user`.
 * Nama tabelnya sekaligus menjadi role akun, jadi tidak ada kolom role.
 *
 * Fungsi ini mencari satu username pada kedua tabel, lalu mengembalikan
 * datanya beserta role-nya. Tabel admin diperiksa lebih dulu.
 * Bila username tidak ditemukan di mana pun, hasilnya null.
 */
function cari_akun($conn, $username)
{
    foreach (["admin", "user"] as $role) {

        $stmt = mysqli_prepare(
            $conn,
            "SELECT id, username, password FROM `" . $role . "` WHERE username = ?"
        );

        mysqli_stmt_bind_param($stmt, "s", $username);
        mysqli_stmt_execute($stmt);

        $baris = mysqli_fetch_assoc(mysqli_stmt_get_result($stmt));

        if ($baris) {
            $baris["role"] = $role;
            return $baris;
        }
    }

    return null;
}

/**
 * Menghapus file foto di folder uploads agar tidak menumpuk jadi sampah.
 * Foto bawaan dan foto yang berupa URL luar sengaja dilewati.
 */
function hapus_foto($nama_file)
{
    if (empty($nama_file) || $nama_file === "logo_wisata.png" || filter_var($nama_file, FILTER_VALIDATE_URL)) {
        return;
    }

    $berkas = __DIR__ . "/uploads/" . basename($nama_file);

    if (is_file($berkas)) {
        unlink($berkas);
    }
}
