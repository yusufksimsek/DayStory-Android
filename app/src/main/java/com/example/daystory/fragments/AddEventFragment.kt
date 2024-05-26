package com.example.daystory.fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.daystory.MainActivity
import com.example.daystory.R
import com.example.daystory.databinding.FragmentAddEventBinding
import com.example.daystory.model.Event
import com.example.daystory.viewmodel.EventViewModel
import java.util.Date
import java.util.Locale

class AddEventFragment : Fragment(R.layout.fragment_add_event), MenuProvider {

    private var addEventBinding: FragmentAddEventBinding? = null
    private val binding get() = addEventBinding!!

    private lateinit var eventsViewModel: EventViewModel
    private lateinit var addEventView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        addEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        eventsViewModel = (activity as MainActivity).eventViewModel
        addEventView = view

        binding.addBackIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            saveEvent()

        }

    }

    private fun saveEvent() {
        val eventTitle = binding.addEventTitle.text.toString().trim()
        val eventDesc = binding.addEventDesc.text.toString().trim()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.format(Date())

        if (eventTitle.isNotEmpty() && eventDesc.isNotEmpty()) {
            val event = Event(0, eventTitle, eventDesc, date)
            eventsViewModel.addEvent(event)
            Log.d("AddEventFragment", "Event Date: $date")
            Toast.makeText(addEventView.context, "Event Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(addEventView.context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_event, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        addEventBinding = null
    }

}