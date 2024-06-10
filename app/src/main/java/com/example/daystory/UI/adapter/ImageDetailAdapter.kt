package com.example.daystory.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daystory.api.model.Event
import com.example.daystory.databinding.EventLayoutBinding

class ImageDetailAdapter : RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {

    private var events: List<Event> = listOf()

    class ImageDetailViewHolder(private val binding: EventLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventDesc.text = event.description
            binding.moreVertIcon.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        val binding = EventLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun submitList(eventList: List<Event>) {
        events = eventList
        notifyDataSetChanged()
    }
}
