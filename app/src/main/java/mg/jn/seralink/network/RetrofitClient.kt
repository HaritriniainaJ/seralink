package mg.jn.seralink.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // ========================================
    private val MODE = AppMode.PHONE
    // ========================================

    enum class AppMode { EMULATOR, PHONE }

    private const val BASE_URL_EMULATOR = "http://10.0.2.2:8000/api/"
    private const val BASE_URL_PHONE    = "http://192.168.43.139:8000/api/"

    val BASE_URL = when (MODE) {
        AppMode.EMULATOR -> BASE_URL_EMULATOR
        AppMode.PHONE    -> BASE_URL_PHONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}