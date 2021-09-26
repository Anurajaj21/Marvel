package com.example.marvel.Repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.covidtracker.Network.ApiClient
import com.example.marvel.BuildConfig
import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Network.ApiInterface
import com.example.marvel.Network.SafeApiRequest
import com.example.marvel.Utils.Functions.getHash
import com.example.marvel.Utils.Functions.getTimeStamp

class Repository() : SafeApiRequest() {
    private val apiClient = ApiClient()

    @RequiresApi(Build.VERSION_CODES.N)
    private val apiInterface = apiClient.getApiClient()?.create(ApiInterface::class.java)


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getCharacters() : ApiResponse<CharacterResponse>? {
        val timeStamp = getTimeStamp()
        val hash = getHash(timeStamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY)
        if (apiInterface != null) {
            return apiRequest {
                apiInterface.GetCharacters(timeStamp, BuildConfig.PUBLIC_KEY, hash)
            }
        }

        return null
    }
}