package com.dev.retrofit_in_use.ui.fragments.pixabay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.retrofit_in_use.databinding.RvItemBinding
import com.dev.retrofit_in_use.models.Hit
import com.dev.retrofit_in_use.models.Pixabay

class PixabayRVAdapter : RecyclerView.Adapter<PixabayRVAdapter.MyViewHolder>() {

    private var pixabay = emptyList<Hit>()

    fun setData(pixabayHits: Pixabay) {
        val pixabayDiffUtil = PixabayDiffUtil(
            oldList = pixabay,
            newList = pixabayHits.hits
        )

        val pixabayCalculateDiff = DiffUtil.calculateDiff(pixabayDiffUtil)

        this.pixabay = pixabayHits.hits

        pixabayCalculateDiff.dispatchUpdatesTo(this)
    }

    class MyViewHolder(private val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(hit: Hit) {
            binding.hit = hit
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentHit = pixabay[position]
        holder.bindData(currentHit)
    }

    override fun getItemCount(): Int {
        return pixabay.size
    }

}