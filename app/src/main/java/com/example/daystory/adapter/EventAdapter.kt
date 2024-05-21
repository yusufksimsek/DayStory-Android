package com.example.daystory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.daystory.databinding.EventLayoutBinding
import com.example.daystory.fragments.HomeFragmentDirections
import com.example.daystory.model.Event

class EventAdapter : ListAdapter<Event,EventAdapter.EventViewHolder>(Mydif) {

    class EventViewHolder(val binding: EventLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentEvent: Event){
            binding.eventTitle.text = currentEvent.eventTitle
            binding.eventDesc.text = currentEvent.eventDesc

            binding.root.setOnClickListener {
                val direction = HomeFragmentDirections.actionHomeFragmentToEditEventFragment(currentEvent)
                it.findNavController().navigate(direction)
            }
        }
    }

    object Mydif : DiffUtil.ItemCallback<Event>(){
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
                    // oldItem.eventDesc == newItem.eventDesc &&
                    //oldItem.eventTitle == newItem.eventTitle
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
