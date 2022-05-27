package com.dev.online_food_recipes_example.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.dev.online_food_recipes_example.databinding.RecipeBottomSheetBinding
import com.dev.online_food_recipes_example.utils.Constants
import com.dev.online_food_recipes_example.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class RecipeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip = Constants.DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = Constants.DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Provide ViewModel
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)

        // Observing Datastore Data Change then apply it to Request-Parameters
        recipesViewModel.readMealAndDietType.asLiveData().observe(
            viewLifecycleOwner, Observer { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType

                // Update Ui to follow the Data Change
                updateChipGroup(binding.cgFoodType, value.selectedMealTypeId)
                updateChipGroup(binding.cgDietType, value.selectedDietTypeId)
            })

        // ChipGroup
        handleChipGroupCheckedChange()

        // Apply
        handleClickListener()

        return binding.root
    }

    /* ======================== Chip Group ======================== */

    private fun handleChipGroupCheckedChange() {
        binding.cgFoodType.setOnCheckedChangeListener { group, selectedChipId ->
            // Get the current Check Chip
            val chip = group.findViewById<Chip>(selectedChipId)

            // Get the current Check Chip Text
            val selectMealType = chip.text.toString().toLowerCase(Locale.ROOT)

            // set Select Chip Text
            mealTypeChip = selectMealType

            // set Select Chip Id
            mealTypeChipId = selectedChipId
        }

        binding.cgDietType.setOnCheckedChangeListener { group, selectedChipId ->
            // Get the current Check Chip
            val chip = group.findViewById<Chip>(selectedChipId)

            // Get the current Check Chip Text
            val selectDietType = chip.text.toString().toLowerCase(Locale.ROOT)

            // set Select Chip Text
            dietTypeChip = selectDietType

            // set Select Chip Id
            dietTypeChipId = selectedChipId
        }
    }

    private fun updateChipGroup(chipGroup: ChipGroup, selectChipId: Int) {
        // Because the default check id is 0
        // When the id is not 0 that means User have click the chipGroup
        if (selectChipId != 0) {
            try {
                chipGroup.findViewById<Chip>(selectChipId).isChecked = true
            } catch (exception: Exception) {
                Log.d("RecipeBottomSheet", exception.message.toString())
            }
        }
    }

    /* ======================== Apply ======================== */

    private fun handleClickListener() {
        binding.btnApply.setOnClickListener {
            // Save data into DataStore
            recipesViewModel.saveMealAndDietType(
                mealType = mealTypeChip,
                mealTypeId = mealTypeChipId,
                dietType = dietTypeChip,
                dietTypeId = dietTypeChipId
            )

            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }
    }
}