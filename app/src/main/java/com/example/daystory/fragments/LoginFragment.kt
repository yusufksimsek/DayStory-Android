package com.example.daystory.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val registerText = getString(R.string.login_prompt)
        val spannableString = SpannableString(registerText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.findNavController()
                    .navigate(R.id.action_loginFragment_to_firstRegisterFragment)
            }
        }

        val startIndex = registerText.indexOf("Kayıt Ol")
        val endIndex = startIndex + "Kayıt Ol".length

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView7.text = spannableString
        binding.textView7.movementMethod = LinkMovementMethod.getInstance()
    }

}