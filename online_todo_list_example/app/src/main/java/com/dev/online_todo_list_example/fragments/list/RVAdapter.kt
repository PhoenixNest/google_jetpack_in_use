package com.dev.online_todo_list_example.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.Priority
import com.dev.online_todo_list_example.data.models.ToDoData
import kotlinx.android.synthetic.main.row_item.view.*

// 1. Create you own ViewHolder
// 2. assign the ViewHolder to RecyclerView.Adapter
// 3. overwrite function: onCreateViewHolder(), getItemCount(), onBindViewHolder()
class RVAdapter : RecyclerView.Adapter<RVAdapter.MyViewHolder>() {

    private var dataList = emptyList<ToDoData>()

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // title
        holder.itemView.tv_title.text = dataList[position].title

        // description
        holder.itemView.tv_description.text = dataList[position].description

        // check the priority level and set the card background color follow the level
        when (dataList[position].priority) {
            Priority.HIGH -> holder.itemView.cv_priority_level.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )

            Priority.MEDIUM -> holder.itemView.cv_priority_level.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.yellow)
            )

            Priority.LOW -> holder.itemView.cv_priority_level.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }

        // click listener
        holder.itemView.row_background.setOnClickListener {
            // if we click the item, navigate to the update fragment, and pass the currentItem object
            val currentItem = dataList[position]
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)

            holder.itemView.findNavController().navigate(action)
        }
    }

}