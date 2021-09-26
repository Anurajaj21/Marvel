package com.example.marvel.Models

import android.widget.ArrayAdapter

data class DataContainer<T:Any>(
    val offset : Int,
    val limit : Int,
    val count : Int,
    val results : ArrayList<T>
)
