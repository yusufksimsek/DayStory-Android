package com.example.daystory.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.daystory.R
import com.example.daystory.UI.viewmodel.ProfileViewModel
import com.example.daystory.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userDetails.observe(viewLifecycleOwner, Observer { user ->
            binding.tvUsername.text = user.username
            binding.tvNameSurname.text = "${user.firstName} ${user.lastName}"
            binding.tvEmail.text = user.email
            binding.tvDate.text = user.birthDate
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

}