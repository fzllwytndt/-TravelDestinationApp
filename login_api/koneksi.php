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
