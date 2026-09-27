<?php

header("Content-Type: application/json");

include "koneksi.php";

$username = trim($_POST["username"] ?? "");
$password = $_POST["password"] ?? "";
$role     = strtolower(trim($_POST["role"] ?? "user"));

if ($username == "" || $password == "") {
    echo json_encode([
        "success" => false,
        "message" => "Username dan password wajib diisi"
    ]);
    exit;
}

if (strlen($username) < 3 || strlen($username) > 20) {
    echo json_encode([
        "success" => false,
        "message" => "Username harus 3-20 karakter"
    ]);
    exit;
}

if (!preg_match("/^[a-zA-Z0-9_]+$/", $username)) {
    echo json_encode([
        "success" => false,
        "message" => "Username hanya boleh huruf, angka, dan _"
    ]);
    exit;
}

// Pilihan role datang dari halaman Register, dan nilainya sekaligus menentukan
// akun disimpan ke tabel yang mana. Nilai lain ditolak supaya tidak ada
// percobaan menulis ke tabel yang tidak dikenal.
if ($role !== "admin" && $role !== "user") {
    echo json_encode([
        "success" => false,
        "message" => "Role hanya boleh admin atau user"
    ]);
    exit;
}

if (strlen($password) < 6) {
    echo json_encode([
        "success" => false,
        "message" => "Password minimal 6 karakter"
    ]);
    exit;
}

// Username diperiksa pada kedua tabel, bukan hanya tabel tujuan. Tanpa ini,
// satu username yang sama bisa terdaftar sebagai admin sekaligus sebagai user,
// dan Login tidak akan tahu yang mana yang dimaksud.
if (cari_akun($conn, $username)) {

    echo json_encode([
        "success" => false,
        "message" => "Username sudah digunakan"
    ]);

    exit;
}

$passwordHash = password_hash($password, PASSWORD_DEFAULT);

$stmt = mysqli_prepare(
    $conn,
    "INSERT INTO `" . $role . "` (username, password) VALUES (?, ?)"
);

mysqli_stmt_bind_param(
    $stmt,
    "ss",
    $username,
    $passwordHash
);

if (mysqli_stmt_execute($stmt)) {

    echo json_encode([
        "success" => true,
        "message" => "Register berhasil sebagai " . $role,
        "username" => $username,
        "role"     => $role
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => "Register gagal"
    ]);
}

?>
