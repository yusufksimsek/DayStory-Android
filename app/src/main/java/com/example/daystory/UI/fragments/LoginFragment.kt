package com.example.daystory.UI.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentLoginBinding
import com.example.daystory.UI.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupDayStorySpan()

        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (loginViewModel.loginValidateFields(email, password)) {
                Log.d("SecondRegisterFragment", "Received email: $email")
                Log.d("SecondRegisterFragment", "Received password: $password")
                    loginViewModel.login(email, password)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginSetupClickableSpan()

        setupToolbarTitle()

        loginViewModel.emailError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputEmail.error = error
        })

        loginViewModel.passwordError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputPassword.error = error
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { token ->
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun setupDayStorySpan(){
        val text = "DayStory'e Hoşgeldin!"
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textViewDaystory.text = spannableString
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

}