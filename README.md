# -TravelDestinationApp

Repository kumpulan tugas magang di Crocodic Semarang. Setiap tugas/fitur dikerjakan pada branch terpisah sesuai nama fiturnya, lalu di-push ke repository ini.

| Branch | Fitur | Status |
|---|---|---|
| `main` | Halaman utama repository | - |
| `login-register` | Halaman Login & Register dengan ConstraintLayout + MySQL | Selesai |
| `Tugas-3-Implementasi-Pagination` | Daftar destinasi wisata Semarang dengan pagination | Selesai |
| `Tugas-4-Implementasi-Fitur-Search-Detail` | Pencarian destinasi wisata & halaman detail destinasi | Selesai |

---

# Branch `Tugas-4-Implementasi-Fitur-Search-Detail`

## Deskripsi

Melanjutkan project **TravelDestinationApp** dengan menambahkan fitur **Search** dan **Detail** destinasi wisata.

Pada tugas ini aplikasi dikembangkan agar pengguna dapat mencari destinasi wisata berdasarkan kata kunci tertentu. Data hasil pencarian diambil melalui **API Search**, sehingga pengguna dapat menemukan tempat wisata yang diinginkan dengan lebih mudah dan cepat.

Selain fitur pencarian, aplikasi juga memiliki halaman **Detail Destinasi**. Ketika pengguna memilih salah satu destinasi dari daftar hasil pencarian maupun daftar destinasi, aplikasi mengambil informasi detail berdasarkan **ID destinasi** menggunakan **API Detail**.

Dalam implementasinya digunakan **`Helper.kt`** untuk membantu meringkas kode yang sering digunakan, sehingga kode program menjadi lebih sederhana, mudah dibaca, dan tidak terjadi penulisan kode yang sama secara berulang.

Aplikasi juga menangani berbagai kondisi yang mungkin terjadi selama proses pengambilan data dari API, seperti **loading**, **error**, dan **data kosong**. Dengan demikian pengguna tetap mendapatkan informasi yang jelas ketika aplikasi sedang mengambil data, terjadi kesalahan, atau tidak ditemukan data yang sesuai dengan pencarian.

## Ketentuan Fitur

| # | Ketentuan | Pemenuhan |
|---|---|---|
| 1 | Menambahkan fitur Search berdasarkan kata kunci yang dimasukkan pengguna | `SearchView` pada `activity_main.xml`, kata kunci dikirim ke API lewat parameter `q` |
| 2 | Data hasil pencarian diambil menggunakan API Search | `wisata.php?page=<n>&q=<kata kunci>` |
| 3 | Menampilkan hasil pencarian secara jelas dan mudah dipahami | Hasil tampil sebagai kartu berisi gambar, nama, dan deskripsi pada `RecyclerView` yang sama |
| 4 | Memilih salah satu destinasi membuka halaman Detail Destinasi | Kartu diketuk, `WisataAdapter` mengirim datanya ke `MainActivity`, lalu `DetailActivity` dibuka |
| 5 | Data detail diambil menggunakan API Detail berdasarkan ID | `wisata_detail.php?id=<id>`, id dikirim lewat `Intent.putExtra("id", ...)` |
| 6 | Menampilkan informasi detail destinasi | Gambar, nama wisata, kategori, lokasi, harga tiket masuk, dan deskripsi |
| 7 | Membuat file `Helper.kt` untuk meringkas kode yang sering digunakan | Berisi pemanggilan API, pembacaan JSON, atur tampil/sembunyi view, muat gambar, toast, dan format rupiah |
| 8 | Menampilkan loading indicator saat mencari atau mengambil data detail | `pbAwal` / `pbMuatLagi` di daftar wisata, `pbLoading` di halaman detail |
| 9 | Menampilkan error state apabila terjadi kesalahan saat mengambil data | Pesan gagal yang dapat diketuk untuk memuat ulang, pada daftar maupun halaman detail |
| 10 | Menampilkan empty state apabila data tidak ditemukan atau kosong | Pesan `Wisata "<kata kunci>" tidak ditemukan` dan `Belum ada data wisata` |
| 11 | Kode rapi, sederhana, dan terstruktur | Setiap pekerjaan dipisah menjadi fungsi pendek, kode yang berulang dipindah ke `Helper.kt` |
| 12 | Tampilan responsif, user-friendly, dan mudah digunakan | `ConstraintLayout` + `ScrollView`, kartu `MaterialCardView`, pencarian otomatis setelah berhenti mengetik |

