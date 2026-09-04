# -TravelDestinationApp

Repository kumpulan tugas magang di Crocodic Semarang. Setiap tugas/fitur dikerjakan pada branch terpisah sesuai nama fiturnya, lalu di-push ke repository ini.

| Branch | Fitur | Status |
|---|---|---|
| `main` | Halaman utama repository | - |
| `login-register` | Halaman Login & Register dengan ConstraintLayout + MySQL | Selesai |

---

# Branch `login-register`

## Deskripsi

Aplikasi Android sederhana berisi halaman **Login** dan **Register**. Seluruh tampilan dibangun memakai **ConstraintLayout**, dan data akun disimpan di **database MySQL**.

Karena Android tidak bisa (dan tidak boleh) terhubung langsung ke MySQL, aplikasi berkomunikasi dengan database melalui **REST API PHP** yang dijalankan di XAMPP. Alur datanya:

```
Aplikasi Android  ->  API PHP (XAMPP)  ->  Database MySQL
   Kotlin              login_api/           login_register
```

Password tidak disimpan sebagai teks asli, melainkan di-hash memakai `password_hash()` bawaan PHP, sehingga isi kolom password di database tidak bisa dibaca langsung.

## Fitur

- **Register** — mendaftarkan akun baru (username & password) ke tabel `users`
- **Login** — mencocokkan username & password dengan data di MySQL
- **Beranda** — menampilkan sapaan berisi username yang berhasil masuk
- **Logout** — kembali ke halaman login
- **Validasi input** — data kosong, panjang username 3-20 karakter, username hanya boleh huruf/angka/underscore, password minimal 6 karakter, username sudah terdaftar, serta username/password salah saat login

## Teknologi

| Bagian | Teknologi |
|---|---|
| Bahasa aplikasi | Kotlin |
| Tampilan | ConstraintLayout, Material Components |
| Koneksi jaringan | `HttpURLConnection` (POST, form-urlencoded) |
| Parsing data | `org.json.JSONObject` |
| Backend | PHP 8 (`mysqli` + prepared statement) |
| Database | MySQL / MariaDB (XAMPP) |
| minSdk / targetSdk | 24 / 37 |

## Struktur File

```
Tugas2_LoginRegister/
├── app/src/main/
│   ├── java/com/example/tugas2_loginregister/
│   │   ├── ApiClient.kt          -> pengirim data ke API PHP
│   │   ├── LoginActivity.kt      -> halaman login
│   │   ├── RegisterActivity.kt   -> halaman register
│   │   └── MainActivity.kt       -> halaman beranda
│   ├── res/layout/
│   │   ├── activity_login.xml    -> tampilan login (ConstraintLayout)
│   │   ├── activity_register.xml -> tampilan register (ConstraintLayout)
│   │   └── activity_main.xml     -> tampilan beranda (ConstraintLayout)
│   └── AndroidManifest.xml
├── login_api/                    -> salin ke C:\xampp\htdocs\
│   ├── koneksi.php               -> koneksi ke MySQL
│   ├── register.php              -> menyimpan user baru
│   ├── login.php                 -> mengecek username & password
│   └── database.sql              -> struktur database
└── screenshot/                   -> tangkapan layar aplikasi
```

## Database

Database: `login_register` — tabel: `users`

| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | INT(11) | Primary key, auto increment |
| `username` | VARCHAR(50) | Unique, tidak boleh sama |
| `password` | VARCHAR(255) | Disimpan dalam bentuk hash |
| `created_at` | TIMESTAMP | Otomatis terisi saat mendaftar |

Struktur lengkapnya ada di `login_api/database.sql`.

## API

Base URL: `http://<IP_LAPTOP>/login_api/`

| Endpoint | Method | Parameter | Response berhasil |
|---|---|---|---|
| `register.php` | POST | `username`, `password` | `{"success":true,"message":"Register berhasil"}` |
| `login.php` | POST | `username`, `password` | `{"success":true,"message":"Login berhasil","user_id":1,"username":"budi"}` |

Response gagal selalu berbentuk `{"success":false,"message":"<alasan>"}`, dan pesan itulah yang ditampilkan aplikasi lewat Toast.

## Alur Aplikasi

```
LoginActivity  --klik "Daftar"-->  RegisterActivity
                                        |
                                   isi data, klik DAFTAR
                                        |
                                   tersimpan di MySQL
                                        |
                                   kembali ke LoginActivity
                                        |
                              isi data, klik LOGIN (dicek ke MySQL)
                                        |
                                   MainActivity (beranda)
                                        |
                                  klik LOGOUT -> LoginActivity
```

## Tangkapan Layar

| Halaman Login | Halaman Register | Register Terisi |
|:---:|:---:|:---:|
| <img src="screenshot/1-login.png" width="230"> | <img src="screenshot/2-register.png" width="230"> | <img src="screenshot/3-register-terisi.png" width="230"> |

| Daftar Berhasil | Login Terisi | Beranda |
|:---:|:---:|:---:|
| <img src="screenshot/4-daftar-berhasil.png" width="230"> | <img src="screenshot/5-login-terisi.png" width="230"> | <img src="screenshot/6-beranda.png" width="230"> |

| Contoh Validasi (username sudah dipakai) |
|:---:|
| <img src="screenshot/7-validasi.png" width="230"> |

## Cara Menjalankan

1. **Salin folder API**
   Copy folder `login_api` ke `C:\xampp\htdocs\` sehingga menjadi `C:\xampp\htdocs\login_api\`

2. **Buat database**
   Nyalakan **Apache** dan **MySQL** di XAMPP Control Panel, buka `http://localhost/phpmyadmin`, masuk tab **Import**, pilih file `login_api/database.sql`, lalu klik **Go**

3. **Sesuaikan alamat API**
   Buka CMD, ketik `ipconfig`, catat **IPv4 Address** laptop (contoh `192.168.1.5`). Ubah baris berikut di `ApiClient.kt`:

   ```kotlin
   const val BASE_URL = "http://192.168.1.5/login_api/"
   ```

4. **Jalankan aplikasi**
   Run project di Android Studio (emulator maupun HP asli). Kalau memakai HP asli, pastikan HP dan laptop terhubung ke WiFi yang sama.

## Catatan

- Alamat pada `BASE_URL` memakai IP laptop, bukan `10.0.2.2`, supaya aplikasi bisa dijalankan di emulator maupun HP asli tanpa mengubah kode. **IP ini berubah saat berpindah jaringan WiFi**, jadi perlu disesuaikan kembali lewat `ipconfig`.
- Apache dan MySQL di XAMPP harus dalam keadaan menyala saat aplikasi dijalankan. Jika tidak, akan muncul Toast "Gagal terhubung ke server".
- Password pada database berbentuk hash (acak), bukan teks asli. Ini normal dan memang disengaja demi keamanan.
