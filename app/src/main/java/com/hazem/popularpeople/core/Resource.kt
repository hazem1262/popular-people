package com.hazem.popularpeople.core

import com.google.gson.Gson
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by hazem.ashraf on 6/14/2019.
 * the base class for all HTTP calls in the app
 */
class Resource<out T> constructor(var status: Status, val data: T?, var exception: Exception?) {

	enum class Status {
		SUCCESS, ERROR, LOADING, SUCCESS_DB_INSERTION
	}

	companion object {
		// creates a SUCCESS resource for HTTP 200, and ERROR otherwise
		fun <T> create(response: Response<T>?): Resource<T> {
			// according to the movieDB docs the api may have 3 status codes [200, 401, 404]

			return if (response?.code() == HttpURLConnection.HTTP_NOT_FOUND || response?.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
				// serializing the error body
				val gSon = Gson()
				val errorBody = gSon.fromJson(response?.errorBody()?.string(), ApiErrorResponse::class.java)
				error(ApiException(Exception(errorBody.statusMessage)))
				// in case of non cashed request
			} else if (response?.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT){
				error(ApiException(Exception("request is not cashed, no internet connection!")))
			} else{
				success(response?.body())
			}
		}

		fun <T> success(data: T?): Resource<T> {
			return Resource(
                Status.SUCCESS,
                data,
                null
            )
		}

		fun <T> error(exception: Exception?): Resource<T> {
			return Resource(
                Status.ERROR,
                null,
                exception
            )
		}

		fun <T> loading(data: T?): Resource<T> {
			return Resource(Status.ERROR, data, null)
		}
	}
}