## Penanganan Kondisi

Aplikasi menangani empat kondisi selama proses pengambilan data:

| Kondisi | Tampilan di Daftar Wisata | Tampilan di Halaman Detail |
|---|---|---|
| **Loading** | `ProgressBar` di tengah layar saat pemuatan pertama, `ProgressBar` di bawah daftar saat memuat halaman berikutnya | `ProgressBar` di tengah layar, isi detail disembunyikan dulu |
| **Success** | Daftar kartu destinasi wisata | Gambar, kategori, nama, lokasi, harga tiket, dan deskripsi |
| **Empty** | `Wisata "<kata kunci>" tidak ditemukan` atau `Belum ada data wisata` | `Data wisata tidak ditemukan` apabila id tidak ada di database |
| **Error** | Pesan gagal memuat yang dapat diketuk untuk mencoba lagi, serta dialog untuk memperbaiki alamat server | `Gagal memuat detail wisata. Ketuk di sini untuk mencoba lagi.` |

## Tujuan

Menerapkan fitur Search dan Detail pada aplikasi TravelDestinationApp agar pengguna dapat menemukan destinasi wisata dengan lebih mudah serta melihat informasi yang lebih lengkap mengenai destinasi yang dipilih.

Penggunaan API Search bertujuan mengambil data berdasarkan kata kunci yang dimasukkan pengguna, sedangkan API Detail berdasarkan ID digunakan untuk mendapatkan informasi lengkap dari destinasi tertentu.

Selain itu, penggunaan `Helper.kt` bertujuan membuat kode menjadi lebih sederhana, rapi, mudah dibaca, dan tidak terjadi pengulangan kode yang tidak diperlukan.

Dengan adanya fitur Search dan Detail, aplikasi menjadi lebih interaktif, ringan, mudah digunakan, dan memiliki struktur kode yang baik, sehingga pengguna dapat mencari destinasi wisata dan melihat detail informasinya dengan lebih nyaman.

## File Baru pada Tugas Ini

| File | Kegunaan |
|---|---|
| `Helper.kt` | Kumpulan kode yang sering dipakai di banyak halaman |
| `DetailActivity.kt` | Halaman detail satu destinasi wisata |
| `activity_detail.xml` | Tampilan halaman detail |
| `bg_kategori.xml` | Latar label kategori yang sudutnya membulat |
| `wisata_detail.php` | API detail wisata berdasarkan id |

## Isi `Helper.kt`

Kode yang sebelumnya ditulis berulang di banyak tempat dikumpulkan menjadi satu:

| Fungsi | Kegunaan |
|---|---|
| `ambilDataApi()` | Memanggil API di thread terpisah, hasilnya dikembalikan ke layar. `Thread` dan `try-catch` cukup ditulis satu kali |
| `bacaWisata()` | Mengubah satu data JSON menjadi objek `Wisata`, dipakai daftar wisata maupun halaman detail |
| `tampil()` / `sembunyi()` | Menampilkan atau menyembunyikan beberapa view sekaligus |
| `tampilJika()` | Menampilkan view apabila syaratnya terpenuhi |
| `muatGambar()` | Memuat gambar dari alamat URL dengan Glide |
| `pesanSingkat()` | Menampilkan `Toast` |
| `rupiah()` | Mengubah `50000` menjadi `Rp 50.000`, dan `0` menjadi `Gratis` |
| `sandikan()` | Merapikan kata kunci supaya aman dipakai di alamat API |

