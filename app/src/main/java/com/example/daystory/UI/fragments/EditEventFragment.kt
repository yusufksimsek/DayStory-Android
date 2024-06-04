package com.example.daystory.UI.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.daystory.R
import com.example.daystory.databinding.FragmentEditEventBinding
import com.example.daystory.UI.viewmodel.EventViewModel
import com.example.daystory.api.model.Event

class EditEventFragment : Fragment(R.layout.fragment_edit_event), MenuProvider {

    private var editEventBinding: FragmentEditEventBinding? = null
    private val binding get() = editEventBinding!!

    private lateinit var eventsViewModel: EventViewModel
    private lateinit var currentEvent: Event

    private val args: EditEventFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editEventBinding = FragmentEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        eventsViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)
        currentEvent = args.event!!

        binding.editEventTitle.setText(currentEvent.title)
        binding.editEventDesc.setText(currentEvent.description)
        binding.textViewDateEdit.setText(currentEvent.date)

        binding.editBackIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            saveEvent()
        }

        binding.btnCancel.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.editEventTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                eventsViewModel.validateTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editEventDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                eventsViewModel.validateDesc(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        eventsViewModel.addTitleError.observe(viewLifecycleOwner) { error ->
            binding.editTitleInputLayout.error = error
        }

        eventsViewModel.addDescError.observe(viewLifecycleOwner) { error ->
            binding.editDescInputLayout.error = error
        }
    }

    private fun saveEvent() {
        val eventTitle = binding.editEventTitle.text.toString().trim()
        val eventDesc = binding.editEventDesc.text.toString().trim()
        val existingDate = currentEvent.date

        eventsViewModel.validateTitle(eventTitle)
        eventsViewModel.validateDesc(eventDesc)

        if (eventTitle.isNotEmpty() && eventDesc.isNotEmpty() && binding.editTitleInputLayout.error == null && binding.editDescInputLayout.error == null) {
            //val event = Event(currentEvent.id, eventTitle, eventDesc, existingDate)
            //eventsViewModel.updateEvent(event)
            Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_event, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        editEventBinding = null
    }
}
