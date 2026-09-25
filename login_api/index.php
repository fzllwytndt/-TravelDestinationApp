<?php

/**
 * Halaman depan folder API.
 *
 * Tanpa berkas ini Apache menampilkan seluruh isi folder login_api kepada
 * siapa pun yang membukanya, termasuk nama berkas database.sql dan koneksi.php.
 * Sekarang alamat http://<host>/login_api/ langsung diarahkan ke daftar wisata,
 * jadi sekaligus memudahkan pengujian lewat browser maupun Postman.
 */

header("Location: wisata.php");
exit;

?>