Contoh perbandingan sebelum dan sesudah memakai `Helper.kt`:

```kotlin
// Sebelum
Thread {
    try {
        val json = JSONObject(ApiClient.get(this, alamat))
        runOnUiThread { tampilkanData(json) }
    } catch (e: Exception) {
        runOnUiThread { tampilkanError() }
    }
}.start()

// Sesudah
Helper.ambilDataApi(
    activity = this,
    alamat = alamat,
    saatBerhasil = { json -> tampilkanData(json) },
    saatGagal = { tampilkanError() }
)
```

## Teknologi

| Bagian | Teknologi |
|---|---|
| Bahasa aplikasi | Kotlin |
| Pencarian | `SearchView` + `OnQueryTextListener` |
| Daftar data | `RecyclerView` + `LinearLayoutManager` |
| Tata letak layar | `ConstraintLayout`, `ScrollView`, `LinearLayout` |
| Tampilan kartu | `MaterialCardView` |
| Memuat gambar | Glide 4.16.0 |
| Perpindahan halaman | `Intent` + `putExtra` |
| Koneksi jaringan | `HttpURLConnection` (GET) |
| Parsing data | `org.json.JSONObject` |
| Backend | PHP 8 (`mysqli` + prepared statement) |
| Database | MySQL / MariaDB (XAMPP) |
| minSdk / targetSdk | 24 / 37 |

## Struktur File

```
TravelDestinationApp/
├── app/src/main/
│   ├── java/com/example/tugas2_loginregister/
│   │   ├── ApiClient.kt           -> penghubung ke API PHP + alamat server
│   │   ├── Helper.kt              -> kode yang sering dipakai di banyak halaman
│   │   ├── DialogServer.kt        -> dialog untuk mengganti alamat server
│   │   ├── Wisata.kt              -> data satu destinasi wisata
│   │   ├── WisataAdapter.kt       -> mengubah data menjadi kartu di layar
│   │   ├── MainActivity.kt        -> daftar wisata + pencarian + pagination
│   │   ├── DetailActivity.kt      -> halaman detail destinasi wisata
│   │   ├── LoginActivity.kt       -> halaman login
│   │   └── RegisterActivity.kt    -> halaman register
│   ├── res/layout/
│   │   ├── activity_main.xml      -> pencarian, daftar, loading, dan pesan
│   │   ├── activity_detail.xml    -> tampilan halaman detail
│   │   ├── item_wisata.xml        -> tampilan satu kartu wisata
│   │   ├── activity_login.xml     -> tampilan login
│   │   └── activity_register.xml  -> tampilan register
│   ├── res/drawable/
│   │   └── bg_kategori.xml        -> latar label kategori
│   └── AndroidManifest.xml
├── login_api/                     -> API PHP
│   ├── koneksi.php                -> koneksi ke MySQL
│   ├── login.php                  -> mengecek username & password
│   ├── register.php               -> menyimpan user baru
│   ├── wisata.php                 -> API daftar wisata (pagination + search)
│   ├── wisata_detail.php          -> API detail wisata berdasarkan id
│   ├── database.sql               -> struktur tabel users
│   ├── database_wisata.sql        -> struktur + 30 data wisata
│   └── uploads/                   -> 30 file gambar wisata
└── screenshot/                    -> tangkapan layar aplikasi
```

## Database

Database: `login_register` — tabel: `wisata`

| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | INT(11) | Primary key, auto increment |
| `nama_wisata` | VARCHAR(100) | Nama destinasi wisata |
| `kategori` | VARCHAR(50) | **Baru** — Alam, Sejarah, Budaya, Religi, Taman, Hiburan, atau Pantai |
| `lokasi` | VARCHAR(100) | **Baru** — kecamatan dan wilayah destinasi |
| `harga_tiket` | INT(11) | **Baru** — harga tiket masuk dalam rupiah, `0` berarti gratis |
| `deskripsi` | TEXT | Penjelasan singkat destinasi |
| `foto` | VARCHAR(255) | **Nama file** gambar di folder `uploads`, bukan gambarnya |
| `created_at` | TIMESTAMP | Otomatis terisi saat data dibuat |

Tiga kolom baru ditambahkan pada tugas ini supaya halaman detail memiliki informasi yang lebih lengkap daripada yang tampil di daftar. Struktur beserta 30 datanya ada di `login_api/database_wisata.sql`.

## API

| Endpoint | Method | Parameter | Keterangan |
|---|---|---|---|
| `wisata.php` | GET | `page` (default `1`), `q` (kata kunci, boleh kosong) | Daftar wisata 10 data per halaman. Bila `q` diisi, data disaring lebih dulu |
| `wisata_detail.php` | GET | `id` | Satu data wisata lengkap berdasarkan id |

### API Search

`http://<IP_LAPTOP>/login_api/wisata.php?page=1&q=candi`

```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": [
    {
      "id": 4,
      "nama_wisata": "Candi Gedong Songo",
      "kategori": "Sejarah",
      "lokasi": "Bandungan, Kabupaten Semarang",
      "harga_tiket": 25000,
      "deskripsi": "Kompleks sembilan candi Hindu peninggalan Wangsa Syailendra ...",
      "foto": "gambar_candi_gedong_songo.jpg",
      "foto_url": "http://<IP_LAPTOP>/login_api/uploads/gambar_candi_gedong_songo.jpg"
    }
  ],
  "meta": {
    "total_data": 2,
    "total_page": 1,
    "current_page": 1,
    "per_page": 10,
    "keyword": "candi"
  }
}
```

Kata kunci dicocokkan ke kolom `nama_wisata` dan `deskripsi` memakai `LIKE`, sehingga pencarian `candi` juga menemukan destinasi yang kata `candi`-nya berada di deskripsi. Jika tidak ada yang cocok, `data` berisi array kosong dan `message` menjadi `Data kosong`, itulah yang dipakai aplikasi untuk menampilkan empty state.

Pencarian tetap memakai pagination, jadi kata kunci yang hasilnya banyak pun tetap dimuat sepuluh-sepuluh sambil digulir.

### API Detail

`http://<IP_LAPTOP>/login_api/wisata_detail.php?id=4`

```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": {
    "id": 4,
    "nama_wisata": "Candi Gedong Songo",
    "kategori": "Sejarah",
    "lokasi": "Bandungan, Kabupaten Semarang",
    "harga_tiket": 25000,
    "deskripsi": "Kompleks sembilan candi Hindu peninggalan Wangsa Syailendra ...",
    "foto": "gambar_candi_gedong_songo.jpg",
    "foto_url": "http://<IP_LAPTOP>/login_api/uploads/gambar_candi_gedong_songo.jpg"
  }
}
```

Apabila id tidak ada di database, `success` bernilai `false` dan `data` bernilai `null`:

```json
{
  "success": false,
  "message": "Data wisata tidak ditemukan",
  "data": null
}
```

## Cara Kerja Search

```
Pengguna mengetik di SearchView
       |
   tunggu 400 ms sampai berhenti mengetik   -->  server tidak dipanggil tiap huruf
       |
   daftar lama dikosongkan, halaman kembali ke 1
       |
   wisata.php?page=1&q=<kata kunci>   -->  ProgressBar tengah tampil
       |
   +-- ada hasil   -->  kartu ditampilkan, scroll ke bawah memuat halaman berikutnya
   |
   +-- kosong      -->  "Wisata <kata kunci> tidak ditemukan"
   |
   +-- gagal       -->  pesan gagal, ketuk untuk mencoba lagi
```

