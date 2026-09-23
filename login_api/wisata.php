<?php

header("Content-Type: application/json");

include "koneksi.php";

$per_page = 10;

$page = (int) ($_GET["page"] ?? 1);

if ($page < 1) {
    $page = 1;
}

$offset = ($page - 1) * $per_page;

$kata_kunci = trim($_GET["q"] ?? "");

$sedang_mencari = $kata_kunci !== "";

$pola = "%" . $kata_kunci . "%";

$syarat = $sedang_mencari ? "WHERE nama_wisata LIKE ? OR deskripsi LIKE ?" : "";

$stmt_total = mysqli_prepare($conn, "SELECT COUNT(*) AS jumlah FROM wisata $syarat");

if ($sedang_mencari) {
    mysqli_stmt_bind_param($stmt_total, "ss", $pola, $pola);
}

mysqli_stmt_execute($stmt_total);

$total_data = (int) mysqli_fetch_assoc(mysqli_stmt_get_result($stmt_total))["jumlah"];
$total_page = (int) ceil($total_data / $per_page);

$base_url = "http://" . $_SERVER["HTTP_HOST"] . "/login_api/uploads/";

$stmt = mysqli_prepare(
    $conn,
    "SELECT id, nama_wisata, kategori, lokasi, harga_tiket, deskripsi, foto
     FROM wisata $syarat ORDER BY id DESC LIMIT ? OFFSET ?"
);

if ($sedang_mencari) {
    mysqli_stmt_bind_param($stmt, "ssii", $pola, $pola, $per_page, $offset);
} else {
    mysqli_stmt_bind_param($stmt, "ii", $per_page, $offset);
}

mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {

    $data[] = [
        "id"          => (int) $row["id"],
        "nama_wisata" => $row["nama_wisata"],
        "kategori"    => $row["kategori"],
        "lokasi"      => $row["lokasi"],
        "harga_tiket" => (int) $row["harga_tiket"],
        "deskripsi"   => $row["deskripsi"],
        "foto"        => $row["foto"],
        "foto_url"    => $base_url . rawurlencode($row["foto"])
    ];
}

echo json_encode([
    "success" => true,
    "message" => count($data) > 0 ? "Data berhasil diambil" : "Data kosong",
    "data"    => $data,
    "meta"    => [
        "total_data"   => $total_data,
        "total_page"   => $total_page,
        "current_page" => $page,
        "per_page"     => $per_page,
        "keyword"      => $kata_kunci
    ]
]);

?>
