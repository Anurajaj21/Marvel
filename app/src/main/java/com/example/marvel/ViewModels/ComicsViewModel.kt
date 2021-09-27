package com.example.marvel.ViewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel.Models.ApiResponse
import com.example.marvel.Models.ComicsResponse
import com.example.marvel.Models.Resource
import com.example.marvel.Network.ApiException
import com.example.marvel.Repositories.Repository
import com.example.marvel.Utils.Constants.FILTER_LAST_WEEK
import com.example.marvel.Utils.Constants.FILTER_NEXT_WEEK
import com.example.marvel.Utils.Constants.FILTER_THIS_MONTH
import com.example.marvel.Utils.Constants.FILTER_THIS_WEEK
import com.example.marvel.Utils.Constants.NO_FILTER
import com.example.marvel.Utils.Functions.getDate
import com.example.marvel.Utils.Functions.isInLastWeek
import com.example.marvel.Utils.Functions.isInNextWeek
import com.example.marvel.Utils.Functions.isInThisMonth
import com.example.marvel.Utils.Functions.isInThisWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.e

class ComicsViewModel() : ViewModel() {

    val repository = Repository()

    private val _comicsList = MutableLiveData<Resource<ApiResponse<ComicsResponse>>>()
    val comicsList : LiveData<Resource<ApiResponse<ComicsResponse>>>
    get() = _comicsList

    private val filteredList : ArrayList<ComicsResponse> = ArrayList()

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterComics(filterType : Int, unfilteredList : ArrayList<ComicsResponse>) : ArrayList<ComicsResponse>{
        when(filterType){
            FILTER_THIS_WEEK -> {
                filteredList.clear()
                for (i in unfilteredList) {
//                    Log.d("loopRun", i.getDate())
                    if (isInThisWeek(i.getDate()))
                        filteredList.add(i)
                }
            }
            FILTER_LAST_WEEK -> {
                filteredList.clear()
                for (i in unfilteredList)
                    if (isInLastWeek(i.getDate()))
                        filteredList.add(i)
            }
            FILTER_NEXT_WEEK -> {
                filteredList.clear()
                for (i in unfilteredList)
                    if (isInNextWeek(i.getDate()))
                        filteredList.add(i)
            }
            FILTER_THIS_MONTH -> {
                filteredList.clear()
                for (i in unfilteredList)
                    if (isInThisMonth(i.getDate()))
                        filteredList.add(i)
            }
        }
        return filteredList
    }
}