package com.example.daystory.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.daystory.MainActivity
import com.example.daystory.R
import com.example.daystory.databinding.FragmentAddEventBinding
import com.example.daystory.model.Event
import com.example.daystory.viewmodel.EventViewModel

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
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        eventsViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)
        addEventView = view

        eventsViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            binding.textViewDateAdd.text = date
        }

        binding.addBackIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            saveEvent()
        }

        binding.btnCancel.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.addEventTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                eventsViewModel.validateTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.addEventDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                eventsViewModel.validateDesc(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        eventsViewModel.addTitleError.observe(viewLifecycleOwner) { error ->
            binding.TitleInputLayout.error = error
        }

        eventsViewModel.addDescError.observe(viewLifecycleOwner) { error ->
            binding.DescInputLayout.error = error
        }
    }

    private fun saveEvent() {
        val eventTitle = binding.addEventTitle.text.toString().trim()
        val eventDesc = binding.addEventDesc.text.toString().trim()
        val date = binding.textViewDateAdd.text.toString()

        eventsViewModel.validateTitle(eventTitle)
        eventsViewModel.validateDesc(eventDesc)

        if (eventTitle.isNotEmpty() && eventDesc.isNotEmpty() && binding.TitleInputLayout.error == null && binding.DescInputLayout.error == null) {
            val event = Event(0, eventTitle, eventDesc, date)
            eventsViewModel.addEvent(event)
            Toast.makeText(addEventView.context, "Event Saved", Toast.LENGTH_SHORT).show()
            addEventView.findNavController().popBackStack()
        } else {
            Toast.makeText(addEventView.context, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
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
