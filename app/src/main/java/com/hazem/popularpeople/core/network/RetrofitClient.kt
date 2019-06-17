package com.hazem.popularpeople.core.network
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.hazem.popularpeople.core.PopularPeopleApplication
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

            urlBuilder.addQueryParameter(
                ApiQueryParams.API_KEY,
                API_KEY_VALUE
            )
            request = request.newBuilder().url(urlBuilder.build()).build()
            chain.proceed(request)
        }

        // create an Interceptor to the OkHttpClient.
        // https://medium.com/mindorks/caching-with-retrofit-store-responses-offline-71439ed32fda
        val cashInterceptor =
            Interceptor { chain ->

            // Get the request from the chain.
            var request = chain.request()

            /*
            *  Leveraging the advantage of using Kotlin,
            *  we initialize the request and change its header depending on whether
            *  the device is connected to Internet or not.
            */
            request = if (PopularPeopleApplication.retrofitCashHelper?.hasNetwork() == true)
            /*
            *  If there is Internet, get the cache that was stored 1 hour ago.
            *  If the cache is older than 1 hour, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-age' attribute is responsible for this behavior.
            */
                request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 60).build()
            else
            /*
            *  If there is no Internet, get the cache that was stored 7 days ago.
            *  If the cache is older than 7 days, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-stale' attribute is responsible for this behavior.
            *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
            */
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            // End of if-else statement

            // Add the modified request to the chain.
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .cache(PopularPeopleApplication.retrofitCashHelper?.myCache)
            .addInterceptor(httpUrlInterceptor)
            .addInterceptor(cashInterceptor)
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