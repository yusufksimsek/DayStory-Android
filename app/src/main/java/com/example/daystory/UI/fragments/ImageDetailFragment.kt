package com.example.daystory.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daystory.UI.adapter.ImageDetailAdapter
import com.example.daystory.databinding.FragmentImageDetailBinding
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

        binding.iconBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        setupRecyclerView()
        setDate()
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
    private fun setDate() {
        val currentDate = SimpleDateFormat("dd-MMus-yyy", Locale.getDefault()).format(Date())
        binding.textViewDate.text = currentDate
    }
}
