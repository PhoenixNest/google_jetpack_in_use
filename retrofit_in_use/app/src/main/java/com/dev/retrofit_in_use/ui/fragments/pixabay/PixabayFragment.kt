package com.dev.retrofit_in_use.ui.fragments.pixabay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dev.retrofit_in_use.databinding.FragmentPixabayBinding
import com.dev.retrofit_in_use.ui.fragments.pixabay.adapter.PixabayRVAdapter
import com.dev.retrofit_in_use.utils.NetworkResult
import com.dev.retrofit_in_use.viewmodel.MainViewModel
import com.dev.retrofit_in_use.viewmodel.PixabayViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PixabayFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var pixabayViewModel: PixabayViewModel

    private var _binding: FragmentPixabayBinding? = null

    // Ensure that we can get the binding Layout
    private val binding get() = _binding!!

    private val pixabayRVAdapter by lazy { PixabayRVAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        pixabayViewModel = ViewModelProvider(requireActivity())[PixabayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPixabayBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner
        binding.lifecycleOwner = this

        setUpRecyclerView(binding.rvPixabay)

        // Fetch Data From Local-Database or Remote-Server
        fetchData()

        return binding.root
    }

    private fun fetchData() {
        lifecycleScope.launch {
            // Always Check the Database in background
            mainViewModel.pixabayData.observe(viewLifecycleOwner, Observer {
                // If Database is not Empty, use the Cache Data
                if (it.isNotEmpty()) {
                    // Because we have only store one row in Database,
                    // The index which is 0 is that we need
                    pixabayRVAdapter.setData(it[0].pixabay)

                    // Hide the Shimmer Effect
                    hideShimmerEffect()
                }
                // If Database is Empty, fetch the newest Data from Remote-Server
                else {
                    requestDataFromRemoteServer()
                }
            })
        }
    }

    /* ======================== Local ROOM Database ======================== */
    private fun readDataFromLocalDatabase() {
        lifecycleScope.launch {
            mainViewModel.pixabayData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    pixabayRVAdapter.setData(it[0].pixabay)
                }
            })
        }
    }

    /* ======================== Remote Server ======================== */
    private fun requestDataFromRemoteServer() {
        mainViewModel.getPixabayData(pixabayViewModel.setUpQueries())
        mainViewModel.pixabayResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        pixabayRVAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()

                    // Always Check and Load the previous cache data when NetworkResult.Error
                    readDataFromLocalDatabase()

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = pixabayRVAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        // Use Recyclerview-Animators
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.shimmer.visibility = View.VISIBLE
        binding.rvPixabay.visibility = View.INVISIBLE
        binding.shimmer.showShimmer(true)
    }

    private fun hideShimmerEffect() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.INVISIBLE
        binding.rvPixabay.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid OOM
        _binding = null
    }
}