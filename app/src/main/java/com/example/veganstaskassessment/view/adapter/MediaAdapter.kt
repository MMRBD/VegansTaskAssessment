package com.example.veganstaskassessment.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.veganstaskassessment.data.models.Content
import com.example.veganstaskassessment.databinding.ItemMediaBinding

class MediaAdapter(
    private val onItemClick: (content: Content) -> Unit
) : ListAdapter<Content, MediaAdapter.MediaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            ItemMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(content)
        }
    }

    inner class MediaViewHolder(private val binding: ItemMediaBinding) : ViewHolder(binding.root) {
        fun bind(content: Content) {
            with(binding) {
                tvMediaTitleCustom.text = content.mediaTitleCustom
                tvDateString.text = content.mediaDate.dateString
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content) =
            oldItem.mediaId == newItem.mediaId

        override fun areContentsTheSame(oldItem: Content, newItem: Content) =
            oldItem == newItem
    }
}