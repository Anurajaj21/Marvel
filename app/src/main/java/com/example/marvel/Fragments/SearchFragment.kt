package com.example.marvel.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marvel.R
import com.example.marvel.databinding.FragmentSearchBinding
import kotlinx.coroutines.channels.consumesAll

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

}