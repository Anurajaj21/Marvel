package com.example.marvel.ViewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.Resource
import com.example.marvel.Network.ApiException
import com.example.marvel.Repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.e
import java.lang.Exception

class CharacterViewModel : ViewModel() {

    val repository = Repository()

    private val _allCharacters = MutableLiveData<Resource<ApiResponse<CharacterResponse>>>()
    val allCharacter: LiveData<Resource<ApiResponse<CharacterResponse>>>
        get() = _allCharacters

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllCharacters() {
        _allCharacters.postValue(Resource.loading())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCharacters()
                Timber.d(response?.data.toString())
                _allCharacters.postValue(Resource.success(response))
            }
            catch (e : Exception) {
                e(e)
                Timber.d(e.message.toString())
                if (e is ApiException)
                    _allCharacters.postValue(Resource.error(e.message.toString()))
                else
                    _allCharacters.postValue(Resource.error("No Internet."))
            }
        }

    }

}