package com.dev.online_food_recipes_example.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dev.online_food_recipes_example.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // Make sure we can get the binding Layout
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        // Setup lifecycleOwner to let the LiveData observe data change
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        // Avoid OOM
        _binding = null
    }

}