package com.dev.online_todo_list_example.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.data.viewmodel.ToDoViewModel
import com.dev.online_todo_list_example.databinding.FragmentListBinding
import com.dev.online_todo_list_example.fragments.ShareViewModel
import com.dev.online_todo_list_example.fragments.list.adapter.RVAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

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

    /* ======================== RecyclerView ======================== */
    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView

        recyclerView.adapter = rvAdapter
        // recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        // Change the default Layout of RecyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        /* ======================== Third-party Extension ======================== */
        // Use Recyclerview-Animators
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

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
                undoDeleteItem(viewHolder.itemView, deleteItem)
            }
        }

        // Attach the Helper with Recyclerview
        val itemTouchHelper = ItemTouchHelper(swiperDeleteItemCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun undoDeleteItem(view: View, deleteItem: ToDoData) {
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deleteItem.title}'",
            Snackbar.LENGTH_SHORT
        )

        // Set the button click listener in SnackBar
        snackBar.setAction("Undo") {
            // Restore the deleted item
            mToDoViewModel.insertData(deleteItem)
        }

        // Show the SnackBar
        snackBar.show()
    }

    /* ======================== [Top Action Bar] ======================== */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        // Search-Action
        val search = menu.findItem(R.id.menu_search)

        // Force change the type of the actionView into SearchView
        val searchView = search.actionView as? SearchView

        // Customize the SearchView
        searchView?.isSubmitButtonEnabled = true

        // Implement the SearchListener
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()

            // Sort By High Priority
            R.id.menu_priority_high -> {
                mToDoViewModel.sortByHighPriority.observe(this, Observer {
                    rvAdapter.setData(it)
                })
            }

            // Sort By Low Priority
            R.id.menu_priority_low -> {
                mToDoViewModel.sortByLowPriority.observe(this, Observer {
                    rvAdapter.setData(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* ======================== [Top Action Bar] - Search ======================== */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }

        return true
    }

    private fun searchThroughDatabase(query: String) {
        // Add "%" to search in Database easily.
        // The SQL-Query will like this: "select * from todo_table where title like %title%"
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer {
            it?.let {
                // "let" will auto set the last line as its return value
                rvAdapter.setData(it)
            }
        })
    }

    /* ======================== [Top Action Bar] - Delete All ======================== */
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
