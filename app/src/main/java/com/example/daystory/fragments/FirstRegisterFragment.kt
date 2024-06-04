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
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.daystory.R
import com.example.daystory.databinding.FragmentFirstRegisterBinding
import com.example.daystory.viewmodel.LoginViewModel
import com.example.daystory.viewmodel.RegistrationViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FirstRegisterFragment : Fragment() {

    private lateinit var binding: FragmentFirstRegisterBinding
    private val registerViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstRegisterBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnDevam.setOnClickListener {

            val userEnteredDate: String = binding.editTextDate.text.toString()
            val selectedGender: String = binding.spinner2.text.toString()
            val name = binding.editTextName.text.toString()
            val surname = binding.editTextSurname.text.toString()

            if (registerViewModel.firstvalidateFields(name,surname,selectedGender,userEnteredDate)) {

                val genderForBackend = when (selectedGender) {
                    "Erkek" -> "Male"
                    "Kadın" -> "Female"
                    "Diğer" -> "Other"
                    else -> "NotSpecified"
                }

                val action = FirstRegisterFragmentDirections
                    .actionFirstRegisterFragmentToSecondRegisterFragment(
                        name = name,
                        surname = surname,
                        gender = genderForBackend,
                        date = userEnteredDate
                    )
                it.findNavController().navigate(action)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarTitle()

        registerViewModel.nameError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputName.error = error
        })

        registerViewModel.surnameError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputSurname.error = error
        })

        registerViewModel.genderError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputGender.error = error
        })

        registerViewModel.dateError.observe(viewLifecycleOwner, Observer { error ->
            binding.textInputDate.error = error
        })

        registerViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            binding.editTextDate.setText(date)
        })

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

        val locale = Locale("tr")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val calendarStart = Calendar.getInstance().apply {
            set(1924, Calendar.JANUARY, 1)
        }
        val calendarEnd = Calendar.getInstance().apply {
            set(2019, Calendar.DECEMBER, 31)
        }

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Tarih Seçiniz")
            .setSelection(today)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(calendarStart.timeInMillis)
                    .setEnd(calendarEnd.timeInMillis)
                    .build()
            )
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = selection ?: return@addOnPositiveButtonClickListener
            registerViewModel.validateDateRange(selectedDate, calendarStart.timeInMillis, calendarEnd.timeInMillis)
        }

        datePicker.show(childFragmentManager, datePicker.toString())
    }

}
