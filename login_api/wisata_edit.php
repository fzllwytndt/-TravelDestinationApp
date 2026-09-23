<?php

header("Content-Type: application/json");

include "koneksi.php";

$id          = (int) ($_POST["id"] ?? 0);
$nama_wisata = trim($_POST["nama_wisata"] ?? "");
$kategori    = trim($_POST["kategori"] ?? "");
$lokasi      = trim($_POST["lokasi"] ?? "");
$harga_tiket = (int) ($_POST["harga_tiket"] ?? 0);
$deskripsi   = trim($_POST["deskripsi"] ?? "");
$foto        = trim($_POST["foto"] ?? "");

if ($id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID wisata tidak valid"
    ]);
    exit;
}

if (empty($nama_wisata) || empty($kategori) || empty($lokasi) || empty($deskripsi)) {
    echo json_encode([
        "success" => false,
        "message" => "Form tidak boleh ada yang kosong. Harap isi semua field wajib."
    ]);
    exit;
}

// Cek data wisata lama di database
$check_stmt = mysqli_prepare($conn, "SELECT foto FROM wisata WHERE id = ?");
mysqli_stmt_bind_param($check_stmt, "i", $id);
mysqli_stmt_execute($check_stmt);
$res = mysqli_stmt_get_result($check_stmt);
$existing = mysqli_fetch_assoc($res);

if (!$existing) {
    echo json_encode([
        "success" => false,
        "message" => "Data wisata tidak ditemukan"
    ]);
    exit;
}

// Apabila ada file foto baru diunggah lewat multipart
if (isset($_FILES["foto_file"]) && $_FILES["foto_file"]["error"] === UPLOAD_ERR_OK) {
    $file_tmp  = $_FILES["foto_file"]["tmp_name"];
    $file_name = time() . "_" . preg_replace("/[^a-zA-Z0-9\._-]/", "", basename($_FILES["foto_file"]["name"]));
    $target    = "uploads/" . $file_name;
    if (move_uploaded_file($file_tmp, $target)) {
        $foto = $file_name;
    }
}

// Jika foto baru tidak diberikan, gunakan foto lama
if (empty($foto)) {
    $foto = $existing["foto"];
}

$stmt = mysqli_prepare(
    $conn,
    "UPDATE wisata SET nama_wisata = ?, kategori = ?, lokasi = ?, harga_tiket = ?, deskripsi = ?, foto = ? WHERE id = ?"
);

mysqli_stmt_bind_param($stmt, "sssissi", $nama_wisata, $kategori, $lokasi, $harga_tiket, $deskripsi, $foto, $id);

if (mysqli_stmt_execute($stmt)) {
    $base_url = "http://" . $_SERVER["HTTP_HOST"] . "/login_api/uploads/";
    $foto_url = (filter_var($foto, FILTER_VALIDATE_URL)) ? $foto : $base_url . rawurlencode($foto);

    echo json_encode([
        "success" => true,
        "message" => "Data wisata berhasil diperbarui",
        "data"    => [
            "id"          => $id,
            "nama_wisata" => $nama_wisata,
            "kategori"    => $kategori,
            "lokasi"      => $lokasi,
            "harga_tiket" => $harga_tiket,
            "deskripsi"   => $deskripsi,
            "foto"        => $foto,
            "foto_url"    => $foto_url
        ]
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal memperbarui data: " . mysqli_error($conn)
    ]);
}

?>
