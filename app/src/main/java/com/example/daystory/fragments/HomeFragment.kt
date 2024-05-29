package com.example.daystory.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daystory.MainActivity
import com.example.daystory.R
import com.example.daystory.adapter.EventAdapter
import com.example.daystory.databinding.FragmentHomeBinding
import com.example.daystory.model.Event
import com.example.daystory.viewmodel.EventViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider,DatePickerDialog.OnDateSetListener {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var eventsViewModel : EventViewModel
    private lateinit var eventAdapter : EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        eventsViewModel = (activity as MainActivity).eventViewModel
        setupHomeRecyclerView()
        setupDateTextView()

        binding.addEventFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addEventFragment)
        }

        binding.textViewDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupDateTextView() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        binding.textViewDate.text = date

        eventsViewModel.setSelectedDate(date)
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Tarih Seçiniz")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = dateFormat.format(it)
            binding.textViewDate.text = dateString

            eventsViewModel.setSelectedDate(dateString)
        }

        datePicker.show(childFragmentManager, datePicker.toString())
    }

     override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        binding.textViewDate.text = date
    }

    private fun updateUI(event: List<Event>?){
        if(event != null){
            if(event.isEmpty()){
                binding.homeRecyclerView.visibility = View.GONE
                binding.textViewNotBulunmuyor.visibility = View.VISIBLE
            } else {
                binding.homeRecyclerView.visibility = View.VISIBLE
                binding.textViewNotBulunmuyor.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        eventAdapter = EventAdapter(eventsViewModel)
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        activity?.let {
            eventsViewModel.getEvents.observe(viewLifecycleOwner){ event ->
                eventAdapter.submitList(event)
                updateUI(event)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

}