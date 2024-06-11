package com.example.daystory.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daystory.api.model.Event
import com.example.daystory.databinding.EventLayoutBinding

class GalleryDetailAdapter : RecyclerView.Adapter<GalleryDetailAdapter.EventViewHolder>() {

    private var events: List<Event> = listOf()

    fun setEvents(events: List<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class EventViewHolder(private val binding: EventLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventDesc.text = event.description
        }
    }
}
