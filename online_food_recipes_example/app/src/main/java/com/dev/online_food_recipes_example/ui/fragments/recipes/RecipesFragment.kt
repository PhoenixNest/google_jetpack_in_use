package com.dev.online_food_recipes_example.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_food_recipes_example.databinding.FragmentRecipesBinding
import com.dev.online_food_recipes_example.ui.fragments.recipes.adapter.RecipesRVAdapter
import com.dev.online_food_recipes_example.utils.NetworkResult
import com.dev.online_food_recipes_example.viewmodels.MainViewModel
import com.dev.online_food_recipes_example.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel

    private var _binding: FragmentRecipesBinding? = null

    // Make sure we can get the binding Layout
    private val binding get() = _binding!!

    private val recipesRVAdapter by lazy { RecipesRVAdapter() }

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

        // Set up lifecycleOwner to let the LiveData observe data change
        binding.lifecycleOwner = this

        setUpRecyclerView(binding.rvRecipes)

        fetchData()

        return binding.root
    }

    // Fetch Data From Server
    private fun fetchData() {
        mainViewModel.getRecipes(recipeViewModel.setUpQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showShimmer()

                }

                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let {
                        recipesRVAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmer()

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showShimmer() {
        binding.shimmer.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.INVISIBLE
        binding.shimmer.showShimmer(true)
    }

    private fun hideShimmer() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.INVISIBLE
        binding.rvRecipes.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = recipesRVAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

}