package com.example.marvel.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.Adapters.CharacterzAdapter
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.Status
import com.example.marvel.ViewModels.CharacterViewModel
import com.example.marvel.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private var searchCharacterList : ArrayList<CharacterResponse> = ArrayList()
    private val adapter = CharacterzAdapter()
    private lateinit var viewModel : CharacterViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.requestFocus()
        setSearchBar()

    }

    private fun setSearchBar() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    searchCharacterList = CharactersFragment.characterList
                    adapter.getData(searchCharacterList)
                }
                else{
                    viewModel.getSearchedCharacters(query)
                }
                return true
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrEmpty()){
                    searchCharacterList = CharactersFragment.characterList
                    adapter.getData(searchCharacterList)
                }
                else{
                    viewModel.getSearchedCharacters(newText)
                }
                return true
            }
        })
        viewModel.searchedCharacters.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {
                    Log.d("Load", "reach")
                    binding.characterPbInSearch.visibility = View.VISIBLE
                    binding.charactersRvInSearch.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.characterPbInSearch.visibility = View.GONE
                    searchCharacterList = res.data?.data?.results!!
                    adapter.getData(searchCharacterList)
                    binding.charactersRvInSearch.visibility = View.VISIBLE
                    binding.charactersRvInSearch.adapter = adapter
                    binding.charactersRvInSearch.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.charactersRvInSearch.setHasFixedSize(true)
                }
                Status.ERROR -> {
                    binding.characterPbInSearch.visibility = View.GONE
                    binding.charactersRvInSearch.visibility = View.GONE
                    Toast.makeText(requireContext(), res.data?.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}