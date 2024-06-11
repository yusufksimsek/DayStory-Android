package com.example.daystory.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daystory.UI.adapter.GalleryAdapter
import com.example.daystory.api.service.RetrofitClient
import com.example.daystory.databinding.FragmentGalleryBinding
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        binding.galleryRecyclerView.layoutManager = LinearLayoutManager(context)
        fetchDaySummaries()
        return binding.root
    }

    private fun fetchDaySummaries() {
        lifecycleScope.launch {
            val response = RetrofitClient.eventApi.getAllDaySummaries()
            if (response.isSuccessful) {
                response.body()?.data?.let { daySummaries ->
                    val adapter = GalleryAdapter(daySummaries)
                    binding.galleryRecyclerView.adapter = adapter
                }
            } else {

            }
        }
    }
}