Jeda 400 ms membuat server hanya dipanggil sekali setelah pengguna selesai mengetik, bukan pada setiap huruf. Selain itu setiap permintaan diberi penanda `tokenPermintaan`, sehingga hasil pencarian lama yang datang terlambat tidak ikut ditampilkan bersama hasil pencarian yang baru.

## Cara Kerja Detail

```
Kartu wisata diketuk
       |
   WisataAdapter mengirim data wisata ke MainActivity
       |
   Intent ke DetailActivity membawa id wisata
       |
   wisata_detail.php?id=<id>   -->  ProgressBar tampil, isi detail disembunyikan
       |
   +-- berhasil  -->  gambar, kategori, nama, lokasi, harga tiket, deskripsi
   |
   +-- id tidak ada  -->  "Data wisata tidak ditemukan"
   |
   +-- gagal     -->  "Gagal memuat detail wisata", ketuk untuk mencoba lagi
```

Yang dikirim antar halaman hanya **id**-nya saja, bukan seluruh data wisata. Dengan begitu halaman detail selalu mengambil data terbaru dari server, dan informasi yang hanya ada di halaman detail tidak perlu ikut dibawa dari daftar.

## Tangkapan Layar

| Hasil Pencarian | Pencarian Tidak Ditemukan |
|:---:|:---:|
| <img src="screenshot/13-search-hasil.png" width="230"> | <img src="screenshot/14-search-kosong.png" width="230"> |

| Detail Wisata | Detail Tiket Gratis | Detail Gagal Dimuat |
|:---:|:---:|:---:|
| <img src="screenshot/15-detail-wisata.png" width="230"> | <img src="screenshot/16-detail-gratis.png" width="230"> | <img src="screenshot/17-detail-error.png" width="230"> |

## Cara Menjalankan

1. **Siapkan folder API**
   Nyalakan **Apache** dan **MySQL** di XAMPP Control Panel, lalu copy folder `login_api` ke `C:\xampp\htdocs\` **beserta folder `uploads` di dalamnya**, sehingga menjadi `C:\xampp\htdocs\login_api\`.

   Alternatif tanpa menyalin file: arahkan Apache langsung ke folder project dengan menambahkan baris berikut di `C:\xampp\apache\conf\httpd.conf`, lalu restart Apache.

   ```apache
   Alias /login_api "D:/path/menuju/TravelDestinationApp/login_api"
   <Directory "D:/path/menuju/TravelDestinationApp/login_api">
       Require all granted
   </Directory>
   ```

2. **Import database**
   Buka `http://localhost/phpmyadmin`, masuk tab **Import**, lalu import dua file secara berurutan:

   | File | Isi |
   |---|---|
   | `login_api/database.sql` | Database `login_register` beserta tabel `users` |
   | `login_api/database_wisata.sql` | Tabel `wisata` beserta 30 datanya |

   Pada tugas ini tabel `wisata` mendapat tiga kolom baru, yaitu `kategori`, `lokasi`, dan `harga_tiket`. Jadi `database_wisata.sql` **perlu diimport ulang** meskipun tabelnya sudah pernah dibuat pada tugas sebelumnya. File tersebut sudah berisi perintah `DROP TABLE IF EXISTS wisata`, sehingga tabel lama otomatis diganti. Tabel `users` tidak ikut terpengaruh.

3. **Sesuaikan alamat server**
   Buka CMD, ketik `ipconfig`, catat **IPv4 Address** laptop, lalu isikan pada `HOST_DEFAULT` di `ApiClient.kt`:

   ```kotlin
   const val HOST_DEFAULT = "192.168.1.5"
   ```

   Alamat ini hanya nilai awal. Ketika aplikasi gagal menghubungi server, muncul dialog **Alamat server** berisi kolom isian: masukkan IP yang baru, tekan **Simpan**, dan permintaan yang gagal langsung diulang. Alamat tersebut tersimpan di `SharedPreferences`, sehingga saat berpindah jaringan WiFi tidak perlu mengubah kode dan memasang ulang aplikasi. Untuk emulator, alamat `10.0.2.2` selalu menunjuk ke laptop yang menjalankannya.

