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

// Pilihan role datang dari halaman Register. Nilai selain "admin" dan "user"
// ditolak supaya kolom ENUM pada tabel users tidak terisi nilai yang tidak dikenal.
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

$stmt = mysqli_prepare(
    $conn,
    "SELECT id FROM users WHERE username = ?"
);

mysqli_stmt_bind_param($stmt, "s", $username);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

if (mysqli_num_rows($result) > 0) {

    echo json_encode([
        "success" => false,
        "message" => "Username sudah digunakan"
    ]);

    exit;
}

$passwordHash = password_hash($password, PASSWORD_DEFAULT);

$stmt = mysqli_prepare(
    $conn,
    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)"
);

mysqli_stmt_bind_param(
    $stmt,
    "sss",
    $username,
    $passwordHash,
    $role
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