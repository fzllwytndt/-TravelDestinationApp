package com.example.tugas2_loginregister.network

import com.example.tugas2_loginregister.model.WisataDetailResponse
import com.example.tugas2_loginregister.model.WisataResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
}