4. **Jalankan aplikasi**
   Run project di Android Studio (emulator maupun HP asli). Daftarkan akun lewat halaman Register, lalu login. Daftar wisata langsung tampil setelah berhasil masuk. Ketik kata kunci di kolom pencarian untuk mencari destinasi, lalu ketuk salah satu kartu untuk membuka halaman detailnya. Kalau memakai HP asli, pastikan HP dan laptop terhubung ke WiFi yang sama.

## Catatan

- Kata kunci dirapikan dengan `URLEncoder` lewat `Helper.sandikan()`, sehingga kata kunci yang mengandung spasi atau tanda baca tetap aman dikirim sebagai bagian dari alamat API.
- Pencarian dan pagination memakai endpoint yang sama. Saat kolom pencarian kosong, parameter `q` ikut dikirim dalam keadaan kosong dan API mengembalikan seluruh data seperti sebelumnya.
- Harga tiket disimpan sebagai angka biasa di database, bukan sebagai teks. Pemberian titik pemisah ribuan dan kata `Gratis` dilakukan di sisi aplikasi lewat `Helper.rupiah()`, supaya data di database tetap mudah diolah.
- Halaman detail memakai `ScrollView` supaya deskripsi yang panjang tetap bisa dibaca seluruhnya pada layar kecil.
- Alamat gambar dibangun memakai `$_SERVER["HTTP_HOST"]`, jadi ketika IP laptop berubah, alamat gambar ikut menyesuaikan sendiri tanpa perlu mengubah isi database.
- Glide menyimpan gambar di cache berdasarkan URL-nya. Jika file di folder `uploads` diganti tanpa mengubah nama, hapus penyimpanan aplikasi di perangkat agar gambar baru diunduh ulang.

---

# Branch `Tugas-3-Implementasi-Pagination`

## Deskripsi

Melanjutkan project **TravelDestinationApp** dengan menambahkan fitur **daftar destinasi wisata di Semarang** menggunakan konsep **pagination**.

Dalam implementasinya, data destinasi ditampilkan secara bertahap agar aplikasi tetap ringan dan nyaman digunakan. Setiap kali pengguna melakukan scroll hingga mendekati bagian bawah daftar, aplikasi akan otomatis memuat data destinasi berikutnya.

Nama wisata dan deskripsinya disimpan di **MySQL**, sedangkan **gambarnya disimpan sebagai file** di folder `login_api/uploads/`. Yang masuk ke database hanyalah **nama filenya** pada kolom `foto`. API kemudian merakit alamat lengkap gambar ke dalam kolom `foto_url`:

```
Folder    :  login_api/uploads/gambar_lawang_sewu.jpg
MySQL     :  foto = "gambar_lawang_sewu.jpg"
API PHP   :  foto_url = "http://<IP_LAPTOP>/login_api/uploads/gambar_lawang_sewu.jpg"
Android   :  Glide.load(foto_url)
```

Cara ini membuat gambar, nama, dan deskripsi selalu sinkron karena penghubungnya adalah kolom di database, bukan tebakan dari nama wisata. Database juga tetap ringan sebab tidak menyimpan berkas gambar.

## Ketentuan Fitur

