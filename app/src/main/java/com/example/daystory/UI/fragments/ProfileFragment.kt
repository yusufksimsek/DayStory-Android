package com.example.daystory.UI.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.daystory.R
import com.example.daystory.UI.viewmodel.ProfileViewModel
import com.example.daystory.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.Locale


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupToolbarTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userDetails.observe(viewLifecycleOwner, Observer { user ->
            binding.tvUsername.text = user.username
            binding.tvNameSurname.text = "${user.firstName} ${user.lastName}"
            binding.tvEmail.text = user.email
            binding.tvDate.text = formatDate(user.birthDate)
            binding.tvGender.text = when (user.gender) {
                "Male" -> "Erkek"
                "Female" -> "Kadın"
                "NotSpecified" -> "Belirtilmemiş"
                "Other" -> "Diğer"
                else -> user.gender
            }
        })

        profileViewModel.fetchUserDetails()
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

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}