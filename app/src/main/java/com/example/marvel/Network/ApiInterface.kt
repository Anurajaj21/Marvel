package com.example.marvel.Network

import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("characters")
    suspend fun GetCharacters(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String
    ) : Response<ApiResponse<CharacterResponse>>

}