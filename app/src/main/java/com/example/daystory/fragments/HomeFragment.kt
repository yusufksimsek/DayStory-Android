package com.example.daystory.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.daystory.MainActivity
import com.example.daystory.R
import com.example.daystory.adapter.EventAdapter
import com.example.daystory.databinding.FragmentHomeBinding
import com.example.daystory.model.Event
import com.example.daystory.viewmodel.EventViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider {

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

        binding.addEventFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addEventFragment)
        }
    }

    private fun updateUI(event: List<Event>?){
        if(event != null){
            if(event.isEmpty()){
                //binding.emptyEventsImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                //binding.emptyEventsImage.visibility = View.VISIBLE
                //binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        eventAdapter = EventAdapter(eventsViewModel)
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            //setHasFixedSize(true)
            adapter = eventAdapter
        }

        activity?.let {
            eventsViewModel.getEvents.observe(viewLifecycleOwner){ event ->
                eventAdapter.submitList(event)
                //eventAdapter.notifyDataSetChanged()
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