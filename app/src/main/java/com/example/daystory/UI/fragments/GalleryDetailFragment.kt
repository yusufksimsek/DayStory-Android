package com.example.daystory.UI.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.daystory.UI.adapter.GalleryDetailAdapter
import com.example.daystory.UI.viewmodel.GalleryDetailViewModel
import com.example.daystory.api.service.RetrofitClient
import com.example.daystory.databinding.FragmentGalleryDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GalleryDetailFragment : Fragment() {

    private lateinit var binding: FragmentGalleryDetailBinding
    private lateinit var adapter: GalleryDetailAdapter
    private val galleryDetailViewModel: GalleryDetailViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryDetailBinding.inflate(inflater, container, false)

        binding.iconBack.setOnClickListener {
            it.findNavController().navigateUp()
        }

        setupToolbarTitle()

        val args: GalleryDetailFragmentArgs by navArgs()
        binding.textViewDate.text = args.date

        Glide.with(this).load(args.imagePath).into(binding.imageAI)

        setupRecyclerView()

        observeViewModel()

        galleryDetailViewModel.fetchEvents(args.date)

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

    private fun setupRecyclerView() {
        adapter = GalleryDetailAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        galleryDetailViewModel.events.observe(viewLifecycleOwner, Observer { events ->
            events?.let {
                adapter.setEvents(it)
            }
        })
    }
}
