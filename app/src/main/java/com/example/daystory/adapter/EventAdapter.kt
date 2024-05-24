package com.example.daystory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.daystory.R
import com.example.daystory.databinding.EventLayoutBinding
import com.example.daystory.fragments.HomeFragmentDirections
import com.example.daystory.model.Event
import com.example.daystory.viewmodel.EventViewModel

class EventAdapter(private val eventsViewModel: EventViewModel) : ListAdapter<Event, EventAdapter.EventViewHolder>(Mydif) {

    class EventViewHolder(val binding: EventLayoutBinding, private val eventsViewModel: EventViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentEvent: Event) {
            binding.eventTitle.text = currentEvent.eventTitle
            binding.eventDesc.text = currentEvent.eventDesc

            binding.moreVertIcon.setOnClickListener { view ->
                showPopupMenu(view, currentEvent)
            }

        }

        private fun showPopupMenu(view: View, currentEvent: Event) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.event_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_event -> {
                        val direction = HomeFragmentDirections.actionHomeFragmentToEditEventFragment(currentEvent)
                        view.findNavController().navigate(direction)
                        true
                    }
                    R.id.delete_event -> {
                        eventsViewModel.deleteEvent(currentEvent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    object Mydif : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            eventsViewModel
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

