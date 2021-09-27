package com.example.marvel.Fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
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
import kotlinx.android.synthetic.main.filter_layout.view.*


class ComicsFragment : Fragment() {

    companion object {
        var comicList: ArrayList<ComicsResponse> = ArrayList()
    }

    private lateinit var binding: FragmentComicsBinding
    private lateinit var viewModel: ComicsViewModel
    private val adapter = ComicsAdapter()
    private lateinit var filterDialog: AlertDialog
    private var selectedOptionId: Int? = null
    private var filteredList : ArrayList<ComicsResponse> = ArrayList()

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
        } else {
            binding.ComicsRv.visibility = View.VISIBLE
            adapter.getData(comicList)
            binding.ComicsRv.adapter = adapter
            binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.ComicsRv.setHasFixedSize(true)
        }

        binding.filter.setOnClickListener {
            showFilterDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFilterDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.filter_layout, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Filter")
            .setCancelable(true)
            .setView(view)

        filterDialog = builder.create()
        filterDialog.show()

        val filterGroup = view.findViewById<RadioGroup>(R.id.filter_option)
        filterGroup.isSelected = true
        if (selectedOptionId == null) {
            selectedOptionId = R.id.allComics
        }
        Log.d("Selected", selectedOptionId.toString())
        filterGroup.check(selectedOptionId!!)
        view.filterSave.setOnClickListener {
            selectedOptionId = filterGroup.checkedRadioButtonId
            filterDialog.cancel()
            handleFilter()
        }
        view.filterCancel.setOnClickListener {
            filterDialog.cancel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleFilter() {
        when (selectedOptionId) {
            R.id.allComics -> {
                adapter.getData(comicList)
                binding.ComicsRv.visibility = View.VISIBLE
                binding.ComicsRv.adapter = adapter
                binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.ComicsRv.setHasFixedSize(true)

                return
            }
            R.id.thisWeek -> filteredList = viewModel.filterComics(1, comicList)
            R.id.lastWeek -> filteredList = viewModel.filterComics(2, comicList)
            R.id.nextWeek -> filteredList = viewModel.filterComics(3, comicList)
            R.id.thisMonth -> filteredList = viewModel.filterComics(4, comicList)
        }
        adapter.getData(filteredList)
        binding.ComicsRv.visibility = View.VISIBLE
        binding.ComicsRv.adapter = adapter
        binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.ComicsRv.setHasFixedSize(true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getComics() {
        viewModel.getComics()
        viewModel.comicsList.observe(viewLifecycleOwner, Observer { res ->
            when (res.status) {
                Status.LOADING -> {
                    binding.comicsPb.visibility = View.VISIBLE
                    binding.ComicsRv.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.comicsPb.visibility = View.GONE
                    comicList = res.data?.data?.results!!
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