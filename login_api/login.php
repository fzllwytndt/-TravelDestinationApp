<?php

header("Content-Type: application/json");

include "koneksi.php";

$username = trim($_POST["username"] ?? "");
$password = $_POST["password"] ?? "";

if ($username == "" || $password == "") {
    echo json_encode([
        "success" => false,
        "message" => "Username dan password wajib diisi"
    ]);
    exit;
}

// Akun dicari pada tabel admin dan tabel user sekaligus. Tabel tempat akun
// ditemukan itulah yang menjadi role-nya, lalu dikirim ke aplikasi untuk
// menentukan dashboard mana yang dibuka.
$akun = cari_akun($conn, $username);

if (!$akun) {

    echo json_encode([
        "success" => false,
        "message" => "Username atau password salah"
    ]);

    exit;
}

if (password_verify($password, $akun["password"])) {

    echo json_encode([
        "success" => true,
        "message" => "Login berhasil sebagai " . $akun["role"],
        "user_id" => $akun["id"],
        "username" => $akun["username"],
        "role"     => $akun["role"]
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => "Username atau password salah"
    ]);
}

?>
