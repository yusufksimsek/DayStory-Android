package com.example.daystory.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupDayStorySpan()

        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginSetupClickableSpan()
    }

    private fun setupDayStorySpan(){
        val text = "DayStory'e Hoşgeldin!"
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textViewDaystory.text = spannableString
    }

    private fun loginSetupClickableSpan() {
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

        binding.textViewKayitOl.text = spannableString
        binding.textViewKayitOl.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.textInputEmail.error = "Email alanı boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputEmail.error = null
        }

        if (password.isEmpty()) {
            binding.textInputPassword.error = "Şifre alanı boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputPassword.error = null
        }

        return isValid
    }

}