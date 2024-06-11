package com.example.daystory.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.daystory.R
import com.example.daystory.databinding.FragmentGalleryDetailBinding


class GalleryDetailFragment : Fragment() {

    private lateinit var binding: FragmentGalleryDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

}