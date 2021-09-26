package com.example.marvel.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.Adapters.ItemsAdapter
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.Status
import com.example.marvel.R
import com.example.marvel.ViewModels.CharacterViewModel
import com.example.marvel.databinding.FragmentCharactersBinding
import timber.log.Timber

class CharactersFragment : Fragment() {

    private lateinit var binding : FragmentCharactersBinding
    private lateinit var viewModel : CharacterViewModel
    private var characterList : ArrayList<CharacterResponse> = ArrayList()
    private val adapter = ItemsAdapter()

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
        setCharacters()
        binding.searchCharacters.setOnClickListener {
            findNavController().navigate(R.id.global_action_to_search_character)        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setCharacters() {
        viewModel.getAllCharacters()
        viewModel.allCharacter.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {
                    Log.d("Load", "reach")
                    binding.characterPb.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.characterPb.visibility = View.GONE
                    characterList = res.data?.data?.results!!
                    adapter.getData(characterList)
                    binding.charactersRv.visibility = View.VISIBLE
                    binding.charactersRv.adapter = adapter
                    binding.charactersRv.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.charactersRv.setHasFixedSize(true)
                }
                Status.ERROR -> {
                    binding.characterPb.visibility = View.GONE
                    Toast.makeText(requireContext(), res.data?.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}