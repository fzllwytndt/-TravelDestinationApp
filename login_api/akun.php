<?php

/**
 * Menampilkan daftar akun yang terdaftar.
 *
 * Akun berada pada dua tabel terpisah, `admin` dan `user`, jadi berkas ini
 * membaca keduanya lalu menggabungkannya menjadi satu daftar. Kolom `role`
 * pada hasilnya berasal dari nama tabel tempat akun itu ditemukan.
 *
 * Contoh pemakaian:
 *   akun.php?username=atik              -> seluruh akun, admin dan user
 *   akun.php?username=atik&role=admin   -> hanya isi tabel admin
 *   akun.php?username=atik&role=user    -> hanya isi tabel user
 *
 * Kolom `password` sengaja tidak pernah ikut dikirim, walaupun isinya sudah
 * berupa hash. Daftar akun juga hanya boleh dilihat Admin, jadi endpoint ini
 * dijaga sama seperti endpoint CRUD wisata.
 */

header("Content-Type: application/json");

include "koneksi.php";

include "cek_admin.php";
wajib_admin($conn, "Hanya Admin yang boleh melihat daftar akun.");

$role = strtolower(trim($_GET["role"] ?? ""));

if ($role !== "" && $role !== "admin" && $role !== "user") {
    echo json_encode([
        "success" => false,
        "message" => "Role hanya boleh admin atau user"
    ]);
    exit;
}

// Tanpa parameter role, kedua tabel dibaca sekaligus.
$tabel = ($role === "") ? ["admin", "user"] : [$role];

$data   = [];
$jumlah = ["admin" => 0, "user" => 0];

foreach ($tabel as $nama) {

    $hasil = mysqli_query(
        $conn,
        "SELECT id, username, created_at FROM `" . $nama . "` ORDER BY id ASC"
    );

    while ($baris = mysqli_fetch_assoc($hasil)) {

        $data[] = [
            "id"         => (int) $baris["id"],
            "username"   => $baris["username"],
            "role"       => $nama,
            "created_at" => $baris["created_at"]
        ];

        $jumlah[$nama]++;
    }
}

echo json_encode([
    "success" => true,
    "message" => "Daftar akun berhasil diambil",
    "jumlah"  => [
        "admin" => $jumlah["admin"],
        "user"  => $jumlah["user"],
        "total" => count($data)
    ],
    "data"    => $data
]);

?>
