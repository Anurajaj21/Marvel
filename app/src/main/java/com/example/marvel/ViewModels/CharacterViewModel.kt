package com.example.marvel.ViewModels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
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

    private val _searchedCharacters = MutableLiveData<Resource<ApiResponse<CharacterResponse>>>()
    val searchedCharacters : LiveData<Resource<ApiResponse<CharacterResponse>>>
    get() = _searchedCharacters

    var characterList : ArrayList<CharacterResponse> = ArrayList()
    var searchedList : ArrayList<CharacterResponse> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCharacters() {
        _allCharacters.postValue(Resource.loading())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCharacters(characterList.size)
                Timber.d(response?.data.toString())
                characterList.addAll(response?.data?.results!!)
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun getSearchedCharacters(sequence : String) {
        _searchedCharacters.postValue(Resource.loading())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getSearchedCharacters(searchedList.size, sequence)
                Timber.d(response?.data.toString())
                Log.d("SearchList", searchedList.size.toString())
                searchedList.addAll(response?.data?.results!!)
                _searchedCharacters.postValue(Resource.success(response))
            }
            catch (e : Exception) {
                e(e)
                Timber.d(e.message.toString())
                if (e is ApiException)
                    _searchedCharacters.postValue(Resource.error(e.message.toString()))
                else
                    _searchedCharacters.postValue(Resource.error("No Internet."))
            }
        }

    }

}