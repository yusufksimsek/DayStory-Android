package com.example.daystory.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentFirstRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class FirstRegisterFragment : Fragment() {

    private lateinit var binding: FragmentFirstRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstRegisterBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnDevam.setOnClickListener {
            if (validateFields()) {
                val userEnteredDate: String = binding.editTextDate.text.toString()
                val selectedGender: String = binding.spinner2.text.toString()

                Log.d("FirstRegisterFragment", "User entered date: $userEnteredDate")
                Log.d("FirstRegisterFragment", "Selected gender: $selectedGender")

                it.findNavController()
                    .navigate(R.id.action_firstRegisterFragment_to_secondRegisterFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerSetupClickableSpan()

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        setupGenderSpinner()
    }

    private fun setupGenderSpinner() {
        val genders = resources.getStringArray(R.array.gender_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.spinner2.setAdapter(arrayAdapter)
    }

    private fun registerSetupClickableSpan() {
        val registerText = getString(R.string.register_prompt)
        val loginText = getString(R.string.login_text)
        val spannableString = SpannableString(registerText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.findNavController()
                    .navigate(R.id.action_firstRegisterFragment_to_loginFragment)
            }
        }

        val startIndex = registerText.indexOf(loginText)
        val endIndex = startIndex + loginText.length

        spannableString.setSpan(clickableSpan, startIndex, endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textGirisYap.text = spannableString
        binding.textGirisYap.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showDatePickerDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Tarih Seçiniz")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = dateFormat.format(it)
            binding.editTextDate.setText(dateString)
        }

        datePicker.show(childFragmentManager, datePicker.toString())
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.editTextName.text.isNullOrEmpty()) {
            binding.textInputName.error = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputName.error = null
        }

        if (binding.editTextSurname.text.isNullOrEmpty()) {
            binding.textInputSurname.error = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputSurname.error = null
        }

        if (binding.spinner2.text.isNullOrEmpty()) {
            binding.textInputLayout4.error = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputLayout4.error = null
        }

        if (binding.editTextDate.text.isNullOrEmpty()) {
            binding.textInputLayoutDate.error = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputLayoutDate.error = null
        }

        return isValid
    }
}
