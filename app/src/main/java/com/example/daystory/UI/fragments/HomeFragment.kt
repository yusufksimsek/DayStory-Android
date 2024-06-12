package com.example.daystory.UI.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private val eventsViewModel: EventViewModel by activityViewModels()
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

        setupToolbarTitle()
        setupHomeRecyclerViewDate()
        setTodayDate()

        binding.addEventFab.setOnClickListener {
            navigateToAddEvent()
        }

        binding.btnAI.setOnClickListener {
            if (binding.btnAI.isClickable) {
                showAIAlertDialog()
            }
        }

        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        val selectedDate = eventsViewModel.selectedDate.value
        if (selectedDate != null) {
            eventsViewModel.checkDaySummary(selectedDate)
        }
    }

    private fun observeLiveData() {
        eventsViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            eventsViewModel.checkDaySummary(date)
        })

        eventsViewModel.daySummaryStatus.observe(viewLifecycleOwner, Observer { isSummaryExists ->
            updateAISummaryStatus(isSummaryExists)
            eventAdapter.updateIsSummaryExists(isSummaryExists)
        })

        eventsViewModel.eventDeletionStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                eventsViewModel.clearEventDeletionStatus()
            }
        })
    }

    private fun updateAISummaryStatus(isSummaryExists: Boolean) {
        if (isSummaryExists) {
            binding.btnAI.setBackgroundResource(R.drawable.pasif_button)
            binding.btnAI.isClickable = false
            binding.addEventFab.visibility = View.GONE
        } else {
            binding.btnAI.setBackgroundResource(R.drawable.button_background2)
            binding.btnAI.isClickable = true
            binding.addEventFab.visibility = View.VISIBLE
        }
    }

    private fun setupToolbarTitle() {
        val toolbar = binding.materialToolbar
        val title = "DayStory"
        val spannableString = SpannableString(title)

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), 0, 3,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        toolbar.title = spannableString
    }

    private fun showAIAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Uyarı")
            .setMessage("Günde yalnızca 1 kez AI gün özetinizi oluşturabilirsiniz." +
                    "\n\nDevam etmek istiyor musunuz?")
            .setPositiveButton("Devam Et") { dialog, which ->
                dialog.dismiss()
                showLogoAnimation()
                createDaySummary()
            }
            .setNegativeButton("Vazgeç") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createDaySummary() {
        val currentDate = getCurrentDate()
        lifecycleScope.launch {
            withContext(Dispatchers.IO){

            }
            try {
                val result =  requestDaySummary(currentDate)
                hideLogoAnimation()
                if (result) {
                    Toast.makeText(context, "Day summary oluşturuldu", Toast.LENGTH_SHORT).show()
                    navigateToImageDetailFragment()
                } else {
                    Toast.makeText(context, "Day summary oluşturulamadı", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun requestDaySummary(currentDate: String): Boolean {
        return try {
            val request = EventService.daySummaryCreateRequest(currentDate)
            val response = RetrofitClient.eventApi.createDaySummary(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private fun showLogoAnimation() {
        val logoImageView = binding.logoImageView
        logoImageView.visibility = View.VISIBLE
        val rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_logo)
        logoImageView.startAnimation(rotateAnimation)
    }

    private fun hideLogoAnimation() {
        val logoImageView = binding.logoImageView
        logoImageView.clearAnimation()
        logoImageView.visibility = View.GONE
    }

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

    private fun navigateToAddEvent() {
        findNavController().navigate(R.id.action_homeFragment_to_addEventFragment)
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
        eventAdapter = EventAdapter(eventsViewModel, false)
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
