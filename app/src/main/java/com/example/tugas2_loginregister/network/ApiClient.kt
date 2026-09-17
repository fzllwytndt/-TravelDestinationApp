package com.example.tugas2_loginregister.network

import android.content.Context
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/** Server tidak dapat dihubungi: alamat salah, beda jaringan, atau Apache mati. */
class GagalKoneksi(pesan: String) : Exception(pesan)

/** Server terhubung, tetapi balasannya bukan JSON yang bisa dibaca. */
class GagalServer(pesan: String) : Exception(pesan)

/**
 * Menyiapkan Retrofit untuk seluruh aplikasi.
 * Alamat server dapat diubah sendiri oleh pengguna, jadi Retrofit dibuat ulang
 * setiap kali alamatnya berganti.
 */
object ApiClient {

    const val HOST_DEFAULT = "192.168.15.181"

    private const val NAMA_PREF = "pengaturan_server"
    private const val KUNCI_HOST = "alamat_server"
    private const val WAKTU_TUNGGU = 5L

    private var retrofit: Retrofit? = null
    private var hostTerpakai = ""

    fun layanan(context: Context): ApiService {
        val host = ambilHost(context)

        if (retrofit == null || host != hostTerpakai) {
            hostTerpakai = host
            retrofit = buatRetrofit(host)
        }

        return retrofit!!.create(ApiService::class.java)
    }

    fun ambilHost(context: Context): String {
        val tersimpan = pref(context).getString(KUNCI_HOST, "") ?: ""

        return if (tersimpan.isBlank()) HOST_DEFAULT else tersimpan
    }

    fun simpanHost(context: Context, host: String) {
        pref(context).edit()
            .putString(KUNCI_HOST, rapikanHost(host))
            .apply()
    }

    /**
     * Memisahkan dua jenis kegagalan supaya tidak semuanya dianggap alamat server salah:
     * masalah jaringan menjadi [GagalKoneksi], balasan server yang aneh menjadi [GagalServer].
     */
    suspend fun <T> panggil(blok: suspend () -> T): T {
        try {
            return blok()
        } catch (e: HttpException) {
            throw GagalServer("Server menjawab kode " + e.code() + ".")
        } catch (e: MalformedJsonException) {
            throw GagalServer("Server tidak mengirim JSON.\n\n" + pesanSingkat(e))
        } catch (e: EOFException) {
            throw GagalServer("Server mengirim balasan kosong.")
        } catch (e: JsonParseException) {
            throw GagalServer("Isi JSON dari server tidak sesuai.\n\n" + pesanSingkat(e))
        } catch (e: IOException) {
            throw GagalKoneksi(pesanKoneksi(e))
        }
    }

    private fun buatRetrofit(host: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://$host/login_api/")
            .client(buatOkHttp())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buatOkHttp(): OkHttpClient {
        val pencatat = HttpLoggingInterceptor()
        pencatat.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(pencatat)
            .connectTimeout(WAKTU_TUNGGU, TimeUnit.SECONDS)
            .readTimeout(WAKTU_TUNGGU, TimeUnit.SECONDS)
            .build()
    }

    private fun pesanKoneksi(e: IOException): String {
        return when (e) {
            is SocketTimeoutException -> "Server tidak menjawab dalam 5 detik."
            is ConnectException -> "Koneksi ditolak. Pastikan Apache di XAMPP sudah menyala."
            is UnknownHostException -> "Alamat server tidak dikenali."
            else -> e.message ?: "Server tidak dapat dihubungi."
        }
    }

    private fun pesanSingkat(e: Exception): String {
        val pesan = e.message ?: return "(tidak ada keterangan)"

        return if (pesan.length > 200) pesan.take(200) + "..." else pesan
    }

    private fun pref(context: Context) =
        context.applicationContext.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE)

    private fun rapikanHost(host: String): String {
        return host.trim()
            .removePrefix("http://")
            .removePrefix("https://")
            .trim('/')
    }
}
