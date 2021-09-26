package com.example.marvel.Network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber.e

//abstract class SafeApiRequest {
//
//    suspend fun <T: Any> apiRequest(call : suspend() -> Response<T>): T {
//
//        e("Api Request")
//        val response = call.invoke()
//
//        if (response.isSuccessful && response.body() != null) {
//            e(response.body().toString())
//            return response.body()!!
//        }
//        else {
//            e(response.code().toString())
//            e(response.message())
//            throw Exception(response.code().toString())
//        }
//
//    }
//}

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()

        if (response.isSuccessful && response.body() != null)
            return response.body()!!

        var msg = ""
        val error = response.errorBody()?.string()

        error?.let {
            try {
                msg += JSONObject(it).getString("message")
            } catch (e: JSONException) {
                msg = response.message()
            }
        }

        throw ApiException(msg)
    }

}

//Exception Class
class ApiException(msg: String) : Exception(msg)