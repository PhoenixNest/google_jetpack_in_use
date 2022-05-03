package com.dev.online_todo_list_example.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.data.viewmodel.ToDoViewModel
import com.dev.online_todo_list_example.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val mShareViewModel: ShareViewModel by viewModels()

    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set Menu
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        view.sp_priorities.onItemSelectedListener = mShareViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDatabase()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val mTitle = edt_title.text.toString()
        val mPriority = sp_priorities.selectedItem.toString()
        val mDescription = edt_description.text.toString()

        val validation = mShareViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validation) {
            // insert data into Database
            val newData = ToDoData(
                id = 0,
                title = mTitle,
                priority = mShareViewModel.parsePriority(mPriority),
                description = mDescription
            )

            mToDoViewModel.insertData(newData)
            Toast.makeText(
                requireContext(),
                getString(R.string.insert_success),
                Toast.LENGTH_SHORT
            ).show()

            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.insert_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}