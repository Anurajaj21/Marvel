package com.example.marvel.ViewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.ComicsResponse
import com.example.marvel.Models.Resource
import com.example.marvel.Network.ApiException
import com.example.marvel.Repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber.e
import java.lang.Exception

class ComicsViewModel() : ViewModel() {

    val repository = Repository()

    private val _comicsList = MutableLiveData<Resource<ApiResponse<ComicsResponse>>>()
    val comicsList : LiveData<Resource<ApiResponse<ComicsResponse>>>
    get() = _comicsList

    @RequiresApi(Build.VERSION_CODES.N)
    fun getComics(){
        _comicsList.postValue(Resource.loading())
        GlobalScope.launch(Dispatchers.IO){
            try {
                val response = repository.getComics()
                _comicsList.postValue(Resource.success(response))
            }
            catch (e : Exception){
                e(e)
                if (e is ApiException) {
                    _comicsList.postValue(Resource.error(e.message.toString()))
                }
                else{
                    _comicsList.postValue(Resource.error("No Internet"))
                }
            }
        }
    }
}