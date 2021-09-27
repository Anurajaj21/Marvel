package com.example.marvel.Models

data class ComicsResponse(
    val title : String,
    val thumbnail : Thumbnail,
    val dates : ArrayList<Date>
)

data class Date(
    val date: String,
    val type: String
)
