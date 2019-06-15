package com.hazem.popularpeople.data
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        val instance: Retrofit by lazy { RetrofitClient().retrofit } // lazy-init the client
    }
    // actual client instance
    private val retrofit: Retrofit

    init {

        val gSonParser = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()!!

        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(ApiEndPoints.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gSonParser))
            .build()
    }
}