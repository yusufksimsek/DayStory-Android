package com.example.daystory.UI.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.daystory.R
import com.example.daystory.UI.fragments.HomeFragmentDirections
import com.example.daystory.databinding.EventLayoutBinding
import com.example.daystory.UI.viewmodel.EventViewModel
import com.example.daystory.api.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventAdapter(
    private val eventsViewModel: EventViewModel,
    private var isSummaryExists: Boolean

) : ListAdapter<Event, EventAdapter.EventViewHolder>(Mydif) {


    inner class EventViewHolder(val binding: EventLayoutBinding, private val eventsViewModel: EventViewModel)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentEvent: Event) {
            binding.eventTitle.text = currentEvent.title
            binding.eventDesc.text = currentEvent.description

            val currentDate = Calendar.getInstance().time
            val eventDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(currentEvent.date)

            if (eventDate.before(currentDate) && !isSameDay(eventDate, currentDate) || isSummaryExists) {
                binding.moreVertIcon.visibility = View.GONE
            } else {
                binding.moreVertIcon.visibility = View.VISIBLE
                binding.moreVertIcon.setOnClickListener { view ->
                    showPopupMenu(view, currentEvent)
                }
            }
        }

        private fun isSameDay(date1: Date, date2: Date): Boolean {
            val calendar1 = Calendar.getInstance().apply { time = date1 }
            val calendar2 = Calendar.getInstance().apply { time = date2 }
            return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
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
                        showDeleteConfirmationDialog(view.context, currentEvent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun showDeleteConfirmationDialog(context: Context, event: Event) {
            AlertDialog.Builder(context)
                .setTitle("Notu Sil")
                .setMessage("Bu notu silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    eventsViewModel.deleteEvent(event.id!!)
                }
                .setNegativeButton("Hayır", null)
                .show()
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
    fun updateIsSummaryExists(isSummaryExists: Boolean) {
        this.isSummaryExists = isSummaryExists
        notifyDataSetChanged()
    }
}

