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

// Kolom role ikut diambil karena aplikasi memakainya untuk menentukan
// dashboard mana yang dibuka setelah login berhasil.
$stmt = mysqli_prepare(
    $conn,
    "SELECT id, username, password, role FROM users WHERE username = ?"
);

mysqli_stmt_bind_param($stmt, "s", $username);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

if (mysqli_num_rows($result) == 0) {

    echo json_encode([
        "success" => false,
        "message" => "Username atau password salah"
    ]);

    exit;
}

$user = mysqli_fetch_assoc($result);

if (password_verify($password, $user["password"])) {

    // Akun lama yang dibuat sebelum kolom role ada dianggap sebagai user biasa.
    $role = $user["role"] ?? "user";

    echo json_encode([
        "success" => true,
        "message" => "Login berhasil sebagai " . $role,
        "user_id" => $user["id"],
        "username" => $user["username"],
        "role"     => $role
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => "Username atau password salah"
    ]);
}

?>
