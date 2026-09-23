<?php

header("Content-Type: application/json");

include "koneksi.php";

$id = (int) ($_POST["id"] ?? $_GET["id"] ?? 0);

if ($id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID wisata tidak valid"
    ]);
    exit;
}

// Nama file foto dicatat dulu karena barisnya akan segera hilang
$cek_stmt = mysqli_prepare($conn, "SELECT foto FROM wisata WHERE id = ?");
mysqli_stmt_bind_param($cek_stmt, "i", $id);
mysqli_stmt_execute($cek_stmt);
$lama = mysqli_fetch_assoc(mysqli_stmt_get_result($cek_stmt));
$foto_lama = $lama["foto"] ?? "";

$stmt = mysqli_prepare($conn, "DELETE FROM wisata WHERE id = ?");
mysqli_stmt_bind_param($stmt, "i", $id);

if (mysqli_stmt_execute($stmt)) {
    if (mysqli_stmt_affected_rows($stmt) > 0) {
        hapus_foto($foto_lama);

        echo json_encode([
            "success" => true,
            "message" => "Data wisata berhasil dihapus"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Data wisata tidak ditemukan atau sudah dihapus"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal menghapus data: " . mysqli_error($conn)
    ]);
}

?>
