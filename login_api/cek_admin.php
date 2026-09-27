<?php

/**
 * Penjaga hak akses untuk endpoint CRUD wisata (tambah, edit, hapus).
 *
 * Menyembunyikan tombol CRUD di aplikasi saja belum cukup, karena endpoint-nya
 * masih bisa dipanggil langsung lewat browser atau Postman. Berkas ini dipanggil
 * paling awal pada wisata_add.php, wisata_edit.php, dan wisata_delete.php supaya
 * hanya akun ber-role admin yang boleh mengubah data.
 *
 * Aplikasi mengirimkan username akun yang sedang login pada setiap permintaan
 * CRUD, lalu dicek ulang di sini apakah username itu benar-benar terdaftar
 * pada tabel `admin`. Akun di tabel `user` otomatis tidak lolos.
 *
 * [$larangan] dapat diisi supaya pesan penolakannya sesuai dengan endpoint
 * yang memanggil, misalnya daftar akun pada akun.php.
 */

function wajib_admin($conn, $larangan = "Hanya Admin yang boleh mengubah data wisata.")
{
    // Username bisa datang sebagai form biasa, multipart, query string, atau JSON.
    $json_data = json_decode(file_get_contents("php://input"), true) ?? [];

    $username = trim(
        $_POST["username"] ?? $_GET["username"] ?? $json_data["username"] ?? ""
    );

    if ($username === "") {
        tolak("Anda harus login terlebih dahulu.");
    }

    $akun = cari_akun($conn, $username);

    if (!$akun) {
        tolak("Akun tidak dikenali. Silakan login ulang.");
    }

    if ($akun["role"] !== "admin") {
        tolak($larangan);
    }
}

function tolak($pesan)
{
    echo json_encode([
        "success" => false,
        "message" => $pesan
    ]);

    exit;
}

?>
