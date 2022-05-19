package com.dev.retrofit_in_use.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dev.retrofit_in_use.databinding.FragmentDetailsBinding
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    // Ensure that we can get the binding Layout
    private val binding get() = _binding!!

    // get the result
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner
        binding.lifecycleOwner = this

        // Bind layout variable
        binding.args = args

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid OOM
        _binding = null
    }
}