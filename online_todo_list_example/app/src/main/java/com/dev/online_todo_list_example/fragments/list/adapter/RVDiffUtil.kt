package com.dev.online_todo_list_example.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dev.online_todo_list_example.data.models.ToDoData

class RVDiffUtil(
    private val oldList: List<ToDoData>,
    private val newList: List<ToDoData>
) : DiffUtil.Callback() {
    // it return the size of the old list.
    override fun getOldListSize(): Int {
        return oldList.size
    }

    // Returns the size of the new list.
    override fun getNewListSize(): Int {
        return newList.size
    }

    // Called by the DiffUtil to decide whether two object represent the same Item.
    // If your item have unique ids, this method should check their id equality.
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        // use "===" to check the two item are identical
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    // Checks whether two items have the same data.
    // You can change its behaviour depending on your Ui.
    // This method is called by DiffUtil only if areItemsTheSame() return true
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        // check the two item Content are equal
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
                && oldList[oldItemPosition].description == newList[newItemPosition].description
    }

}