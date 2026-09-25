package com.example.tugas2_loginregister.network

import com.example.tugas2_loginregister.model.WisataActionResponse
import com.example.tugas2_loginregister.model.WisataDetailResponse
import com.example.tugas2_loginregister.model.WisataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/** Daftar alamat API yang dipakai aplikasi. Semuanya dipanggil dari Repository. */
interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse

    @GET("wisata.php")
    suspend fun ambilDaftarWisata(
        @Query("page") halaman: Int,
        @Query("q") kataKunci: String
    ): WisataResponse

    @GET("wisata_detail.php")
    suspend fun ambilDetailWisata(
        @Query("id") id: Int
    ): WisataDetailResponse

    @FormUrlEncoded
    @POST("wisata_add.php")
    suspend fun tambahWisata(
        @Field("nama_wisata") namaWisata: String,
        @Field("kategori") kategori: String,
        @Field("lokasi") lokasi: String,
        @Field("harga_tiket") hargaTiket: Int,
        @Field("deskripsi") deskripsi: String,
        @Field("foto") foto: String
    ): WisataActionResponse

    /** Dipakai saat pengguna memilih foto dari galeri, karena file tidak muat lewat form biasa. */
    @Multipart
    @POST("wisata_add.php")
    suspend fun tambahWisataDenganFoto(
        @Part("nama_wisata") namaWisata: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("harga_tiket") hargaTiket: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part fotoFile: MultipartBody.Part
    ): WisataActionResponse

    @FormUrlEncoded
    @POST("wisata_edit.php")
    suspend fun editWisata(
        @Field("id") id: Int,
        @Field("nama_wisata") namaWisata: String,
        @Field("kategori") kategori: String,
        @Field("lokasi") lokasi: String,
        @Field("harga_tiket") hargaTiket: Int,
        @Field("deskripsi") deskripsi: String,
        @Field("foto") foto: String
    ): WisataActionResponse

    /** Versi multipart dari editWisata, dipakai bila pengguna mengganti foto dengan file baru. */
    @Multipart
    @POST("wisata_edit.php")
    suspend fun editWisataDenganFoto(
        @Part("id") id: RequestBody,
        @Part("nama_wisata") namaWisata: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("harga_tiket") hargaTiket: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part fotoFile: MultipartBody.Part
    ): WisataActionResponse

    @FormUrlEncoded
    @POST("wisata_delete.php")
    suspend fun hapusWisata(
        @Field("id") id: Int
    ): WisataActionResponse
}
