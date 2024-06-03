package com.example.daystory.fragments

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
import androidx.navigation.fragment.navArgs
import com.example.daystory.R
import com.example.daystory.api.model.UserRegister
import com.example.daystory.databinding.FragmentSecondRegisterBinding
import com.example.daystory.viewmodel.RegistrationViewModel


class SecondRegisterFragment : Fragment() {

    private lateinit var binding: FragmentSecondRegisterBinding
    private val registerViewModel: RegistrationViewModel by viewModels()
    val args: SecondRegisterFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSecondRegisterBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {

            val name = args.name
            val surname = args.surname
            val gender = args.gender
            val date = args.date

            val email: String = binding.editTextEmail.text.toString()
            val username: String = binding.editTextUsername.text.toString()
            val password1 = binding.editTextPassword1.text.toString()
            val password2 = binding.editTextPassword2.text.toString()

            if (registerViewModel.secondvalidateFields(email,username,password1,password2)) {

                val user = UserRegister(
                    firstName = name,
                    lastName = surname,
                    username = username,
                    email = email,
                    password = password1,
                    passwordConfirmed = password2,
                    birthDate = date,
                    gender = gender
                )

                registerViewModel.registerUser(user)

                //it.findNavController().navigate(R.id.action_secondRegisterFragment_to_loginFragment)
                Log.d("SecondRegisterFragment", "Received email: $email")
                Log.d("SecondRegisterFragment", "Received username: $username")
                Log.d("SecondRegisterFragment", "Received password1: $password1")
                Log.d("SecondRegisterFragment", "Received password2: $password2")
                Log.d("SecondRegisterFragment", "Received name: $name")
                Log.d("SecondRegisterFragment", "Received surname: $surname")
                Log.d("SecondRegisterFragment", "Received gender: $gender")
                Log.d("SecondRegisterFragment", "Received date: $date")

                //Log.d("SecondRegisterFragment", "User registered: $user")
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerSetupClickableSpan()

        setupToolbarTitle()

        registerViewModel.emailError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputEmail.error = error
        })

        registerViewModel.usernameError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputUsername.error = error
        })

        registerViewModel.password1Error.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputPassword.error = error
        })

        registerViewModel.password2Error.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputPassword2.error = error
        })

        registerViewModel.registrationError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun registerSetupClickableSpan() {
        val registerText = getString(R.string.register_prompt)
        val loginText = getString(R.string.login_text)
        val spannableString = SpannableString(registerText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.findNavController()
                    .navigate(R.id.action_secondRegisterFragment_to_loginFragment)
            }
        }

        val startIndex = registerText.indexOf(loginText)
        val endIndex = startIndex + loginText.length

        spannableString.setSpan(clickableSpan, startIndex, endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textGirisyap.text = spannableString
        binding.textGirisyap.movementMethod = LinkMovementMethod.getInstance()
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

}