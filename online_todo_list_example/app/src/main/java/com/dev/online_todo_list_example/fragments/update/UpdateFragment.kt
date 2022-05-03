package com.dev.online_todo_list_example.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.data.viewmodel.ToDoViewModel
import com.dev.online_todo_list_example.databinding.FragmentUpdateBinding
import com.dev.online_todo_list_example.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null

    // Make sure we can get the binding Layout
    private val binding get() = _binding!!

    // get the result
    private val args by navArgs<UpdateFragmentArgs>()

    // lazy put the ViewModel
    private val mShareViewModel: ShareViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner to let the LiveData observe data change
        binding.lifecycleOwner = this

        // Bind layout variable
        binding.args = args

        // Spinner Item Selected Listener
        binding.spCurrentPriorities.onItemSelectedListener = mShareViewModel.listener

        // Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = edt_current_title.text.toString()
        val priority = sp_current_priorities.selectedItem.toString()
        val description = edt_current_description.text.toString()

        val validation = mShareViewModel.verifyDataFromUser(title, description)
        if (validation) {
            // Update current item by id
            val updatedItem = ToDoData(
                id = args.currentItem.id,
                title = title,
                priority = mShareViewModel.parsePriority(priority),
                description = description
            )
            mToDoViewModel.updateData(updatedItem)

            Toast.makeText(
                requireContext(),
                getString(R.string.update_success),
                Toast.LENGTH_SHORT
            ).show()

            // Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.update_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Show AlertDialog to confirm item removal
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete ${args.currentItem.title} ?")
        builder.setMessage("Are you sure you want to delete '${args.currentItem.title}' ?")
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }

        // show the dialog
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid OOM
        _binding = null
    }
}