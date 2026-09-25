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
    "INSERT INTO users (username, password) VALUES (?, ?)"
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
        "message" => "Register berhasil"
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => "Register gagal"
    ]);
}

?>