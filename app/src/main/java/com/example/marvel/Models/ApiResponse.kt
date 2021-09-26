package com.example.marvel.Models

data class ApiResponse<T:Any>(
    val data : DataContainer<T>
)
