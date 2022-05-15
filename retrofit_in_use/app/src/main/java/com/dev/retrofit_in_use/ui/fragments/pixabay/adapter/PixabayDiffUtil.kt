package com.dev.retrofit_in_use.ui.fragments.pixabay.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dev.retrofit_in_use.models.Hit

class PixabayDiffUtil(
    private val oldList: List<Hit>,
    private val newList: List<Hit>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}