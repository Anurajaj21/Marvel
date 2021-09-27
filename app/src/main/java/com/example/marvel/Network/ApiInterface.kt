package com.example.marvel.Network

import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.ComicsResponse
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

    @GET("characters")
    suspend fun GetSearchedcCharacters(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String,
        @Query("nameStartsWith") nameStartsWith : String
    ) : Response<ApiResponse<CharacterResponse>>

    @GET("comics")
    suspend fun GetComics(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String,
    ) : Response<ApiResponse<ComicsResponse>>

}