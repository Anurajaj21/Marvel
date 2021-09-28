package com.example.marvel.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.Adapters.CharacterzAdapter
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.Status
import com.example.marvel.R
import com.example.marvel.ViewModels.CharacterViewModel
import com.example.marvel.databinding.FragmentCharactersBinding
import com.google.android.material.snackbar.Snackbar

class CharactersFragment : Fragment() {

    companion object{
        var characterList : ArrayList<CharacterResponse> = ArrayList()
    }

    private lateinit var binding : FragmentCharactersBinding
    private lateinit var viewModel : CharacterViewModel
    private val adapter = CharacterzAdapter()
    private var currentlyLoading = false
    private var isLastPage = false
    private var currentlyScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(R.raw.loading).into(binding.loader)

        binding.charactersRv.adapter = adapter
        binding.charactersRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.charactersRv.setHasFixedSize(true)
        binding.charactersRv.addOnScrollListener(scrollListener)

        setCharacters()
        if (characterList.isNotEmpty()) {
            binding.charactersRv.visibility = View.VISIBLE
            viewModel.characterList = characterList
            adapter.getData(characterList)
        }
        else{
            viewModel.getCharacters()
        }

        binding.searchCharacters.setOnClickListener {
            findNavController().navigate(R.id.global_action_to_search_character)        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setCharacters() {
        viewModel.allCharacter.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {
                    if (characterList.isEmpty()) {
                        binding.characterPb.visibility = View.VISIBLE
                        binding.charactersRv.visibility = View.GONE
                        currentlyLoading = true
                    }
                    else{
                        showLoading()
                    }
                }
                Status.SUCCESS -> {
                    if (res.data?.data?.results?.isNullOrEmpty() == true){
                        isLastPage = true
                        binding.charactersRv.visibility = View.VISIBLE
                        stopLoading()
                        return@Observer
                    }

                    characterList.addAll(res.data?.data?.results!!)
                    adapter.getData(characterList)
                    binding.charactersRv.visibility = View.VISIBLE
                    binding.characterPb.visibility = View.GONE
                    stopLoading()
                }
                Status.ERROR -> {
                    binding.characterPb.visibility = View.GONE
                    binding.charactersRv.visibility = View.GONE
                    stopLoading()
                    Snackbar.make(binding.root, res.msg.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        viewModel.getCharacters()
                    }.show()
                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = binding.charactersRv.layoutManager as LinearLayoutManager
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItems = layoutManager.childCount
            val totalItems = layoutManager.itemCount

            val atEnd = firstItemPosition + visibleItems >= totalItems
            val notAtBegining = firstItemPosition > 0
            val canPaginate = !currentlyLoading && !isLastPage && atEnd && notAtBegining && currentlyScrolling

            if (canPaginate){
                viewModel.getCharacters()
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