| # | Ketentuan | Pemenuhan |
|---|---|---|
| 1 | Menampilkan daftar destinasi wisata yang berada di Semarang | 30 destinasi Kota & Kabupaten Semarang, diambil dari tabel `wisata` |
| 2 | Menampilkan 10 item data pada setiap pemuatan | `$per_page = 10` pada `wisata.php`, dibagi menjadi 3 halaman |
| 3 | Scroll mendekati bawah memuat 10 data berikutnya secara otomatis | `OnScrollListener` memanggil `muatData()` saat tersisa 3 item menuju bawah |
| 4 | Menampilkan loading indicator saat pemuatan berlangsung | `pbAwal` di tengah layar saat pemuatan pertama, `pbMuatLagi` di bawah daftar saat memuat halaman berikutnya |
| 5 | Mencegah data yang sama ditampilkan berulang atau duplikat | `idSudahAda`, sebuah `Set` berisi id yang sudah tampil, menyaring data sebelum ditambahkan |
| 6 | Menampilkan informasi ketika seluruh data selesai dimuat | Keterangan "Semua data sudah ditampilkan" muncul saat pengguna sampai di kartu terakhir |
| 7 | Menangani kondisi loading, error, dan data kosong | Ketiganya ditangani, pesan error dapat diketuk untuk memuat ulang, dan saat server tidak terjangkau muncul dialog untuk memperbaiki alamat server |
| 8 | Tampilan responsif, user-friendly, dan mudah digunakan | Kartu `MaterialCardView` dengan gambar, nama, dan deskripsi, mengikuti area aman layar |

## Tujuan

Menerapkan konsep pagination untuk mengoptimalkan proses pemuatan data. Dengan pagination, data destinasi tidak dimuat sekaligus, melainkan secara bertahap.

Tujuannya agar aplikasi tetap ringan, cepat, responsif, dan nyaman digunakan, meskipun jumlah data destinasi wisatanya cukup banyak.

## Cara Kerja Pagination

```
Buka MainActivity
       |
   muatData() halaman 1  -->  ProgressBar tengah tampil
       |
   10 data masuk daftar   -->  halaman = 2
       |
   pengguna menggulir ke bawah
       |
   sisa 3 item menuju bawah?  --> ya --> muatData() halaman 2
       |                                      |
       |                              ProgressBar bawah tampil
       |                                      |
       |                              10 data masuk, halaman = 3
       |
   halaman terakhir terambil
       |
   "Semua data sudah ditampilkan"  -->  pemuatan dihentikan
```

Tiga penanda yang menjaga alur ini tetap benar:

| Penanda | Gunanya |
|---|---|
| `sedangMemuat` | Mencegah permintaan ganda saat satu permintaan masih berjalan |
| `semuaSudahDimuat` | Menghentikan permintaan setelah halaman terakhir terambil |
| `idSudahAda` | Menyaring data yang id-nya sudah pernah tampil, mencegah duplikat |

## Tangkapan Layar

| Loading Pemuatan Pertama | Daftar Wisata | Loading Data Berikutnya |
|:---:|:---:|:---:|
| <img src="screenshot/8-wisata-loading-awal.png" width="230"> | <img src="screenshot/9-wisata-daftar.png" width="230"> | <img src="screenshot/10-wisata-loading-berikutnya.png" width="230"> |

| Seluruh Data Selesai Dimuat | Kondisi Gagal Memuat |
|:---:|:---:|
| <img src="screenshot/11-wisata-data-habis.png" width="230"> | <img src="screenshot/12-wisata-error.png" width="230"> |

## Catatan

- Jumlah data per halaman diatur lewat variabel `$per_page` di `wisata.php`. Mengubah angkanya cukup di satu tempat, sisi Android tidak perlu diubah karena ikut membaca `meta`.
- Nama file gambar disimpan apa adanya di kolom `foto`, lalu dibungkus `rawurlencode()` saat dijadikan URL supaya nama file yang mengandung spasi tetap bisa diakses.
- Destinasi yang dipakai mencakup wilayah Kota Semarang dan Kabupaten Semarang (Ungaran, Bandungan, Ambarawa, Bawen).
- Alamat server disimpan di `SharedPreferences` melalui `ApiClient.simpanHost()`, dan dipakai oleh halaman login, register, maupun daftar wisata. Selama alamat tersebut belum pernah diisi, aplikasi memakai nilai `HOST_DEFAULT`.
