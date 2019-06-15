package com.hazem.popularpeople.core
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
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

        // add api key in all requests
        val httpUrlInterceptor = Interceptor { chain ->
            var request = chain.request()

            val url = request.url()
            val urlBuilder = url.newBuilder()

            urlBuilder.addQueryParameter(ApiQueryParams.API_KEY, API_KEY_VALUE)
            request = request.newBuilder().url(urlBuilder.build()).build()
            chain.proceed(request)
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(httpUrlInterceptor)
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