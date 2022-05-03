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
import com.dev.online_todo_list_example.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {
    // get the result
    private val args by navArgs<UpdateFragmentArgs>()

    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val mShareViewModel: ShareViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        // Set Menu
        setHasOptionsMenu(true)

        // initView
        initView(view)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun initView(view: View) {
        view.edt_current_title.setText(args.currentItem.title)
        view.sp_current_priorities.setSelection(mShareViewModel.parsePriorityToInt(args.currentItem.priority))
        view.edt_current_description.setText(args.currentItem.description)

        view.sp_current_priorities.onItemSelectedListener = mShareViewModel.listener
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
}