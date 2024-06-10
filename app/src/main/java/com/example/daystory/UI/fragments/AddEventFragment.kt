package com.example.daystory.UI.fragments

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.UI.viewmodel.EventViewModel
import com.example.daystory.UI.viewmodel.EventViewModelFactory
import com.example.daystory.api.model.Event
import com.example.daystory.databinding.FragmentAddEventBinding
import com.example.daystory.repository.EventRepository
import java.util.Locale

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private var addEventBinding: FragmentAddEventBinding? = null
    private val binding get() = addEventBinding!!
    private lateinit var eventsViewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        addEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventRepository = EventRepository()
        val viewModelFactory = EventViewModelFactory(requireActivity().application, eventRepository)
        eventsViewModel = ViewModelProvider(this, viewModelFactory)[EventViewModel::class.java]

        val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

        binding.textViewDateAdd.text = todayDate

        binding.btnCancel.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.addBackIcon.setOnClickListener {
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

        binding.btnSave.setOnClickListener {
            val title = binding.addEventTitle.text.toString().trim()
            val desc = binding.addEventDesc.text.toString().trim()

            eventsViewModel.validateTitle(title)
            eventsViewModel.validateDesc(desc)

            if (eventsViewModel.addTitleError.value == null && eventsViewModel.addDescError.value == null) {
                val newEvent = Event(
                    title = title,
                    description = desc,
                    date = todayDate
                )
                eventsViewModel.addEvent(newEvent)
            }else {
                if (binding.TitleInputLayout.error != null) {
                    binding.TitleInputLayout.error = "Lütfen bu alanı doldurun"
                }
            }
        }

        eventsViewModel.eventCreationStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                eventsViewModel.clearEventCreationStatus()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        addEventBinding = null
    }
}
