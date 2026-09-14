<?php

header("Content-Type: application/json");

include "koneksi.php";

$id = (int) ($_GET["id"] ?? 0);

$stmt = mysqli_prepare(
    $conn,
    "SELECT id, nama_wisata, kategori, lokasi, harga_tiket, deskripsi, foto
     FROM wisata WHERE id = ?"
);

mysqli_stmt_bind_param($stmt, "i", $id);
mysqli_stmt_execute($stmt);

$row = mysqli_fetch_assoc(mysqli_stmt_get_result($stmt));

if (!$row) {

    echo json_encode([
        "success" => false,
        "message" => "Data wisata tidak ditemukan",
        "data"    => null
    ]);

    exit;
}

$base_url = "http://" . $_SERVER["HTTP_HOST"] . "/login_api/uploads/";

echo json_encode([
    "success" => true,
    "message" => "Data berhasil diambil",
    "data"    => [
        "id"          => (int) $row["id"],
        "nama_wisata" => $row["nama_wisata"],
        "kategori"    => $row["kategori"],
        "lokasi"      => $row["lokasi"],
        "harga_tiket" => (int) $row["harga_tiket"],
        "deskripsi"   => $row["deskripsi"],
        "foto"        => $row["foto"],
        "foto_url"    => $base_url . rawurlencode($row["foto"])
    ]
]);

?>
