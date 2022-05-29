package com.dev.online_food_recipes_example.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_food_recipes_example.R
import com.dev.online_food_recipes_example.databinding.FragmentRecipesBinding
import com.dev.online_food_recipes_example.ui.fragments.recipes.adapter.RecipesRVAdapter
import com.dev.online_food_recipes_example.utils.NetworkListener
import com.dev.online_food_recipes_example.utils.NetworkResult
import com.dev.online_food_recipes_example.viewmodels.MainViewModel
import com.dev.online_food_recipes_example.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel

    private var _binding: FragmentRecipesBinding? = null

    // Make sure we can get the binding Layout
    private val binding get() = _binding!!

    private val recipesRVAdapter by lazy { RecipesRVAdapter() }

    // Network Checker
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind ViewModel
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipeViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        // Setup lifecycleOwner to let the LiveData observe data change
        binding.lifecycleOwner = this

        // Bind Layout Parameter
        binding.mainViewModel = mainViewModel

        setUpRecyclerView(binding.rvRecipes)

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner, Observer {
            recipeViewModel.backOnline = it
        })

        /* ======================== Check Network Status ======================== */

        // Always check the network capability in background
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()

            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())

                    // Show the Error Message when network is unavailable
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()

                    // Fetch Data From Local-Database or Remote-Server
                    fetchData()
                }
        }

        // Handle BottomSheet
        binding.fbRecipes.setOnClickListener {
            if (recipeViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheet)
            } else {
                recipeViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    // Fetch Data From Local-Database or Remote-Server
    private fun fetchData() {
        lifecycleScope.launch {
            // Always Check the Database in background
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer { dataBaseList ->
                // If Database is not Empty, use the Cache Data
                if (dataBaseList.isNotEmpty() && !args.backFromBottomSheet) {
                    // Because we have only store one row in Database,
                    // The index which is 0 is that we need
                    recipesRVAdapter.setData(dataBaseList.first().foodRecipe)

                    // Hide the Shimmer Effect
                    hideShimmerEffect()
                }
                // If Database is Empty, fetch the newest Data from Remote-Server
                // Also if we navigate back form BottomSheet, Request the new Data again
                else {
                    requestDataFromRemoteServer()
                }
            })
        }
    }

    // Custom Function to Fix the Fetch-Data-Twice-Call
    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T) {
                // The Data is not from the Local access is obtained from the Cloud
                // we just need Observe the LiveData Object only Once and not Every-Time
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }

    /* ======================== Local Room Database ======================== */

    private fun readDataFromLocalDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, Observer { dataBaseList ->
                if (dataBaseList.isNotEmpty()) {
                    // Because we have only store one row in Database,
                    // The index which is 0 is that we need
                    recipesRVAdapter.setData(dataBaseList.first().foodRecipe)
                }
            })
        }
    }

    /* ======================== Remote Server ======================== */

    private fun requestDataFromRemoteServer() {
        mainViewModel.getRecipes(recipeViewModel.setUpQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        recipesRVAdapter.setData(it)
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

    /* ======================== RecyclerView ======================== */

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = recipesRVAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    /* ======================== Shimmer ======================== */

    private fun showShimmerEffect() {
        binding.shimmer.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.INVISIBLE
        binding.shimmer.showShimmer(true)
    }

    private fun hideShimmerEffect() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.INVISIBLE
        binding.rvRecipes.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroyView()

        // Avoid OOM
        _binding = null
    }

}