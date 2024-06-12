package com.example.daystory.UI.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.daystory.UI.adapter.GalleryAdapter
import com.example.daystory.UI.viewmodel.GalleryViewModel
import com.example.daystory.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(context, 2)

        setupToolbarTitle()

        observeViewModel()

        galleryViewModel.fetchDaySummaries()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        return binding.root
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
