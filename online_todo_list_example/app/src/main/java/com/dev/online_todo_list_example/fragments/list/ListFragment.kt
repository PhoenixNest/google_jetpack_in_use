package com.dev.online_todo_list_example.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.viewmodel.ToDoViewModel
import com.dev.online_todo_list_example.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    // lazy put the RVAdapter and ViewModel
    private val rvAdapter by lazy { RVAdapter() }

    private val mShareViewModel: ShareViewModel by viewModels()

    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // initView

        // set up RV
        view.recyclerView.adapter = rvAdapter
        view.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // bind Data to View
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            mShareViewModel.checkIfDatabaseEmpty(it)
            rvAdapter.setData(it)
        })

        // show the Empty View if the Database is empty
        mShareViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseView(view, it)
        })

        // set up FLB
        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Set Menu
        setHasOptionsMenu(true)

        return view
    }

    private fun showEmptyDatabaseView(view: View, emptyDatabase: Boolean) {
        if (emptyDatabase) {
            view.no_data_imageView.visibility = View.VISIBLE
            view.no_data_textView.visibility = View.VISIBLE
        } else {
            view.no_data_imageView.visibility = View.INVISIBLE
            view.no_data_textView.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    // Show AlertDialog to confirm removal of all items from database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Everything ?")
        builder.setMessage("Are you sure you want to delete Everything ?")
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything...",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }

        // show the dialog
        builder.create().show()
    }
}
