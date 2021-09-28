package com.example.marvel.Fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.Adapters.CharacterzAdapter
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.Status
import com.example.marvel.R
import com.example.marvel.Utils.hideKeyboard
import com.example.marvel.ViewModels.CharacterViewModel
import com.example.marvel.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private var searchCharacterList : ArrayList<CharacterResponse> = ArrayList()
    private val adapter = CharacterzAdapter()
    private lateinit var viewModel : CharacterViewModel
    private var currentlyLoading = false
    private var isLastPage = false
    private var currentlyScrolling = false
    private var isSearched = false
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(R.raw.loading).into(binding.loader)
        binding.searchBar.requestFocus()
        binding.charactersRvInSearch.adapter = adapter
        binding.charactersRvInSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.charactersRvInSearch.setHasFixedSize(true)
        binding.charactersRvInSearch.addOnScrollListener(scrollListener)

        setSearchBar()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setSearchBar() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    isSearched = false
                    isLastPage = false
                    viewModel.characterList.clear()
                    searchCharacterList.clear()
                    viewModel.getCharacters()

                }
                else{
                    searchQuery = query
                    isSearched = true
                    isLastPage = false
                    viewModel.searchedList.clear()
                    searchCharacterList.clear()
                    viewModel.getSearchedCharacters(query)
                }
                hideKeyboard()
                return true
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        viewModel.allCharacter.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {

                    if (searchCharacterList.isEmpty()) {
                        binding.characterPbInSearch.visibility = View.VISIBLE
                        binding.charactersRvInSearch.visibility = View.GONE
                        currentlyLoading = true
                    }
                    else{
                        showLoading()
                    }
                }
                Status.SUCCESS -> {
                    if (res.data?.data?.results?.isNullOrEmpty() == true){
                        isLastPage = true
                        binding.charactersRvInSearch.visibility = View.VISIBLE
                        stopLoading()
                        return@Observer
                    }

                    binding.characterPbInSearch.visibility = View.GONE
                    searchCharacterList.addAll(res.data?.data?.results!!)
                    adapter.getData(searchCharacterList)
                    binding.charactersRvInSearch.visibility = View.VISIBLE
                    stopLoading()
                }
                Status.ERROR -> {
                    binding.characterPbInSearch.visibility = View.GONE
                    binding.charactersRvInSearch.visibility = View.GONE
                    stopLoading()
                    Snackbar.make(binding.root, res.msg.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        viewModel.getCharacters()
                    }.show()
                }
            }
        })
        viewModel.searchedCharacters.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {
                    if (searchCharacterList.isEmpty()) {
                        binding.characterPbInSearch.visibility = View.VISIBLE
                        binding.charactersRvInSearch.visibility = View.GONE
                        currentlyLoading = true
                    }
                    else{
                        showLoading()
                    }
                }
                Status.SUCCESS -> {
                    if (res.data?.data?.results?.isNullOrEmpty() == true){
                        isLastPage = true
                        binding.charactersRvInSearch.visibility = View.VISIBLE
                        stopLoading()
                        return@Observer
                    }
                    binding.characterPbInSearch.visibility = View.GONE
                    searchCharacterList.addAll(res.data?.data?.results!!)
                    adapter.getData(searchCharacterList)
                    stopLoading()
                    if (searchCharacterList.isEmpty()){
                        binding.charactersRvInSearch.visibility = View.GONE
                        binding.notFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.charactersRvInSearch.visibility = View.VISIBLE
                        binding.notFound.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    binding.characterPbInSearch.visibility = View.GONE
                    binding.charactersRvInSearch.visibility = View.GONE
                    stopLoading()
                    Snackbar.make(binding.root, res.msg.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        viewModel.getSearchedCharacters(searchQuery)
                    }.show()
                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = binding.charactersRvInSearch.layoutManager as LinearLayoutManager
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItems = layoutManager.childCount
            val totalItems = layoutManager.itemCount

            val atEnd = firstItemPosition + visibleItems >= totalItems
            val notAtBegining = firstItemPosition > 0
            val canPaginate = !currentlyLoading && !isLastPage && atEnd && notAtBegining && currentlyScrolling

            if (canPaginate){
                if (isSearched){
                    viewModel.getSearchedCharacters(searchQuery)
                }
                else {
                    viewModel.getCharacters()
                }
                currentlyScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                currentlyScrolling = true
            }
        }
    }

    fun showLoading(){
        currentlyLoading = true
        binding.loader.visibility = View.VISIBLE
    }

    fun stopLoading(){
        currentlyLoading = false
        binding.loader.visibility = View.GONE
    }


}