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
        @Query("hash") hash : String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : Response<ApiResponse<CharacterResponse>>

    @GET("characters")
    suspend fun GetSearchedcCharacters(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String,
        @Query("nameStartsWith") nameStartsWith : String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : Response<ApiResponse<CharacterResponse>>

    @GET("comics?offset=90&limit=40&orderBy=-onsaleDate")
    suspend fun GetComics(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String,
    ) : Response<ApiResponse<ComicsResponse>>

    @GET("comics?offset=0&limit=40&orderBy=-onsaleDate")
    suspend fun GetFilterComics(
        @Query("ts") timeStamp : String,
        @Query("apikey") apiKey : String,
        @Query("hash") hash : String,
        @Query("dateDescriptor") type : String
    ) : Response<ApiResponse<ComicsResponse>>

}