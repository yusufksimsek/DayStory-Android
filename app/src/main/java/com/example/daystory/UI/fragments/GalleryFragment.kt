package com.example.daystory.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daystory.UI.adapter.GalleryAdapter
import com.example.daystory.UI.viewmodel.GalleryViewModel
import com.example.daystory.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        binding.galleryRecyclerView.layoutManager = LinearLayoutManager(context)

        observeViewModel()

        galleryViewModel.fetchDaySummaries()
        return binding.root
    }

    private fun observeViewModel() {
        galleryViewModel.daySummaries.observe(viewLifecycleOwner, Observer { daySummaries ->
            if (daySummaries != null) {
                val adapter = GalleryAdapter(daySummaries) { daySummary ->
                    val action = GalleryFragmentDirections.actionGalleryFragmentToGalleryDetailFragment(daySummary.date,daySummary.imagePath)
                    findNavController().navigate(action)
                }
                binding.galleryRecyclerView.adapter = adapter
            }
        })
    }
}
