package com.example.daystory.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentFirstRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale


class FirstRegisterFragment : Fragment() {

    private lateinit var binding: FragmentFirstRegisterBinding
    private var selectedGender: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstRegisterBinding.inflate(inflater, container, false)


        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnDevam.setOnClickListener {
            if (validateFields()) {
                val userEnteredDate: String = binding.editTextDate.text.toString()
                Log.d("FirstRegisterFragment", "User entered date: $userEnteredDate")

                it.findNavController()
                    .navigate(R.id.action_firstRegisterFragment_to_secondRegisterFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter.createFromResource(requireContext(),
        R.array.gender_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGender = parent.getItemAtPosition(position) as String
                Log.d("FirstRegisterFragment", "Spinner selected: $selectedGender")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedGender = null
                Log.d("FirstRegisterFragment", "No spinner selection")
            }
        }

        val registerText = getString(R.string.register_prompt)
        val spannableString = SpannableString(registerText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.findNavController()
                    .navigate(R.id.action_firstRegisterFragment_to_loginFragment)
            }
        }

        val startIndex = registerText.indexOf("Giriş yap")
        val endIndex = startIndex + "Giriş yap".length

        spannableString.setSpan(clickableSpan, startIndex, endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textGirisYap.text = spannableString
        binding.textGirisYap.movementMethod = LinkMovementMethod.getInstance()

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

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

        if (binding.editTextDate.text.isNullOrEmpty()) {
            binding.textInputLayoutDate.error = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            binding.textInputLayoutDate.error = null
        }

        return isValid
    }

}

