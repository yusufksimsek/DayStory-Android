package com.example.daystory.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.daystory.UI.adapter.GalleryDetailAdapter
import com.example.daystory.api.service.RetrofitClient
import com.example.daystory.databinding.FragmentGalleryDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GalleryDetailFragment : Fragment() {

    private lateinit var binding: FragmentGalleryDetailBinding
    private lateinit var adapter: GalleryDetailAdapter
    private val retrofitClient = RetrofitClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryDetailBinding.inflate(inflater, container, false)

        binding.iconBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        val args: GalleryDetailFragmentArgs by navArgs()
        binding.textViewDate.text = args.date

        Glide.with(this).load(args.imagePath).into(binding.imageAI)

        setupRecyclerView()

        fetchEvents(args.date)

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = GalleryDetailAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun fetchEvents(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitClient.eventApi.getEventsByDate(date)
                if (response.isSuccessful && response.body()?.data != null) {
                    withContext(Dispatchers.Main) {
                        adapter.setEvents(response.body()?.data ?: emptyList())
                    }
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
}
