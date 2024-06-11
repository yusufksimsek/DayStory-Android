package com.example.daystory.UI.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.daystory.GlideApp
import com.example.daystory.UI.adapter.ImageDetailAdapter
import com.example.daystory.api.service.RetrofitClient
import com.example.daystory.databinding.FragmentImageDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageDetailFragment : Fragment() {
    private lateinit var binding: FragmentImageDetailBinding
    private lateinit var imageDetailAdapter: ImageDetailAdapter

    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarTitle()

        binding.iconBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        setupRecyclerView()
        loadDaySummary()

        val currentDate = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
        binding.textViewDate.text = currentDate

        val events = args.events!!.toList()
        imageDetailAdapter.submitList(events)
    }

    private fun setupRecyclerView() {
        imageDetailAdapter = ImageDetailAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = imageDetailAdapter
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

    private fun loadDaySummary() {
        val currentDate = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.eventApi.getDaySummary(currentDate)
                if (response.isSuccessful) {
                    val daySummary = response.body()?.data
                    Log.d("ImageDetailFragment", "Day summary response: $daySummary")
                    daySummary?.let {
                        val imageUrl = "https://talent.mobven.com:5043/" + it.imagePath
                        Log.d("ImageDetailFragment", "Loading image from URL: $imageUrl")

                        Glide.with(this@ImageDetailFragment)
                            .load(imageUrl)
                            .into(binding.imageAI)
                    }
                } else {
                    Log.e("ImageDetailFragment", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ImageDetailFragment", "Error loading day summary: ${e.message}", e)
            }
        }
    }

}





