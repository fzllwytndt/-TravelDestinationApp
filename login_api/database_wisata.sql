USE login_register;

DROP TABLE IF EXISTS wisata;

CREATE TABLE wisata (
    id          INT(11) NOT NULL AUTO_INCREMENT,
    nama_wisata VARCHAR(100) NOT NULL,
    kategori    VARCHAR(50) NOT NULL,
    lokasi      VARCHAR(100) NOT NULL,
    harga_tiket INT(11) NOT NULL DEFAULT 0,
    deskripsi   TEXT NOT NULL,
    foto        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO wisata (id, nama_wisata, kategori, lokasi, harga_tiket, deskripsi, foto) VALUES
(1, 'Ayana Gedong Songo', 'Taman', 'Bandungan, Kabupaten Semarang', 30000, 'Taman rekreasi di kawasan Gedong Songo dengan spot foto bertema taman bunga dan bangunan ala Eropa. Berada di dataran tinggi Bandungan yang berudara sejuk dengan panorama pegunungan.', 'gambar_ayana_gedong_songo.jpg'),
(2, 'Brown Canyon', 'Alam', 'Rowosari, Kota Semarang', 0, 'Bekas area penambangan galian di Rowosari yang menyisakan tebing-tebing tinggi menyerupai ngarai. Pemandangan tebing cadasnya populer sebagai lokasi foto, terutama menjelang matahari terbenam.', 'gambar_brown_canyon.jpg'),
(3, 'Bukit Cinta Rawa Pening', 'Alam', 'Banyubiru, Kabupaten Semarang', 10000, 'Taman wisata di tepi Rawa Pening yang menyajikan panorama danau dan perbukitan di sekitarnya. Pengunjung dapat berkeliling danau dengan perahu atau bersantai di area taman.', 'gambar_bukit_cinta_rawa_pening.jpg'),
(4, 'Candi Gedong Songo', 'Sejarah', 'Bandungan, Kabupaten Semarang', 25000, 'Kompleks sembilan candi Hindu peninggalan Wangsa Syailendra di lereng Gunung Ungaran. Jalur antar candinya menyuguhkan panorama pegunungan dan sumber air panas belerang.', 'gambar_candi_gedong_songo.jpg'),
(5, 'Curug Lawe', 'Alam', 'Ungaran Barat, Kabupaten Semarang', 10000, 'Air terjun di kawasan hutan Ungaran yang dicapai lewat jalur trekking menyusuri saluran irigasi. Suasananya sejuk dengan rimbun pepohonan di sepanjang jalan setapak.', 'gambar_curug_lawe.jpg'),
(6, 'Desa Wisata Lembah Kalipancur', 'Alam', 'Ngaliyan, Kota Semarang', 15000, 'Kawasan wisata alam di Semarang dengan area perkemahan dan suasana pedesaan yang asri. Cocok untuk kegiatan luar ruang bersama keluarga maupun rombongan.', 'gambar_desa_wisata_lembah_kalipancur.jpg'),
(7, 'Dusun Semilir', 'Hiburan', 'Bawen, Kabupaten Semarang', 30000, 'Kawasan wisata terpadu di Bawen dengan bangunan ikonik berbentuk stupa warna-warni. Menyediakan taman bermain, kolam, pusat oleh-oleh, dan beragam spot foto.', 'gambar_dusun_semilir.jpg'),
(8, 'Gereja Blenduk', 'Sejarah', 'Kota Lama, Kota Semarang', 10000, 'Gereja Protestan tertua di Jawa Tengah yang berdiri sejak tahun 1753 di kawasan Kota Lama. Dikenal dari kubah tembaganya yang membulat, asal nama blenduk dalam bahasa Jawa.', 'gambar_gereja_blenduk.jpg'),
(9, 'Grand Maerakaca', 'Taman', 'Tawangsari, Kota Semarang', 25000, 'Taman miniatur Jawa Tengah yang menampilkan anjungan rumah adat dari seluruh kabupaten dan kota. Dilengkapi jalur susur mangrove dan jembatan kayu di atas air.', 'gambar_grand_maerakaca.jpg'),
(10, 'Kampung Batik Gedong', 'Budaya', 'Rejomulyo, Kota Semarang', 0, 'Perkampungan pengrajin batik di dekat Kota Lama dengan dinding rumah bermotif batik. Pengunjung bisa melihat proses membatik secara langsung dan mencoba membatik sendiri.', 'gambar_kampung_batik_gedong.jpg'),
(11, 'Kampung Pelangi', 'Budaya', 'Randusari, Kota Semarang', 0, 'Permukiman di kawasan Randusari yang rumah-rumahnya dicat dengan warna cerah beraneka rupa. Kawasan ini menjadi ikon foto Semarang setelah program penataan kampung pada tahun 2017.', 'gambar_kampung_pelangi.jpg'),
(12, 'Kanal Banjir Barat', 'Taman', 'Semarang Barat, Kota Semarang', 0, 'Kawasan tepi kanal di pusat Semarang yang ditata menjadi ruang publik. Jalur pedestrian dan taman di sisinya ramai dipakai warga untuk berolahraga dan bersantai.', 'gambar_kanal_banjir_barat.jpg'),
(13, 'Kota Lama Semarang', 'Sejarah', 'Semarang Utara, Kota Semarang', 0, 'Kawasan bersejarah berisi bangunan bergaya kolonial Belanda yang dijuluki Little Netherland. Setelah direvitalisasi, kawasan ini dipenuhi kafe, galeri seni, dan ruang publik.', 'gambar_kota_lama_semarang.jpg'),
(14, 'Lawang Sewu', 'Sejarah', 'Semarang Tengah, Kota Semarang', 20000, 'Bekas kantor pusat perusahaan kereta api Hindia Belanda yang dibangun pada tahun 1904. Namanya berarti seribu pintu, merujuk pada banyaknya pintu dan jendela lengkung di gedung ini.', 'gambar_lawang_sewu.jpg'),
(15, 'Masjid Agung Jawa Tengah', 'Religi', 'Gayamsari, Kota Semarang', 0, 'Masjid megah dengan perpaduan arsitektur Jawa, Timur Tengah, dan Yunani. Ciri khasnya adalah enam payung raksasa di pelataran dan Menara Al Husna setinggi 99 meter.', 'gambar_masjid_agung_jawa_tengah.jpg'),
(16, 'Museum Kereta Api Ambarawa', 'Sejarah', 'Ambarawa, Kabupaten Semarang', 20000, 'Stasiun kereta api bersejarah peninggalan Hindia Belanda yang kini dialihfungsikan menjadi museum lokomotif uap kuno.', 'gambar_museum_kereta_api_ambarawa.jpg'),
(17, 'Pantai Marina', 'Pantai', 'Semarang Barat, Kota Semarang', 10000, 'Pantai di pesisir utara Semarang dengan area taman dan jalur pejalan kaki di tepi laut. Menjadi tempat favorit warga untuk menikmati matahari terbenam di sore hari.', 'gambar_pantai_marina.jpg'),
(18, 'Pecinan Semarang', 'Budaya', 'Semarang Tengah, Kota Semarang', 0, 'Kawasan permukiman Tionghoa tua dengan deretan kelenteng dan bangunan berarsitektur khas. Pasar Semawis di kawasan ini ramai oleh pedagang kuliner pada akhir pekan.', 'gambar_pecinan_semarang.jpg'),
(19, 'Rawa Pening', 'Alam', 'Ambarawa, Kabupaten Semarang', 10000, 'Danau alami luas yang berada di cekungan antara Gunung Ungaran, Telomoyo, dan Merbabu. Hamparan eceng gondok dan perahu nelayan menjadi pemandangan khasnya.', 'gambar_rawa_pening.jpg'),
(20, 'Saloka Theme Park', 'Hiburan', 'Tuntang, Kabupaten Semarang', 130000, 'Taman hiburan terbesar di Jawa Tengah yang terletak di tepi Rawa Pening. Memiliki puluhan wahana yang dibagi ke dalam beberapa zona bertema.', 'gambar_saloka_theme_park.jpg'),
(21, 'Sam Poo Kong', 'Religi', 'Semarang Barat, Kota Semarang', 15000, 'Kelenteng megah yang dibangun untuk memperingati persinggahan Laksamana Cheng Ho. Bangunannya didominasi warna merah dengan halaman luas bergaya Tiongkok.', 'gambar_sam_poo_kong.jpg'),
(22, 'Setiya Aji Flower Farm', 'Taman', 'Bandungan, Kabupaten Semarang', 20000, 'Kebun bunga di kawasan Bandungan dengan hamparan bunga beraneka warna. Populer sebagai lokasi berfoto dengan latar perbukitan.', 'gambar_setiya_aji_flower_farm.jpg'),
(23, 'Taman Bunga Celosia', 'Taman', 'Bandungan, Kabupaten Semarang', 35000, 'Taman bunga di lereng Gunung Ungaran dengan koleksi celosia dan beragam spot foto bertema. Udara sejuk dan latar pegunungan menjadi daya tarik utamanya.', 'gambar_taman_bunga_celosia.jpg'),
(24, 'Taman Diponegoro', 'Taman', 'Candisari, Kota Semarang', 0, 'Taman kota yang rindang di kawasan Semarang atas. Menjadi ruang terbuka hijau untuk bersantai dan berolahraga di tengah kota.', 'gambar_taman_diponegoro.jpg'),
(25, 'Taman Indonesia Kaya', 'Taman', 'Gajahmungkur, Kota Semarang', 0, 'Taman kota sekaligus panggung seni terbuka di kawasan Menteri Supeno. Kerap menjadi lokasi pertunjukan musik dan tari yang terbuka gratis untuk umum.', 'gambar_taman_indonesia_kaya.jpg'),
(26, 'Taman Lele', 'Taman', 'Tugu, Kota Semarang', 10000, 'Taman rekreasi keluarga di Semarang barat dengan danau kecil, wahana permainan anak, dan koleksi satwa. Suasananya teduh oleh pepohonan besar di sekelilingnya.', 'gambar_taman_lele.jpg'),
(27, 'Taman Wilis', 'Taman', 'Candisari, Kota Semarang', 0, 'Taman kota yang tertata rapi dengan tanaman hias di kawasan Candi, Semarang. Sering digunakan warga sekitar untuk bersantai pada sore hari.', 'gambar_taman_wilis.jpg'),
(28, 'TBRS Semarang', 'Budaya', 'Semarang Selatan, Kota Semarang', 0, 'Taman Budaya Raden Saleh, pusat kegiatan seni dan budaya Kota Semarang. Kompleks ini memiliki gedung pertunjukan dan kerap menggelar pentas wayang serta pameran.', 'gambar_tbrs_semarang.jpg'),
(29, 'Umbul Sidomukti', 'Alam', 'Bandungan, Kabupaten Semarang', 25000, 'Kawasan wisata pegunungan di lereng Gunung Ungaran dengan kolam renang alami berair jernih. Tersedia pula wahana flying fox dan panorama lembah dari ketinggian.', 'gambar_umbul_sidomukti.jpg'),
(30, 'Watu Gunung', 'Alam', 'Ungaran Barat, Kabupaten Semarang', 30000, 'Kawasan wisata di Ungaran dengan kolam renang, area outbound, dan taman yang asri. Dikelilingi pepohonan rindang dengan latar Gunung Ungaran.', 'gambar_watu_gunung.jpg');
