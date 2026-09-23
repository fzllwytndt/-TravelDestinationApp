<?php

header("Content-Type: application/json");

include "koneksi.php";

$nama_wisata = trim($_POST["nama_wisata"] ?? "");
$kategori    = trim($_POST["kategori"] ?? "");
$lokasi      = trim($_POST["lokasi"] ?? "");
$harga_tiket = (int) ($_POST["harga_tiket"] ?? 0);
$deskripsi   = trim($_POST["deskripsi"] ?? "");
$foto        = trim($_POST["foto"] ?? "");

if (empty($nama_wisata) || empty($kategori) || empty($lokasi) || empty($deskripsi)) {
    echo json_encode([
        "success" => false,
        "message" => "Form tidak boleh ada yang kosong. Harap isi semua field wajib."
    ]);
    exit;
}

// Apabila ada file foto diunggah lewat multipart form-data
if (isset($_FILES["foto_file"]) && $_FILES["foto_file"]["error"] === UPLOAD_ERR_OK) {
    $file_tmp  = $_FILES["foto_file"]["tmp_name"];
    $file_name = time() . "_" . preg_replace("/[^a-zA-Z0-9\._-]/", "", basename($_FILES["foto_file"]["name"]));
    $target    = "uploads/" . $file_name;
    if (move_uploaded_file($file_tmp, $target)) {
        $foto = $file_name;
    }
}

if (empty($foto)) {
    $foto = "logo_wisata.png";
}

$stmt = mysqli_prepare(
    $conn,
    "INSERT INTO wisata (nama_wisata, kategori, lokasi, harga_tiket, deskripsi, foto) VALUES (?, ?, ?, ?, ?, ?)"
);

mysqli_stmt_bind_param($stmt, "sssiss", $nama_wisata, $kategori, $lokasi, $harga_tiket, $deskripsi, $foto);

if (mysqli_stmt_execute($stmt)) {
    $new_id   = mysqli_insert_id($conn);
    $base_url = "http://" . $_SERVER["HTTP_HOST"] . "/login_api/uploads/";

    // Jika foto berupa URL lengkap, gunakan langsung, jika nama file lokal, gabungkan dengan base_url
    $foto_url = (filter_var($foto, FILTER_VALIDATE_URL)) ? $foto : $base_url . rawurlencode($foto);

    echo json_encode([
        "success" => true,
        "message" => "Data wisata berhasil ditambahkan",
        "data"    => [
            "id"          => $new_id,
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
        "message" => "Gagal menambahkan data ke database: " . mysqli_error($conn)
    ]);
}

?>
