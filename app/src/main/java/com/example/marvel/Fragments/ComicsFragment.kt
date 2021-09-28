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
import com.example.marvel.Utils.Constants.FILTER_LAST_WEEK
import com.example.marvel.Utils.Constants.FILTER_NEXT_WEEK
import com.example.marvel.Utils.Constants.FILTER_THIS_MONTH
import com.example.marvel.Utils.Constants.FILTER_THIS_WEEK
import com.example.marvel.ViewModels.ComicsViewModel
import com.example.marvel.databinding.FragmentComicsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.filter_layout.view.*


class ComicsFragment : Fragment() {

    companion object {
        var allComicList : ArrayList<ComicsResponse> = ArrayList()
        var allStored : Boolean = false
    }


    private lateinit var binding: FragmentComicsBinding
    private lateinit var viewModel: ComicsViewModel
    private var comicList: ArrayList<ComicsResponse> = ArrayList()
    private val adapter = ComicsAdapter()
    private lateinit var filterDialog: AlertDialog
    private var selectedOptionId: Int? = null

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

        binding.ComicsRv.adapter = adapter
        binding.ComicsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.ComicsRv.setHasFixedSize(true)

        getComics()
        if (!allStored) {
            viewModel.getComics()
        } else {
            binding.ComicsRv.visibility = View.VISIBLE
            adapter.getData(allComicList)
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
            R.id.allComics -> viewModel.getComics()
            R.id.thisWeek -> viewModel.getFilterComicsComics(FILTER_THIS_WEEK)
            R.id.lastWeek -> viewModel.getFilterComicsComics(FILTER_LAST_WEEK)
            R.id.nextWeek -> viewModel.getFilterComicsComics(FILTER_NEXT_WEEK)
            R.id.thisMonth -> viewModel.getFilterComicsComics(FILTER_THIS_MONTH)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getComics() {
        viewModel.comicsList.observe(viewLifecycleOwner, Observer { res ->
            when (res.status) {
                Status.LOADING -> {
                    binding.comicsPb.visibility = View.VISIBLE
                    binding.ComicsRv.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.comicsPb.visibility = View.GONE
                    comicList = res.data?.data?.results!!
                    if (!allStored){
                        allComicList.addAll(comicList)
                        allStored = true
                    }
                    adapter.getData(comicList)
                    binding.ComicsRv.visibility = View.VISIBLE

                    if (comicList.isEmpty()){
                        binding.ComicsRv.visibility = View.GONE
                        binding.notFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.ComicsRv.visibility = View.VISIBLE
                        binding.notFound.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    binding.comicsPb.visibility = View.GONE
                    binding.ComicsRv.visibility = View.GONE
                    Snackbar.make(binding.root, res.msg.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        viewModel.getComics()
                    }.show()
                }
            }
        })
    }
}