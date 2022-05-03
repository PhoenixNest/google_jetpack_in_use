package com.dev.online_todo_list_example.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.Priority
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

// View Action Binding
class BindingAdapters {
    companion object {

        /* ======================== ListFragment ======================== */
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        // FLB action
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            if (navigate) {
                view.setOnClickListener {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:checkEmptyDatabase")
        @JvmStatic
        // Check if the Database is empty, then show the empty preview
        fun checkEmptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE

                else -> View.VISIBLE
            }
        }

        /*======================== UpdateFragment ========================*/
        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        // Check the Priority which inside the UpdateFragment
        fun parsePriorityToInt(spinner: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> spinner.setSelection(0)
                Priority.MEDIUM -> spinner.setSelection(1)
                Priority.LOW -> spinner.setSelection(2)
            }
        }

        /*======================== RV item ========================*/
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        // Check the Priority Level and set the card background color follow the level
        fun parsePriorityColor(cardView: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
                Priority.MEDIUM -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
                Priority.LOW -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        // RV onItemClickListener: pass the currentItem to UpdateFragment
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }
    }
}