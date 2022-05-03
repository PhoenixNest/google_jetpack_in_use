package com.dev.online_todo_list_example.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.databinding.RowItemBinding

/*
* RV Adapter:
* 1. Create you own ViewHolder
* 2. assign the ViewHolder to RecyclerView.Adapter
* 3. overwrite function: onCreateViewHolder(), getItemCount(), onBindViewHolder()
*/
class RVAdapter : RecyclerView.Adapter<RVAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData

        notifyDataSetChanged()
    }

    class MyViewHolder(
        private val binding: RowItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(toDoData: ToDoData) {
            binding.toDoData = toDoData

            // Observe the data change
            binding.executePendingBindings()
        }

        companion object {
            // Bind the layout resource
            fun form(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder = MyViewHolder.form(parent)

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Bind layout variable
        val currentItem = dataList[position]
        holder.bindData(currentItem)
    }
}