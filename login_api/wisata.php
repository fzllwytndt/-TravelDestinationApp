<?php

header("Content-Type: application/json");

include "koneksi.php";

$per_page = 10;

$page = (int) ($_GET["page"] ?? 1);

if ($page < 1) {
    $page = 1;
}

$offset = ($page - 1) * $per_page;

$result_total = mysqli_query($conn, "SELECT COUNT(*) AS jumlah FROM wisata");
$total_data   = (int) mysqli_fetch_assoc($result_total)["jumlah"];
$total_page   = (int) ceil($total_data / $per_page);

$base_url = "http://" . $_SERVER["HTTP_HOST"] . "/login_api/uploads/";

$stmt = mysqli_prepare(
    $conn,
    "SELECT id, nama_wisata, deskripsi, foto FROM wisata ORDER BY id ASC LIMIT ? OFFSET ?"
);

mysqli_stmt_bind_param($stmt, "ii", $per_page, $offset);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {

    $data[] = [
        "id"          => (int) $row["id"],
        "nama_wisata" => $row["nama_wisata"],
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
        "per_page"     => $per_page
    ]
]);

?>
