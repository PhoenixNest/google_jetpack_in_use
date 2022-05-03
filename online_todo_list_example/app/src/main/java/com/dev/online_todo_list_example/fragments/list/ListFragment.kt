package com.dev.online_todo_list_example.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.data.viewmodel.ToDoViewModel
import com.dev.online_todo_list_example.databinding.FragmentListBinding
import com.dev.online_todo_list_example.fragments.ShareViewModel
import com.dev.online_todo_list_example.fragments.list.adapter.RVAdapter
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    // Make sure we can get the binding Layout
    private val binding get() = _binding!!

    // lazy put the RVAdapter and ViewModel
    private val rvAdapter by lazy { RVAdapter() }

    private val mShareViewModel: ShareViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner to let the LiveData observe data change
        binding.lifecycleOwner = this

        // Bind layout variable
        binding.mShareViewModel = mShareViewModel

        // Setup RV
        setupRecyclerView()

        // Bind Data to RecyclerView
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            mShareViewModel.checkIfDatabaseEmpty(it)
            rvAdapter.setData(it)
        })

        // Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView

        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Swiper to Delete
        setUpRecyclerViewSwiperToDelete(recyclerView)
    }

    private fun setUpRecyclerViewSwiperToDelete(recyclerView: RecyclerView) {
        val swiperDeleteItemCallback = object : SwiperToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Delete the swiper item, use viewHolder.adapterPosition to get locate to the current-item
                val deleteItem = rvAdapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(deleteItem)

                // Inform the adapter
                rvAdapter.notifyItemChanged(viewHolder.adapterPosition)

                // Undo delete
                undoDeleteItem(viewHolder.itemView, deleteItem, viewHolder.adapterPosition)
            }
        }

        // Attach the Helper with Recyclerview
        val itemTouchHelper = ItemTouchHelper(swiperDeleteItemCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun undoDeleteItem(view: View, deleteItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deleteItem.title}'",
            Snackbar.LENGTH_SHORT
        )

        // Set the button click listener in SnackBar
        snackBar.setAction("Undo") {
            // Restore the deleted item
            mToDoViewModel.insertData(deleteItem)

            // Inform the adapter
            rvAdapter.notifyItemChanged(position)
        }

        // Show the SnackBar
        snackBar.show()
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

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid OOM
        _binding = null
    }
}
