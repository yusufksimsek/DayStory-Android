package com.example.daystory.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentWelcomePageBinding


class WelcomePageFragment : Fragment() {

    private lateinit var binding: FragmentWelcomePageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWelcomePageBinding.inflate(inflater, container, false)

        val text = "DayStory"
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(android.graphics.Typeface.BOLD)
        spannableString.setSpan(boldSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDayStoryfirst.text = spannableString

        binding.btnHesabimVar.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomePageFragment_to_loginFragment)
        }

        binding.btnYeniHesapOlustur.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomePageFragment_to_firstRegisterFragment)
        }

        return binding.root
    }

}