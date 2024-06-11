package com.example.daystory.UI.fragments

import android.app.AlertDialog
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
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daystory.MainActivity
import com.example.daystory.R
import com.example.daystory.UI.adapter.EventAdapter
import com.example.daystory.databinding.FragmentHomeBinding
import com.example.daystory.UI.viewmodel.EventViewModel
import com.example.daystory.api.model.Event
import com.example.daystory.api.service.EventService
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var eventsViewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        eventsViewModel = (activity as MainActivity).eventViewModel
        setupHomeRecyclerViewDate()
        setTodayDate()

        binding.addEventFab.setOnClickListener {
            navigateToAddEvent()
        }

        checkDateAndToggleFab()

        binding.btnAI.setOnClickListener {
            if (binding.btnAI.isClickable) {
                showAIAlertDialog()
            }
        }

        eventsViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->

        })

        eventsViewModel.eventDeletionStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                eventsViewModel.clearEventDeletionStatus()
            }
        })
    }

    private fun showAIAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Uyarı")
            .setMessage("Günde yalnızca 1 kez AI gün özetinizi oluşturabilirsiniz.\n\nDevam etmek istiyor musunuz?")
            .setPositiveButton("Devam Et") { dialog, which ->
                dialog.dismiss()
                //createDaySummary()
                navigateToImageDetailFragment()
            }
            .setNegativeButton("Vazgeç") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
        /*
    private fun createDaySummary() {
        val currentDate = getCurrentDate()
        lifecycleScope.launch {
            try {
                val request = EventService.daySummaryCreateRequest(currentDate)
                val response = RetrofitClient.eventApi.createDaySummary(request)
                if (response.isSuccessful && response.body()?.status == 200) {
                    Toast.makeText(context, "Day summary oluşturuldu", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Day summary oluşturulamadı", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
         */

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    private fun navigateToImageDetailFragment() {
        val selectedDateEvents = eventsViewModel.eventsByDate.value ?: emptyList()
        val direction = HomeFragmentDirections.actionHomeFragmentToImageDetailFragment(selectedDateEvents.toTypedArray())
        findNavController().navigate(direction)
    }

    private fun setTodayDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        binding.textViewDate.text = date

        eventsViewModel.setSelectedDate(date)
    }

    private fun checkDateAndToggleFab() {
        val currentDate = Calendar.getInstance().time
        val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(binding.textViewDate.text.toString())

        if (selectedDate != null && (selectedDate.after(currentDate) || isSameDay(selectedDate, currentDate))) {
            binding.addEventFab.visibility = View.VISIBLE
        } else {
            binding.addEventFab.visibility = View.GONE
        }
    }

    private fun navigateToAddEvent() {
        findNavController().navigate(R.id.action_homeFragment_to_addEventFragment)
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = date1 }
        val calendar2 = Calendar.getInstance().apply { time = date2 }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun updateUI(event: List<Event>?) {
        if (event != null) {
            if (event.isEmpty()) {
                binding.homeRecyclerView.visibility = View.GONE
                binding.textViewNotBulunmuyor.visibility = View.VISIBLE
                binding.btnAI.setBackgroundResource(R.drawable.pasif_button)
                binding.btnAI.isClickable = false
            } else {
                binding.homeRecyclerView.visibility = View.VISIBLE
                binding.textViewNotBulunmuyor.visibility = View.GONE
                binding.btnAI.setBackgroundResource(R.drawable.button_background2)
                binding.btnAI.isClickable = true
            }
        }
    }

    private fun setupHomeRecyclerViewDate() {
        eventAdapter = EventAdapter(eventsViewModel)
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        activity?.let {
            eventsViewModel.eventsByDate.observe(viewLifecycleOwner, Observer { events ->
                eventAdapter.submitList(events)
                updateUI(events)
            })
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
