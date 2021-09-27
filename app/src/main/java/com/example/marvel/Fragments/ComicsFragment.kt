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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.Adapters.ComicsAdapter
import com.example.marvel.Models.ComicsResponse
import com.example.marvel.Models.Status
import com.example.marvel.R
import com.example.marvel.ViewModels.ComicsViewModel
import com.example.marvel.databinding.FragmentComicsBinding


class ComicsFragment : Fragment() {

    companion object{
        var comicList : ArrayList<ComicsResponse> = ArrayList()
    }

    private lateinit var binding : FragmentComicsBinding
    private lateinit var viewModel : ComicsViewModel
    private val adapter = ComicsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComicsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ComicsViewModel::class.java)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (comicList.isNullOrEmpty()) {
            getComics()
        }
        else{
            binding.ComicsRv.visibility = View.VISIBLE
            adapter.getData(comicList)
            binding.ComicsRv.adapter = adapter
            binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.ComicsRv.setHasFixedSize(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getComics() {
        viewModel.getComics()
        viewModel.comicsList.observe(viewLifecycleOwner, Observer { res ->
            when(res.status){
                Status.LOADING -> {
                    binding.comicsPb.visibility= View.VISIBLE
                    binding.ComicsRv.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.comicsPb.visibility = View.GONE
                    comicList = res.data?.data?.results!!
                    Log.d("Load", comicList.toString())
                    adapter.getData(comicList)
                    binding.ComicsRv.visibility = View.VISIBLE
                    binding.ComicsRv.adapter = adapter
                    binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.ComicsRv.setHasFixedSize(true)
                }
                Status.ERROR -> {
                    binding.comicsPb.visibility = View.GONE
                    binding.ComicsRv.visibility = View.GONE
                    Toast.makeText(requireContext(), res.data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}