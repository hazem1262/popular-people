package com.hazem.popularpeople.core

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
		fun <T> create(response: Response<T>?, errorMsg:String? = ""): Resource<T> {

			return if (response?.code() == HttpURLConnection.HTTP_OK){
				success(response?.body())
			} else {
				error(ApiException(Exception(errorMsg)))
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
